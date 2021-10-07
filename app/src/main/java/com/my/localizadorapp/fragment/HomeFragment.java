package com.my.localizadorapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.localizadorapp.MainActivity;
import com.my.localizadorapp.OnItemClickListener;
import com.my.localizadorapp.R;
import com.my.localizadorapp.act.InviteNewFriend;
import com.my.localizadorapp.act.LoginActivity;
import com.my.localizadorapp.act.NotificationScree;
import com.my.localizadorapp.databinding.HomeFragmentBinding;

public class HomeFragment extends Fragment implements OnMapReadyCallback, OnItemClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    HomeFragmentBinding binding;
    private GoogleMap mMap;
    private View promptsView;
    private AlertDialog alertDialog;
    int PERMISSION_ID = 44;
    BottomFragmentMap bottomSheetFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);

        binding.imgNotification.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), NotificationScree.class));

        });

        binding.imgShare.setOnClickListener(v -> {

            AlertDaliog();

        });

        binding.RRjoinCircle.setOnClickListener(v -> {

            AlertDaliogJoinCircle();

        });

        binding.imgMap.setOnClickListener(v -> {

            bottomSheetFragment = new BottomFragmentMap(getActivity(),HomeFragment.this,mMap);
            bottomSheetFragment.show(getActivity().getSupportFragmentManager(), "BottomSheet");

        });
      binding.RRAddMember.setOnClickListener(v -> {

          Intent i = new Intent(getActivity(), InviteNewFriend.class);
          startActivity(i);


        });

        return binding.getRoot();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void AlertDaliog() {

        LayoutInflater li;
        RelativeLayout RRShare;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(getActivity());
        promptsView = li.inflate(R.layout.alert_map, null);
        RRShare = (RelativeLayout) promptsView.findViewById(R.id.RRShare);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        RRShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void AlertDaliogJoinCircle() {

        LayoutInflater li;
        RelativeLayout RRjoinCircle;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(getActivity());
        promptsView = li.inflate(R.layout.alert_joincircle, null);
        RRjoinCircle = (RelativeLayout) promptsView.findViewById(R.id.RRjoinCircle);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        RRjoinCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onItemClick(int position, GoogleMap map) {
        mMap = map;
        if(position ==1)
        {
            if(mMap != null){
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
            bottomSheetFragment.dismiss();

        }else if(position == 2)
        {
            if(mMap != null){
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }

            bottomSheetFragment.dismiss();

        }else if(position == 3)
        {
            if(mMap != null){
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
            bottomSheetFragment.dismiss();

        }else
        {
            if(mMap != null){

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
