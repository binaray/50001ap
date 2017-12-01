package com.five_o_one.ap1d;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { MainFragment.OnMainFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements MyAdapter.OnItemClicked{
    private static final String ARG_DATALIST = "datalist";
    private static final String ARG_RANDOMNO="randomNum";

    private List<LocationData> dataList;
    private String mParam2;
    private LinearLayoutManager linearLayoutManager;
    private ImageView featuredImg;
    private TextView featuredName;
    private TextView featuredDetails;
    LocationData featured;
    private static int f_pos;
    private OnMainFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(List<LocationData> dataList,int rand) {
        MainFragment fragment = new MainFragment();
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
            f_pos=getArguments().getInt(ARG_RANDOMNO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        featuredImg=view.findViewById(R.id.featured_img);
        featuredName=view.findViewById(R.id.featured_title);
        featuredDetails=view.findViewById(R.id.featured_details);

        featured = dataList.get(f_pos);
        String title="Featured Location: ";
        title=title.concat(featured.getName());
        featuredName.setText(title);
        featuredImg.setImageResource(featured.getImageUrl());
        featuredDetails.setText(featured.getDetails());
        RecyclerView rv= (RecyclerView) view.findViewById(R.id.recycler_v);
        MyAdapter adapter=new MyAdapter(dataList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(linearLayoutManager);
        adapter.setOnClick(this);
        return view;
    }

    // For interfacing to main activity if needed
    public void onItemSelected(int position) {
        if (mListener != null) {
            mListener.onMainFragmentInteraction(position);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        linearLayoutManager=new LinearLayoutManager(context);
        if (context instanceof OnMainFragmentInteractionListener) {
            mListener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainFragmentInteractionListener");
        }
    }

    @Override
    public void onItemClicked(int position) {
        featured = dataList.get(position);
        String title="Selected Location: ";
        title=title.concat(featured.getName());
        featuredName.setText(title);
        featuredImg.setImageResource(featured.getImageUrl());
        featuredDetails.setText(featured.getDetails());
        onItemSelected(position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMainFragmentInteractionListener {
        void onMainFragmentInteraction(int position);
    }
}
