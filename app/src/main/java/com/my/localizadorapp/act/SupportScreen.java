package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivitySupportScreenBinding;

public class SupportScreen extends AppCompatActivity {

    ActivitySupportScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_support_screen);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}