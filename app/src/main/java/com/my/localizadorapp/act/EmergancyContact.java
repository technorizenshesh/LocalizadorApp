package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityEmergancyContactBinding;
import com.my.localizadorapp.utils.SessionManager;

public class EmergancyContact extends AppCompatActivity {

    ActivityEmergancyContactBinding binding;
    InterstitialAd interstitialAd;


    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(EmergancyContact.this
                , "ca-app-pub-3940256099942544/1033173712",
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialA) {
                        interstitialAd = interstitialA;
                        interstitialAd.show(EmergancyContact.this);
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
       binding= DataBindingUtil.setContentView(this,R.layout.activity_emergancy_contact);

      String CheckIt = Preference.get(EmergancyContact.this,Preference.key_On_off);
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
      if(CheckIt.equalsIgnoreCase("on"))
      {
          binding.SwithON.setChecked(true);

      }else
      {
          binding.SwithON.setChecked(false);
      }

       binding.RRback.setOnClickListener(v -> {

           onBackPressed();

       });

       binding.RRASend.setOnClickListener(v -> {
           Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
           sharingIntent.setType("text/plain");
           String shareBody = "Hello sir Please Urgent Call me +910731456698";
           sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
           sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
           startActivity(Intent.createChooser(sharingIntent, "Share via"));
         }
       );

        binding.SwithON.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Preference.save(EmergancyContact.this,Preference.key_On_off,"on");

                }else
                {
                    Preference.save(EmergancyContact.this,Preference.key_On_off,"off");
                }
            }
        });

    }
}