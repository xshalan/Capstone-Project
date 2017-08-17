package app.com.shalan.spacego.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
    List<Double> spacesDistance = new ArrayList<>();

    private nearbyRecyclerAdapter mNearbyAdapter;
    private RecyclerView nearbyRecyclerView;

    private String NEARBY_LIST_KEY = "spaceList";
    private String DISTANCE_LIST_KEY = "distanceList";

    public NearbyListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby_list, container, false);
        nearbyRecyclerView = (RecyclerView) view.findViewById(R.id.nearby_recyclerView);
        ImageView whoops = (ImageView) view.findViewById(R.id.nearby_whoops);
       if(savedInstanceState == null ){
           if (!(spaceModel.size() > 0)) {
               whoops.setVisibility(View.VISIBLE);
               nearbyRecyclerView.setVisibility(View.GONE);
           }
       }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        nearbyRecyclerView.setLayoutManager(layoutManager);
        mNearbyAdapter = new nearbyRecyclerAdapter(R.layout.item_nearby_space_layout);
        mNearbyAdapter.setOnItemClickListener(new onSpaceClickListener() {
            @Override
            public void onSpaceClick(View view, int position) {
                Log.v(TAG, String.valueOf(position));
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mNearbyAdapter.setSpaceModel(spaceModel);
        mNearbyAdapter.setSpaceDistance(spacesDistance);
        nearbyRecyclerView.setAdapter(mNearbyAdapter);

    }

    public static NearbyListFragment newInstance(List<Space> space, List<Double> distance) {
        NearbyListFragment fragment = new NearbyListFragment();
        fragment.init(space, distance);
        return fragment;
    }

    public void init(List<Space> space, List<Double> distance) {
        spaceModel = space;
        this.spacesDistance = distance;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(NEARBY_LIST_KEY, new ArrayList<Space>(spaceModel));
        outState.putDoubleArray(DISTANCE_LIST_KEY,turnListIntoArray(spacesDistance));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            spacesDistance = turnArrayIntoList(savedInstanceState.getDoubleArray(DISTANCE_LIST_KEY));
            spaceModel = savedInstanceState.getParcelableArrayList(NEARBY_LIST_KEY) ;
        }
    }

    private List<Double> turnArrayIntoList(double[] array){
        List<Double> list = new ArrayList<>();
        for(int i=0; i<array.length;i++){
            list.add(array[i]);
        }
        return list ;
    }
    private double[] turnListIntoArray(List<Double> list){
        double[] array =new double[list.size()];
        for(int i=0; i<list.size();i++){
            array[i] = list.get(i);
        }
        return array ;
    }
}
