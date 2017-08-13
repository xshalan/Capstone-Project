package app.com.shalan.spacego.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AboutFragment extends Fragment {
    @BindView(R.id.about_space)
    TextView aboutSpaceTextview;
    @BindView(R.id.commentInput)
    EditText commentInput;
    @BindView(R.id.details_space_address)
    TextView spaceAddress ;
    @BindView(R.id.details_space_phone)
    TextView spacePhone ;
    @BindView(R.id.details_space_website)
    TextView spaceWebsite ;

    private Space spaceModel;
    private String spaceID ;

    private FirebaseAuth mFirebaseAuth ;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        mFirebaseAuth = FirebaseAuth.getInstance() ;
        if ((spaceModel != null)) {
            aboutSpaceTextview.setText(spaceModel.getDescription());
            spaceAddress.setText(spaceModel.getAddress());
            spacePhone.setText(Integer.toString(spaceModel.getPhone()));
            spaceWebsite.setText(spaceModel.getWebsite());
        }

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
