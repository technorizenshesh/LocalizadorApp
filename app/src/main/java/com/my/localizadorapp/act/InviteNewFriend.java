package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
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
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityInviteNewFriendBinding;
import com.my.localizadorapp.utils.SessionManager;

public class InviteNewFriend extends AppCompatActivity {
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

    ActivityInviteNewFriendBinding binding;
    String UserCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_invite_new_friend);

         UserCode = Preference.get(InviteNewFriend.this,Preference.KEY_CircleCode);
        MobileAds.initialize(this, initializationStatus -> {
            //loadRewardedAd();
            String ads = new SessionManager(this).getADES();
            Log.e("TAG", "onCreate: dvxvxvxvf -----" + ads);

            if (ads.equalsIgnoreCase("")) {
                loadInterstitialAd();
                Log.e("TAG", "onCreate: hgjfhnfdh");
            }
            Log.e("TAG", "onCreate: dvxvxvxvf");

        });
        binding.txtCode.setText(UserCode);

       binding.RRShare.setOnClickListener(v -> {
           onShareClicked();
       });

       binding.RRShare1.setOnClickListener(v -> {
           onShareClicked();
       });

       binding.llMyCotact.setOnClickListener(v -> {
         startActivity(new Intent(InviteNewFriend.this,AllContactList.class));
       });

       binding.llMyCotact1.setOnClickListener(v -> {
         startActivity(new Intent(InviteNewFriend.this,AllContactList.class));
       });

       binding.RRback.setOnClickListener(v -> {
          onBackPressed();
       });
    }
    InterstitialAd interstitialAd;


    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(InviteNewFriend.this, "ca-app-pub-6669202856871108/4796400259", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialA) {
                interstitialAd = interstitialA;
                interstitialAd.show(InviteNewFriend.this);
                //  Toast.makeText(PlaceListAddress.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
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
                Log.i("TAG", "onAdFailedToLoad" + loadAdError.getMessage());
                interstitialAd = null;
            }
        });
    }
    private void onShareClicked() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
      /*  try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName+" : Code - "+UserCode)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName+" : Code - "+UserCode)));
        }*/

        String link = "https://play.google.com/store/apps/details?id="+
                appPackageName+" : Code - "+UserCode;
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link);
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
        startActivity(Intent.createChooser(intent, "Share Link"));

    }

}