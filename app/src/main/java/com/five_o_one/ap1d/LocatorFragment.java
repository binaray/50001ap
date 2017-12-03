package com.five_o_one.ap1d;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocatorFragment extends Fragment{
    private static final String ARG_DATALIST = "datalist";
    private static final String ARG_RANDOMNO="randomNum";

    private List<LocationData> dataList;
    private TextView tv;
    private static LocatorFragment fragment=null;

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
        View v= inflater.inflate(R.layout.fragment_locator, container, false);
        tv=v.findViewById(R.id.testView);
        return v;
    }

    public void locate(int currentPos) {
        //show location
        String s= dataList.get(currentPos).getName();
        Log.v("Passed to locator!",s);
        tv.setText(s);
    }

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
