package app.com.shalan.spacego.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import app.com.shalan.spacego.Adapters.nearbyRecyclerAdapter;
import app.com.shalan.spacego.Handler.onSpaceClickListener;
import app.com.shalan.spacego.Models.Space;
import app.com.shalan.spacego.R;


public class NearbyListFragment extends Fragment {
    private String TAG = NearbyListFragment.class.getSimpleName();

    List<Space> spaceModel = new ArrayList<>();
    List<Double> spacesDistance = new ArrayList<>() ;
    Double distance ;
    public NearbyListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby_list, container, false);
        RecyclerView nearbyRecyclerView = (RecyclerView) view.findViewById(R.id.nearby_recyclerView);
        ImageView whoops = (ImageView) view.findViewById(R.id.nearby_whoops);

        if(!(spaceModel.size()> 0)){
            whoops.setVisibility(View.VISIBLE);
            nearbyRecyclerView.setVisibility(View.GONE);
        }
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        nearbyRecyclerView.setLayoutManager(layoutManager);
        nearbyRecyclerAdapter mNearbyAdapter = new nearbyRecyclerAdapter(spaceModel,spacesDistance,R.layout.item_nearby_space_layout);
        mNearbyAdapter.setOnItemClickListener(new onSpaceClickListener() {
            @Override
            public void onSpaceClick(View view, int position) {
                Log.v(TAG, String.valueOf(position));
            }
        });
        nearbyRecyclerView.setAdapter(mNearbyAdapter);
        return view;
    }
    public static NearbyListFragment newInstance(List<Space> space,List<Double> distance){
        NearbyListFragment fragment = new NearbyListFragment();
        fragment.init(space,distance);
        return fragment ;
    }

    public void init(List<Space> space,List<Double> distance){
        spaceModel =space ;
        this.spacesDistance = distance ;

    }

}
