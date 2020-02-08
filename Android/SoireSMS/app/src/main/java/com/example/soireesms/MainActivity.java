package com.example.soireesms;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.soireesms.ui.home.HomeFragment;
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

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
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

    public void updateView(Sms sms){
        assert getFragmentManager() != null;
        Log.i(TAG, "Found fragment: " );

        //Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        try{
            for (Fragment fragment : fragments){
                ((HomeFragment)fragment).listUpdate(sms);
            }
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }
    }

}
