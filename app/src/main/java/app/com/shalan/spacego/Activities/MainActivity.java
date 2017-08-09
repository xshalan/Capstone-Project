package app.com.shalan.spacego.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.com.shalan.spacego.Adapters.spaceItemViewHolder;
import app.com.shalan.spacego.Handler.onSpaceClickListener;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = MainActivity.class.getSimpleName() ;
    @BindView(R.id.spaces_recyclerView)
    RecyclerView spaceRecyclerView;

    FirebaseDatabase spaceDatabase = FirebaseDatabase.getInstance();
    DatabaseReference spaceDatabaseRef;

    private FirebaseRecyclerAdapter<Space, spaceItemViewHolder> recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        spaceDatabase = FirebaseDatabase.getInstance();
        spaceDatabaseRef = spaceDatabase.getReference("Spaces").child("Egypt");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        spaceRecyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new FirebaseRecyclerAdapter<Space, spaceItemViewHolder>(
                Space.class,
                R.layout.item_card_layout,
                spaceItemViewHolder.class,
                spaceDatabaseRef) {

            @Override
            protected void populateViewHolder(spaceItemViewHolder viewHolder, final Space model, int position) {
                viewHolder.spaceName.setText(model.getName());
                viewHolder.spaceRate.setText("9");
                Glide.with(MainActivity.this).load(model.getImageUrl()).into(viewHolder.spaceImage);
                viewHolder.setOnItemClickListener(new onSpaceClickListener() {
                    @Override
                    public void onSpaceClick(View view, int position) {
                        Log.v(TAG,Integer.toString(position));
                        Intent intent = new Intent(MainActivity.this,DetailsActivity.class) ;
                        intent.putExtra("spaceModel",model);
                        startActivity(intent);
                    }
                });
            }
        };
        spaceRecyclerView.setAdapter(recyclerAdapter);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
