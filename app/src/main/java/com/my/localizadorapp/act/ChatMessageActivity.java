package com.my.localizadorapp.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.my.localizadorapp.Chat.MsgChatAct;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.adapter.ChatMemberListAdapter;
import com.my.localizadorapp.adapter.CircleSpinnerAdapter;
import com.my.localizadorapp.databinding.ActivityChatMessageBinding;
import com.my.localizadorapp.model.CircleListNewModel;
import com.my.localizadorapp.model.GetUserChatModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMessageActivity extends AppCompatActivity {
    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String AD_UNIT_ID2 = "ca-app-pub-5017067604593087/6794040495";
    ArrayList<CircleListNewModel.Result> modelList_my = new ArrayList<>();
    ArrayList<CircleListNewModel.Result> modelList_my_circle = new ArrayList<>();
    ArrayList<CircleListNewModel.Result> modelList = new ArrayList<>();
    ArrayList<GetUserChatModel.Result> modelListMember = new ArrayList<>();
    ArrayList<GetUserChatModel.Result> modelListMemberNew = new ArrayList<>();
    ChatMemberListAdapter mAdapter;
    String Code = "";
    SessionManager sessionManager;
    ActivityChatMessageBinding binding;
    private RewardedAd rewardedAd;
    private boolean isLoading = false;
    private InterstitialAd interstitialAd;

    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, AD_UNIT_ID2, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialA) {
                interstitialAd = interstitialA;
                interstitialAd.show(ChatMessageActivity.this);
                Log.i(TAG, "onAdLoaded");
                Toast.makeText(ChatMessageActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                ChatMessageActivity.this.interstitialAd = null;
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                ChatMessageActivity.this.interstitialAd = null;
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
                Log.i(TAG, "onAdFailedToLoad" + loadAdError.getMessage());
                interstitialAd = null;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_message);
        MobileAds.initialize(this, initializationStatus -> {
            // loadRewardedAd();

        });
        String ads = new SessionManager(this).getADES();
        Log.e(TAG, "onCreate: dvxvxvxvf -----" + ads);

        if (ads.equalsIgnoreCase("")) {
            loadInterstitialAd();
            Log.e(TAG, "onCreate: hgjfhnfdh");
        }
        Log.e(TAG, "onCreate: dvxvxvxvf");

        setUi();
    }

    private void setUi() {

        sessionManager = new SessionManager(ChatMessageActivity.this);
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ApiGetCircleList();

        } else {
            Toast.makeText(ChatMessageActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        binding.spinnerSbcategoy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3) {

                Code = modelList.get(pos).getCode();


                if (sessionManager.isNetworkAvailable()) {

                    binding.progressBar.setVisibility(View.VISIBLE);
                    ApiGetMemberList(Code);

                } else {
                    Toast.makeText(ChatMessageActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });


    }

    private void setAdapter(ArrayList<GetUserChatModel.Result> modelListMemberNew) {
        // modelList_my_circle.add(new RatingModel("Corn"));
        mAdapter = new ChatMemberListAdapter(ChatMessageActivity.this, modelListMemberNew);
        binding.recyclerChat.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerChat.setLayoutManager(new LinearLayoutManager(ChatMessageActivity.this));
        binding.recyclerChat.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new ChatMemberListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetUserChatModel.Result model) {

                startActivity(new Intent(ChatMessageActivity.this, MsgChatAct.class)
                        .putExtra("UserId", model.getId())
                        .putExtra("UserName", model.getUserName())
                        .putExtra("UserImage", model.getImage())
                        .putExtra("request_id", model.getId()));

            }
        });
    }

    public void ApiGetCircleList() {

        String UserId = Preference.get(ChatMessageActivity.this, Preference.KEY_USER_ID);

        Call<CircleListNewModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_get_circle_new(UserId);
        call.enqueue(new Callback<CircleListNewModel>() {
            @Override
            public void onResponse(Call<CircleListNewModel> call, Response<CircleListNewModel> response) {
                try {
                    binding.progressBar.setVisibility(View.GONE);
                    if (response.body()!=null){


                    if (response.body().status.equalsIgnoreCase("1")) {
                        CircleListNewModel myclass = response.body();
                        modelList_my = (ArrayList<CircleListNewModel.Result>) myclass.getResult();
                        modelList_my_circle = (ArrayList<CircleListNewModel.Result>) myclass.getCircleData();
                        if (modelList_my != null) {
                            for (int i = 0; i < modelList_my.size(); i++) {
                                modelList.add(modelList_my.get(i));
                            }
                        }
                        if (modelList_my_circle != null) {
                            for (int i = 0; i < modelList_my_circle.size(); i++) {
                                modelList.add(modelList_my_circle.get(i));
                            }
                        }
                        CircleSpinnerAdapter customAdapter = new CircleSpinnerAdapter(ChatMessageActivity.this, modelList);
                        binding.spinnerSbcategoy.setAdapter(customAdapter);

                    }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CircleListNewModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void ApiGetMemberList(String CircleCode) {
        modelListMember.clear();
        modelListMemberNew.clear();

        String UserId = Preference.get(ChatMessageActivity.this, Preference.KEY_USER_ID);

        Log.e("CircleCode---------", "" + CircleCode);

        Call<GetUserChatModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_circle_user(CircleCode);
        call.enqueue(new Callback<GetUserChatModel>() {
            @Override
            public void onResponse(Call<GetUserChatModel> call, Response<GetUserChatModel> response) {
                try {
                    binding.progressBar.setVisibility(View.GONE);
                    if (response.body() != null) {
                        if (Objects.equals(response.body().status, "1")) {
                            GetUserChatModel myclass = response.body();

                            if (myclass.getStatus().equalsIgnoreCase("1")) {

                                if (myclass.getResult() != null) {

                                    modelListMember = (ArrayList<GetUserChatModel.Result>) myclass.getResult();

                                    for (int i = 0; i < modelListMember.size(); i++) {
                                        if (!UserId.equalsIgnoreCase(modelListMember.get(i).getId())) {
                                            modelListMemberNew.add(modelListMember.get(i));
                                        }
                                    }

                                    setAdapter(modelListMemberNew);
                                }
                            }


                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetUserChatModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}