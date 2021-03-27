package com.raz.eventbum.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.raz.eventbum.adapters.MyRecyclerViewAdapter;
import com.raz.eventbum.data.MyFirebase_RTDB;
import com.raz.eventbum.data.MyFirebase_Storage;
import com.raz.eventbum.R;
import com.raz.eventbum.objects.ImageInfo;
import com.raz.eventbum.recyclerView.GridSpacingItemDecoration;
import com.raz.eventbum.utils.Util_Images;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Activity_Event extends Activity_Base {


    public static final String ALBUM_CODE = "ALBUM_CODE";
    public static final String EVENT_NAME = "EVENT_NAME";



    private String albumCode;
    private String eventName;
    private MaterialTextView event_LBL_eventName;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView event_IMG_image;
    private File photoFile;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
//    private MaterialButton event_BTN_addImage;
    private FloatingActionButton event_floatingactionbutton_addImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);




        setEventName(savedInstanceState);

        getDataFromLastActivity();
        findViews();
        initViews();

        // MyFirebase_Storage.loadImage("JPEG_20201207_143055_2364604087298963852.jpg",this,event_IMG_image);


//        MyFirebaseRLDB.downloadString(new MyFirebaseRLDB.CallBack_DataIsReady() {
//            @Override
//            public void callBack_DataIsReady(String data) {
//                Log.d("ptttEVENT", data);
//            }
//        });

    }

    private void setEventName(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                eventName= null;
            } else {
                eventName= extras.getString(Activity_Event.EVENT_NAME);
            }
        } else {
            eventName= (String) savedInstanceState.getSerializable(Activity_Event.EVENT_NAME);
        }
    }

    private void getDataFromLastActivity() {
        albumCode = getIntent().getStringExtra(ALBUM_CODE);
        eventName = getIntent().getStringExtra(EVENT_NAME);
    }

    private void initViews() {
        readData();

//        event_BTN_addImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addImage();
//            }
//        });

        event_floatingactionbutton_addImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addImage();

            }
        });

        event_LBL_eventName.setText(eventName);

    }

    private void addImage() {
        selectImage(this);
    }

    private void readData() {
        MyFirebase_RTDB.readAllImagesByAlbumCode(albumCode, new MyFirebase_RTDB.CallBack_DataIsReady() {
            @Override
            public void callBack_StringDataIsReady(String data) {

            }

            @Override
            public void callBack_ArrayImagesDataIsReady(ArrayList<ImageInfo> arrayList) {
                for (int i = 0; i < arrayList.size(); i++) {
                    Log.d("ptttWelcome", arrayList.get(i).toString());
                }
                initRCYV(arrayList);
            }
        });
    }

    private void initRCYV(ArrayList<ImageInfo> arrayList) {
        // data to populate the RecyclerView with
        //String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"};


        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.event_RCLV_imagesRCLV);
        final int numberOfColumns = 3;

//        int spanCount = 3; // 3 columns
//        int spacing = 50; // 50px
//        boolean includeEdge = false;
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
//

        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, arrayList);
        // myRecyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(myRecyclerViewAdapter);




    }

    private void findViews() {
        event_LBL_eventName = findViewById(R.id.event_LBL_eventName);
        event_IMG_image = findViewById(R.id.event_IMG_image);
//        event_BTN_addImage = findViewById(R.id.event_BTN_addImage);
        event_floatingactionbutton_addImage= findViewById(R.id.event_floatingactionbutton_addImage);
    }


    private void selectImage(Context context) {


        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_photo_from_galary), getString(R.string.cancle)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("בחר תמונה לאלבום:");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.take_photo))) {

                    // takePhotoFromCamera();
                    photoFile = Util_Images.takePhotoFromCamera(Activity_Event.this);

                } else if (options[item].equals(getString(R.string.choose_photo_from_galary))) {

                    Util_Images.takePhotoFromGallery(Activity_Event.this);
                    // takePhotoFromGallery();

                } else if (options[item].equals(getString(R.string.cancle))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if (requestCode == Util_Images.GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //event_IMG_image.setImageBitmap(bitmap); //displayImageBeforeUpload //TODO

//

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataByte = baos.toByteArray();

                MyFirebase_Storage.uploadByte(dataByte, albumCode, new MyFirebase_Storage.CallBack_upload() {
                    @Override
                    public void onFailure(String reason) {
                        uploadImageFailed();

                    }

                    @Override
                    public void onSuccess() {
                        uploadImageSuccess();

                    }
                });//


            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
          //  saveImageFromCamera(photoFile);
            Util_Images.saveImageFromfileToStorage_and_saveImageInfoToRTDB(photoFile, this, albumCode, new MyFirebase_Storage.CallBack_upload() {
                @Override
                public void onFailure(String reason) {
                    uploadImageFailed();
                }

                @Override
                public void onSuccess() {
                    uploadImageSuccess();
                }
            });
        }
    }


private void uploadImageSuccess()
{
    Toast.makeText(this, "התמונה הועלתה בהצלחה" ,
            Toast.LENGTH_SHORT).show();
}

    private void uploadImageFailed()
    {
        Toast.makeText(this, "התמונה לא הועלתה" ,
                Toast.LENGTH_SHORT).show();
    }

//
//
//    private void saveImageFromCamera(File photoFile) {
//
//        //Not high resolution
////            Bundle extras = data.getExtras();
////            Bitmap imageBitmap = (Bitmap) extras.get("data");
////            event_IMG_image.setImageBitmap(imageBitmap);
//
//        File imgFile = new File(photoFile.getAbsolutePath());
//
//        if (imgFile.exists()) {
//
//
////                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
////                event_IMG_image.setImageBitmap(myBitmap);
//
//            MyFirebase_Storage.uploadImage_toStorage_andSaveInfoToRTDB(photoFile.getAbsolutePath(),this ,albumCode);
//
//
//            Uri imageUri = Uri.fromFile(imgFile);
//            //Show picture
//            Glide.with(this)
//                    .load(imageUri)
//                    .into(event_IMG_image);
//
//
//        }
//    }
//

}