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
    public static List<Sms> getAllSms(Activity context){
        //Gets all sms from the phone
        //Returns List<Sms> of all sms, all having them information complete (see the class)
        Sms sms;
        List<Sms> listSms = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cur = context.getContentResolver().query(uri, null, null, null, null);

        while (cur != null && cur.moveToNext()) {
            sms = new Sms();
            sms.setId(cur.getString(cur.getColumnIndexOrThrow("_id")));
            sms.setAddress(cur.getString(cur.getColumnIndex("address")));
            sms.setMsg(cur.getString(cur.getColumnIndexOrThrow("body")));
            sms.setReadState(cur.getString(cur.getColumnIndex("read")));
            sms.setTime(cur.getString(cur.getColumnIndexOrThrow("date")));
            if (cur.getString(cur.getColumnIndexOrThrow("type")).contains("1")) {
                sms.setFolderName("inbox");
            } else {
                sms.setFolderName("sent");
            }

            listSms.add(sms);
        }

        if (cur != null) {
            cur.close();
        }
        return listSms;
    }


    public static void bubbleSort(List<Sms> arr)
    {
        for (int i = 0; i < arr.size(); i++) {
            for (int j = 0; j < arr.size()-1-i; j++) {
                if(Long.parseLong(arr.get(j).getTime()) < Long.parseLong(arr.get(j+1).getTime()))
                { //Comparing Epoch time in milliseconds
                    Sms temp=arr.get(j);
                    arr.set(j, arr.get(j+1));
                    arr.set(j+1, temp);
                }
            }
        }
    }
}
