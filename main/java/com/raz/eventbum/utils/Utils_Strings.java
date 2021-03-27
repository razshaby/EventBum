package com.raz.eventbum.utils;

import java.util.UUID;

public class Utils_Strings {

    public static String generateRandomString(int len)
    {
        String str = UUID.randomUUID().toString().replace("-", "");
        while(str.length() < len)
        {
            str+= UUID.randomUUID().toString().replace("-", "");
        }
        str.substring(0,len);
        return str;
    }
}
