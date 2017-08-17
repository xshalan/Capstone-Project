package app.com.shalan.spacego.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import app.com.shalan.spacego.R;

import static app.com.shalan.spacego.Handler.Utils.askPermissionsForLocatoin;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {


    MapView mMapView;
    private GoogleMap googleMap;

    private double latitude;
    private double longitude;
    private String spaceName;

    private String LONGITUDE_KEY = "longitude";
    private String LATITUDE_KEY = "latitude";
    private String SPACE_NAME_KEY = "name" ;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            latitude = savedInstanceState.getDouble(LATITUDE_KEY);
            longitude = savedInstanceState.getDouble(LONGITUDE_KEY);
            spaceName = savedInstanceState.getString(SPACE_NAME_KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    public static MapFragment newInstance(String name, double latitude, double longitude) {
        MapFragment fragment = new MapFragment();
        fragment.init(name, latitude, longitude);
        return fragment;
    }

    private void init(String name, double latitude, double longitude) {
        this.spaceName = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext()
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermissionsForLocatoin(getActivity());
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng spaceLocation = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(spaceLocation).title(spaceName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spaceLocation, 15));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(LONGITUDE_KEY,longitude);
        outState.putDouble(LATITUDE_KEY,latitude);
        outState.putString(SPACE_NAME_KEY,spaceName);
    }
}
