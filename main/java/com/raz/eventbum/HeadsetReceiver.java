package com.raz.eventbum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class HeadsetReceiver extends BroadcastReceiver {
    int scale = -1;
    int level = -1;
    int voltage = -1;
    int temp = -1;
    @Override
    public void onReceive(Context context, Intent intent) {



        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

       // throw new UnsupportedOperationException("Not yet implemented");
//        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
//            int state = intent.getIntExtra("state", -1);
//            switch (state) {
//                case 0:
//                    Toast.makeText(context,"Headset is unplugged",Toast.LENGTH_SHORT).show();
//
//                    break;
//                case 1:
//                    Toast.makeText(context,"Headset is plugged",Toast.LENGTH_SHORT).show();
//
//                    break;
//                default:
//                    Toast.makeText(context,"I have no idea what the headset state is",Toast.LENGTH_SHORT).show();
//
//            }
//        }


        int lastLevel = level;
        level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        if(lastLevel!=level && lastLevel>level) {
            Toast.makeText(context, "LastLevel: " + lastLevel + "level: " + level + "scale: " + scale + "temp: " + temp + "voltage: " + voltage, Toast.LENGTH_SHORT).show();


            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Your Battery running out, please charge your phone,you have " + level + "% battery level");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

//        builder1.setNegativeButton(
//                "No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }


    }
}