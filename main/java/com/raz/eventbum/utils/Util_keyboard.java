package com.raz.eventbum.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Util_keyboard {




    public static void showKeyboard(Context context)
    {
        InputMethodManager imm = (InputMethodManager)   context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
