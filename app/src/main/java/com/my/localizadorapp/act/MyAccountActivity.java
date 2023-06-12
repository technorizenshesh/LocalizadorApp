package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityMyAccountBinding;
import com.my.localizadorapp.model.SignUpModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountActivity extends AppCompatActivity {
    ActivityMyAccountBinding binding;
    SessionManager sessionManager;
     String Mobile="";
    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_account);
        sessionManager =new SessionManager(MyAccountActivity.this);
        MobileAds.initialize(this, initializationStatus -> {
           // loadRewardedAd();
        });
        loadAd();
        binding.RRback.setOnClickListener(v -> {

            onBackPressed();

        });
        /*binding.txtMobile.setOnClickListener(v -> {
            startActivity(new Intent(MyAccountActivity.this,ChangeMobile.class)
                    .putExtra("mobile",Mobile));
        });*/

        binding.RREdit.setOnClickListener(v -> {
            startActivity(new Intent(MyAccountActivity.this,EditProfileActivity.class));
        });


    }

    @Override
    protected void onResume() {
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ApiMethodmyProfile();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                "ca-app-pub-3940256099942544/1033173712",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Log.i(TAG, "onAdLoaded");
                        if (interstitialAd != null) {
                            interstitialAd.show(MyAccountActivity.this);
                        } else {
                        }
                        // Toast.makeText(HomeActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Log.d("TAG", "The ad was dismissed.");

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
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
                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());

                    }
                });
    }

    public void ApiMethodmyProfile() {

       String UserId = Preference.get(MyAccountActivity.this,Preference.KEY_USER_ID);

        Call<SignUpModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_get_profile(UserId);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    SignUpModel myclass= response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")){
                        String UserName = myclass.result.userName;
                        String country_code = myclass.result.country_code;
                         Mobile = myclass.result.mobile;
                        binding.txtName1.setText(""+UserName);
                        binding.txtMobile.setText("+"+country_code+"-"+Mobile);
                        if(!myclass.result.image.equalsIgnoreCase(""))
                        {
                            Glide.with(MyAccountActivity.this).load(myclass.result.image).circleCrop().into(binding.imgUser);
                        }


                        Toast.makeText(MyAccountActivity.this, message, Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(MyAccountActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(MyAccountActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}