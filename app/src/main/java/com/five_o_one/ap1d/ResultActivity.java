package com.five_o_one.ap1d;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.LinkedList;


public class ResultActivity extends FragmentActivity{
    private RecyclerView mRecyclerView;
    private LocationAdapter mAdapter;
    private final LinkedList<String> mLocationList = new LinkedList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        for (int i = 0; i < 20; i++) {
            mLocationList.addLast("Word " + i);
        }

        // Create recycler view.
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new LocationAdapter(this, mLocationList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add a floating action click handler for creating new entries.
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int wordListSize = mLocationList.size();
//                // Add a new word to the wordList.
//                mLocationList.addLast("+ Word " + wordListSize);
//                // Notify the adapter, that the data has changed.
//                mRecyclerView.getAdapter().notifyItemInserted(wordListSize);
//                // Scroll to the bottom.
//                mRecyclerView.smoothScrollToPosition(wordListSize);
//            }
//        });
    }
}