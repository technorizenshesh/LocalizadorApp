package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityShariingHistoryBinding;

public class ShariingHistory extends AppCompatActivity {

    ActivityShariingHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_shariing_history);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

    }
}