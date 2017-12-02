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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainFragment extends Fragment implements MyAdapter.OnItemClicked{
    private static final String ARG_DATALIST = "datalist";
    private static final String ARG_RANDOMNO="randomNum";

    private DatabaseHelper databaseHelper;
    private List<LocationData> dataList;
    private String mParam2;
    private LinearLayoutManager linearLayoutManager;
    private ImageView featuredImg;
    private TextView featuredName;
    private TextView featuredDetails;
    LocationData featured;
    Button addButton;
    Button locateButton;
    private static int currentPos;
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
            currentPos=getArguments().getInt(ARG_RANDOMNO);
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
        addButton=view.findViewById(R.id.addButton);
        locateButton=view.findViewById( R.id.locate_button);

        featured = dataList.get(currentPos);
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLocationAdded(currentPos);
            }
        });
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
        currentPos=position;
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

    public interface OnMainFragmentInteractionListener {
        void onMainFragmentInteraction(int position);
        void onLocationAdded(int position);
    }
}
