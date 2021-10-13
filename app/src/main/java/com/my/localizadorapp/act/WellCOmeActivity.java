package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityWellCOmeBinding;

public class WellCOmeActivity extends AppCompatActivity {

    ActivityWellCOmeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_well_c_ome);

        binding.RRContinue.setOnClickListener(v ->
        {

            startActivity(new Intent(WellCOmeActivity.this,HomeActivity.class));

        });

    }

}