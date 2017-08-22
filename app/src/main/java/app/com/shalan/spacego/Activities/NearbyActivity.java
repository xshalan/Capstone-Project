package app.com.shalan.spacego.Activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.com.shalan.spacego.Adapters.NearbyViewPagerAdapter;
import app.com.shalan.spacego.Fragments.NearbyListFragment;
import app.com.shalan.spacego.Fragments.NearbyMapFragment;
import app.com.shalan.spacego.Handler.Utils;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;

public class NearbyActivity extends AppCompatActivity implements LocationListener{

    private String TAG = NearbyActivity.class.getSimpleName();
    LinearLayout noSpacesLayout ;

    private FirebaseDatabase spaceDatabase;
    private DatabaseReference spaceDatabaseRef;

    public  List<Space> nearbySpaces;
    public  List<Double[]> spacesLocation;
    public  List<String[]> spacesName;
    public  List<Double> spacesDistance;

    public static double currentLatitude = 0.0;
    public static double currentLongitude = 0.0;
    Double distanceTwoPoints;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    NearbyViewPagerAdapter adapter;
    private List<Fragment> mFragmentList = new ArrayList<>();

    LocationManager locationManager;
    Location mlastLocation;

    SharedPreferences mSharedPreferences ;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String SPACE_LIST_KEY = "spacesList" ;
    public static final String DISTANCE_LIST_KEY = "distanceList" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        TextView toolbarTitle = (TextView) findViewById(R.id.nearby_toolbarTitle);
        ImageView connectionWhoops = (ImageView) findViewById(R.id.connection_whoops);
        noSpacesLayout = (LinearLayout) findViewById(R.id.noSpaces_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Fonts/Pacifico-Regular.ttf");
        toolbarTitle.setTypeface(typeface);

        mViewPager = (ViewPager) findViewById(R.id.nearby_viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.nearby_tabLayour);

        if (!Utils.isConnected(getApplicationContext())) {
            connectionWhoops.setVisibility(View.VISIBLE);
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this
                    , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
                Log.v("checkSelfPermission", "Great!");

            }

            mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View page, float position) {
                }

            });
            mTabLayout.setupWithViewPager(mViewPager);

        }
    }

    private void getAllSpaceLocation() {
        spaceDatabase = FirebaseDatabase.getInstance();
        spaceDatabaseRef = spaceDatabase.getReference("Spaces").child("Egypt");
        nearbySpaces = new ArrayList<>();
        spacesLocation = new ArrayList<>();
        spacesName = new ArrayList<>();
        spacesDistance = new ArrayList<>();

        spaceDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot spaceSnapshot : dataSnapshot.getChildren()) {
                    Space space = spaceSnapshot.getValue(Space.class);
                    int radius = 50;
                    Log.v("onDataChange", Double.toString(currentLatitude) + "  " + Double.toString(currentLongitude));

                    distanceTwoPoints = getDistance(space.getLatitude(), space.getLongitude(), currentLatitude, currentLongitude);
                    if (inRange(distanceTwoPoints, radius)) {
                        Log.v(TAG, "name: " + space.getName());
                        nearbySpaces.add(space);
                        Double[] spaceLocation = {space.getLatitude(), space.getLongitude()};
                        String[] spaceName = {space.getName()};
                        spacesName.add(spaceName);
                        spacesLocation.add(spaceLocation);
                        spacesDistance.add(distanceTwoPoints);
                    }
                }
                if(spacesName.size()!= 0) {
                    adapter = new NearbyViewPagerAdapter(getSupportFragmentManager());
                    mFragmentList.clear();
                    mFragmentList.add(NearbyListFragment.newInstance(nearbySpaces, spacesDistance));
                    mFragmentList.add(NearbyMapFragment.newInstance(spacesLocation, spacesName));
                    adapter.setmFragmentList(mFragmentList);
                    mViewPager.setAdapter(adapter);
                }else{
                    noSpacesLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nearby_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_to_widget:{
                mSharedPreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
                putList(DISTANCE_LIST_KEY,spacesDistance,mSharedPreferences.edit());
                putList(SPACE_LIST_KEY,nearbySpaces,mSharedPreferences.edit());
                Toast.makeText(getApplicationContext(), R.string.addToWidgetToast_messege,Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);

    }

    public Double getDistance(Double lat, Double lng, Double myLat, Double myLng) {
        /* This uses the ‘haversine’ formula to calculate the great-circle distance between two points
        Haversineformula:
        	            a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
                        c = 2 ⋅ atan2( √a, √(1−a) )
                        d = R ⋅ c
            where	φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
            @param lat in Degree(Double value)
            @param lng in Degree(Double value)
            @param Radius in Km(Double value)
            @param myLat in Degree(Double value)
            @param myLng in Degree(Double value)
        */
        int kmRadius = 6371;
        int mileRadius = 3959;
        Double lat1 = Math.toRadians(lat);
        Double lng1 = Math.toRadians(lat);
        Double lat2 = Math.toRadians(myLat - lat);
        Double lng2 = Math.toRadians(myLng - lng);
        Double a = Math.sin(lat2 / 2) * Math.sin(lat2 / 2) +
                Math.cos(lat1) * Math.cos(lng1) *
                        Math.sin(lng2 / 2) * Math.sin(lng2 / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return kmRadius * c;

    }

    private boolean inRange(Double distance, int radius) {
        // if the distance between two point in range of radius
        return distance < radius;
    }



    @Override
    public void onLocationChanged(Location location) {
        mlastLocation = location ;
        currentLongitude = location.getLongitude() ;
        currentLatitude = location.getLatitude() ;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.v("onStart", String.valueOf(currentLatitude));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("onResume", String.valueOf(currentLatitude));

        getAllSpaceLocation();

    }

    public <T> void putList(String key, List<T> list,SharedPreferences.Editor editor) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.commit();
    }
}
