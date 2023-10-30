package com.my.localizadorapp.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.StateSet;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.my.localizadorapp.MainActivity;
import com.my.localizadorapp.R;
import com.my.localizadorapp.adapter.RatingPrimiumAdapter;
import com.my.localizadorapp.databinding.ActivityPremiumBinding;
import com.my.localizadorapp.model.RatingModel;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;

public class PremiumActivity extends AppCompatActivity {
    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String AD_UNIT_ID = "ca-app-pub-6669202856871108/4796400259	";
    ActivityPremiumBinding binding;
   // private static final String AD_UNIT_ID2 = "ca-app-pub-6669202856871108/4796400259";
    private static final String AD_UNIT_ID2 = "ca-app-pub-6669202856871108/4796400259";

    private ArrayList<RatingModel> modelList = new ArrayList<>();
    RatingPrimiumAdapter mAdapter;
    private InterstitialAd interstitialAd;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_premium);
        Log.e(TAG, "onCreateView: i am loading--------------------------------");
        sessionManager=new SessionManager(PremiumActivity.this);
        MobileAds.initialize(this, initializationStatus -> {
        });
        loadRewardedAd();
           String ads=  new SessionManager(this).getADES();
            Log.e(TAG, "onCreate: dvxvxvxvf -----"+ads );
            if (ads.equalsIgnoreCase("")) {
                 loadInterstitialAd();
                 Log.e(TAG, "onCreate: hgjfhnfdh" );
             }
            Log.e(TAG, "onCreate: dvxvxvxvf" );

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.imgCross.setOnClickListener(v -> {
            onBackPressed();

        });

        binding.llOne.setOnClickListener(v -> {
            //Intent i = new Intent(PremiumActivity.this, BenifitActiviity.class);
            Intent i = new Intent(PremiumActivity.this, CheckoutActivity.class);
            i.putExtra("Name", "1 Week Subscription Plan");
            startActivity(i);
        });

        binding.llTwo.setOnClickListener(v -> {
           // Intent i = new Intent(PremiumActivity.this, BenifitActiviity.class);
            Intent i = new Intent(PremiumActivity.this, CheckoutActivity.class);
            i.putExtra("Name","1 Month Subscription Plan");
            startActivity(i);
        });

        binding.llThree.setOnClickListener(v -> {
         //   Intent i = new Intent(PremiumActivity.this, BenifitActiviity.class);
           Intent i = new Intent(PremiumActivity.this, CheckoutActivity.class);
            i.putExtra("Name","1 Year Subscription Plan");
            startActivity(i);
        });

        binding.txtPrivacy.setOnClickListener(v -> {
            Intent i = new Intent(PremiumActivity.this, PrivacyPolicy.class);
            startActivity(i);
        });

        binding.txtTrms.setOnClickListener(v -> {
            Intent i = new Intent(PremiumActivity.this, TermsActivity.class);
            startActivity(i);
        });
        binding.watchAd.setOnClickListener(v -> {
            showRewardedVideo();
        });
        binding.watchAd2.setOnClickListener(v -> {
            showInterstitial();
        });
        setAdapter();
    }

    private void loadRewardedAd() {
        if (rewardedAd == null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    getApplicationContext(),
                    AD_UNIT_ID,
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(StateSet.TAG, StateSet.TAG + loadAdError.getMessage());
                            rewardedAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rd) {
                            rewardedAd = rd;
                            Log.d(StateSet.TAG, StateSet.TAG + "onAdLoaded");
                            if (!isLoading) {
                            }
                        }
                    });
        }
    }
    private void showRewardedVideo() {
        if (rewardedAd == null) {
            Log.d(TAG, TAG + "The rewarded ad wasn't ready yet.");
            Toast.makeText(this, "The rewarded ad wasn't ready yet.", Toast.LENGTH_SHORT).show();
            return;
        }
        isLoading = true;

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {Log.d(TAG, TAG + "onAdShowedFullScreenContent");}
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {Log.d(TAG, TAG + "onAdFailedToShowFullScreenContent");rewardedAd = null;}

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        rewardedAd = null;
                        Log.d(TAG, TAG + "onAdDismissedFullScreenContent");
                        loadRewardedAd();

                    }
                });
        rewardedAd.show(
                this,
                rewardItem -> {
                    Log.d("TAG", TAG + "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                   // sessionManager.setADES("1");
                    Log.e(TAG, "onUserEarnedReward: "+sessionManager.getADES() );
                    loadRewardedAd();
                });
    }

    private void setAdapter() {
        this.modelList.add(new RatingModel("Corn"));
        this.modelList.add(new RatingModel("Tomotoes"));
        mAdapter = new RatingPrimiumAdapter(PremiumActivity.this, modelList);
        binding.recyclerRating.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerRating.setLayoutManager(new LinearLayoutManager(PremiumActivity.this, LinearLayoutManager.HORIZONTAL, true));
        binding.recyclerRating.setAdapter(mAdapter);
    mAdapter.SetOnItemClickListener((view, position, model) -> {

    });

}
    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, AD_UNIT_ID2, adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialA) {
                        interstitialAd = interstitialA;
                        Log.i(TAG, "onAdLoaded");
                        Toast.makeText(PremiumActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        PremiumActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        PremiumActivity.this.interstitialAd = null;
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

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
       if (interstitialAd != null) {
            interstitialAd.show(this);
       } else {
          //  Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
               String ads=  new SessionManager(this).getADES();
            Log.e(TAG, "onCreate: dvxvxvxvf -----"+ads );
            if (ads.equalsIgnoreCase("")) {
                 loadInterstitialAd();
                 Log.e(TAG, "onCreate: hgjfhnfdh" );
             }
            Log.e(TAG, "onCreate: dvxvxvxvf" );

       }
    }
}