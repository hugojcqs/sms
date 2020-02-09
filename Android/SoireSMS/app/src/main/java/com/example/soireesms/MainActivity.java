package com.example.soireesms;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.soireesms.ui.home.HomeFragment;
import com.example.soireesms.ui.home.Server;
import com.example.soireesms.ui.home.Sms;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //adding the receiver to the manifest
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(999);
        this.registerReceiver(new SmsListener(this), filter);

        verifyPermission();
        if (!domainOk()){
            CharSequence text = getString(R.string.domainError);
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


    private void verifyPermission(){
        Log.d(TAG, "verifyPermission : checking permissions");

        int permissionSMS = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS);
        int permissionINTERNET = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET);
        int permissionRECEIVE_SMS = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS);

        if (permissionSMS != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_SMS},
                    1);
        }
        if (permissionINTERNET != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }
        if (permissionRECEIVE_SMS != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    1);
        }
    }

    public boolean domainOk(){
        return !(null == this.url || this.url.isEmpty());
    }

    public void sendSmsInformation(Sms sms){
        //Send an sms to the server specified and a toast indicating wether or not something went wrong appear
        CharSequence text;
        if (!domainOk()){
            text = getString(R.string.domainError);
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
            return;
        }
        try {
            Server.sendSms(this, sms, this.url);
            text = getString(R.string.messageSent);
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        } catch (Exception e){
            Log.e(TAG, e.toString());
            text = getString(R.string.sendingError);
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }
    }


    public void updateView(Sms sms){
        List<Fragment> navHostFragments = getSupportFragmentManager().getFragments();

        for (Fragment navHostFragment : navHostFragments){
            Log.d(TAG, navHostFragment.toString());
            try{
                ((HomeFragment)navHostFragment.getChildFragmentManager().getFragments().get(0)).listUpdate(sms);
                Log.i(TAG, "Method found in fragment object" );
            } catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }
    }



}
