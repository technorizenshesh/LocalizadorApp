package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.localizadorapp.MainActivity;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.RRContinue.setOnClickListener(v -> {

            Intent i = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(i);

        });
    }

}