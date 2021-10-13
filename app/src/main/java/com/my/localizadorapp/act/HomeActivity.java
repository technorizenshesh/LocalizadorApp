package com.my.localizadorapp.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.my.localizadorapp.MainActivity;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityHomeBinding;
import com.my.localizadorapp.databinding.ActivityHomeNavBinding;
import com.my.localizadorapp.fragment.HomeFragment;
import com.my.localizadorapp.fragment.PlaceFragment;
import com.my.localizadorapp.fragment.PremiumFragment;
import com.my.localizadorapp.utils.SessionManager;

public class HomeActivity extends AppCompatActivity {

    Fragment fragment;
    ActivityHomeNavBinding binding;
    private View promptsView;
    private AlertDialog alertDialog;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_nav);


        String UserName = Preference.get(HomeActivity.this,Preference.KEY_UserName);

        binding.childNavDrawer.txtName.setText(UserName);

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

  binding.childNavDrawer.RRProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this, MyAccountActivity.class);
                startActivity(i);
            }
        });

  binding.childNavDrawer.llTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this, TutorialOneActivity.class);
                startActivity(i);
            }
        });

  binding.childNavDrawer.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Preference.clearPreference(HomeActivity.this);

                Intent i = new Intent(HomeActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        fragment = new HomeFragment();
        loadFragment(fragment);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                doubleBackToExitPressedOnce=false;
                finishAffinity();
            }
        }, 2000);
    }



}