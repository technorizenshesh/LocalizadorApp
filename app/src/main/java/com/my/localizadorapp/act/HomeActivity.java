package com.my.localizadorapp.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.StateSet;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.my.localizadorapp.GPSTracker;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.Upd.MyService;
import com.my.localizadorapp.databinding.ActivityHomeNavBinding;
import com.my.localizadorapp.fragment.HomeFragment;
import com.my.localizadorapp.fragment.PlaceFragment;
import com.my.localizadorapp.model.CricleCreate;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    Fragment fragment;
    ActivityHomeNavBinding binding;
    private View promptsView;
    private AlertDialog alertDialog;
    boolean doubleBackToExitPressedOnce = false;

    SessionManager sessionManager;

    double latitude = 0;
    double longitude = 0;
    GPSTracker gpsTracker;

    String Battery="";
    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
   // private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    private RewardedAd rewardedAd;
    private boolean isLoading = false;
    private InterstitialAd interstitialAd;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float) scale;
            Battery = String.valueOf(batteryPct);
           // binding.txtBatery.setText(String.valueOf(batteryPct) + "%");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_nav);

       registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        String UserName = Preference.get(HomeActivity.this, Preference.KEY_UserName);
        String IMage = Preference.get(HomeActivity.this, Preference.key_image);

        if(!IMage.equalsIgnoreCase(""))
        {
            Glide.with(HomeActivity.this).load(IMage).placeholder(R.drawable.user_default).error(R.drawable.user_default).circleCrop().into(binding.childNavDrawer.imgUser);
        }

        sessionManager = new SessionManager(HomeActivity.this);

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
            startActivity(new Intent(HomeActivity.this,
                    PremiumActivity.class));
            //fragment = new PremiumFragment();
            //loadFragment(fragment);
        });

        binding.childNavDrawer.llChat.setOnClickListener(v -> {

            startActivity(new Intent(HomeActivity.this,
                    ChatMessageActivity.class));
        });

        binding.childNavDrawer.llPrimium.setOnClickListener(v -> {
            navmenu();
            startActivity(new Intent(HomeActivity.
                    this, PremiumActivity.class));
            /*fragment = new PremiumFragment();
            loadFragment(fragment);*/
        });

        binding.childNavDrawer.llSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navmenu();
                Intent i = new Intent(HomeActivity.
                        this, SupportScreen.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.LLsharingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navmenu();
                Intent i = new Intent(HomeActivity.
                        this, ShariingHistory.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navmenu();
                Intent i = new Intent(
                        HomeActivity.this,
                        PrivacyPolicy.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.LlDriving.setOnClickListener(v -> {

            navmenu();
            Intent i = new Intent(HomeActivity.this,
                    DrivingProtection.class);
            startActivity(i);

        });

        binding.childNavDrawer.llPurchesitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navmenu();
                Intent i = new Intent(
                        HomeActivity.this,
                        PurchaseItemsActivity.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.llJoinCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDaliogJoinCircle();

            }
        });

        binding.childNavDrawer.llSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this
                        , SettingActivity.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.llEmergancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this
                        , EmergancyContact.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.RRProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showInterstitial();
                Intent i = new Intent(HomeActivity.this, MyAccountActivity.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.llTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(HomeActivity.this
                        , TutorialOneActivity.class);
                startActivity(i);
            }
        });

        binding.childNavDrawer.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Preference.clearPreference(HomeActivity.this);

                Intent i = new Intent(HomeActivity
                        .this, SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });


        //Gps Lat Long
        gpsTracker = new GPSTracker(HomeActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }


        fragment = new HomeFragment();
        loadFragment(fragment);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        loadAd();
       /* MobileAds.initialize(this, initializationStatus -> {
            loadRewardedAd();

        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContextCompat.startForegroundService(getApplicationContext(), new Intent(getApplicationContext(), MyService.class));
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

    private void AlertDaliogJoinCircle() {

        LayoutInflater li;
        RelativeLayout RRjoinCircle;
        EditText edtCode;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(HomeActivity.this);
        promptsView = li.inflate(R.layout.alert_joincircle, null);
        RRjoinCircle = (RelativeLayout) promptsView.findViewById(R.id.RRjoinCircle);
        edtCode = (EditText) promptsView.findViewById(R.id.edtCode);
        alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(promptsView);

        RRjoinCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navmenu();
                String JoinCircle = edtCode.getText().toString();

                if (!JoinCircle.equalsIgnoreCase("")) {

                 ApiMethodJoinCircle(JoinCircle);

                }else {
                    Toast.makeText(HomeActivity.this, "Please Enter Valid Code.", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void ApiMethodJoinCircle(String Code) {

        String UserId = Preference.get(HomeActivity.this, Preference.KEY_USER_ID);

        Call<CricleCreate> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_Join_circle(UserId, Code, String.valueOf(latitude), String.valueOf(longitude),Battery);
        call.enqueue(new Callback<CricleCreate>() {
            @Override
            public void onResponse(Call<CricleCreate> call, Response<CricleCreate> response) {
                try {

                    CricleCreate myclass = response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")) {

                        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CricleCreate> call, Throwable t) {

            }
        });

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

                doubleBackToExitPressedOnce = false;
                finishAffinity();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MyService.class));
    }
    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                AD_UNIT_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitiald) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        HomeActivity.this.interstitialAd = interstitiald;
                        Log.i(TAG, "onAdLoaded");
                       // Toast.makeText(HomeActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        HomeActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        HomeActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;

                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());

                    }
                });
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null) {
            interstitialAd.show(this);
        } else {
        }
    }
}