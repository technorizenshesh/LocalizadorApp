package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityTutorialOneBinding;

public class TutorialOneActivity extends AppCompatActivity {

    ActivityTutorialOneBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_tutorial_one);

       binding.RRback.setOnClickListener(v -> {
           onBackPressed();
       });

       binding.RRSNext.setOnClickListener(v -> {

          startActivity(new Intent(TutorialOneActivity.this,TutorialTwoActivity.class));

       });
    }
}