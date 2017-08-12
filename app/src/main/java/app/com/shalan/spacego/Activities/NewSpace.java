package app.com.shalan.spacego.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import app.com.shalan.spacego.Handler.Utils;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewSpace extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private String TAG = NewSpace.class.getSimpleName();
    private int SELECT_PHOTO = 100;

    @BindView(R.id.add_image_fab)
    FloatingActionButton imageAddFab;
    @BindView(R.id.id_add_space)
    Button addSpaceButton;
    @BindView(R.id.id_space_mylocation_button)
    Button useMyLocationButton;
    @BindView(R.id.space_address_text)
    TextView addressText;
    @BindView(R.id.space_desc_text)
    TextView descriptionText;
    @BindView(R.id.space_website_text)
    TextView websiteText;
    @BindView(R.id.space_name_text)
    TextView nameText;
    @BindView(R.id.space_map_lag_text)
    TextView lngMapText;
    @BindView(R.id.space_map_lat_text)
    TextView latMapText;
    @BindView(R.id.space_phone_text)
    TextView phoneText;
    @BindView(R.id.feature_fast_wifi)
    ToggleButton feature_wifi_text;
    @BindView(R.id.feature_24_hour)
    ToggleButton feature_hour_text;
    @BindView(R.id.feature_conference)
    ToggleButton feature_conference_text;
    @BindView(R.id.feature_free_coffee)
    ToggleButton feature_freeCoffe_text;
    @BindView(R.id.feature_free_parking)
    ToggleButton feature_freeParking_text;
    @BindView(R.id.feature_green_space)
    ToggleButton feature_greenSpace_text;
    @BindView(R.id.feature_kitchenette)
    ToggleButton feature_kitchen_text;
    @BindView(R.id.feature_maker_space)
    ToggleButton feature_makerSpace_text;
    @BindView(R.id.feature_membership)
    ToggleButton feature_membership_text;
    @BindView(R.id.feature_office)
    ToggleButton feature_office_text;
    @BindView(R.id.feature_printer_access)
    ToggleButton feature_printer_text;
    @BindView(R.id.feature_public_transporter)
    ToggleButton feature_transportation_text;
    @BindView(R.id.space_state_text)
    Spinner stateSpinner;

    //Firebase storage and database configuration
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    FirebaseDatabase spaceDatabase;
    DatabaseReference spaceDatabaseRef;

    private String imageCoverUrl;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    private ProgressDialog getLocationProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_space);
        setCutomView();
        ButterKnife.bind(this);
        // set up storage ref
        storageRef = storage.getReferenceFromUrl("gs://spacego-database.appspot.com/");
        // Add image of your space
        imageAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AskForPermissions()) {
                    askPermissions();
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });
        // set up Adapter for states Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.states_arrays));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(spinnerAdapter);
        // set up GoogleApiClient to get my current location
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        addSpaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spaceDatabase = FirebaseDatabase.getInstance();
                spaceDatabaseRef = spaceDatabase.getReference("Spaces").child("Egypt");
                List<String> featureList = new ArrayList<>();
                Space spaceModel = new Space();

                // Check if internet connection is fine ?!
                if (Utils.isConnected(getApplicationContext())) {
                    isFeatureSelected(featureList);
                    addSpace(spaceModel, featureList);
                } else {
                    Snackbar.make(view, "Check your Connection!", Snackbar.LENGTH_LONG);
                }

            }
        });
        useMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationProgress = new ProgressDialog(NewSpace.this);
                getLocationProgress.setMessage("Please wait!");
                getLocationProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                getLocationProgress.setIndeterminate(true);
                getLocationProgress.show();

                //Now lets connect to the API
                mGoogleApiClient.connect();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // get photo from Local storage to upload it
        if (requestCode == SELECT_PHOTO && data != null) {
            Uri uri = data.getData();
            Log.v("url:", uri.getPath());
            StorageReference imageGalleryRef = storageRef.child("images/" + uri.getLastPathSegment() + "_space");
            imageGalleryRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            imageCoverUrl = downloadUrl.toString();
                            imageAddFab.setImageResource(R.drawable.done_icon);
                            imageAddFab.setClickable(false);
                            Log.v(TAG, imageCoverUrl);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.v(TAG, e.getMessage());
                            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Method to set Custom Font to specific UI components
    public void setCutomView() {
        TextView title = (TextView) findViewById(R.id.id_space_name_label);
        TextView desc = (TextView) findViewById(R.id.id_space_desc_label);
        TextView website = (TextView) findViewById(R.id.id_space_map_label);
        TextView address = (TextView) findViewById(R.id.id_space_address_label);
        TextView map = (TextView) findViewById(R.id.id_space_map_label2);
        Button useMyLocation = (Button) findViewById(R.id.id_space_mylocation_button);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Fonts/Dosis-Medium.ttf");
        title.setTypeface(typeface);
        desc.setTypeface(typeface);
        website.setTypeface(typeface);
        address.setTypeface(typeface);
        map.setTypeface(typeface);
        useMyLocation.setTypeface(typeface);
    }

    // Ask for permission for API > 23
    protected boolean AskForPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    // Method to set a list of features of space
    public void isFeatureSelected(List<String> FeatureList) {
        ArrayList<ToggleButton> featureButtonsList = new ArrayList();
        featureButtonsList.add(feature_conference_text);
        featureButtonsList.add(feature_freeCoffe_text);
        featureButtonsList.add(feature_freeParking_text);
        featureButtonsList.add(feature_greenSpace_text);
        featureButtonsList.add(feature_hour_text);
        featureButtonsList.add(feature_makerSpace_text);
        featureButtonsList.add(feature_membership_text);
        featureButtonsList.add(feature_office_text);
        featureButtonsList.add(feature_printer_text);
        featureButtonsList.add(feature_transportation_text);
        featureButtonsList.add(feature_wifi_text);
        featureButtonsList.add(feature_kitchen_text);
        for (int i = 0; i < featureButtonsList.size(); i++) {
            if (featureButtonsList.get(i).isChecked()) {
                FeatureList.add(featureButtonsList.get(i).getTextOn().toString());
            }
        }

    }

    public void addSpace(Space spaceModel, List<String> featureList) {
        if (nameText.getText() != null && descriptionText.getText() != null
                && addressText.getText() != null && stateSpinner.getSelectedItem() != null
                && featureList.size() != 0) {
            spaceModel.setName(nameText.getText().toString());
            spaceModel.setDescription(descriptionText.getText().toString());
            spaceModel.setAddress(addressText.getText().toString());
            spaceModel.setCity(stateSpinner.getSelectedItem().toString());
            spaceModel.setCountry("Egypt");
            spaceModel.setFeatures(featureList);
            spaceModel.setLatitude(Double.parseDouble(latMapText.getText().toString()));
            spaceModel.setLongitude(Double.parseDouble(lngMapText.getText().toString()));
            spaceModel.setWebsite(websiteText.getText().toString());
            spaceModel.setPhone(Integer.valueOf(phoneText.getText().toString()));
            spaceModel.setImageUrl(imageCoverUrl);
        } else {
            Toast.makeText(getApplicationContext(), "Check your informaiton!", Toast.LENGTH_SHORT).show();

        }
        if (imageCoverUrl != null) {

            spaceDatabaseRef.push().setValue(spaceModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), nameText.getText().toString() + " added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to add space! try later!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.v(TAG, e.getMessage());
                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "Please, Pick a photo! ", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (AskForPermissions()) {
            askPermissions();
        }
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
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            latMapText.setText(Double.toString(location.getLatitude()));
            lngMapText.setText(Double.toString(location.getLongitude()));
        }
        getLocationProgress.hide();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
