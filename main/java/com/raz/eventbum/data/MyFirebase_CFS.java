package com.raz.eventbum.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.raz.eventbum.MyApp;
import com.raz.eventbum.objects.Album;
import com.raz.eventbum.objects.ImageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyFirebase_CFS {


    public interface CallBack_readAlbums {

        void getAlbums(ArrayList<Album> albums);



    }

    public interface CallBack_isExist {

        void onFailure(String reason);

        void onSuccess(Boolean isExist);

    }


    public interface CallBack_saveData {

        void onFailure(String reason);

        void onSuccess();

    }

    public interface CallBack_readData {
        void callBack_DataIsReady(String data);
    }

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String USERS_PATH = "users";
    public static String ALBUMS_PATH_TO_USER = "AlbumsCode_To_UserID";
    public static String ALBUMS_PATH = "albums";

    public static String EMAIL_KEY = "email";
    public static String NAME_KEY = "name";
    public static String USERID_KEY = "userID";
    public static String EVENT_NAME_KEY = "eventName";
    public static String TIME_AND_DATE_KEY = "time";

    public static void isCodeExist(String code, CallBack_isExist callBack_isExist) {
        DocumentReference ref = db.collection(ALBUMS_PATH_TO_USER).document(code);

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        callBack_isExist.onSuccess(true);
                    } else {
                        callBack_isExist.onSuccess(false);
                    }
                } else {
                    callBack_isExist.onFailure("general error");
                    Log.d("ptttCFS", "error");
                }
            }
        });


//        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//        DocumentReference docIdRef = rootRef.collection(ALBUMS_PATH_TO_USER).document(code);
//        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d("ptttCFS", "Document exists!");
//                    } else {
//                        Log.d("ptttCFS", "Document does not exist!");
//                    }
//                } else {
//                    Log.d("ptttCFS", "Failed with: ", task.getException());
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("ptttCFS", "Failed with: ",  e);
//
//            }
//        });


    }

    public static void writeNewAlbumCode(String albumCode, String userID, String eventName, CallBack_saveData callBack_saveData) {
        //TODO writeNewCode
//        Map<String, Object> emptyMap = new HashMap<>();
////        codeMap.put("code", code);

        Map<String, Object> albumMap = new HashMap<>();
        albumMap.put(EVENT_NAME_KEY, eventName);


//        db.collection(USERS_PATH)
//                .document(userID)
//                .update(codeMap);

        DocumentReference documentReference = db.collection(USERS_PATH).document(userID).collection(ALBUMS_PATH).document(albumCode);
        documentReference.set(albumMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                write_codeAlbum_to_AlbumsCode_To_UserID(userID, albumCode, eventName, callBack_saveData);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack_saveData.onFailure(e.toString());

            }
        });


    }

    private static void write_codeAlbum_to_AlbumsCode_To_UserID(String userID, String albumCode, String eventName, CallBack_saveData callBack_saveData) {
        Map<String, Object> userIDMap = new HashMap<>();
        userIDMap.put(USERID_KEY, userID);
        userIDMap.put(EVENT_NAME_KEY, eventName);
        userIDMap.put(TIME_AND_DATE_KEY, System.currentTimeMillis());


        DocumentReference documentReference = db.collection(ALBUMS_PATH_TO_USER).document(albumCode);
        documentReference.set(userIDMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack_saveData.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack_saveData.onFailure(e.toString());

            }
        });
    }

    public static void writeNewUser(String UID, String email, String name, String eventName, String albumCode, CallBack_saveData callBack_saveData) {

        Map<String, Object> user = new HashMap<>();
        user.put(EMAIL_KEY, email);
        user.put(NAME_KEY, name);
        // user.put(EVENT_NAME_KEY,eventName);


        DocumentReference documentReference = db.collection(USERS_PATH).document(UID);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                writeNewAlbumCode(albumCode, UID, eventName, callBack_saveData);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack_saveData.onFailure(e.toString());
            }
        });
    }



    public static void writeNewUserExample(String UID) {

////Example1
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);
//
//// Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("ptttCFS", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("ptttCFS", "Error adding document", e);
//                    }
//                });


//Example2
//
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Alan");
//        user.put("middle", "Mathison");
//        user.put("last", "Turing");
//        user.put("born", 1912);
//
//// Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("ptttCFS", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("ptttCFS", "Error adding document", e);
//                    }
//                });


    }

    public static void readUserInformation(String UID, String field, MyFirebase_CFS.CallBack_readData callBack_readData) {
        // FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection(USERS_PATH).document(UID);


        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("ptttMFCFS", documentSnapshot.getData() + "");

                callBack_readData.callBack_DataIsReady(documentSnapshot.getString(field));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ptttMFCFS", e + "");

            }
        });


        //example1
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("ptttCFS", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w("ptttCFS", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
    }
    public static void readAlbums(CallBack_readAlbums callBack_readAlbums) {

        ArrayList<Album> albums = new ArrayList<>();
        CollectionReference collectionReference = db.collection(USERS_PATH).document(MyApp.getUserInfo().getUserID()).collection(ALBUMS_PATH);


        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    albums.add(new Album(document.getData().get(EVENT_NAME_KEY).toString(),document.getId()));
                    Log.d("ptttMFCFS",document.getId() + " => " + document.getData().get(EVENT_NAME_KEY));
                }
                callBack_readAlbums.getAlbums(albums);
            }
        });

    }


}
