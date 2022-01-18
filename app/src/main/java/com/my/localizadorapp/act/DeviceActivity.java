package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.jaredrummler.android.device.DeviceName;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityDeviceBinding;

public class DeviceActivity extends AppCompatActivity {

    ActivityDeviceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_device);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRSetting.setOnClickListener(v -> {

            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

        });

        binding.DeviceName.setText(""+DeviceName.getDeviceName());

        binding.DeviceVersion.setText(""+getAndroidVersion());

    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }

}