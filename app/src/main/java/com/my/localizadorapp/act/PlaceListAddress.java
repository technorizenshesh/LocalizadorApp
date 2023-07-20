package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityPlaceListAddressBinding;
import com.my.localizadorapp.utils.SessionManager;

public class PlaceListAddress extends AppCompatActivity {

    ActivityPlaceListAddressBinding binding;
    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String AD_UNIT_ID = "ca-app-pub-5017067604593087/6794040495	";
    InterstitialAd interstitialAd;


    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-5017067604593087/6794040495", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialA) {
                 interstitialAd = interstitialA;
                interstitialA.show(PlaceListAddress.this);
                //  Toast.makeText(PlaceListAddress.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                PlaceListAddress.this.interstitialAd = null;
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                PlaceListAddress.this.interstitialAd = null;
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
                Log.i(TAG,"onAdFailedToLoad"+ loadAdError.getMessage());
                interstitialAd = null;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_place_list_address);
        MobileAds.initialize(this, initializationStatus -> {
           // loadRewardedAd();

        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        String ads=  new SessionManager(this).getADES();
            Log.e(TAG, "onCreate: dvxvxvxvf -----"+ads );

            if (ads.equalsIgnoreCase("")) {
                 loadInterstitialAd();
                 Log.e(TAG, "onCreate: hgjfhnfdh" );
             }
            Log.e(TAG, "onCreate: dvxvxvxvf" );

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