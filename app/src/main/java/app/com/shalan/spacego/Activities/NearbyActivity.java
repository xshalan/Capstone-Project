package app.com.shalan.spacego.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.com.shalan.spacego.Adapters.nearbyViewPagerAdater;
import app.com.shalan.spacego.Fragments.NearbyListFragment;
import app.com.shalan.spacego.Fragments.NearbyMapFragment;
import app.com.shalan.spacego.Handler.Utils;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;

public class NearbyActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private String TAG = NearbyActivity.class.getSimpleName();


    private static FirebaseDatabase spaceDatabase;
    private DatabaseReference spaceDatabaseRef;

    public List<Space> nearbySpaces;
    public List<Double[]> spacesLocation;
    public List<String[]> spacesName;
    public List<Double> spacesDistance;


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    private double currentLatitude;
    private double currentLongitude;
    Double distanceTwoPoints ;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    nearbyViewPagerAdater adapter ;
    private Space spaceModel;
    private List<Fragment> mFragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        TextView toolbarTitle = (TextView) findViewById(R.id.nearby_toolbarTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Fonts/Pacifico-Regular.ttf");
        toolbarTitle.setTypeface(typeface);

        mViewPager = (ViewPager) findViewById(R.id.nearby_viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.nearby_tabLayour);
        ImageView connectionWhoops = (ImageView) findViewById(R.id.connection_whoops);

        if (!Utils.isConnected(getApplicationContext())) {
            connectionWhoops.setVisibility(View.VISIBLE);
        }else{
            buildGoogleApiClient();
            getAllSpaceLocation();
            adapter = new nearbyViewPagerAdater(getSupportFragmentManager());

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
        spacesDistance = new ArrayList<>() ;

        spaceDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot spaceSnapshot : dataSnapshot.getChildren()) {
                    Space space = spaceSnapshot.getValue(Space.class);
                    int radius = 50 ;
                    distanceTwoPoints = getDistance(space.getLatitude(), space.getLongitude(), 30.3159, 31.7229) ;
                    if (inRange(distanceTwoPoints,radius )) {
                        Log.v(TAG, "name: " + space.getName());
                        nearbySpaces.add(space);
                        Double[] spaceLocation = {space.getLatitude(), space.getLongitude()};
                        String[] spaceName = {space.getName()} ;
                        spacesName.add(spaceName);
                        spacesLocation.add(spaceLocation);
                        spacesDistance.add(distanceTwoPoints);
                    }
                }
                mFragmentList.add(NearbyListFragment.newInstance(nearbySpaces,spacesDistance));
                mFragmentList.add(NearbyMapFragment.newInstance(spacesLocation,spacesName));
                adapter.setmFragmentList(mFragmentList);
                mViewPager.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    // $sql = "SELECT *, ( 3959 * acos( cos( radians(" . $lat . ") ) * cos( radians( lat ) ) * cos( radians( lng )
    // - radians(" . $lng . ") ) + sin( radians(" . $lat . ") ) * sin( radians( lat ) ) ) ) AS distance FROM your_table HAVING distance < 5";

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
    private boolean inRange(Double distance,int radius){
        // if the distance between two point in range of radius
        if(distance<radius){
            return true ;

        }else {
            return false ;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        if(mGoogleApiClient!=null){
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(mGoogleApiClient!=null){
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Create the LocationRequest object
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(TAG, "Connection failed: ConnectionResult.getErrorCode() = "+ connectionResult.getErrorCode());

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        currentLatitude = location.getLongitude();
        currentLongitude = location.getLongitude();
        Log.v(TAG, String.valueOf(currentLatitude));
        Log.v(TAG, String.valueOf(currentLongitude));
        Toast.makeText(getApplicationContext(),String.valueOf(currentLatitude)+"  "+ String.valueOf(currentLongitude),Toast.LENGTH_SHORT);
    }


}
