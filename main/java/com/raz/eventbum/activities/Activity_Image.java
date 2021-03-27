package com.raz.eventbum.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.raz.eventbum.R;
import com.zolad.zoominimageview.ZoomInImageView;


public class Activity_Image extends Activity_Base {
    private ZoomInImageView image_IMG_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        findViews();
        initViews();

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Glide.with(this)
                .load(url)
                .into(image_IMG_image);


    }

    private void findViews() {
        image_IMG_image = findViewById(R.id.image_IMG_image);
    }

    private void initViews() {

//        PhotoViewAttacher pAttacher;
//        pAttacher = new PhotoViewAttacher(image_IMG_image);
//        pAttacher.update();


    }


}