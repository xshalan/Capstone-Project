package app.com.shalan.spacego.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.com.shalan.spacego.Adapters.SpaceItemViewHolder;
import app.com.shalan.spacego.Handler.Utils;
import app.com.shalan.spacego.Handler.OnSpaceClickListener;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.Models.User;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    @BindView(R.id.spaces_recyclerView)
    RecyclerView spaceRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.connection_whoops)
    ImageView connectionWhoops;

    //Firebase variables declaration
    private static FirebaseDatabase spaceDatabase;
    private DatabaseReference spaceDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseRecyclerAdapter<Space, SpaceItemViewHolder> recyclerAdapter;

    // UI Components Variables
    TextView profileUsername;

    final private int LOCATION_PERMISSION_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        //Bind ButterKnife library to UI
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();

        // Configure Navigation drawer view to able to change its components attributes
        View navigationViewHeaderView = navigationView.getHeaderView(0);
        profileUsername = (TextView) navigationViewHeaderView.findViewById(R.id.profile_username);

        // Set up Navigation drawer menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        if (!Utils.isConnected(mContext)) {
            connectionWhoops.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
        } else {
            // Firebase authentication configuration
            mFirebaseAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        spaceDatabase = FirebaseDatabase.getInstance();
                        spaceDatabaseRef = spaceDatabase.getReference("Users").child(user.getUid());

                        // Access "USER" database to get all information about the signed user like (username)
                        spaceDatabaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                User mUser = dataSnapshot.getValue(User.class);
                                profileUsername.setText(mUser.getUsername());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        profileUsername.setVisibility(View.VISIBLE);
                        Menu nav_Menu = navigationView.getMenu();

                        // show sign out icon in NavigationDrawer Menu
                        nav_Menu.findItem(R.id.sign_in).setVisible(false);
                        nav_Menu.findItem(R.id.sign_out).setVisible(true);
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");

                        profileUsername.setVisibility(View.INVISIBLE);
                        Menu nav_Menu = navigationView.getMenu();

                        // show sign in icon in NavigationDrawer Menu
                        nav_Menu.findItem(R.id.sign_out).setVisible(false);
                        nav_Menu.findItem(R.id.sign_in).setVisible(true);
                    }
                }
            };
        }
        // Configure fab button to add new space
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewSpace.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        if (spaceDatabase == null && Utils.isConnected(mContext)) {
            spaceDatabase = FirebaseDatabase.getInstance();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        spaceDatabase = FirebaseDatabase.getInstance();
        spaceDatabaseRef = spaceDatabase.getReference("Spaces").child("Egypt");
        // set up Recycler view layout
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        spaceRecyclerView.setLayoutManager(layoutManager);
        spaceRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == recyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sign_in) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.sign_out) {
            mFirebaseAuth.signOut();
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.sign_in).setVisible(true);
            nav_Menu.findItem(R.id.sign_out).setVisible(false);
            profileUsername.setVisibility(View.GONE);

        } else if (id == R.id.nav_nearby) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {
                        "android.permission.ACCESS_FINE_LOCATION",
                        "android.permission.ACCESS_COARSE_LOCATION"
                };
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                Intent intent = new Intent(MainActivity.this, NearbyActivity.class);
                startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                Intent intent = new Intent(MainActivity.this, NearbyActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter = new FirebaseRecyclerAdapter<Space, SpaceItemViewHolder>(
                Space.class,
                R.layout.item_card_layout,
                SpaceItemViewHolder.class,
                spaceDatabaseRef.orderByChild("rating").limitToFirst(10)) {
            @Override
            protected void onDataChanged() {
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            protected void populateViewHolder(SpaceItemViewHolder viewHolder, final Space model, int position) {
                viewHolder.spaceName.setText(model.getName());  // Space name
                viewHolder.spaceRate.setText(Double.toString(model.getRating()));          // Space rate
                Glide.with(mContext).load(model.getImageUrl()).into(viewHolder.spaceImage); //space Img
                // Hanfle when space onClicked
                viewHolder.setOnItemClickListener(new OnSpaceClickListener() {
                    @Override
                    public void onSpaceClick(View view, int position) {
                        DatabaseReference mDatabaseReference = getRef(position);
                        Log.v(TAG, Integer.toString(position) + model.getName());
                        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                        intent.putExtra("spaceModel", model);
                        intent.putExtra("spaceID", mDatabaseReference.getKey());
                        startActivity(intent);

                    }
                });
            }
        };
        spaceRecyclerView.setAdapter(recyclerAdapter);

        if (mFirebaseAuth != null) {
            mFirebaseAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
