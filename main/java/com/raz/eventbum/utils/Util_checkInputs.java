package com.raz.eventbum.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.raz.eventbum.data.MyFirebase_CFS;

public class Util_checkInputs {

    public static boolean checkEmail(TextInputEditText EDT_email) {
        String email = EDT_email.getText().toString();

        if (TextUtils.isEmpty(email)) {
            EDT_email.setError("יש להכניס מייל");
            EDT_email.requestFocus();

            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EDT_email.setError("יש להכניס כתובת מייל תקינה");
            EDT_email.requestFocus();
            return false;

        }
        return true;
    }


    public static boolean checkName(TextInputEditText EDT_name) {
        String name = EDT_name.getText().toString();

        if (TextUtils.isEmpty(name)) {
            EDT_name.setError("יש להכניס שם");
            EDT_name.requestFocus();
            return false;
        }

        return true;
    }

    public static boolean checkEventName(TextInputEditText EDT_EventName) {
        String name = EDT_EventName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            EDT_EventName.setError("יש להכניס שם אירוע");
            EDT_EventName.requestFocus();
            return false;
        }

        return true;
    }

    public static boolean checkPassword(TextInputEditText EDT_password, TextInputEditText EDT_passwordValidation) {
        String password = EDT_password.getText().toString();
        String passwordValidation = EDT_passwordValidation.getText().toString();

        if (TextUtils.isEmpty(password)) {
            EDT_password.setError("יש להכניס סיסמה");
            EDT_password.requestFocus();

            return false;
        }

        if (password.length() < 6) {
            EDT_password.setError("הסיסמה חייבת להיות לפחות 6 תווים");
            EDT_password.requestFocus();

            return false;
        }

        if (TextUtils.isEmpty(passwordValidation)) {
            EDT_passwordValidation.setError("יש להכניס אימות סיסמה");
            EDT_passwordValidation.requestFocus();
            return false;

        }


        if (!passwordValidation.equals(password)) {
            EDT_passwordValidation.setError("אימות הסיסמה לא תואם לסיסמה");

            return false;

        }
        return true;
    }


    public static boolean checkCodeNotEmpty(TextInputEditText EDT_codeName) {
        String code = EDT_codeName.getText().toString();

        if (TextUtils.isEmpty(code)) {
            EDT_codeName.setError("יש לבחור קוד לאירוע");
            EDT_codeName.requestFocus();
            return false;

        }
        return true;

    }

    public static boolean checkRBCode(MaterialRadioButton register_rb_chooseCode, MaterialRadioButton register_rb_automaticCode) {
        if(!register_rb_chooseCode.isChecked() && !register_rb_automaticCode.isChecked())
        {
            register_rb_chooseCode.setError("יש לבחור קוד");
            register_rb_automaticCode.setError("יש לבחור קוד");
            return false;
        }
        return true;
    }
}
