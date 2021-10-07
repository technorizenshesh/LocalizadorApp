package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityOtpScreenBinding;

public class OtpScreenActivity extends AppCompatActivity {

    ActivityOtpScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_otp_screen);

       binding.edtOne.setOnClickListener(v -> {


       });

       binding.edtTwo.setOnClickListener(v -> {


       });

       binding.edtThree.setOnClickListener(v -> {


       });

       binding.edtFour.setOnClickListener(v -> {


       });

    }
}