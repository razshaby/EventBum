package com.raz.eventbum.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MyFirebase_Storage {

    public interface CallBack_upload {
        void onFailure(String reason);

        void onSuccess();
    }


    private static final String PATH = "images";

    public static void loadImage(String data, Context context, ImageView imageView) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference imageRef = storageReference.child(PATH + "/" + data);

        Glide.with(context)
                .load(imageRef)
                .into(imageView);

    }


    public static void uploadImage_toStorage_andSaveInfoToRTDB(String data, Context context, String albumCode, CallBack_upload callBack_upload) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        Uri file = Uri.fromFile(new File(data));
        StorageReference imageRef = storageRef.child(PATH + "/"+albumCode+"/" + file.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("ptttFBS", "onFailure: ");
                callBack_upload.onFailure(exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("ptttFBS", "onSuccess: ");
                Toast.makeText(context, "התמונה הועלתה בהצלחה",
                        Toast.LENGTH_SHORT).show();

//                String name = taskSnapshot.getMetadata().getName();
//                String url = taskSnapshot.getUploadSessionUri().toString();

                // use Firebase Realtime Database to store [name + url]
//                MyFirebase_RLDB.writeNewImageInfoToDB(name, "albumName",url);

                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        String name = taskSnapshot.getMetadata().getName();
                        String url = uri.toString();

                        // just do your task //like hashmaps to put in
                        MyFirebase_RTDB.writeNewImageInfoToDB(name, "albumName", albumCode, url, new MyFirebase_RTDB.CallBack_save() {
                            @Override
                            public void onFailure(String reason) {
                                callBack_upload.onFailure(reason);
                            }

                            @Override
                            public void onSuccess() {
                                callBack_upload.onSuccess();
                            }
                        });

                    }
                });
            }
        });


    }


    public static void uploadByte(byte[] dataByte, String albumCode, CallBack_upload callBack_upload) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" + UUID.randomUUID().toString().replace("-", "") + ".jpg";


        StorageReference imageRef = storageRef.child(PATH + "/" + imageFileName);

        UploadTask uploadTask = imageRef.putBytes(dataByte);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        String name = taskSnapshot.getMetadata().getName();
                        String url = uri.toString();

                        // just do your task //like hashmaps to put in
                        MyFirebase_RTDB.writeNewImageInfoToDB(name, "albumName", albumCode, url, new MyFirebase_RTDB.CallBack_save() {
                            @Override
                            public void onFailure(String reason) {
                                callBack_upload.onFailure(reason);
                            }

                            @Override
                            public void onSuccess() {
                                callBack_upload.onSuccess();
                            }
                        });

                    }
                });
            }
        });

    }
}
