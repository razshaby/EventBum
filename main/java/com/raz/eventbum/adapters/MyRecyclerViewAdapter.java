package com.raz.eventbum.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raz.eventbum.R;
import com.raz.eventbum.activities.Activity_Image;
import com.raz.eventbum.activities.Activity_Welcome;
import com.raz.eventbum.objects.ImageInfo;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ImageInfo> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    // Data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, ArrayList<ImageInfo>  data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
    }

    // Inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageUrl = mData.get(position).getUrl();
        //holder.myImageView.setText(animal);
        Glide.with(holder.myImageView.getContext())
                .load(imageUrl)
                .into(holder.myImageView);
    }

    // Total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView myImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            myImageView =  itemView.findViewById(R.id.recyclerviewImage_IMG_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(view, getAdapterPosition());
        }
    }

    // Convenience method for getting data at click position
    public ImageInfo getItem(int id) {
        return mData.get(id);
    }

    // Method that executes your code for the action received
    public void onItemClick(View view, int position) {
        Log.d("ptttAdapter", "You clicked number " + getItem(position).toString() + ", which is at cell position " + position);


        Intent myIntent = new Intent(context, Activity_Image.class);
        myIntent.putExtra("url", getItem(position).getUrl());
        context.startActivity(myIntent);

    }

}

