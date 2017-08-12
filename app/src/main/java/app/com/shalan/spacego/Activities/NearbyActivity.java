package app.com.shalan.spacego.Activities;

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

import app.com.shalan.spacego.Handler.Utils;
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

        if (!Utils.isConnected(this)) {
            connectionState.setVisibility(View.VISIBLE);
        }
        getAllSpaceLocation();


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
                    if (inRange(space.getLatitude(), space.getLongitude(),50 , 30.0680582, 31.0194825)) {
                        Log.v(TAG, "name: " + space.getName());
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
        Double a = Math.sin(lat2/2) * Math.sin(lat2/2) +
                   Math.cos(lat1) * Math.cos(lng1) *
                   Math.sin(lng2/2) * Math.sin(lng2/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double distance = kmRadius * c ;
        Log.v(TAG, String.valueOf(distance));
        if (distance > Radius) {
            Log.v(TAG, "false");
            return false;
        } else {
            Log.v(TAG, "true");
            return true;
        }
    }


}
