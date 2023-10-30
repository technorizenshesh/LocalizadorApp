package com.my.localizadorapp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.StateSet;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.my.localizadorapp.act.CheckoutActivity;
import com.my.localizadorapp.act.PrivacyPolicy;
import com.my.localizadorapp.act.TermsActivity;
import com.my.localizadorapp.adapter.RatingPrimiumAdapter;
import com.my.localizadorapp.databinding.PremiumFragmentBinding;
import com.my.localizadorapp.model.RatingModel;

import java.util.ArrayList;

public class PremiumFragment extends Fragment {
    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String AD_UNIT_ID = "ca-app-pub-6669202856871108/4796400259	";
    private static final long COUNTER_TIME = 10;
    private static final int GAME_OVER_REWARD = 1;

    PremiumFragmentBinding binding;

    private ArrayList<RatingModel> modelList = new ArrayList<>();
    RatingPrimiumAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.premium_fragment, container, false);
        loadRewardedAd();
        Log.e(TAG, "onCreateView: i am loading--------------------------------");

        MobileAds.initialize(requireActivity(), initializationStatus -> {
            loadRewardedAd();

        });

        binding.llOne.setOnClickListener(v -> {
            // Intent i = new Intent(getActivity(), BenifitActiviity.class);
            Intent i = new Intent(getActivity(), CheckoutActivity.class);
            i.putExtra("Name", "1 Week Subscription Plan");
            startActivity(i);
        });

        binding.llTwo.setOnClickListener(v -> {
            // Intent i = new Intent(getActivity(), BenifitActiviity.class);
            Intent i = new Intent(getActivity(), CheckoutActivity.class);
            i.putExtra("Name","1 Month Subscription Plan");
            startActivity(i);
        });

        binding.llThree.setOnClickListener(v -> {
          //  Intent i = new Intent(getActivity(), BenifitActiviity.class);
            Intent i = new Intent(getActivity(), CheckoutActivity.class);
            i.putExtra("Name","1 Year Subscription Plan");
            startActivity(i);
        });

        binding.txtPrivacy.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), PrivacyPolicy.class);
            startActivity(i);
        });

        binding.txtTrms.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), TermsActivity.class);
            startActivity(i);
        });

        setAdapter();

        return binding.getRoot();
    }


    private void setAdapter() {

        this.modelList.add(new RatingModel("Corn"));
        this.modelList.add(new RatingModel("Tomotoes"));
        mAdapter = new RatingPrimiumAdapter(getActivity(),modelList);
        binding.recyclerRating.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerRating.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        binding.recyclerRating.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new RatingPrimiumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, RatingModel model) {

            }
        });
    }


    private void loadRewardedAd() {
        if (rewardedAd == null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    getActivity(),
                    AD_UNIT_ID,
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d(StateSet.TAG, StateSet.TAG + loadAdError.getMessage());
                            rewardedAd = null;
                            //  Toast.makeText(HomeActivity.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rd) {
                            rewardedAd = rd;
                            Log.d(StateSet.TAG, StateSet.TAG + "onAdLoaded");
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
            Log.d(TAG, TAG + "The rewarded ad wasn't ready yet.");
            return;
        }
        // showVideoButton.setVisibility(View.INVISIBLE);
        isLoading = true;

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, TAG + "onAdShowedFullScreenContent");
                       /* Toast.makeText(HomeActivity.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();*/
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.d(TAG, TAG + "onAdFailedToShowFullScreenContent");
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
                        Log.d(TAG, TAG + "onAdDismissedFullScreenContent");
                        // Toast.makeText(HomeActivity.this, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT).show();
                        // Preload the next rewarded ad.

                        loadRewardedAd();

                    }
                });
        Activity activityContext = getActivity();
        rewardedAd.show(
                activityContext,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d("TAG", TAG + "The user earned the reward.");
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
}
