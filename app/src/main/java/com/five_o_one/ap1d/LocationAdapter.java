/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.five_o_one.ap1d;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Shows how to implement a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private final List<Route> mLocationList;
    private final LayoutInflater mInflater;

    class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView locationView;
        public final TextView timeView;
        public final ImageView vehicleView;
        final LocationAdapter mAdapter;

        /**
         * Creates a new custom view holder to hold the view to display in the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and views for the RecyclerView.
         */
        public LocationHolder(View itemView, LocationAdapter adapter) {
            super(itemView);
            locationView = (TextView) itemView.findViewById(R.id.location);
            timeView = (TextView) itemView.findViewById(R.id.time);
            vehicleView = (ImageView) itemView.findViewById(R.id.transportsymbol);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // All we do here is prepend "Clicked! " to the text in the view, to verify that
            // the correct item was clicked. The underlying data does not change.
            locationView.setText ("Clicked! "+ locationView.getText());
        }
    }

    public LocationAdapter(Context context, List<Route> locationList) {
        mInflater = LayoutInflater.from(context);
        this.mLocationList = locationList;
    }

    /**
     * Inflates an item view and returns a new view holder that contains it.
     * Called when the RecyclerView needs a new view holder to represent an item.
     *
     * @param parent The view group that holds the item views.
     * @param viewType Used to distinguish views, if more than one type of item view is used.
     * @return a view holder.
     */
    @Override
    public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(R.layout.locationlist_item, parent, false);
        return new LocationHolder(mItemView, this);
    }

    /**
     * Sets the contents of an item at a given position in the RecyclerView.
     * Called by RecyclerView to display the data at a specificed position.
     *
     * @param holder The view holder for that position in the RecyclerView.
     * @param position The position of the item in the RecycerView.
     */
    @Override
    public void onBindViewHolder(LocationHolder holder, int position) {
        // Retrieve the data for that position.
        Route mCurrent = mLocationList.get(position);
        String location = mCurrent.destination;
        int transport = mCurrent.transportType;
        String timeCost = mCurrent.getTimeCost();
        if(Integer.valueOf(timeCost)/60==0){
            timeCost = timeCost+"min";
        }
        else{
            timeCost = Integer.valueOf(timeCost)/60 + "h" + Integer.valueOf(timeCost)%60 + "m";
        }
        // Add the data to the view holder.
        holder.locationView.setText(location);
        holder.timeView.setText(timeCost);
        if(transport==0){
            holder.vehicleView.setImageResource(R.drawable.walking);
        }
        else if(transport==1){
            holder.vehicleView.setImageResource(R.drawable.bus);
        }
        else if(transport==2){
            holder.vehicleView.setImageResource(R.drawable.cab);
        }
    }

    /**
     * Returns the size of the container that holds the data.
     *
     * @return Size of the list of data.
     */
    @Override
    public int getItemCount() {
        return mLocationList.size();
    }
}


