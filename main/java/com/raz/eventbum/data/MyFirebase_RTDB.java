package com.raz.eventbum.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raz.eventbum.MyApp;
import com.raz.eventbum.objects.Album;
import com.raz.eventbum.objects.ImageInfo;

import java.util.ArrayList;


public class MyFirebase_RTDB {



    public interface CallBack_DataIsReady
    {
        void callBack_StringDataIsReady(String data);
        void callBack_ArrayImagesDataIsReady(ArrayList<ImageInfo> arrayList);
    }
    public interface CallBack_save
    {
        void onFailure(String reason);
        void onSuccess();
    }

    public static final String ALBUMS_PATH = "albums";




    public static void writeNewImageInfoToDB(String name, String albumName,String albumCode,String url,CallBack_save callBack_save) {
//        ImageInfo imageInfo = new ImageInfo(name,albumName,url,"nameOfUploader");
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child(REFERENCE_PATH+"/"+albumName).push().setValue(imageInfo).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("ptttRLDB", "onFailure: "+e.toString());
//            }
//        });
        ImageInfo imageInfo = new ImageInfo(name,albumName,url, MyApp.getUserInfo().getUserName());
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(ALBUMS_PATH+"/"+albumCode).push().setValue(imageInfo).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ptttRLDB", "onFailure: "+e.toString());
                callBack_save.onFailure(e.toString());

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            callBack_save.onSuccess();
            }
        });

    }


    public static void readAllImagesByAlbumCode(String albumCode, CallBack_DataIsReady callBack_dataIsReady)
    {
        FirebaseDatabase.getInstance().getReference().child(ALBUMS_PATH+"/"+albumCode)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    ArrayList<ImageInfo> arrayList = new ArrayList();
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ImageInfo image = snapshot.getValue(ImageInfo.class);
                            arrayList.add(image);

                        }
                        callBack_dataIsReady.callBack_ArrayImagesDataIsReady(arrayList);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public static void uploadString(String data,String path)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(path).setValue(data);
    }




    public static void downloadString(CallBack_DataIsReady callBack_dataIsReady,String path)
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if(callBack_dataIsReady!=null)
                {
                    callBack_dataIsReady.callBack_StringDataIsReady(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
