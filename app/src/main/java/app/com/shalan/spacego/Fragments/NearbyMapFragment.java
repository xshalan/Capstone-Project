package app.com.shalan.spacego.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import app.com.shalan.spacego.Handler.Utils;
import app.com.shalan.spacego.R;


public class NearbyMapFragment extends Fragment implements OnMapReadyCallback {
    MapView mMapView;
    private GoogleMap googleMap;

    private List<Double[]> spacesLocation = new ArrayList<>();
    private List<String[]> spaceName;

    public NearbyMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.nearbyMapView);
        ImageView whoops = (ImageView) view.findViewById(R.id.nearby_whoops);
        ImageView connectionWhoops = (ImageView) view.findViewById(R.id.connection_whoops);

        if (!Utils.isConnected(getContext())) {
            connectionWhoops.setVisibility(View.VISIBLE);
            mMapView.setVisibility(View.GONE);
        }
        if (!(spacesLocation.size() > 0)) {
            whoops.setVisibility(View.VISIBLE);
            mMapView.setVisibility(View.GONE);
        }
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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        for (int i = 0; i < spacesLocation.size(); i++) {
            Double locations[] = spacesLocation.get(i);
            Double lat = locations[0];
            Double lng = locations[1];
            int zoom = 9 ;
            createMarker(lat, lng, spaceName.get(i)[0]);
            LatLng spaceLocation = new LatLng(lat, lng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spaceLocation,zoom));

        }
    }

    public static NearbyMapFragment newInstance(List<Double[]> spacesLocation, List<String[]> spaceName) {
        NearbyMapFragment fragment = new NearbyMapFragment();
        fragment.init(spacesLocation, spaceName);
        return fragment;
    }

    private void init(List<Double[]> spacesLocation, List<String[]> spaceName) {
        this.spacesLocation = spacesLocation;
        this.spaceName = spaceName;
    }

    private Marker createMarker(double lat, double lng, String title) {
        LatLng spaceLocation = new LatLng(lat, lng);
        return googleMap.addMarker(
                new MarkerOptions().position(spaceLocation).title(title));
    }
}
