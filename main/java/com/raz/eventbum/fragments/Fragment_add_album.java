package com.raz.eventbum.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.raz.eventbum.R;
import com.raz.eventbum.data.MyFirebase_Authentication;
import com.raz.eventbum.data.MyFirebase_CFS;
import com.raz.eventbum.utils.Util_checkInputs;
import com.raz.eventbum.utils.Util_keyboard;
import com.raz.eventbum.utils.Utils_Strings;


public class Fragment_add_album extends Fragment {

    private TextInputEditText addAlbum_EDT_eventName;
    private TextInputEditText addAlbum_EDT_userCode;
    private TextInputEditText addAlbum_EDT_automaticCode;
    private TextInputLayout addAlbum_LYO_userCode;
    private TextInputLayout addAlbum_LYO_automaticCode;
    private MaterialRadioButton addAlbum_RB_chooseCode;
    private MaterialRadioButton addAlbum_RB_automaticCode;


    private MaterialButton addAlbum_BTN_addAlbum;

    private ProgressBar addAlbum_PB_progressBar;


    protected View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_add_album, container, false);
        }
        findViews();
        initViews();
        return view;
    }

    private void findViews() {
        addAlbum_BTN_addAlbum = view.findViewById(R.id.addAlbum_BTN_addAlbum);
        addAlbum_EDT_eventName = view.findViewById(R.id.addAlbum_EDT_eventName);
        addAlbum_RB_chooseCode = view.findViewById(R.id.addAlbum_RB_chooseCode);
        addAlbum_RB_automaticCode = view.findViewById(R.id.addAlbum_RB_automaticCode);
        addAlbum_LYO_userCode = view.findViewById(R.id.addAlbum_LYO_userCode);
        addAlbum_LYO_automaticCode = view.findViewById(R.id.addAlbum_LYO_automaticCode);
        addAlbum_EDT_userCode = view.findViewById(R.id.addAlbum_EDT_userCode);
        addAlbum_EDT_automaticCode = view.findViewById(R.id.addAlbum_EDT_automaticCode);
        addAlbum_PB_progressBar = view.findViewById(R.id.addAlbum_PB_progressBar);

    }

    private void initViews() {


        addAlbum_BTN_addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlbumClicked();
            }
        });


        addAlbum_RB_chooseCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChooseCodeRB();
                clean_RB_Errors();
            }
        });
        addAlbum_RB_automaticCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChooseAutomaticEventCode();
                clean_RB_Errors();
            }
        });


    }

    private void codeAlreadyExist() {
        addAlbum_EDT_userCode.setError("קוד זה קיים במערכת, יש לבחור קוד אחר");
        addAlbum_EDT_userCode.requestFocus();
    }


    private boolean checkInputs() {
        if (!Util_checkInputs.checkEventName(addAlbum_EDT_eventName) ||
                !Util_checkInputs.checkRBCode(addAlbum_RB_chooseCode, addAlbum_RB_automaticCode) ||
                (addAlbum_RB_chooseCode.isChecked()) && !Util_checkInputs.checkCodeNotEmpty(addAlbum_EDT_userCode)

        )
            return false;
        return true;
    }

    private void addAlbumClicked() {

        clean_RB_Errors();

        if (!checkInputs())
            return;


        String eventName = addAlbum_EDT_eventName.getText().toString();
        String albumCode;
        if (addAlbum_RB_automaticCode.isChecked())
            albumCode = addAlbum_EDT_automaticCode.getText().toString();
        else
            albumCode = addAlbum_EDT_userCode.getText().toString();


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

                    MyFirebase_CFS.writeNewAlbumCode(albumCode, MyFirebase_Authentication.mAuth.getCurrentUser().getUid(), eventName, new MyFirebase_CFS.CallBack_saveData() {
                        @Override
                        public void onFailure(String reason) {
                            hideProgressBar();
                        }

                        @Override
                        public void onSuccess() {
                            albumCreated();
                        }
                    });

                }
            }
        });


    }

    private void albumCreated() {
        Toast.makeText(view.getContext(), "האלבום נוסף בהצלחה",
                Toast.LENGTH_SHORT).show();
        hideProgressBar();
        cleanForms();

    }

    private void cleanForms() {
        addAlbum_EDT_eventName.setText("");
        addAlbum_EDT_userCode.setText("");
        addAlbum_EDT_automaticCode.setText("");
        addAlbum_RB_chooseCode.setChecked(false);
        addAlbum_RB_automaticCode.setChecked(false);
    }


    private void userChooseAutomaticEventCode() {
        addAlbum_RB_chooseCode.setChecked(false);
        addAlbum_LYO_automaticCode.setVisibility(View.VISIBLE);
        addAlbum_LYO_userCode.setVisibility(View.GONE);
        generateAutomaticEventCode();
        Util_keyboard.hideKeyBoard(view.getContext(), addAlbum_EDT_automaticCode);
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
                    addAlbum_EDT_automaticCode.setText(code);
                    hideProgressBar();
                }
            }
        });
    }

    private void showProgressBar() {
        addAlbum_BTN_addAlbum.setVisibility(View.GONE);
        addAlbum_PB_progressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar() {
        addAlbum_BTN_addAlbum.setVisibility(View.VISIBLE);
        addAlbum_PB_progressBar.setVisibility(View.GONE);

    }

    private void connectError() {
        Toast.makeText(view.getContext(), "שגיאה בחיבור",
                Toast.LENGTH_SHORT).show();
    }

    private void clean_RB_Errors() {
        addAlbum_RB_chooseCode.setError(null);
        addAlbum_RB_automaticCode.setError(null);
    }

    private void userChooseCodeRB() {
        addAlbum_RB_automaticCode.setChecked(false);
        addAlbum_LYO_userCode.setVisibility(View.VISIBLE);
        addAlbum_LYO_automaticCode.setVisibility(View.GONE);

        addAlbum_EDT_userCode.requestFocus();
        Util_keyboard.showKeyboard(view.getContext());

    }


}
