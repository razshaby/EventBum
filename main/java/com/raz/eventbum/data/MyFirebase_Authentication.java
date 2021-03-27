package com.raz.eventbum.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyFirebase_Authentication {
    public final static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static boolean isUserSignedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            return true;
        return false;
    }


    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }



}
