package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityManageMebersCirclesBinding;

public class ManageMebersCircles extends AppCompatActivity {

    ActivityManageMebersCirclesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_manage_mebers_circles);

        binding.RRback.setOnClickListener(v -> {

            onBackPressed();
            
        });
    }
}