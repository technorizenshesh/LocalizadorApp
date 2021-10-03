package com.my.localizadorapp.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityHomeBinding;
import com.my.localizadorapp.databinding.ActivityHomeNavBinding;
import com.my.localizadorapp.fragment.HomeFragment;
import com.my.localizadorapp.fragment.PlaceFragment;
import com.my.localizadorapp.fragment.PremiumFragment;

public class HomeActivity extends AppCompatActivity {

    Fragment fragment;
    ActivityHomeNavBinding binding;
    private View promptsView;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_nav);

        binding.dashboard.RRHome.setOnClickListener(v -> {

            fragment = new HomeFragment();
            loadFragment(fragment);

        });

        binding.dashboard.RRMenu.setOnClickListener(v -> {
            navmenu();
        });

       binding.dashboard.RRPlace.setOnClickListener(v -> {

            fragment = new PlaceFragment();
            loadFragment(fragment);

        });

        binding.dashboard.RRPremium.setOnClickListener(v -> {

            fragment = new PremiumFragment();
            loadFragment(fragment);

        });

        binding.childNavDrawer.llPrimium.setOnClickListener(v -> {

            fragment = new PremiumFragment();
            loadFragment(fragment);

        });


        binding.childNavDrawer.llSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this, SupportScreen.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.LLsharingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this, ShariingHistory.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this, PrivacyPolicy.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.llPurchesitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this, PurchaseItemsActivity.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.llJoinCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDaliog();
            }
        });

        binding.childNavDrawer.llSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });


        fragment = new HomeFragment();
        loadFragment(fragment);
    }



    public void navmenu() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START);
        } else {
            binding.drawer.openDrawer(GravityCompat.START);
        }
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_homeContainer, fragment);
        transaction.addToBackStack("home");
        transaction.commit();
    }

    private void AlertDaliog() {

        LayoutInflater li;
        RelativeLayout RRShare;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(HomeActivity.this);
        promptsView = li.inflate(R.layout.alert_joincircle, null);
        RRShare = (RelativeLayout) promptsView.findViewById(R.id.RRShare);
        alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(promptsView);

        RRShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}