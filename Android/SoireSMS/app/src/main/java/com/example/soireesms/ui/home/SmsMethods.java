package com.example.soireesms.ui.home;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.example.soireesms.ui.home.Sms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//adapted from :
//https://stackoverflow.com/questions/848728/how-can-i-read-sms-messages-from-the-device-programmatically-in-android

public class SmsMethods{
    public static List<Sms> getAllSms(Activity context) {
        List<Sms> lstSms = new ArrayList<>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = context.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        context.startManagingCursor(c); //TODO Replace this method -> Makes the app crash
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }

    public static List<Sms> bubbleSort(List<Sms> arr)
    {
        for (int i = 0; i < arr.size(); i++) {
            for (int j = 0; j < arr.size()-1-i; j++) {
                if(Long.parseLong(arr.get(j).getTime()) < Long.parseLong(arr.get(j+1).getTime())) //TODO crash why ?
                { //Comparing Epoch time in milliseconds
                    Sms temp=arr.get(j);
                    arr.set(j, arr.get(j+1));
                    arr.set(j+1, temp);

                }
            }
            System.out.print("Iteration "+(i+1)+": ");
        }
        return arr;
    }

    public static void printArray(int arr[])
    {
        for (int i = 0; i <arr.length; i++) {
            System.out.print(arr[i]+" ");
        }
        System.out.println();
    }
}
