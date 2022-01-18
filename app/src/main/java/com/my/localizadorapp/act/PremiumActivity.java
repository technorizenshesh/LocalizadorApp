package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.my.localizadorapp.R;
import com.my.localizadorapp.adapter.RatingPrimiumAdapter;
import com.my.localizadorapp.databinding.ActivityPremiumBinding;
import com.my.localizadorapp.model.RatingModel;

import java.util.ArrayList;

public class PremiumActivity extends AppCompatActivity {

    ActivityPremiumBinding binding;

    private ArrayList<RatingModel> modelList = new ArrayList<>();
    RatingPrimiumAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_premium);

        binding.imgCross.setOnClickListener(v -> {

            onBackPressed();

        });

        binding.llOne.setOnClickListener(v -> {
            //Intent i = new Intent(PremiumActivity.this, BenifitActiviity.class);
            Intent i = new Intent(PremiumActivity.this, CheckoutActivity.class);
            i.putExtra("Name","1 Week Subscription Plan");
            startActivity(i);
        });

        binding.llTwo.setOnClickListener(v -> {
           // Intent i = new Intent(PremiumActivity.this, BenifitActiviity.class);
            Intent i = new Intent(PremiumActivity.this, CheckoutActivity.class);
            i.putExtra("Name","1 Month Subscription Plan");
            startActivity(i);
        });

        binding.llThree.setOnClickListener(v -> {
         //   Intent i = new Intent(PremiumActivity.this, BenifitActiviity.class);
           Intent i = new Intent(PremiumActivity.this, CheckoutActivity.class);
            i.putExtra("Name","1 Year Subscription Plan");
            startActivity(i);
        });

        binding.txtPrivacy.setOnClickListener(v -> {
            Intent i = new Intent(PremiumActivity.this, PrivacyPolicy.class);
            startActivity(i);
        });

        binding.txtTrms.setOnClickListener(v -> {
            Intent i = new Intent(PremiumActivity.this, TermsActivity.class);
            startActivity(i);
        });

        setAdapter();
    }

    private void setAdapter() {

    this.modelList.add(new RatingModel("Corn"));
    this.modelList.add(new RatingModel("Tomotoes"));
    mAdapter = new RatingPrimiumAdapter(PremiumActivity.this,modelList);
    binding.recyclerRating.setHasFixedSize(true);
    // use a linear layout manager
    binding.recyclerRating.setLayoutManager(new LinearLayoutManager(PremiumActivity.this, LinearLayoutManager.HORIZONTAL, true));
    binding.recyclerRating.setAdapter(mAdapter);
    mAdapter.SetOnItemClickListener(new RatingPrimiumAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, RatingModel model) {

        }
    });

}

}