package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.my.localizadorapp.databinding.ActivitySettingBinding;
import com.my.localizadorapp.fragment.BottomFragmentMap;
import com.my.localizadorapp.fragment.HomeFragment;

public class SettingActivity extends AppCompatActivity {
    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";

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
    ActivitySettingBinding binding;
    private View promptsView;
    private AlertDialog alertDialog;
    BottomFragmentMap  bottomSheetFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_setting);
        MobileAds.initialize(this, initializationStatus -> {
            loadRewardedAd();

        });
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.llFaQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, FaqScreen.class);
                startActivity(i);

            }
        });

        binding.llMyDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, DeviceActivity.class);
                startActivity(i);

            }
        });

        binding.llPrimium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingActivity.this, PremiumActivity.class));

            }
        });

        binding.SendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, SendFeedbaackReport.class);
                startActivity(i);
            }
        });

        binding.llMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, MyAccountActivity.class);
                startActivity(i);
            }
        });

        binding.llManageMeber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, ManageMebersCircles.class);
                startActivity(i);
            }
        });

        binding.llTodolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, ToDoList.class);
                startActivity(i);
            }
        });

        binding.llTodolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, ToDoList.class);
                startActivity(i);
            }
        });

        binding.llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, NotificationScree.class);
                startActivity(i);
            }
        });

        binding.llBateryAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, BaterryAlertScreen.class);
                startActivity(i);
            }
        });

        binding.llInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, ManageInvitations.class);
                startActivity(i);
            }
        });

        binding.llEmergancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, EmergancyContact.class);
                startActivity(i);
            }
        });

        binding.llDistanceUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDaliog();
                
            }
        });

        binding.RRMap.setOnClickListener(v -> {

            AlertDaliogMap();

        });

    }

    private void AlertDaliogMap() {

        LayoutInflater li;
        RelativeLayout RRNORMAL;
        RelativeLayout RRTERRAIN;
        RelativeLayout RRSATELLITE;
        TextView txtOne;
        TextView txtTwo;
        TextView txtThree;

        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(SettingActivity.this);
        promptsView = li.inflate(R.layout.bottom_alert_map, null);
        RRNORMAL = (RelativeLayout) promptsView.findViewById(R.id.RRNORMAL);
        RRTERRAIN = (RelativeLayout) promptsView.findViewById(R.id.RRTERRAIN);
        RRSATELLITE = (RelativeLayout) promptsView.findViewById(R.id.RRSATELLITE);
        txtOne = (TextView) promptsView.findViewById(R.id.txtOne);
        txtTwo = (TextView) promptsView.findViewById(R.id.txtTwo);
        txtThree = (TextView) promptsView.findViewById(R.id.txtThree);

        alertDialogBuilder = new AlertDialog.Builder(SettingActivity.this);
        alertDialogBuilder.setView(promptsView);

      RRNORMAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RRNORMAL.setBackgroundResource(R.drawable.map_border);
                RRTERRAIN.setBackgroundResource(R.drawable.map_border2);
                RRSATELLITE.setBackgroundResource(R.drawable.map_border2);

                txtOne.setTextColor(getResources().getColor(R.color.black));
                txtTwo.setTextColor(getResources().getColor(R.color.gray));
                txtThree.setTextColor(getResources().getColor(R.color.gray));


                //alertDialog.dismiss();
            }
        });

        RRTERRAIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                RRNORMAL.setBackgroundResource(R.drawable.map_border2);
                RRTERRAIN.setBackgroundResource(R.drawable.map_border);
                RRSATELLITE.setBackgroundResource(R.drawable.map_border2);

                txtOne.setTextColor(getResources().getColor(R.color.gray));
                txtTwo.setTextColor(getResources().getColor(R.color.black));
                txtThree.setTextColor(getResources().getColor(R.color.gray));

                //alertDialog.dismiss();
            }
        });
        RRSATELLITE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RRNORMAL.setBackgroundResource(R.drawable.map_border2);
                RRTERRAIN.setBackgroundResource(R.drawable.map_border2);
                RRSATELLITE.setBackgroundResource(R.drawable.map_border);

                txtOne.setTextColor(getResources().getColor(R.color.gray));
                txtTwo.setTextColor(getResources().getColor(R.color.gray));
                txtThree.setTextColor(getResources().getColor(R.color.black));

                //alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void AlertDaliog() {

        LayoutInflater li;
        RelativeLayout RRkg;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(SettingActivity.this);
        promptsView = li.inflate(R.layout.alert_distacne, null);
        alertDialogBuilder = new AlertDialog.Builder(SettingActivity.this);
        alertDialogBuilder.setView(promptsView);
    /*    RRkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });
*/
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}