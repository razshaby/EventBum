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
import com.raz.eventbum.objects.Demo_Data;
import com.raz.eventbum.objects.ImageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class MyFirebase_RTDB_tests {
    public interface CallBack_DataIsReady
    {
        void callBack_StringDataIsReady(String data);
        void callBack_ArrayImagesDataIsReady(ArrayList<Demo_Data> arrayList);
    }
    public interface CallBack_save
    {
        void onFailure(String reason);
        void onSuccess();
    }

    public static final String DEMOS_PATH = "demos";
    public static final String DEMOS_ID_PATH = "demos_id";


//    public static void writeNewImageInfoToDB(String name, String albumName, String albumCode, String url, CallBack_save callBack_save) {
//        ImageInfo imageInfo = new ImageInfo(name,albumName,url, MyApp.getUserInfo().getUserName());
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child(DEMOS_PATH+"/"+albumCode).push().setValue(imageInfo).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("ptttRLDB", "onFailure: "+e.toString());
//                callBack_save.onFailure(e.toString());
//
//            }
//        }).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                callBack_save.onSuccess();
//            }
//        });
//
//    }

    public static void writeNewDemo_data(Demo_Data demo_Data) {


    }

    public static void writeNewDemo_data_key(String id, boolean active, CallBack_save callBack_save) {
        Demo_Data demo_Data = new Demo_Data(id,active);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(DEMOS_ID_PATH).child(id).setValue(demo_Data).addOnFailureListener(new OnFailureListener() {
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


    public static void writeNewDemo_data(String id,String name, int number, HashMap<String,String> map, CallBack_save callBack_save) {
        Demo_Data demo_Data = new Demo_Data(id,name,number,map);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(DEMOS_PATH).push().setValue(demo_Data).addOnFailureListener(new OnFailureListener() {
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

    public static void readAll_Demo_data(CallBack_DataIsReady callBack_dataIsReady)
    {
        FirebaseDatabase.getInstance().getReference().child(DEMOS_PATH)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    ArrayList<Demo_Data> arrayList = new ArrayList();
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Demo_Data demo_Data = snapshot.getValue(Demo_Data.class);

                            arrayList.add(demo_Data);

                        }
                        changeState( arrayList, callBack_dataIsReady);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private static void changeState(ArrayList<Demo_Data> arrayList, CallBack_DataIsReady callBack_dataIsReady) {
        HashMap<String,Demo_Data> idMaps = new HashMap();
        FirebaseDatabase.getInstance().getReference().child(DEMOS_ID_PATH)
                //.addListenerForSingleValueEvent(new ValueEventListener() { //Not Real Time
        .addValueEventListener(new ValueEventListener() {//RealTime
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Demo_Data demo_Data = snapshot.getValue(Demo_Data.class);

                            idMaps.put(snapshot.getKey(),demo_Data);

                        }

                        for(int i=0;i<arrayList.size();i++)
                        {
                            Demo_Data demo_Data = idMaps.get(arrayList.get(i).getId());
                            arrayList.get(i).setActive(  demo_Data.isActive()  );
                        }
                        callBack_dataIsReady.callBack_ArrayImagesDataIsReady(arrayList);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }




}
