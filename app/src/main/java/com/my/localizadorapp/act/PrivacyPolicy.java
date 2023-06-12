package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.my.localizadorapp.databinding.ActivityPrivacyPolicyBinding;
import com.my.localizadorapp.model.PrivacyModel;
import com.my.localizadorapp.model.SignUpModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyPolicy extends AppCompatActivity {

    ActivityPrivacyPolicyBinding binding;
    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    InterstitialAd interstitialAd;


    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(PrivacyPolicy.this
                , "ca-app-pub-3940256099942544/1033173712",
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialA) {
                        interstitialAd = interstitialA;
                        interstitialAd.show(PrivacyPolicy.this);
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
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_privacy_policy);
        MobileAds.initialize(this, initializationStatus -> {
            //loadRewardedAd();
                String ads=  new SessionManager(this).getADES();
            Log.e(TAG, "onCreate: dvxvxvxvf -----"+ads );

            if (ads.equalsIgnoreCase("")) {
                 loadInterstitialAd();
                 Log.e(TAG, "onCreate: hgjfhnfdh" );
             }
            Log.e(TAG, "onCreate: dvxvxvxvf" );

        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
       

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        sessionManager=new SessionManager(this);
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            getApiPrivacy();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    public void getApiPrivacy() {

        Call<PrivacyModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_PrivacyPolicy();
        call.enqueue(new Callback<PrivacyModel>() {
            @Override
            public void onResponse(Call<PrivacyModel> call, Response<PrivacyModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    PrivacyModel myclass= response.body();

                    String status = myclass.getStatus();
                    String message = myclass.getMessage();

                    if (status.equalsIgnoreCase("1")){

                        String test = myclass.getResult().getText();

                        binding.txtPrivacyPolicy.setText(test);

                        Toast.makeText(PrivacyPolicy.this, message, Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(PrivacyPolicy.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PrivacyModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(PrivacyPolicy.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}