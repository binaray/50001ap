package com.five_o_one.ap1d;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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

public class LocatorFragment extends Fragment implements GoogleMap.OnMarkerClickListener{
    private static final String ARG_DATALIST = "datalist";
    private static final String ARG_RANDOMNO="randomNum";
    MapView mMapView;
    private GoogleMap googleMap;
    List<Address> addresses;
    private static LocatorFragment fragment=null;
    private OnLocatorFragmentInteractionListener nListener;

    private List<LocationData> dataList;

    public LocatorFragment() {
        // Required empty public constructor
    }

    public static LocatorFragment getInstance(List<LocationData> dataList){
        if (fragment==null) {
            fragment = new LocatorFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(ARG_DATALIST, (ArrayList) dataList);
            fragment.setArguments(args);
            return fragment;
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dataList = (ArrayList) getArguments().getParcelableArrayList(ARG_DATALIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_locator, container, false);



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
                googleMap.setOnMarkerClickListener(LocatorFragment.this);
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());


                for(LocationData ii: dataList) {

                    try {
                        // GET LATITUDE AND LONGITUDE
                        //addresses = geocoder.getFromLocationName("Resorts World Sentosa", 1);
                        addresses = geocoder.getFromLocationName(ii.getName(), 1);
                        double latitude = addresses.get(0).getLatitude();
                        double longitude = addresses.get(0).getLongitude();
                        Log.i("Ken Jyi", "Sentosa is at latitude " + latitude
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
    public boolean onMarkerClick(Marker marker) {
        String name = marker.getTitle();
        for(int ii=0;ii<dataList.size();ii++) {
            if (name.equals(dataList.get(ii).getName())) {
                Log.i("Kenjyi", "The button works!");
                nListener.onMain(ii);
            }
        }
        return false;
    }

    public void locate(int currentPos){
        //show location
        String s = dataList.get(currentPos).getName();
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocationName(s, 1);
            double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();
            LatLng location = new LatLng(latitude, longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragment.OnMainFragmentInteractionListener) {
            nListener = (LocatorFragment.OnLocatorFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLocatorFragmentInteractionListener");
        }
    }

    public interface OnLocatorFragmentInteractionListener {
        void onMain(int currentPos);
    }



    //    @Override
//    public void onMainFragmentInteraction(int position) {
//        //do nothing
//    }
//
//    @Override
//    public void onLocationAdded(int position) {
//        //do nothing
//    }
//
//    @Override
//    public void onLocate(int currentPos) {
//        //show location
//        String s= dataList.get(currentPos).getName();
//    }

//    // For interfacing to main activity if needed
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
