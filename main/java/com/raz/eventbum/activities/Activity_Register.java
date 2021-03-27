package com.raz.eventbum.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.raz.eventbum.R;
import com.raz.eventbum.data.MyFirebase_Authentication;
import com.raz.eventbum.data.MyFirebase_CFS;
import com.raz.eventbum.data.PhoneStorage_Management;
import com.raz.eventbum.messages.Messages_toast;
import com.raz.eventbum.utils.Util_checkInputs;
import com.raz.eventbum.utils.Util_keyboard;
import com.raz.eventbum.utils.Utils_Strings;

public class Activity_Register extends AppCompatActivity {




    private TextInputEditText register_EDT_email;
    private TextInputEditText register_EDT_password;
    private TextInputEditText register_EDT_passwordValidation;
    private TextInputEditText register_EDT_name;
    private TextInputEditText register_EDT_eventName;

    private TextInputEditText register_EDT_userCode;
    private TextInputEditText register_EDT_automaticCode;
    private TextInputLayout register_LYO_userCode;
    private TextInputLayout register_LYO_automaticCode;
    private MaterialRadioButton register_RB_chooseCode;
    private MaterialRadioButton register_RB_automaticCode;


    private MaterialButton register_BTN_startParty;

    private ProgressBar register_PB_progressBar;

    interface CallBack_userCreate {
        void onSuccess(FirebaseUser user);

        void onFailure();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        findViews();
        initViews();
        setExample();

    }

    private void setExample() {
        register_EDT_email.setText("example@example.com");
        register_EDT_password.setText("123456");
        register_EDT_passwordValidation.setText("123456");
        register_EDT_name.setText("example");
        register_EDT_eventName.setText("examp event name");
    }

    private void findViews() {
        register_BTN_startParty = findViewById(R.id.register_BTN_startParty);
        register_EDT_email = findViewById(R.id.register_EDT_email);
        register_EDT_password = findViewById(R.id.register_EDT_password);
        register_EDT_passwordValidation = findViewById(R.id.register_EDT_passwordValidation);
        register_EDT_name = findViewById(R.id.register_EDT_name);
        register_EDT_eventName = findViewById(R.id.register_EDT_eventName);


        register_RB_chooseCode = findViewById(R.id.register_RB_chooseCode);
        register_RB_automaticCode = findViewById(R.id.register_RB_automaticCode);
        register_LYO_userCode = findViewById(R.id.register_LYO_userCode);
        register_LYO_automaticCode = findViewById(R.id.register_LYO_automaticCode);

        register_EDT_userCode = findViewById(R.id.register_EDT_userCode);
        register_EDT_automaticCode = findViewById(R.id.register_EDT_automaticCode);

        register_PB_progressBar = findViewById(R.id.register_PB_progressBar);

        //register_RG_chooseCode = findViewById(R.id.register_RG_chooseCode);
    }

    private void initViews() {


        register_BTN_startParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPartyClicked();
            }
        });


        register_RB_chooseCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChooseCodeRB();
                clean_RB_Errors();
            }
        });
        register_RB_automaticCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChooseAutomaticEventCode();
                clean_RB_Errors();
            }
        });
