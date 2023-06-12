package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityDrivingProtectionBinding;
import com.my.localizadorapp.utils.SessionManager;

public class DrivingProtection extends AppCompatActivity {

    ActivityDrivingProtectionBinding binding;
    InterstitialAd interstitialAd;


    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(DrivingProtection.this
                , "ca-app-pub-3940256099942544/1033173712",
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialA) {
                        interstitialAd = interstitialA;
                        interstitialAd.show(DrivingProtection.this);
                        //  Toast.makeText(PlaceListAddress.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i("TAG","onAdFailedToLoad"+ loadAdError.getMessage());
                        interstitialAd = null;
                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding= DataBindingUtil.setContentView(this,R.layout.activity_driving_protection);
        MobileAds.initialize(this, initializationStatus -> {
            //loadRewardedAd();
                String ads=  new SessionManager(this).getADES();
            Log.e("TAG", "onCreate: dvxvxvxvf -----"+ads );

            if (ads.equalsIgnoreCase("")) {
                 loadInterstitialAd();
                 Log.e("TAG", "onCreate: hgjfhnfdh" );
             }
            Log.e("TAG", "onCreate: dvxvxvxvf" );

        });
       binding.RRback.setOnClickListener(v -> {

           onBackPressed();
           
       });
    }
}