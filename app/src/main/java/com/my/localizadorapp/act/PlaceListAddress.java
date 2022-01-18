package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityPlaceListAddressBinding;

public class PlaceListAddress extends AppCompatActivity {

    ActivityPlaceListAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_place_list_address);

        binding.RRback.setOnClickListener(v -> {

            onBackPressed();

        });

        binding.RRHome.setOnClickListener(v -> {

            startActivity(new Intent(PlaceListAddress.this,MyPlaceActivity.class).putExtra("Type","Home"));

        });

        binding.RRSchool.setOnClickListener(v -> {

            startActivity(new Intent(PlaceListAddress.this,MyPlaceActivity.class).putExtra("Type","School"));
        });

        binding.RRWork.setOnClickListener(v -> {
            startActivity(new Intent(PlaceListAddress.this,MyPlaceActivity.class).putExtra("Type","Work"));
        });

        binding.RRCustom.setOnClickListener(v -> {
            startActivity(new Intent(PlaceListAddress.this,MyPlaceActivity.class).putExtra("Type","Custom"));
        });

    }

}