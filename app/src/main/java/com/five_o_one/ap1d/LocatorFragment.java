package com.five_o_one.ap1d;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class LocatorFragment extends Fragment implements MainFragment.OnMainFragmentInteractionListener{
    private static final String ARG_DATALIST = "datalist";
    private static final String ARG_RANDOMNO="randomNum";

    private List<LocationData> dataList;

    public LocatorFragment() {
        // Required empty public constructor
    }

    public static LocatorFragment newInstance(List<LocationData> dataList, int rand) {
        LocatorFragment fragment = new LocatorFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_DATALIST, (ArrayList) dataList);
        args.putInt(ARG_RANDOMNO,rand);
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_locator, container, false);
    }

    @Override
    public void onMainFragmentInteraction(int position) {
        //do nothing
    }

    @Override
    public void onLocationAdded(int position) {
        //do nothing
    }

    @Override
    public void onLocate(int currentPos) {
        //show location
        String s= dataList.get(currentPos).getName();
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
