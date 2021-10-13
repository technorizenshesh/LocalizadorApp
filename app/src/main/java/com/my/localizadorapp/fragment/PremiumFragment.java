package com.my.localizadorapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.localizadorapp.MainActivity;
import com.my.localizadorapp.R;
import com.my.localizadorapp.act.BenifitActiviity;
import com.my.localizadorapp.act.NotificationScree;
import com.my.localizadorapp.act.PrivacyPolicy;
import com.my.localizadorapp.act.TermsActivity;
import com.my.localizadorapp.adapter.RatingPrimiumAdapter;
import com.my.localizadorapp.databinding.HomeFragmentBinding;
import com.my.localizadorapp.databinding.PremiumFragmentBinding;
import com.my.localizadorapp.model.RatingModel;

import java.util.ArrayList;

public class PremiumFragment extends Fragment implements OnMapReadyCallback {

    PremiumFragmentBinding binding;
    private GoogleMap mMap;

    private ArrayList<RatingModel> modelList = new ArrayList<>();
    RatingPrimiumAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.premium_fragment, container, false);

        binding.llOne.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), BenifitActiviity.class);
            i.putExtra("Name","1 Week Subscription Plan");
            startActivity(i);
        });

        binding.llTwo.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), BenifitActiviity.class);
            i.putExtra("Name","1 Month Subscription Plan");
            startActivity(i);
        });

        binding.llThree.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), BenifitActiviity.class);
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


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


}
