package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityBenifitActiviityBinding;

public class BenifitActiviity extends AppCompatActivity {

    ActivityBenifitActiviityBinding binding;
    String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_benifit_activiity);

        Intent intent=getIntent();

        if(intent !=null)
        {
            name=intent.getStringExtra("Name").toString();
            binding.txtName.setText(name);
        }

    binding.RRback.setOnClickListener(v -> {
        onBackPressed();
    });

    }
}