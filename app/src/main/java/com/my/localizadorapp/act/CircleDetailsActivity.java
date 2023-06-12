package com.my.localizadorapp.act;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.adapter.MemberListAdapter;
import com.my.localizadorapp.databinding.ActivityCircleDetailsBinding;
import com.my.localizadorapp.model.DeleteModel;
import com.my.localizadorapp.model.MemberListDataModel;
import com.my.localizadorapp.model.MemberListModel;
import com.my.localizadorapp.model.UpdatedCircleModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CircleDetailsActivity extends AppCompatActivity {
    private boolean isLoading = false;
    private static final String TAG = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    ActivityCircleDetailsBinding binding;
    private View promptsView;
    private AlertDialog alertDialog;
    String CircleName = "";
    SessionManager sessionManager;
    private ArrayList<MemberListDataModel> modelList = new ArrayList<>();
    MemberListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_circle_details);
        sessionManager = new SessionManager(CircleDetailsActivity.this);
        CircleName = Preference.get(CircleDetailsActivity.this, Preference.KEY_CircleName);
        String CircleCount = Preference.get(CircleDetailsActivity.this, Preference.KEY_CircleCount);
        MobileAds.initialize(this, initializationStatus -> {
           // loadRewardedAd();
        });
        loadAd();
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        if (CircleCount.equalsIgnoreCase("1")) {binding.RRDelete.setVisibility(View.GONE);}
        binding.txtCircleName.setText(CircleName);

        binding.RRUserProfile.setOnClickListener(v -> {

            startActivity(new Intent(CircleDetailsActivity.this, MyAccountActivity.class));
        });

       binding.RRDelete.setOnClickListener(v -> {

           if (sessionManager.isNetworkAvailable()) {

               binding.progressBar.setVisibility(View.VISIBLE);

               ApiDeleteCircle();

           }else {

               Toast.makeText(CircleDetailsActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
           }

        });

        binding.RRback.setOnClickListener(v ->
        {
            onBackPressed();
        });

        binding.RRupdateCircle.setOnClickListener(v ->
        {
            AlertDaliogUpdateCircle();
        });

        binding.RRInvite.setOnClickListener(v ->
        {
            Intent i = new Intent(CircleDetailsActivity.this, InviteNewFriend.class);
            startActivity(i);

        });

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);
            ApiGetMemberList();
        }else {
            Toast.makeText(CircleDetailsActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }
    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                "ca-app-pub-3940256099942544/1033173712",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Log.i(TAG, "onAdLoaded");
                        if (interstitialAd != null) {
                            interstitialAd.show(CircleDetailsActivity.this);
                        } else {
                        }
                        // Toast.makeText(HomeActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Log.d("TAG", "The ad was dismissed.");

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());

                    }
                });
    }
    private void AlertDaliogUpdateCircle() {
        LayoutInflater li;
        RelativeLayout RRContinueCreate;
        EditText edtCircleName;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(CircleDetailsActivity.this);
        promptsView = li.inflate(R.layout.alert_update_circle, null);
        RRContinueCreate = (RelativeLayout) promptsView.findViewById(R.id.RRContinueCreate);
        edtCircleName = (EditText) promptsView.findViewById(R.id.edtCircleName);
        alertDialogBuilder = new AlertDialog.Builder(CircleDetailsActivity.this);
        alertDialogBuilder.setView(promptsView);
        edtCircleName.setText(CircleName);
        RRContinueCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String CricelName = edtCircleName.getText().toString();

                if (sessionManager.isNetworkAvailable()) {

                    binding.progressBar.setVisibility(View.VISIBLE);

                    ApiMethodUpdate(CricelName);

                }else {

                    Toast.makeText(CircleDetailsActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();

                }

                alertDialog.dismiss();

            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
    private void setAdapter(ArrayList<MemberListDataModel> modelList) {

        //this.modelList_my_circle.add(new RatingModel("Corn"));

        mAdapter = new MemberListAdapter(CircleDetailsActivity.this,modelList);
        binding.recyclerMemebr.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerMemebr.setLayoutManager(new LinearLayoutManager(CircleDetailsActivity.this));
        binding.recyclerMemebr.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new MemberListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, MemberListDataModel model) {


            }
        });
    }

    public void ApiMethodUpdate(String cricelName) {

        String CircleId = Preference.get(CircleDetailsActivity.this,Preference.KEY_Circle_ID);
        String UserCode = Preference.get(CircleDetailsActivity.this,Preference.KEY_CircleCode);

        Call<UpdatedCircleModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_update_circle_name(CircleId,cricelName);
        call.enqueue(new Callback<UpdatedCircleModel>() {
            @Override
            public void onResponse(Call<UpdatedCircleModel> call, Response<UpdatedCircleModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    UpdatedCircleModel myclass= response.body();

                    if (myclass.status.equalsIgnoreCase("1")){

                        binding.txtCircleName.setText(myclass.circleName);

                        Toast.makeText(CircleDetailsActivity.this, myclass.message, Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(CircleDetailsActivity.this, myclass.message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UpdatedCircleModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(CircleDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ApiGetMemberList() {

        String UserId = Preference.get(CircleDetailsActivity.this,Preference.KEY_USER_ID);
        String UserName = Preference.get(CircleDetailsActivity.this,Preference.KEY_UserName);
        String UserCode = Preference.get(CircleDetailsActivity.this,Preference.KEY_CircleCode);

        Call<MemberListModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_get_member_detail(UserCode);
        call.enqueue(new Callback<MemberListModel>() {
            @Override
            public void onResponse(Call<MemberListModel> call, Response<MemberListModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    MemberListModel myclass= response.body();

                    if (myclass.getStatus().equalsIgnoreCase("1")){

                        if(myclass.getOwnerDetail()!=null)
                        {
                            binding.txtUser.setText(myclass.getOwnerDetail().getUserName());

                        }else
                        {
                            binding.txtUser.setText(UserName);
                        }

                        if(myclass.getResult()!=null)
                        {
                            modelList = (ArrayList<MemberListDataModel>) myclass.getResult();

                            setAdapter(modelList);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<MemberListModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void ApiDeleteCircle() {

        String Circle_id = Preference.get(CircleDetailsActivity.this,Preference.KEY_Circle_ID);

        Call<DeleteModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_delete_circle(Circle_id);
        call.enqueue(new Callback<DeleteModel>() {
            @Override
            public void onResponse(Call<DeleteModel> call, Response<DeleteModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    DeleteModel myclass= response.body();

                    if (myclass.getStatus().equalsIgnoreCase("1")){

                        startActivity(new Intent(CircleDetailsActivity.this,HomeActivity.class));

                        Toast.makeText(CircleDetailsActivity.this, myclass.getMessage(), Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(CircleDetailsActivity.this, myclass.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DeleteModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(CircleDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}