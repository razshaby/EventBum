package com.raz.eventbum.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.raz.eventbum.R;
import com.raz.eventbum.objects.Album;

import java.util.List;

public class Adapter_Albums extends RecyclerView.Adapter<Adapter_Albums.MyViewHolder> {

    private List<Album> albums;
    private LayoutInflater mInflater;
    private MyItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_Albums(Context context, List<Album> _albums) {
        this.mInflater = LayoutInflater.from(context);
        this.albums = _albums;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_album, parent, false);
        return new MyViewHolder(view);
    }



   

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("pttt", "Position = " + position);
        Album album = albums.get(position);
        holder.album_LBL_name.setText( album.getName());

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return albums.size();
    }
    
    // convenience method for getting data at click position
    Album getItem(int id) {
        return albums.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(MyItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }



    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends ViewHolder {

        TextView album_LBL_name;

        MyViewHolder(View itemView) {
            super(itemView);
           
            album_LBL_name = itemView.findViewById(R.id.album_LBL_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });
        }
    }

}