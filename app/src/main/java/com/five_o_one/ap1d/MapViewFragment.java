package com.five_o_one.ap1d;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link MapViewFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link MapViewFragment#newInstance} factory method to
// * create an instance of this fragment.
// */

public class MapViewFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    private static MapViewFragment fragment=null;
    private static final String ARG_DATALIST = "data";
    private List<LocationData> data;
    private DatabaseHelper dbHelper;
    List<Address> addresses;

    public MapViewFragment(){

    }

//    public static MapViewFragment getInstance(List<LocationData> data){
//
//        fragment = new MapViewFragment();
//        Bundle args = new Bundle();
//        args.putParcelableArrayList(ARG_DATALIST, (ArrayList) data);
//        fragment.setArguments(args);
//        return fragment;
//
//    }

    public static MapViewFragment newInstance(List<LocationData> data) {
        if (fragment==null) {
            fragment = new MapViewFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(ARG_DATALIST, (ArrayList) data);
            fragment.setArguments(args);
            return fragment;
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (ArrayList) getArguments().getParcelableArrayList(ARG_DATALIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);

        dbHelper=DatabaseHelper.getInstance(getContext());
        data=dbHelper.getSelectedDataList();
        
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                Marker marker;
                googleMap = mMap;
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                for(LocationData ii: data) {
                    try {
                        // GET LATITUDE AND LONGITUDE
                        //addresses = geocoder.getFromLocationName("Resorts World Sentosa", 1);
                        addresses = geocoder.getFromLocationName(ii.getName(), 1);
                        double latitude = addresses.get(0).getLatitude();
                        double longitude = addresses.get(0).getLongitude();
                        Log.i("Ken Jyi", ii.getName() + " is at latitude " + latitude
                                + " and longitude " + longitude);

                        // MOVE CAMERA TO ADDRESS
                        LatLng location = new LatLng(latitude, longitude);

                        marker = mMap.addMarker(new MarkerOptions().position(location).title(ii.getName()));
                        marker.setTag(location);

                        marker.setPosition(location);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                LatLngBounds singapore = new LatLngBounds(new LatLng(1.28, 103.7), new LatLng(1.3, 103.9));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(singapore.getCenter()));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(11));


            }

        });

        return rootView;
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
