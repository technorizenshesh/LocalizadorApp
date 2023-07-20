package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivitySettingBinding;
import com.my.localizadorapp.fragment.BottomFragmentMap;
import com.my.localizadorapp.fragment.HomeFragment;
import com.my.localizadorapp.utils.SessionManager;

public class SettingActivity extends AppCompatActivity {
    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    InterstitialAd interstitialAd;


    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(SettingActivity.this
                , "ca-app-pub-5017067604593087/6794040495",
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialA) {
                        interstitialAd = interstitialA;
                        interstitialAd.show(SettingActivity.this);
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
    ActivitySettingBinding binding;
    private View promptsView;
    private AlertDialog alertDialog;
    BottomFragmentMap  bottomSheetFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_setting);
        MobileAds.initialize(this, initializationStatus -> {
           // loadRewardedAd();
                String ads=  new SessionManager(this).getADES();
            Log.e(TAG, "onCreate: dvxvxvxvf -----"+ads );

            if (ads.equalsIgnoreCase("")) {
                 loadInterstitialAd();
                 Log.e(TAG, "onCreate: hgjfhnfdh" );
             }
            Log.e(TAG, "onCreate: dvxvxvxvf" );

        });
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
       

        binding.llFaQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this
                        , FaqScreen.class);
                startActivity(i);

            }
        });

        binding.llMyDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity
                        .this, DeviceActivity.class);
                startActivity(i);

            }
        });

        binding.llPrimium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingActivity.
                        this, PremiumActivity.class));

            }
        });

        binding.SendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.
                        this, SendFeedbaackReport.class);
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

                Intent i = new Intent(SettingActivity.this,
                        ToDoList.class);
                startActivity(i);
            }
        });

        binding.llTodolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this,
                        ToDoList.class);
                startActivity(i);
            }
        });

        binding.llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this,
                        NotificationScree.class);
                startActivity(i);
            }
        });

        binding.llBateryAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this,
                        BaterryAlertScreen.class);
                startActivity(i);
            }
        });

        binding.llInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this,
                        ManageInvitations.class);
                startActivity(i);
            }
        });

        binding.llEmergancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this,
                        EmergancyContact.class);
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