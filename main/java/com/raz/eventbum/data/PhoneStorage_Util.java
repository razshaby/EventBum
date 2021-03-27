package com.raz.eventbum.data;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class PhoneStorage_Util {
    public static void saveToFile(String value, String fileName, Context context) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            //default mode is PRIVATE, can be APPEND etc.
            fos.write(value.getBytes());

            fos.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String fileName, Context context) {

        File dir = context.getFilesDir();
        File file = new File(dir, fileName);
        if (!file.delete()) {
            try {
            } catch (Exception e) {
                Log.d("logd", "cant delete file");
            }

        }
    }


        public static String readData (String filename, Context context){


            StringBuffer stringBuffer = new StringBuffer();
            try {
                //Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        context.openFileInput(filename)));
                String inputString;
                //Reading data line by line and storing it into the stringbuffer
                while ((inputString = inputReader.readLine()) != null) {
                    stringBuffer.append(inputString + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            //Displaying data on the toast
            //Toast.makeText(getApplicationContext(),stringBuffer.toString(),Toast.LENGTH_LONG).show();
            return stringBuffer.toString().trim().replace("\n", "").replace("\r", "");
        }


        public static boolean fileExists (String filename, Context context){
            File file = context.getFileStreamPath(filename);
            if (file == null || !file.exists()) {
                return false;
            }
            return true;
        }

    }
