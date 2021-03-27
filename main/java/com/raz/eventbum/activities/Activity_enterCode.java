package com.raz.eventbum.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textview.MaterialTextView;
import com.raz.eventbum.R;

public class Activity_enterCode extends AppCompatActivity {

    private MaterialTextView enterCode_LBL_registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        findViews();
        initViews();
    }

    private void findViews() {
        enterCode_LBL_registered = findViewById(R.id.enterCode_LBL_registered);
    }

    private void initViews() {

        enterCode_LBL_registered.setOnClickListener(new View.OnClickListener() {
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