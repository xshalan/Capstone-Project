package app.com.shalan.spacego.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import app.com.shalan.spacego.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AboutFragment extends Fragment {
    @BindView(R.id.about_space)
    TextView aboutSpaceTextview;
    @BindView(R.id.commentInput)
    EditText commentInput;

    private String about_space;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        if (!about_space.isEmpty()) {
            aboutSpaceTextview.setText(about_space);
        }

        return view;
    }

    public static AboutFragment newInstance(String aboutSpace) {
        AboutFragment fragment = new AboutFragment();
        fragment.init(aboutSpace);
        return fragment;
    }

    private void init(String aboutSpaceInput) {
        about_space = aboutSpaceInput;
    }

}
