package app.com.shalan.spacego.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NearbyActivity extends AppCompatActivity {
    private String TAG = NewSpace.class.getSimpleName();
    @BindView(R.id.connection_image_state)
    ImageView connectionState;

    private static FirebaseDatabase spaceDatabase;
    private DatabaseReference spaceDatabaseRef;

    public List<Double[]> spaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        ButterKnife.bind(this);

        if (!isConnected()) {
            connectionState.setVisibility(View.VISIBLE);
        }
        getAllSpaceLocation();


    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void getAllSpaceLocation() {
        spaceDatabase = FirebaseDatabase.getInstance();
        spaceDatabaseRef = spaceDatabase.getReference("Spaces").child("Egypt");
        spaceList = new ArrayList<>();
        spaceDatabaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot spaceSnapshot : dataSnapshot.getChildren()) {
                    Space space = spaceSnapshot.getValue(Space.class);
                    if(inRange(space.getLatitude(), space.getLongitude(),5,30.0680582,31.0194825)){
                        Log.v(TAG,"name: "+ space.getName()) ;
                    }
                    Double[] spaceLocation = {space.getLatitude(), space.getLongitude()};
                    spaceList.add(spaceLocation);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    // $sql = "SELECT *, ( 3959 * acos( cos( radians(" . $lat . ") ) * cos( radians( lat ) ) * cos( radians( lng )
    // - radians(" . $lng . ") ) + sin( radians(" . $lat . ") ) * sin( radians( lat ) ) ) ) AS distance FROM your_table HAVING distance < 5";

    public boolean inRange(Double lat, Double lng, int Radius, Double myLat, Double myLng) {
        double distance = 3959 * Math.acos(Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(myLat)) * Math.cos(Math.toRadians(myLng))
                - Math.toRadians(lng) + Math.sin(Math.toRadians(lat) * Math.sin(Math.toRadians(myLat))));
        Log.v(TAG, String.valueOf(distance));
        if (distance > Radius) {
            Log.v(TAG,"false");
            return false;
        } else {
            Log.v(TAG,"true");
            return true;
        }
    }


}
