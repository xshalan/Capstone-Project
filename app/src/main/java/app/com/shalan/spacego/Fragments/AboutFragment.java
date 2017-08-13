package app.com.shalan.spacego.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import app.com.shalan.spacego.Activities.loginActivity;
import app.com.shalan.spacego.Activities.writeReviewActivity;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AboutFragment extends Fragment {
    @BindView(R.id.about_space)
    TextView aboutSpaceTextview;
    @BindView(R.id.details_space_address)
    TextView spaceAddress ;
    @BindView(R.id.details_space_phone)
    TextView spacePhone ;
    @BindView(R.id.details_space_website)
    TextView spaceWebsite ;
    @BindView(R.id.write_reviewButton)
    Button writeReview ;

    private FirebaseAuth mAuth ;

    private Space spaceModel;
    private String spaceID ;
    private ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());


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

        if ((spaceModel != null)) {
            aboutSpaceTextview.setText(spaceModel.getDescription());
            spaceAddress.setText(spaceModel.getAddress());
            spacePhone.setText(Integer.toString(spaceModel.getPhone()));
            spaceWebsite.setText(spaceModel.getWebsite());
        }
        writeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser()!=null){
                    Intent intent = new Intent(getActivity(), writeReviewActivity.class);
                    intent.putExtra("spaceId",spaceID);
                    startActivity(intent);
                }else {
                    progressDialog.setMessage("Please Login first to add your review!");
                    progressDialog.setCancelable(true);
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), loginActivity.class);
                            startActivity(intent);
                        }
                    });
                }

            }
        });

        return view;
    }

    public static AboutFragment newInstance(Space spaceModel,String spaceID) {
        AboutFragment fragment = new AboutFragment();
        fragment.init(spaceModel,spaceID);
        return fragment;
    }

    private void init(Space spaceModel,String spaceID) {
        this.spaceModel = spaceModel ;
        this.spaceID = spaceID;
    }

}
