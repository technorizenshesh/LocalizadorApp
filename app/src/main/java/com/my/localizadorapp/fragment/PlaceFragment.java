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
import com.my.localizadorapp.R;
import com.my.localizadorapp.act.BenifitActiviity;
import com.my.localizadorapp.adapter.RatingPrimiumAdapter;
import com.my.localizadorapp.databinding.PlaceFragmentBinding;
import com.my.localizadorapp.databinding.PremiumFragmentBinding;
import com.my.localizadorapp.model.RatingModel;

import java.util.ArrayList;

public class PlaceFragment extends Fragment {

    PlaceFragmentBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.place_fragment, container, false);

        return binding.getRoot();

    }
}
