package com.raz.eventbum.data;

import android.content.Context;

import com.raz.eventbum.MyApp;
import com.raz.eventbum.objects.User_Info;

public class PhoneStorage_Management {


    public interface DataKeys {
          String USER_NAME = "USER_NAME";
          String USER_ID = "USER_ID";

    }


    public static void saveDataOfUserToPhoneMemory(String userName, /**not importent*/String userId,Context context) {
        MyApp.getUserInfo().setUserName(userName);
        PhoneStorage_Util.saveToFile(userName, DataKeys.USER_NAME, context);


        MyApp.getUserInfo().setUserID(userId);
        PhoneStorage_Util.saveToFile(userId, DataKeys.USER_ID, context);

    }


    public static void deleteDataOfUserToPhoneMemory(Context context) {
       PhoneStorage_Util.deleteFile(DataKeys.USER_NAME,context);
        PhoneStorage_Util.deleteFile(DataKeys.USER_ID,context);


    }



    public static void setUserSettingFromMemory(User_Info userInfo, Context context) {

        setUserName(userInfo,context);
        setUserId(userInfo,context);

    }


    private static void setUserName(User_Info userInfo, Context context) {
        if (PhoneStorage_Util.fileExists(DataKeys.USER_NAME, context)) {
            userInfo.setUserName((PhoneStorage_Util.readData(DataKeys.USER_NAME, context)));
        }
    }

    private static void setUserId(User_Info userInfo, Context context) {
        if (PhoneStorage_Util.fileExists(DataKeys.USER_ID, context)) {
            userInfo.setUserID((PhoneStorage_Util.readData(DataKeys.USER_ID, context)));
        }
    }


}
