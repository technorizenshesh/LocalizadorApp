package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityTermsBinding;
import com.my.localizadorapp.model.PrivacyModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity {

    ActivityTermsBinding binding;

    SessionManager sessionManager;
    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String AD_UNIT_ID = "ca-app-pub-6669202856871108/4796400259	";

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
                            // Handle the error.
                            rewardedAd = null;
                            //  Toast.makeText(HomeActivity.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rd) {
                            rewardedAd = rd;
                            if (!isLoading) {
                                showRewardedVideo();
                            }
                            //  Toast.makeText(HomeActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showRewardedVideo() {

        if (rewardedAd == null) {
            Toast.makeText(this, "The rewarded ad wasn't ready yet.", Toast.LENGTH_SHORT).show();
            return;
        }
        // showVideoButton.setVisibility(View.INVISIBLE);
        isLoading = true;

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                       /* Toast.makeText(HomeActivity.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();*/
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                       /* Toast.makeText(
                                HomeActivity.this, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
                                .show();*/
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        // Toast.makeText(HomeActivity.this, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();
                        // Preload the next rewarded ad.

                        loadRewardedAd();

                    }
                });
        // Activity activityContext = getActivity();
        rewardedAd.show(
                this,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        //session.setSWIPECount(session.getSWIPECount() + 5);
                        //  RewardCollectedApi
                        //     ("5");
                        // TastyToast.makeText(getContext(), "5 Swipes Added", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();
                        //getMyScore("",coi__n);
                        loadRewardedAd();
                    }

                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_terms);

        MobileAds.initialize(this, initializationStatus -> {
           // loadRewardedAd();

        });
/*https://technorizen.com/localizador/privacy.html*/
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        sessionManager=new SessionManager(this);
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.container.loadUrl("https://technorizen.com/localizador/privacy.html");
            binding.progressBar.setVisibility(View.GONE);

            // getApiPrivacy();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    public void getApiPrivacy() {

        Call<PrivacyModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_Terms();
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

                        binding.txtTerms.setText(test);

                        Toast.makeText(TermsActivity.this, message, Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(TermsActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PrivacyModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(TermsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}