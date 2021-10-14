package com.my.localizadorapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.my.localizadorapp.act.InviteNewFriend;
import com.my.localizadorapp.act.MyPlaceActivity;
import com.my.localizadorapp.adapter.MyAddressAdapter;
import com.my.localizadorapp.adapter.MyCircleListAdapter;
import com.my.localizadorapp.adapter.RatingPrimiumAdapter;
import com.my.localizadorapp.databinding.PlaceFragmentBinding;
import com.my.localizadorapp.databinding.PremiumFragmentBinding;
import com.my.localizadorapp.model.CircleListModel;
import com.my.localizadorapp.model.RatingModel;

import java.util.ArrayList;

public class PlaceFragment extends Fragment {

    PlaceFragmentBinding binding;

    ArrayList<RatingModel> modelList_my_circle = new ArrayList<>();
    MyAddressAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.place_fragment, container, false);

        binding.RRLocation.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), MyPlaceActivity.class));
        });

       binding.RRAdd.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), MyPlaceActivity.class));
        });

       binding.txtPlaces.setOnClickListener(v -> {

           binding.txtPlaces.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
           binding.txtVisited.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
           binding.txtgeo.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

           binding.txtPlaces.setBackgroundResource(R.color.bg_SPLASH);
           binding.txtVisited.setBackgroundResource(R.color.gray_light);
           binding.txtgeo.setBackgroundResource(R.color.gray_light);


       });

       binding.txtVisited.setOnClickListener(v -> {

           binding.txtPlaces.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
           binding.txtVisited.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
           binding.txtgeo.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

           binding.txtPlaces.setBackgroundResource(R.color.gray_light);
           binding.txtVisited.setBackgroundResource(R.color.bg_SPLASH);
           binding.txtgeo.setBackgroundResource(R.color.gray_light);

       });


       binding.txtgeo.setOnClickListener(v -> {

           binding.txtPlaces.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
           binding.txtVisited.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
           binding.txtgeo.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

           binding.txtPlaces.setBackgroundResource(R.color.gray_light);
           binding.txtVisited.setBackgroundResource(R.color.gray_light);
           binding.txtgeo.setBackgroundResource(R.color.bg_SPLASH);

       });

        setAdapter();

        return binding.getRoot();
    }


    private void setAdapter() {

        this.modelList_my_circle.add(new RatingModel("Corn"));

        mAdapter = new MyAddressAdapter(getActivity(), this.modelList_my_circle);
        binding.recyclerMyAddress.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerMyAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerMyAddress.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new MyAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, RatingModel model) {

            }
        });
    }
}