//        register_RG_chooseCode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//
//                switch (checkedId) {
//                    case R.id.register_RB_chooseCode:
//                        userChooseCode();
//                        break;
//                    case R.id.register_RB_automaticCode:
//                        generateEventCode();
//                        break;
//
//                    default:
//                        break;
//                }
//
//
//            }
//        });

    }

    private void userChooseCodeRB() {
//        register_EDT_code.setText("");
//        register_EDT_code.setEnabled(true);
//        register_EDT_code.requestFocus();
//        Util_keyboard.showKeyboard(this);
        register_RB_automaticCode.setChecked(false);
        register_LYO_userCode.setVisibility(View.VISIBLE);
        register_LYO_automaticCode.setVisibility(View.GONE);

        register_EDT_userCode.requestFocus();
        Util_keyboard.showKeyboard(this);

    }

    private void generateAutomaticEventCode() {
        showProgressBar();
        String code = Utils_Strings.generateRandomString(15);
        MyFirebase_CFS.isCodeExist(code, new MyFirebase_CFS.CallBack_isExist() {
            @Override
            public void onFailure(String reason) {
                connectError();
                hideProgressBar();
            }

            @Override
            public void onSuccess(Boolean isExist) {
                if (isExist) {
                    generateAutomaticEventCode();
                } else {
                    register_EDT_automaticCode.setText(code);
                    hideProgressBar();
                }
            }
        });
    }

    private void userChooseAutomaticEventCode() {
        register_RB_chooseCode.setChecked(false);
        register_LYO_automaticCode.setVisibility(View.VISIBLE);
        register_LYO_userCode.setVisibility(View.GONE);
        generateAutomaticEventCode();
        Util_keyboard.hideKeyBoard(this, register_EDT_automaticCode);
    }


    private void showProgressBar() {
        register_BTN_startParty.setVisibility(View.GONE);
        register_PB_progressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar() {
        register_BTN_startParty.setVisibility(View.VISIBLE);
        register_PB_progressBar.setVisibility(View.GONE);

    }

    private void startPartyClicked() {

        clean_RB_Errors();

        if (!checkInputs())
            return;


        String email = register_EDT_email.getText().toString();
        String password = register_EDT_password.getText().toString();
        String userName = register_EDT_name.getText().toString();
        String eventName = register_EDT_eventName.getText().toString();
        String albumCode;
        if (register_RB_automaticCode.isChecked())
            albumCode = register_EDT_automaticCode.getText().toString();
        else
            albumCode = register_EDT_userCode.getText().toString();


        showProgressBar();

        MyFirebase_CFS.isCodeExist(albumCode, new MyFirebase_CFS.CallBack_isExist() {
            @Override
            public void onFailure(String reason) {
                connectError();
                hideProgressBar();

            }

            @Override
            public void onSuccess(Boolean isExist) {
                if (isExist) {

                    codeAlreadyExist();
                    hideProgressBar();
                } else {
                    createNewUser(email, password, userName, eventName, new CallBack_userCreate() {
                        @Override
                        public void onSuccess(FirebaseUser user) {

                            //Write user to CFS
                            MyFirebase_CFS.writeNewUser(user.getUid(),
                                    email,
                                    userName,
                                    eventName,
                                    albumCode,
                                    new MyFirebase_CFS.CallBack_saveData() {
                                        @Override
                                        public void onFailure(String reason) {
                                            hideProgressBar();

                                        }

                                        @Override
                                        public void onSuccess() {
                                            userCreated(user,albumCode,userName,eventName);

                                        }
                                    });

                        }

                        @Override
                        public void onFailure() {
                            hideProgressBar();

                        }
                    });
                }
            }
        });


    }

    private void userCreated(FirebaseUser user, String albumCode, String userName,String eventName) {
        userCreatedSuccessfullyMessage(user);
        PhoneStorage_Management.saveDataOfUserToPhoneMemory(userName,user.getUid(),this);
        openEventPage(albumCode,eventName);
    }

    private void clean_RB_Errors() {
        register_RB_chooseCode.setError(null);
        register_RB_automaticCode.setError(null);

    }


    private void codeAlreadyExist() {
        register_EDT_userCode.setError("קוד זה קיים במערכת, יש לבחור קוד אחר");
        register_EDT_userCode.requestFocus();
    }

    private void connectError() {
        Toast.makeText(this, "שגיאה בחיבור",
                Toast.LENGTH_SHORT).show();
    }

    private void createNewUser(String email, String password, String name, String eventName, CallBack_userCreate callBack_userCreatedSuccessfully) {
        MyFirebase_Authentication.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ptttRe", "createUserWithEmail:success");
                            FirebaseUser user = MyFirebase_Authentication.mAuth.getCurrentUser();
                            callBack_userCreatedSuccessfully.onSuccess(user);
                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w("ptttRe", "createUserWithEmail:failure", task.getException());
                            userNotCreated(task);
                        }

                    }
                });
    }

    private void userNotCreated(Task<AuthResult> task) {
        hideProgressBar();
        try {
            throw task.getException();
        }
        // if user enters wrong email.
        catch (FirebaseAuthWeakPasswordException weakPassword) {
            Log.d("ptttRe", "onComplete: weak_password");
            Toast.makeText(this, Messages_toast.Weak_password,
                    Toast.LENGTH_LONG).show();
        }
        // if user enters wrong password.
        catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
            Log.d("ptttRe", "onComplete: malformed_email");
            Toast.makeText(this, Messages_toast.Invalid_email,
                    Toast.LENGTH_LONG).show();
        } catch (FirebaseAuthUserCollisionException existEmail) {
            Log.d("ptttRe", "onComplete: exist_email");
            Toast.makeText(this, Messages_toast.This_email_exists_in_the_system,
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("ptttRe", "onComplete: " + e.getMessage());
            Toast.makeText(this, Messages_toast.Error_creating_user,
                    Toast.LENGTH_LONG).show();
        }


    }

    private void userCreatedSuccessfullyMessage(FirebaseUser user) {
        Toast.makeText(this, "המשתמש נוצר בהצלחה " + user.toString(),
                Toast.LENGTH_SHORT).show();
    }

    private boolean checkInputs() {
        if (!Util_checkInputs.checkName(register_EDT_name) ||
                !Util_checkInputs.checkEmail(register_EDT_email) ||
                !Util_checkInputs.checkPassword(register_EDT_password, register_EDT_passwordValidation) ||
                !Util_checkInputs.checkEventName(register_EDT_eventName) ||
                !Util_checkInputs.checkRBCode(register_RB_chooseCode, register_RB_automaticCode) ||
                (register_RB_chooseCode.isChecked()) && !Util_checkInputs.checkCodeNotEmpty(register_EDT_userCode)

        )
            return false;
        return true;
    }

    private void openEventPage(String albumCode,String eventName) {
        Intent eventIntent = new Intent(this, Activity_Event.class);
        eventIntent.putExtra(Activity_Event.ALBUM_CODE, albumCode);
        eventIntent.putExtra(Activity_Event.EVENT_NAME,eventName);
        startActivity(eventIntent);
        finish();
        finish();

    }


}