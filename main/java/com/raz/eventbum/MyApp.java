package com.raz.eventbum;

import android.app.Application;
import android.util.Log;

import com.raz.eventbum.data.PhoneStorage_Management;
import com.raz.eventbum.objects.User_Info;


public class MyApp extends Application {

    private static User_Info userInfo;

    @Override
    public void onCreate() {
        super.onCreate();


        initParams();

        Log.d("pttt", "MyApp onCreate");
    }



    private void initParams() {
        userInfo = new User_Info();
        PhoneStorage_Management.setUserSettingFromMemory(userInfo,this);

    }

    public static User_Info getUserInfo() {
        return userInfo;
    }

    public static void deleteUserInfo() {
        userInfo.setUserName(null);
        userInfo.setUserID(null);
    }


}
