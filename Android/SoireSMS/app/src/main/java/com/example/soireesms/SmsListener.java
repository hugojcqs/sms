package com.example.soireesms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.soireesms.ui.home.Server;
import com.example.soireesms.ui.home.Sms;

import static java.security.AccessController.getContext;

public class SmsListener extends BroadcastReceiver {

    private final String TAG = "SmsListener";
    MainActivity main = null;
    public SmsListener(){} //needed
    public SmsListener(MainActivity main){
        this.main=main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Log.i(TAG, "New sms received");
        for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            String messageBody = smsMessage.getMessageBody();
            String sender      = smsMessage.getOriginatingAddress();
            String dateTime    = Long.toString(smsMessage.getTimestampMillis());

            Sms sms = new Sms();
            sms.setAddress(sender);
            sms.setMsg(messageBody);
            sms.setTime(dateTime);


            assert main.url != null;
            main.updateView(sms);
            Server.sendSms(main, sms, main.url);
            }
        }
    }
}