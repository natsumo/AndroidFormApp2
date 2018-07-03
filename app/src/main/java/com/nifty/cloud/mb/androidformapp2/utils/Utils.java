package com.nifty.cloud.mb.androidformapp2.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formatTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date d = sdf.parse(time);
            return output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void showDialog(Context context, String message) {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(context);
        mbuilder.setTitle("Alert");
        mbuilder.setMessage(message);
        mbuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = mbuilder.create();
        alertDialog.show();
    }
}
