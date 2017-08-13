package app.com.shalan.spacego.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

import app.com.shalan.spacego.Models.Review;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class writeReviewActivity extends AppCompatActivity {
    private String TAG = writeReviewActivity.class.getSimpleName();

    @BindView(R.id.comment_edittext)
    EditText commentInput;
    @BindView(R.id.space_ratingBar)
    RatingBar spaceRatingBar;
    @BindView(R.id.submit_review)
    Button submitReview;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;

    String spaceID;
    String userID;
    String review;
    float rating;

    private ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ButterKnife.bind(this);
        spaceID = getIntent().getStringExtra("spaceID");

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Reviews").child(spaceID);

        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();
        spaceRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
                // For debuging
                Log.v(TAG, String.valueOf(v));
            }
        });
        review = commentInput.getText().toString();
        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (review != null && rating != 0) {
                    progressDialog.setMessage("Please wait...");
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Review review = new Review();
                    review.setDate(timestamp.getTime());
                    review.setRating(rating);
                    review.setUserId(userID);
                    myRef.setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.hide();

                            if (task.isSuccessful()) {
                                finish();
                                Toast.makeText(getApplicationContext(), "Your review added successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed! try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
