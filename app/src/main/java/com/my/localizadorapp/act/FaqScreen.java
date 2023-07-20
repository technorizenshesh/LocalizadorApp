package com.my.localizadorapp.act;

import static com.my.localizadorapp.Preference.LANGUAGE;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.my.localizadorapp.adapter.FAQAdapter;
import com.my.localizadorapp.databinding.ActivityFaqScreenBinding;
import com.my.localizadorapp.model.FAQModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqScreen extends AppCompatActivity {

    ActivityFaqScreenBinding binding;
    InterstitialAd interstitialAd;


    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(FaqScreen.this, "ca-app-pub-5017067604593087/6794040495", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialA) {
                interstitialAd = interstitialA;
                interstitialAd.show(FaqScreen.this);
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

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq_screen);
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
        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });


        sessionManager = new SessionManager(this);
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            get_faq();

        } else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    public void get_faq() {
        String language = Preference.get(getApplicationContext(), LANGUAGE);
        String  lang="sp";
        if (language.equalsIgnoreCase("en")){
            lang = "en";
}
        Call<FAQModel> call = RetrofitClients.getInstance().getApi().get_faq(lang);
        call.enqueue(new Callback<FAQModel>() {
            @Override
            public void onResponse(Call<FAQModel> call, Response<FAQModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    FAQModel myclass = response.body();

                    if (myclass.status.equalsIgnoreCase("1")) {

                        ArrayList<FAQModel.Result> test = myclass.result;
                        // binding.txtTerms.setText(test);
                        binding.recy.setAdapter(new FAQAdapter(getApplicationContext(), test));
                        Toast.makeText(FaqScreen.this, myclass.message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(FaqScreen.this, myclass.message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FAQModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(FaqScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}