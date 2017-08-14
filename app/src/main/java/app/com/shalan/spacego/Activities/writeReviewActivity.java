package app.com.shalan.spacego.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    @BindView(R.id.ReviewprogressBar)
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;

    String spaceID;
    String userID;
    String username;
    float rating;

    Double oldRating ;
    Double newRating ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ButterKnife.bind(this);
        spaceID = AboutFragment.spaceID;
        Log.v(TAG, spaceID);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final DatabaseReference ReviewRef = database.getReference("Reviews").child(spaceID);

        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();
        setUsername(mAuth, userID);
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
                    progressBar.setVisibility(View.VISIBLE);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Review review = new Review();
                    review.setDate(timestamp.getTime());
                    review.setRating(rating);
                    review.setReview(commentInput.getText().toString());
                    review.setUserId(userID);
                    review.setUsername(username);
                    submitReview.setClickable(false);
                    submitReview.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.submit_inactive_bg));
                    ReviewRef.push().setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                double newRate = rating ;
                                setNewRating(newRate,spaceID,ReviewRef) ;
                                progressBar.setProgress(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "You are awesome! Thanks", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed! try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please! Tell us your review!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUsername(FirebaseAuth mFirebaseAuth, String userId) {
        this.mAuth = mFirebaseAuth;
        if (mFirebaseAuth != null) {
            DatabaseReference userRef = database.getReference("Users").child(userId);
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
        }

    }

    private void setNewRating(Double newRate,String spaceID,DatabaseReference reviewRef){
        this.newRating = newRate ;
        final DatabaseReference spaceRef = database.getReference("Spaces").child("Egypt").child(spaceID);
        spaceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("rating").getValue() instanceof Long){
                    Long Rating = (Long) dataSnapshot.child("rating").getValue();
                    oldRating = Rating.doubleValue();

                } else {
                    oldRating = (Double) dataSnapshot.child("rating").getValue();
                }
                Log.v(TAG,Double.toString(oldRating));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        reviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long reviewsCount = dataSnapshot.getChildrenCount();
                Log.v(TAG,Long.toString(reviewsCount));
                double newRate = (newRating+oldRating) / (reviewsCount );
                spaceRef.child("rating").setValue(Math.floor(newRate));
                Log.v(TAG,Double.toString(newRate));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
