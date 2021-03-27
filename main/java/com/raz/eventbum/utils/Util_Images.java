package com.raz.eventbum.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.raz.eventbum.activities.Activity_Event;
import com.raz.eventbum.data.MyFirebase_Storage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util_Images {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int GET_FROM_GALLERY = 2;

    public static final String LOGO_IMAGE_NAME = "logo";



    private static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
       //String currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    public static void takePhotoFromGallery(Context context) {
        ((Activity)context).startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    public static File takePhotoFromCamera(Context context) {
        File photoFile=null;

        //                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 1);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile(context);
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(context,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                ((Activity)context).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        return photoFile;

    }

    public static void saveImageFromfileToStorage_and_saveImageInfoToRTDB(File photoFile,Context context,String albumCode,MyFirebase_Storage.CallBack_upload callBack_upload) {

        //Not high resolution
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            event_IMG_image.setImageBitmap(imageBitmap);

        File imgFile = new File(photoFile.getAbsolutePath());

        if (imgFile.exists()) {


//                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                event_IMG_image.setImageBitmap(myBitmap);

            MyFirebase_Storage.uploadImage_toStorage_andSaveInfoToRTDB(photoFile.getAbsolutePath(), context, albumCode, new MyFirebase_Storage.CallBack_upload() {
                @Override
                public void onFailure(String reason) {
                    callBack_upload.onFailure(reason);
                }

                @Override
                public void onSuccess() {
                    callBack_upload.onSuccess();
                }
            });


            Uri imageUri = Uri.fromFile(imgFile);

            //TODO show to user before uploaded
//            Glide.with(this)//Show picture
//                    .load(imageUri)
//                    .into(event_IMG_image);


        }
    }


    public static int getImageGlide(String imageName,Context context) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }

}
