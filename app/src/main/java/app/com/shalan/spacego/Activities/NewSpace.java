package app.com.shalan.spacego.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewSpace extends AppCompatActivity {
    private String TAG = NewSpace.class.getSimpleName();
    private int SELECT_PHOTO = 100;

    @BindView(R.id.add_image_fab)
    FloatingActionButton imageAddFab;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    UploadTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_space);
        setCutomView();
        ButterKnife.bind(this);
        storageRef = storage.getReferenceFromUrl("gs://spacego-database.appspot.com/");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && data != null) {
            Uri uri = data.getData();
            Log.v("url:", uri.getPath());
            StorageReference imageGalleryRef = storageRef.child("images/" + uri.getLastPathSegment() + "_space");
            imageGalleryRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String imageURL = taskSnapshot.toString();
                            imageAddFab.setImageResource(R.drawable.done_icon);
                            imageAddFab.setClickable(false);
                            Log.v(TAG, imageURL);
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

}
