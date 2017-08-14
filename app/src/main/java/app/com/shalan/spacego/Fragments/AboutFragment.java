package app.com.shalan.spacego.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.com.shalan.spacego.Activities.loginActivity;
import app.com.shalan.spacego.Activities.writeReviewActivity;
import app.com.shalan.spacego.Adapters.reviewViewHolder;
import app.com.shalan.spacego.Handler.getCoolTime;
import app.com.shalan.spacego.Models.Review;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AboutFragment extends Fragment {
    private String TAG = AboutFragment.class.getSimpleName();

    @BindView(R.id.about_space)
    TextView aboutSpaceTextview;
    @BindView(R.id.details_space_address)
    TextView spaceAddress;
    @BindView(R.id.details_space_phone)
    TextView spacePhone;
    @BindView(R.id.details_space_website)
    TextView spaceWebsite;
    @BindView(R.id.write_reviewButton)
    Button writeReview;
    @BindView(R.id.reviewRecyclerView)
    RecyclerView mReviewsRecyclerView;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private Space spaceModel;
    public static String spaceID;
    private AlertDialog.Builder progressDialog;

    protected FirebaseRecyclerAdapter<Review, reviewViewHolder> mRecyclerAdapter;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Reviews").child(spaceID);

        if ((spaceModel != null)) {

            aboutSpaceTextview.setText(spaceModel.getDescription());
            spaceAddress.setText(spaceModel.getAddress());
            spacePhone.setText("0" + spaceModel.getPhone());
            spaceWebsite.setText(spaceModel.getWebsite());
        }
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mReviewsRecyclerView.setLayoutManager(layoutManager);


        writeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(getActivity(), writeReviewActivity.class);
                    intent.putExtra("spaceId", spaceID);
                    Log.v(TAG, spaceID);
                    startActivity(intent);
                } else {
                    progressDialog = new AlertDialog.Builder(getContext());
                    progressDialog.setMessage("Please Login first to add your review!");
                    progressDialog.setCancelable(true);
                    progressDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), loginActivity.class);
                            startActivity(intent);
                        }
                    }).show();
                }

            }
        });

        return view;
    }

    public static AboutFragment newInstance(Space spaceModel, String spaceID) {
        AboutFragment fragment = new AboutFragment();
        fragment.init(spaceModel, spaceID);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerAdapter = new FirebaseRecyclerAdapter<Review, reviewViewHolder>(
                Review.class,
                R.layout.item_comment_card_layout,
                reviewViewHolder.class,
                myRef) {
            @Override
            protected void populateViewHolder(reviewViewHolder viewHolder, Review model, int position) {
                if (model.getRating() != 0 && model.getReview() != null && model.getDate() != 0) {
                    getCoolTime coolTime = new getCoolTime(getContext());
                    viewHolder.username.setText(model.getUsername());
                    viewHolder.review.setText(model.getReview());
                    viewHolder.ratingBar.setRating(model.getRating());
                    viewHolder.timeStamp.setText(coolTime.timeAgo(model.getDate()));
                }

            }
        };
        mReviewsRecyclerView.setNestedScrollingEnabled(false);
        mReviewsRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private void init(Space spaceModel, String spaceID) {
        this.spaceModel = spaceModel;
        this.spaceID = spaceID;
    }


}
