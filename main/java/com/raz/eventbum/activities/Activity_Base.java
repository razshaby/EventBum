package com.raz.eventbum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.raz.eventbum.MyApp;
import com.raz.eventbum.R;
import com.raz.eventbum.data.MyFirebase_Authentication;
import com.raz.eventbum.data.PhoneStorage_Management;

public class Activity_Base extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserSignedInAndRedirect();
    }


    private void checkUserSignedInAndRedirect() {
        if (!MyFirebase_Authentication.isUserSignedIn())
        {
            openAlbumsActivity();
        }
    }

    private void openLoginActivity() {
        Intent intent = new Intent(this, Activity_Login.class);
        startActivity(intent);
        finish();
    }
    private void openAlbumsActivity() {
        Intent intent = new Intent(this, Activity_Albums.class);
        startActivity(intent);
        finish();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(MyFirebase_Authentication.isUserSignedIn())
        {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top, menu);
        return true;
        }
        return false;
    }

    //On click menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuTop_MyAlbums:
                openAlbumsActivity();
                return true;
            case R.id.menuTop_logOut:
                userLogout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void userLogout() {
        MyFirebase_Authentication.logout();
        MyApp.deleteUserInfo();
        PhoneStorage_Management.deleteDataOfUserToPhoneMemory(this);
        openLoginActivity();
    }


}
