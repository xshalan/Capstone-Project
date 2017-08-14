package app.com.shalan.spacego.Activities;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;

import app.com.shalan.spacego.Fragments.AboutFragment;
import app.com.shalan.spacego.Models.Review;
import app.com.shalan.spacego.Models.User;
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
    DatabaseReference usersRef;

    String spaceID;
    String userID;
    String username;
    String review;
    float rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ButterKnife.bind(this);
        spaceID = AboutFragment.spaceID;
        Log.v(TAG, spaceID);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Reviews").child(spaceID);
        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();
        DatabaseReference userRef = database.getReference("Users").child(userID);

        // Access "USER" database to get all information about the signed user like (username)
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User mUser = dataSnapshot.getValue(User.class);
                username = mUser.getUsername();
                Log.v(TAG, username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spaceRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
                // For debuging
                Log.v(TAG, String.valueOf(v));
            }
        });

        submitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rating != 0) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Review review = new Review();
                    review.setDate(timestamp.getTime());
                    review.setRating(rating);
                    review.setReview(commentInput.getText().toString());
                    review.setUserId(userID);
                    review.setUsername(username);
                    Log.v(TAG, username);
                    Log.v(TAG, commentInput.getText().toString());
                    submitReview.setClickable(false);
                    myRef.push().setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

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

    private void getUsername(FirebaseAuth mFirebaseAuth) {
        this.mAuth = mFirebaseAuth;
        usersRef = database.getReference("Users").child(mFirebaseAuth.getCurrentUser().getUid());
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mUser = dataSnapshot.getValue(User.class);
                username = mUser.getUsername();
                Log.v(TAG, username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
