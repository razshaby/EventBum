package com.raz.eventbum.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.raz.eventbum.R;
import com.raz.eventbum.data.MyFirebase_Authentication;
import com.raz.eventbum.data.MyFirebase_Storage;
import com.raz.eventbum.utils.Util_Images;

public class Activity_Welcome extends AppCompatActivity {

    private MaterialButton welcome_BTN_enter;
    private ImageView welcome_IMG_logo;
    private Button welcome_BTN_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        checkIfUserLoggedIn();

        findViews();
        initViews();


    }

    private void checkIfUserLoggedIn() {
        if(MyFirebase_Authentication.isUserSignedIn())
        {
            openAlbumsActivity();
            finish();
        }
    }

    void openAlbumsActivity()
    {
        Intent albumsIntent = new Intent(this, Activity_Albums.class);
        startActivity(albumsIntent);
    }

    private void openEventPage() {
        Intent eventIntent = new Intent(this, Activity_Event.class);
        startActivity(eventIntent);
        finish();

    }

    private void findViews() {
        welcome_BTN_enter = findViewById(R.id.welcome_BTN_enter);
        welcome_IMG_logo = findViewById(R.id.welcome_IMG_logo);
        welcome_BTN_check= findViewById(R.id.welcome_BTN_check);
    }

    private void initViews() {


        Glide.with(this).load(Util_Images.getImageGlide(Util_Images.LOGO_IMAGE_NAME,this)).into(welcome_IMG_logo);

        welcome_BTN_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventIntent = new Intent(Activity_Welcome.this, Activity_test.class);
                startActivity(eventIntent);
            }
        });
        welcome_BTN_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });
    }



    private void openLoginActivity() {
        Intent registerIntent = new Intent(this, Activity_Login.class);
        startActivity(registerIntent);
    }


}