package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.gms.ads.AdRequest;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityOtpScreenBinding;

public class OtpScreenActivity extends AppCompatActivity {

    ActivityOtpScreenBinding binding;
    String Mobile="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_otp_screen);
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        Intent intent=getIntent();
       if(intent!=null)
       {
           Mobile=intent.getStringExtra("mobile").toString();
       }

        binding.edtOne.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(  binding.edtOne.getText().toString().length()==1)     //size as per your requirement
                {
                    binding.edtTwo.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        binding.edtTwo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(  binding.edtTwo.getText().toString().length()==1)     //size as per your requirement
                {

                    binding.edtThree.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        binding.edtThree.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(  binding.edtThree.getText().toString().length()==1)     //size as per your requirement
                {

                    binding.edtFour.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


       binding.RRback.setOnClickListener(v -> {
           onBackPressed();
       });

       binding.RRContinue.setOnClickListener(v -> {

           startActivity(new Intent(OtpScreenActivity.this,UserNameScree.class).putExtra("mobile1",Mobile));

       });

    }
}