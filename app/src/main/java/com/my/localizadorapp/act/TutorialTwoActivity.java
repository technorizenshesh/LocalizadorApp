package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityTutorialTwoBinding;

public class TutorialTwoActivity extends AppCompatActivity {

    ActivityTutorialTwoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_tutorial_two);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.RRSNext.setOnClickListener(v -> {

            startActivity(new Intent(TutorialTwoActivity.this,HomeActivity.class));

        });
    }
}