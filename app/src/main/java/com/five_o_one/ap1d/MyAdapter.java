package com.five_o_one.ap1d;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Choco〜bourbon on 27-Nov-17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<LocationData> dataList;
    private Context context;
    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClicked(int position);
    }

    MyAdapter(List<LocationData> dataList){
        this.dataList=dataList;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, final int position) {
        holder.bind(position);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClicked(position);
            }
        });
    }

    public void setOnClick(OnItemClicked onClick){
        this.onClick=onClick;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        LinearLayout item;
        //ImageView itemImage;
        TextView itemName;
        TextView itemAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            item=itemView.findViewById(R.id.recycler_item);
            //itemImage=itemView.findViewById(R.id.item_image);
            itemName=itemView.findViewById(R.id.item_name);
            itemAddress=itemView.findViewById(R.id.item_address);
        }

        public void bind(int position ){
            LocationData data=dataList.get(position);
            itemName.setText(data.getName());
            itemAddress.setText(data.getDetails());
            //itemImage.setImageResource(data.getImageUrl());
        }
    }
}
