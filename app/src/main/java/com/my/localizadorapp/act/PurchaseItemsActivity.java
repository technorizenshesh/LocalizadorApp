package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.ProductOnItemlisner;
import com.my.localizadorapp.R;
import com.my.localizadorapp.adapter.HomeCategoryAdapter;
import com.my.localizadorapp.adapter.HomeProductAdapter;
import com.my.localizadorapp.databinding.ActivityPurchaseItemsBinding;
import com.my.localizadorapp.model.CategoryModel;
import com.my.localizadorapp.model.CircleListModel;
import com.my.localizadorapp.model.ProductShopDeatils;
import com.my.localizadorapp.model.ProductShopModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseItemsActivity extends AppCompatActivity implements ProductOnItemlisner {

    HomeCategoryAdapter mAdapterCategory;
    private ArrayList<CategoryModel.Result> modelListCategory = new ArrayList<>();

    ActivityPurchaseItemsBinding binding;
    private SessionManager sessionManager;

    HomeProductAdapter mAdapter;
    private ArrayList<ProductShopModel.Result> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_purchase_items);
        MobileAds.initialize(this, initializationStatus -> {
           // loadRewardedAd();

        });
        sessionManager = new SessionManager(PurchaseItemsActivity.this);

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            getCategory();

        }else {
            Toast.makeText(PurchaseItemsActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
    }
    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String AD_UNIT_ID = "ca-app-pub-5017067604593087/6794040495	";

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

    private void setAdapterOne(ArrayList<CategoryModel.Result> modelListCategory) {

        mAdapterCategory = new HomeCategoryAdapter(PurchaseItemsActivity.this,modelListCategory,PurchaseItemsActivity.this);
        binding.recyclerCategory.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PurchaseItemsActivity.this);
        binding.recyclerCategory.setLayoutManager(new LinearLayoutManager(PurchaseItemsActivity.this, LinearLayoutManager.HORIZONTAL, true));
        binding.recyclerCategory.setAdapter(mAdapterCategory);

        mAdapterCategory.SetOnItemClickListener(new HomeCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CategoryModel.Result model) {

            }
        });
    }

    private void setAdapter(ArrayList<ProductShopModel.Result> modelList) {

        mAdapter = new HomeProductAdapter(PurchaseItemsActivity.this,modelList);
        binding.recycleProduct.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PurchaseItemsActivity.this);
        binding.recycleProduct.setLayoutManager(new GridLayoutManager(PurchaseItemsActivity.this,2));
        binding.recycleProduct.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new HomeProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ProductShopModel.Result model) {

                Preference.save(PurchaseItemsActivity.this, Preference.Product_id,model.getId());

                startActivity(new Intent(PurchaseItemsActivity.this, PurchaseItemsActivityDetails.class));

            }
        });
    }


    public void getCategory() {

        Call<CategoryModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_category();
        call.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    CategoryModel myclass = response.body();

                    String status = String.valueOf(myclass.getStatus());
                    String message = myclass.getMessage();

                    if (status.equalsIgnoreCase("1")) {

                        modelListCategory = (ArrayList<CategoryModel.Result>) myclass.getResult();

                        setAdapterOne(modelListCategory);

                        ApiGetProductList("1");

                    } else {
                        Toast.makeText(PurchaseItemsActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(PurchaseItemsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ApiGetProductList(String id) {
        modelList.clear();
        Call<ProductShopModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_product_by_category(id);
        call.enqueue(new Callback<ProductShopModel>() {
            @Override
            public void onResponse(Call<ProductShopModel> call, Response<ProductShopModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    ProductShopModel myclass = response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")) {
                        binding.txtEmty.setVisibility(View.GONE);
                        modelList = (ArrayList<ProductShopModel.Result>) myclass.getResult();

                        setAdapter(modelList);
                    }
                } catch (Exception e) {
                    binding.txtEmty.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductShopModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.txtEmty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(String id) {

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            ApiGetProductList(id);

        }else {
            Toast.makeText(PurchaseItemsActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }
}