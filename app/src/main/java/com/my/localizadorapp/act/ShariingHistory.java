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
import com.my.localizadorapp.databinding.ActivityShariingHistoryBinding;
import com.my.localizadorapp.utils.SessionManager;

public class ShariingHistory extends AppCompatActivity {

    ActivityShariingHistoryBinding binding;
    InterstitialAd interstitialAd;


    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(ShariingHistory.this
                , "ca-app-pub-6669202856871108/4796400259",
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialA) {
                        interstitialAd = interstitialA;
                        interstitialAd.show(ShariingHistory.this);
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
        binding= DataBindingUtil.setContentView(this,R.layout.activity_shariing_history);
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