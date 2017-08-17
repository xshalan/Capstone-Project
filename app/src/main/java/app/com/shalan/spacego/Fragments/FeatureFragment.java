package app.com.shalan.spacego.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class FeatureFragment extends Fragment {

    private List<String> featureList = new ArrayList<>();

    @BindView(R.id.image_24hour)
    ImageView feature24hourImage;
    @BindView(R.id.image_coffee)
    ImageView featureCoffeeImage;
    @BindView(R.id.image_conference)
    ImageView featureConferenceImage;
    @BindView(R.id.image_greenSpace)
    ImageView featureGreenSpaceImage;
    @BindView(R.id.image_kitchen)
    ImageView featureKitchenImage;
    @BindView(R.id.image_membership)
    ImageView featureMembershipImage;
    @BindView(R.id.image_parking)
    ImageView featureParkingImage;
    @BindView(R.id.image_printer)
    ImageView featurePrinterImage;
    @BindView(R.id.image_privateOffice)
    ImageView featurePrivateOfficeImage;
    @BindView(R.id.image_publicTransp)
    ImageView featurePublicTransImage;
    @BindView(R.id.image_wifi)
    ImageView featureWifiImage;

    private String FEATURE_LIST_KEY = "featureList" ;

    public FeatureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feature, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (featureList != null ) {

            for (String i : featureList) {
                System.out.println("list: " + i);
                if (i.contains("WiFi")) {
                    featureWifiImage.setImageResource(R.drawable.a_wifi);
                } else if (i.contains("24")) {
                    feature24hourImage.setImageResource(R.drawable.a_24hout);
                } else if (i.contains("Coffee")) {
                    featureCoffeeImage.setImageResource(R.drawable.a_coffee);
                } else if (i.contains("Green")) {
                    featureGreenSpaceImage.setImageResource(R.drawable.a_greenspace);
                } else if (i.contains("Conference")) {
                    featureConferenceImage.setImageResource(R.drawable.a_conference);
                } else if (i.contains("Kitchenette")) {
                    featureKitchenImage.setImageResource(R.drawable.a_kitchen);
                } else if (i.contains("Membership")) {
                    featureMembershipImage.setImageResource(R.drawable.a_membership);
                } else if (i.contains("Parking")) {
                    featureParkingImage.setImageResource(R.drawable.a_parking);
                } else if (i.contains("Printer")) {
                    featurePrinterImage.setImageResource(R.drawable.a_printer);
                } else if (i.contains("Private")) {
                    featurePrinterImage.setImageResource(R.drawable.a_privateoffice);
                } else if (i.contains("Public")) {
                    featurePublicTransImage.setImageResource(R.drawable.a_publictransport);
                } else {
                }
            }
        }
    }

    public static FeatureFragment newInstance(List<String> featureList) {
        FeatureFragment fragment = new FeatureFragment();
        fragment.init(featureList);
        return fragment;
    }

    private void init(List<String> featureList) {
        this.featureList = featureList;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            featureList = savedInstanceState.getStringArrayList(FEATURE_LIST_KEY);

        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(FEATURE_LIST_KEY,new ArrayList<String>(featureList));
    }
}
