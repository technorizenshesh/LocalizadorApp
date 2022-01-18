package com.my.localizadorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationSettingsResult;
import com.my.localizadorapp.act.HomeActivity;
import com.my.localizadorapp.act.SignUpActivity;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener,
        ResultCallback<LocationSettingsResult>
{

    public static final int RequestPermissionCode = 1;
    SessionManager sessionManager;
    private RelativeLayout googlePayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      sessionManager=new SessionManager(MainActivity.this);

        if (permissioncheck()){
            finds();
        } else {
           requestPermission();
        }

    }

    private boolean permissioncheck() {
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        return FourthPermissionResult ==
                PackageManager.PERMISSION_GRANTED && FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,

                }, RequestPermissionCode);

        if (permissioncheck()) {
            finds();
        } else {
            requestPermission();
        }

    }

    private void finds() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String User_id = Preference.get(MainActivity.this,Preference.KEY_USER_ID);

                if(User_id != null && !User_id.trim().equalsIgnoreCase("0")){

                   Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }else
                {
                    Intent intent=new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult){

    }

}