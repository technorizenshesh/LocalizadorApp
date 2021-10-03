package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityBaterryAlertScreenBinding;

public class BaterryAlertScreen extends AppCompatActivity {

    ActivityBaterryAlertScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_baterry_alert_screen);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}