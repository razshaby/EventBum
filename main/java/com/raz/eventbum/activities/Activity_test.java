package com.raz.eventbum.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.raz.eventbum.HeadsetReceiver;
import com.raz.eventbum.R;
import com.raz.eventbum.adapters.Adapter_DemoData;
import com.raz.eventbum.data.MyFirebase_RTDB_tests;
import com.raz.eventbum.objects.Demo_Data;
import com.raz.eventbum.objects.ImageInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_test extends AppCompatActivity {

    private HeadsetReceiver receiver;
    private ListView test_LST_test;
    private ArrayList<Demo_Data> arrayList;
    private Button test_BTN_add;
    private Button test_BTN_read;
    private Button test_BTN_makeSound;
    private ToggleButton toggleButton;


    private EditText test_EDT_id;
    private EditText test_EDT_name;
    private EditText test_EDT_number;


    private CameraManager mCameraManager;
    private String mCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.d("lifecycle","onCreate");

        arrayList = new ArrayList();
        checkFlash();
        getCameraManager();
        findViews();
        initViews();
        receiver = new HeadsetReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume");
//        IntentFilter headSetFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
//        registerReceiver(receiver, headSetFilter);

        registerReceiver(receiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause");
        unregisterReceiver(receiver);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy");
    }

    private void getCameraManager() {
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void checkFlash() {
        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {
            showNoFlashError();
        }
    }

    private void initViews() {


        test_BTN_makeSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNoise();
            }
        });

        test_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < 10; i++) {
                    map.put("_" + i, "hello+" + i);
                }
                MyFirebase_RTDB_tests.writeNewDemo_data(test_EDT_id.getText().toString(), test_EDT_name.getText().toString(), Integer.parseInt(test_EDT_number.getText().toString()), map, new MyFirebase_RTDB_tests.CallBack_save() {
                    @Override
                    public void onFailure(String reason) {
                        Toast.makeText(Activity_test.this, "נכשל",
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(Activity_test.this, "נשמר בהצלחה",
                                Toast.LENGTH_SHORT).show();
                    }
                });


                MyFirebase_RTDB_tests.writeNewDemo_data_key(test_EDT_id.getText().toString(), true, new MyFirebase_RTDB_tests.CallBack_save() {
                    @Override
                    public void onFailure(String reason) {

                    }

                    @Override
                    public void onSuccess() {

                    }
                });


            }
        });


        test_BTN_read.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 MyFirebase_RTDB_tests.readAll_Demo_data(new MyFirebase_RTDB_tests.CallBack_DataIsReady() {
                                                     @Override
                                                     public void callBack_StringDataIsReady(String data) {

                                                     }

                                                     @Override
                                                     public void callBack_ArrayImagesDataIsReady(ArrayList<Demo_Data> arrayListRe) {
                                                         for (int i = 0; i < arrayListRe.size(); i++) {
                                                             Log.d("pttt", arrayListRe.get(i).toString());
                                                         }
                                                         arrayList = arrayListRe;
                                                         initListView();
                                                     }
                                                 });

                                             }
                                         }
        );


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //called when the button status is changed
                switchFlashLight(isChecked);

            }
        });
    }

    private void onItemListViewClicked(int pos)
    {
        Toast.makeText(this,"The id is "  + arrayList.get(pos).getId(),
                Toast.LENGTH_SHORT).show();

    }

    private void initListView() {
        Adapter_DemoData adapter = new Adapter_DemoData(this, arrayList);
        test_LST_test.setAdapter(adapter);
        test_LST_test.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                onItemListViewClicked(position);

            }

        });

    }


    public void switchFlashLight(boolean status) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void findViews() {
        test_LST_test = findViewById(R.id.test_LST_test);
        test_BTN_add = findViewById(R.id.test_BTN_add);
        test_BTN_read = findViewById(R.id.test_BTN_read);
        toggleButton = findViewById(R.id.toggleButton);
        test_BTN_makeSound = findViewById(R.id.test_BTN_makeSound);
        test_EDT_id = findViewById(R.id.test_EDT_id);
        test_EDT_name = findViewById(R.id.test_EDT_name);
        test_EDT_number = findViewById(R.id.test_EDT_number);
    }


    public void showNoFlashError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash not available in this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

    private void makeNoise() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.tiger);
        mp.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.release();
            }
        }, 1000);
    }

}