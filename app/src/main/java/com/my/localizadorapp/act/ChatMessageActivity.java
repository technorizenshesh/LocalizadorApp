package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.my.localizadorapp.Chat.MsgChatAct;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.adapter.ChatMemberListAdapter;
import com.my.localizadorapp.adapter.CircleSpinnerAdapter;
import com.my.localizadorapp.databinding.ActivityChatMessageBinding;
import com.my.localizadorapp.model.CircleListNewModel;
import com.my.localizadorapp.model.GetUserChatModel;
import com.my.localizadorapp.model.MemberListDataModel;
import com.my.localizadorapp.model.MemberListModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMessageActivity extends AppCompatActivity {
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
    ArrayList<CircleListNewModel.Result> modelList_my = new ArrayList<>();
    ArrayList<CircleListNewModel.Result> modelList_my_circle = new ArrayList<>();
    ArrayList<CircleListNewModel.Result> modelList = new ArrayList<>();

    ArrayList<GetUserChatModel.Result> modelListMember = new ArrayList<>();
    ArrayList<GetUserChatModel.Result> modelListMemberNew = new ArrayList<>();

    ChatMemberListAdapter mAdapter;
    String Code="";
    SessionManager sessionManager;
    ActivityChatMessageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_chat_message);
        MobileAds.initialize(this, initializationStatus -> {
            loadRewardedAd();

        });
        setUi();
    }

    private void setUi() {

        sessionManager = new SessionManager(ChatMessageActivity.this);

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ApiGetCircleList();

        }else {
            Toast.makeText(ChatMessageActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        binding.spinnerSbcategoy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                Code = modelList.get(pos).getCode();


            if (sessionManager.isNetworkAvailable()) {

                    binding.progressBar.setVisibility(View.VISIBLE);
                    ApiGetMemberList(Code);

                }else {
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
        mAdapter = new ChatMemberListAdapter(ChatMessageActivity.this,modelListMemberNew);
        binding.recyclerChat.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerChat.setLayoutManager(new LinearLayoutManager(ChatMessageActivity.this));
        binding.recyclerChat.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new ChatMemberListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetUserChatModel.Result model) {

               startActivity(new Intent(ChatMessageActivity.this, MsgChatAct.class)
                        .putExtra("UserId",model.getId())
                        .putExtra("UserName",model.getUserName())
                        .putExtra("UserImage",model.getImage())
                        .putExtra("request_id",model.getId()));

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

                    CircleListNewModel myclass = response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")) {

                        modelList_my= (ArrayList<CircleListNewModel.Result>) myclass.getResult();

                        modelList_my_circle= (ArrayList<CircleListNewModel.Result>) myclass.getCircleData();

                        if(modelList_my!=null)
                        {
                            for(int i=0;i<modelList_my.size();i++)
                            {
                                modelList.add(modelList_my.get(i));
                            }
                        }
                        if(modelList_my_circle!=null)
                        {
                            for(int i=0;i<modelList_my_circle.size();i++)
                            {
                                modelList.add(modelList_my_circle.get(i));
                            }
                        }

                        CircleSpinnerAdapter customAdapter=new CircleSpinnerAdapter(ChatMessageActivity.this,modelList);
                        binding.spinnerSbcategoy.setAdapter(customAdapter);

                       // ApiGetMemberList(modelList.get(0).getCode());

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

    public void ApiGetMemberList(String CircleCode)
    {
        modelListMember.clear();
        modelListMemberNew.clear();

        String UserId = Preference.get(ChatMessageActivity.this, Preference.KEY_USER_ID);

        Log.e("CircleCode---------",""+CircleCode);

        Call<GetUserChatModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_circle_user(CircleCode);
        call.enqueue(new Callback<GetUserChatModel>() {
            @Override
            public void onResponse(Call<GetUserChatModel> call, Response<GetUserChatModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    GetUserChatModel myclass= response.body();

                    if (myclass.getStatus().equalsIgnoreCase("1")){

                        if(myclass.getResult()!=null)
                        {

                            modelListMember = (ArrayList<GetUserChatModel.Result>) myclass.getResult();

                            for(int i=0;i<modelListMember.size();i++)
                            {
                                if(!UserId.equalsIgnoreCase(modelListMember.get(i).getId()))
                                {
                                    modelListMemberNew.add(modelListMember.get(i));
                                }
                            }

                            setAdapter(modelListMemberNew);
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