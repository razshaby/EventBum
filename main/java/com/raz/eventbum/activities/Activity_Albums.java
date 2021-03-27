package com.raz.eventbum.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.raz.eventbum.R;
import com.raz.eventbum.adapters.Adapter_Albums;
import com.raz.eventbum.data.MyFirebase_CFS;
import com.raz.eventbum.data.MyFirebase_RTDB;
import com.raz.eventbum.fragments.Fragment_add_album;
import com.raz.eventbum.objects.Album;

import java.util.ArrayList;

public class Activity_Albums extends Activity_Base {

    private RecyclerView albums_LST_albums;
    private Fragment_add_album fragment_add_album;
    private MaterialButton albums_BTN_openAddAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        findViews();
        setViews();
        getAlbums();
        initFragments();
    }


    private void initFragments() {
        fragment_add_album = new Fragment_add_album();
        FragmentTransaction fragmentTransaction_Top = getSupportFragmentManager().beginTransaction();
        fragmentTransaction_Top.add(R.id.albums_LAY_topLayout, fragment_add_album);
        fragmentTransaction_Top.commit();
        hide_add_album();
    }

    private void findViews() {
        albums_LST_albums = findViewById(R.id.albums_LST_albums);
        albums_BTN_openAddAlbum = findViewById(R.id.albums_BTN_openAddAlbum);

    }
    private void setViews() {
        albums_BTN_openAddAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrHideAddAlbum();
            }
        });

    }

    private void showOrHideAddAlbum() {
        if (fragment_add_album.isVisible()) {
            hide_add_album();
        } else
            show_add_album();
    }

    private void show_add_album() {
        getSupportFragmentManager().beginTransaction().show(fragment_add_album).commit();
    }

    private void hide_add_album() {
        getSupportFragmentManager().beginTransaction().hide(fragment_add_album).commit();
    }


    private void setAdapter(ArrayList<Album> albums) {
        Adapter_Albums adapter_movie = new Adapter_Albums(this, albums);
        adapter_movie.setClickListener(new Adapter_Albums.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openEventActivity(albums, position);
            }

        });

        albums_LST_albums.setLayoutManager(new LinearLayoutManager(this));
        albums_LST_albums.setAdapter(adapter_movie);
    }

    private void openEventActivity(ArrayList<Album> albums, int position) {
        // Toast.makeText(Activity_Albums.this, albums.get(position).getName(), Toast.LENGTH_SHORT).show();
        Intent eventIntent = new Intent(this, Activity_Event.class);
        eventIntent.putExtra(Activity_Event.ALBUM_CODE, albums.get(position).getCode());
        eventIntent.putExtra(Activity_Event.EVENT_NAME, albums.get(position).getName());
        startActivity(eventIntent);
    }


    private void getAlbums() {

//        ArrayList<Album> albums = new ArrayList<>();//        MyFirebase_RTDB.readAlbums();
//        albums.add(new Album("בר מצווה1", "123"));
//        albums.add(new Album("בר מצווה2", "1234"));
//        albums.add(new Album("בר מצווה3", "1235"));
//        albums.add(new Album("בר מצווה4", "1236"));

        MyFirebase_CFS.readAlbums(new MyFirebase_CFS.CallBack_readAlbums() {
            @Override
            public void getAlbums(ArrayList<Album> albums) {
                setAdapter(albums);
            }
        });


    }
}