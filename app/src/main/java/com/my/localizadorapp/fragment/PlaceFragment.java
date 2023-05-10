package com.my.localizadorapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.StateSet;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.act.BenifitActiviity;
import com.my.localizadorapp.act.InviteNewFriend;
import com.my.localizadorapp.act.MyPlaceActivity;
import com.my.localizadorapp.act.PlaceListAddress;
import com.my.localizadorapp.adapter.MyAddressAdapter;
import com.my.localizadorapp.adapter.MyCircleListAdapter;
import com.my.localizadorapp.adapter.RatingPrimiumAdapter;
import com.my.localizadorapp.databinding.PlaceFragmentBinding;
import com.my.localizadorapp.databinding.PremiumFragmentBinding;
import com.my.localizadorapp.model.CircleListModel;
import com.my.localizadorapp.model.GetAddressModel;
import com.my.localizadorapp.model.RatingModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceFragment extends Fragment {

    PlaceFragmentBinding binding;

    ArrayList<GetAddressModel.Result> modelList_my_circle = new ArrayList<>();
    MyAddressAdapter mAdapter;
    SessionManager sessionManager;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private RewardedAd rewardedAd;
    private boolean isLoading = false;
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
            Log.d("TAG", "TAG" + "The rewarded ad wasn't ready yet.");
            Toast.makeText(requireContext(), "The rewarded ad wasn't ready yet.", Toast.LENGTH_SHORT).show();
            return;
        }
        // showVideoButton.setVisibility(View.INVISIBLE);
        isLoading = true;

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d("TAG", "TAG" + "onAdShowedFullScreenContent");
                       /* Toast.makeText(HomeActivity.this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();*/
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.d("TAG", "TAG" + "onAdFailedToShowFullScreenContent");
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
                requireActivity(),
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.place_fragment, container, false);


            AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        sessionManager =new SessionManager(getActivity());
        MobileAds.initialize(requireContext(), initializationStatus -> {
            loadRewardedAd();

        });
        binding.RRLocation.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), PlaceListAddress.class));
        });

       binding.RRAdd.setOnClickListener(v ->
       {
            startActivity(new Intent(getActivity(), PlaceListAddress.class));
        });

       binding.txtPlaces.setOnClickListener(v -> {

           binding.RRAddressLst.setVisibility(View.VISIBLE);
           binding.llemty.setVisibility(View.GONE);

           binding.txtPlaces.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
           binding.txtVisited.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
           binding.txtgeo.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

           binding.txtPlaces.setBackgroundResource(R.color.bg_SPLASH);
           binding.txtVisited.setBackgroundResource(R.color.gray_light);
           binding.txtgeo.setBackgroundResource(R.color.gray_light);


       });

       binding.txtVisited.setOnClickListener(v -> {

           binding.RRAddressLst.setVisibility(View.GONE);
           binding.llemty.setVisibility(View.VISIBLE);

           binding.txtPlaces.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
           binding.txtVisited.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
           binding.txtgeo.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

           binding.txtPlaces.setBackgroundResource(R.color.gray_light);
           binding.txtVisited.setBackgroundResource(R.color.bg_SPLASH);
           binding.txtgeo.setBackgroundResource(R.color.gray_light);

       });


       binding.txtgeo.setOnClickListener(v -> {

           binding.RRAddressLst.setVisibility(View.GONE);
           binding.llemty.setVisibility(View.VISIBLE);


           binding.txtPlaces.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
           binding.txtVisited.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
           binding.txtgeo.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

           binding.txtPlaces.setBackgroundResource(R.color.gray_light);
           binding.txtVisited.setBackgroundResource(R.color.gray_light);
           binding.txtgeo.setBackgroundResource(R.color.bg_SPLASH);

       });

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            ApiGetAddressList();

        }else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }


    private void setAdapter(ArrayList<GetAddressModel.Result> modelList_my_circle) {

       // modelList_my_circle.add(new RatingModel("Corn"));

        mAdapter = new MyAddressAdapter(getActivity(),modelList_my_circle);
        binding.recyclerMyAddress.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerMyAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerMyAddress.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new MyAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetAddressModel.Result model) {

            }
        });
    }

    public void ApiGetAddressList() {

        String UserId = Preference.get(getActivity(), Preference.KEY_USER_ID);

        Call<GetAddressModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_address(UserId);
        call.enqueue(new Callback<GetAddressModel>() {
            @Override
            public void onResponse(Call<GetAddressModel> call, Response<GetAddressModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    GetAddressModel myclass = response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")) {

                        modelList_my_circle= (ArrayList<GetAddressModel.Result>) myclass.getResult();

                        setAdapter(modelList_my_circle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetAddressModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}
