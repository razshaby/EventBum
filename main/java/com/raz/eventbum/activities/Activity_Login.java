package com.raz.eventbum.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.raz.eventbum.R;
import com.raz.eventbum.data.MyFirebase_CFS;
import com.raz.eventbum.data.PhoneStorage_Management;
import com.raz.eventbum.utils.Util_Images;

public class Activity_Login extends AppCompatActivity {

    private TextInputEditText login_EDT_email;
    private TextInputEditText login_EDT_password;

    private MaterialButton login_BTN_login;

    private MaterialTextView login_LBL_createCode;


    private ImageView login_IMG_logo;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initViews();
        initAuthentication();


        {//TODO - remove (for testing)
            login_EDT_email.setText("example3@example.com");
            login_EDT_password.setText("123456");
        }


        //logout
        //FirebaseAuth.getInstance().signOut();

    }

    private void initViews() {
        Glide.with(this).load(Util_Images.getImageGlide(Util_Images.LOGO_IMAGE_NAME, this)).into(login_IMG_logo);


        login_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(login_EDT_email.getText().toString(), login_EDT_password.getText().toString());
            }
        });

        login_LBL_createCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    private void openRegisterActivity() {
        Intent registerIntent = new Intent(this, Activity_Register.class);
        startActivity(registerIntent);
    }


    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ptttLogin", "signInWithEmail:success");
                            loginSuccessful();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("ptttLogin", "signInWithEmail:failure", task.getException());
                            loginFailed();
                        }

                        // ...
                    }
                });
    }

    private void loginFailed() {
        Toast.makeText(this, "ההתחברות נכשלה",
                Toast.LENGTH_SHORT).show();
    }

    //Open albums activity after successful login
    private void loginSuccessful() {
        FirebaseUser user = mAuth.getCurrentUser();
        getUserDetailsAndSaveToPhone(user);

        Toast.makeText(this, "התחברת בהצלחה  " + user.toString(),
                Toast.LENGTH_SHORT).show();


    }

    void openAlbumsActivity() {
        //TODO add intent parameters
        Intent albumsIntent = new Intent(this, Activity_Albums.class);
        startActivity(albumsIntent);
    }

    private void getUserDetailsAndSaveToPhone(FirebaseUser user) {
        MyFirebase_CFS.readUserInformation(user.getUid(), MyFirebase_CFS.NAME_KEY, new MyFirebase_CFS.CallBack_readData() {
            @Override
            public void callBack_DataIsReady(String data) {
                PhoneStorage_Management.saveDataOfUserToPhoneMemory(data, user.getUid(), Activity_Login.this);
                openAlbumsActivity();
                finish();
            }
        });

    }


    private void findViews() {
        login_EDT_email = findViewById(R.id.login_EDT_email);
        login_EDT_password = findViewById(R.id.login_EDT_password);
        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_LBL_createCode = findViewById(R.id.login_LBL_createCode);
        login_IMG_logo = findViewById(R.id.login_IMG_logo);
    }


    private void initAuthentication() {
        mAuth = FirebaseAuth.getInstance();
    }


}