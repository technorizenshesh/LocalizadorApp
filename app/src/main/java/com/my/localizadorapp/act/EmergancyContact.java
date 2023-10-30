package com.my.localizadorapp.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import com.my.localizadorapp.model.EmargancyModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergancyContact extends AppCompatActivity {

    ActivityEmergancyContactBinding binding;
    InterstitialAd interstitialAd;


    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(EmergancyContact.this
                , "ca-app-pub-6669202856871108/4796400259",
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
                        Log.i("TAG", "onAdFailedToLoad" + loadAdError.getMessage());
                        interstitialAd = null;
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emergancy_contact);

        String CheckIt = Preference.get(EmergancyContact.this, Preference.key_On_off);
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
        if (CheckIt.equalsIgnoreCase("on")) {
            binding.SwithON.setChecked(true);

        } else {
            binding.SwithON.setChecked(false);
        }

        binding.RRback.setOnClickListener(v -> {

            onBackPressed();

        });
     //   Apidelete_member();
        binding.textContact.setText(Preference.get(this,Preference.PHONE));
        binding.RRASend.setOnClickListener(v -> {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Hello sir Please Urgent Call me " +binding.textContact.getText().toString();
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
        );

        binding.SwithON.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Preference.save(EmergancyContact.this, Preference.key_On_off, "on");

                } else {
                    Preference.save(EmergancyContact.this, Preference.key_On_off, "off");
                }
            }
        });

    }


    public void Apidelete_member() {
        Call<EmargancyModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_emergency();
        call.enqueue(new Callback<EmargancyModel>() {
            @Override
            public void onResponse(@NonNull Call<EmargancyModel> call,
                                   @NonNull Response<EmargancyModel> response) {
                try {
                    EmargancyModel data = response.body();
                 if (response.body()!=null) {
                     assert data != null;
                     binding.textContact.setText(data.getResult().getMobile());
                 }


                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<EmargancyModel> call, Throwable t) {
                // binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}