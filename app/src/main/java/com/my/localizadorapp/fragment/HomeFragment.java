package com.my.localizadorapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.localizadorapp.GPSTracker;
import com.my.localizadorapp.MainActivity;
import com.my.localizadorapp.OnItemClickListener;
import com.my.localizadorapp.R;
import com.my.localizadorapp.act.InviteNewFriend;
import com.my.localizadorapp.act.LoginActivity;
import com.my.localizadorapp.act.NotificationScree;
import com.my.localizadorapp.databinding.HomeFragmentBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements OnMapReadyCallback, OnItemClickListener {

    HomeFragmentBinding binding;
    private GoogleMap mMap;
    private View promptsView;
    private AlertDialog alertDialog;
    int PERMISSION_ID = 44;
    BottomFragmentMap bottomSheetFragment;
    GPSTracker gpsTracker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);

        gpsTracker = new GPSTracker(getActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);

        binding.imgNotification.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), NotificationScree.class));

        });

        binding.imgShare.setOnClickListener(v -> {

            AlertDaliog();

        });

        binding.txtSkip.setOnClickListener(v -> {

            binding.llJoinAlert.setVisibility(View.GONE);
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

        getCurrentLocation();

        return binding.getRoot();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        mMap = googleMap;

        mMap.clear();
        LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        Marker mSydney = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Sydney")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));


      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))));
    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(14).build();
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

                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }

            bottomSheetFragment.dismiss();

        }else if(position == 3)
        {
            if(mMap != null){
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            }
            bottomSheetFragment.dismiss();

        }else
        {
            if(mMap != null){

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }
        }
    }


    public void getCurrentLocation(){
        String loc = "";
        if(gpsTracker.canGetLocation())
        {
             loc = getAddress(getActivity(),gpsTracker.getLatitude(),gpsTracker.getLongitude());
        }
        Log.e("Location=====",loc);
    }

    public String getAddress(Context context, double latitude, double longitute) {
        List<Address> addresses;
        String addressStreet="";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitute, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addressStreet = addresses.get(0).getAddressLine(0);
            // address2 = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            //  city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String region = addresses.get(0).getAdminArea();

            binding.txtAddress.setText(addressStreet+"");

            Log.e("region====", region);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressStreet;

    }

}
