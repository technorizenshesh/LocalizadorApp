package com.my.localizadorapp.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.my.localizadorapp.GPSTracker;
import com.my.localizadorapp.MainActivity;
import com.my.localizadorapp.OnItemClickListener;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.act.ChangeMobile;
import com.my.localizadorapp.act.CircleAddScreen;
import com.my.localizadorapp.act.CircleDetailsActivity;
import com.my.localizadorapp.act.HomeActivity;
import com.my.localizadorapp.act.InviteNewFriend;
import com.my.localizadorapp.act.NotificationScree;
import com.my.localizadorapp.act.OtpScreenActivity;
import com.my.localizadorapp.act.SignUpActivity;
import com.my.localizadorapp.adapter.MyCircleListAdapter;
import com.my.localizadorapp.adapter.RatingPrimiumAdapter;
import com.my.localizadorapp.databinding.HomeFragmentBinding;
import com.my.localizadorapp.model.ChangeMobileModel;
import com.my.localizadorapp.model.CircleListModel;
import com.my.localizadorapp.model.CricleCreate;
import com.my.localizadorapp.model.RatingModel;
import com.my.localizadorapp.model.SignUpModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback, OnItemClickListener {

    HomeFragmentBinding binding;
    private GoogleMap mMap;
    private View promptsView;
    private AlertDialog alertDialog;
    private AlertDialog alertDialog_create;
    int PERMISSION_ID = 44;
    BottomFragmentMap bottomSheetFragment;
    BottomFragmentAllUser bottomSheetFragmentUser;
    GPSTracker gpsTracker;

    boolean isCircleList=false;

    double latitude=0;
    double longitude=0;
    String UserName="";
    SessionManager sessionManager;
    String addressStreet="";

    ArrayList<CircleListModel.Result> modelList_my_circle = new ArrayList<>();
    MyCircleListAdapter mAdapter;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float)scale;
           String Battery =  String.valueOf(batteryPct);

            binding.txtBatery.setText(String.valueOf(batteryPct) + "%");

            Preference.save(getActivity(),Preference.KEY_battery,Battery);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);

        getActivity().registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        //Gps Lat Long
        gpsTracker=new GPSTracker(getActivity());
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        }else{
            gpsTracker.showSettingsAlert();
        }

        sessionManager =new SessionManager(getActivity());

         String UserId = Preference.get(getActivity(),Preference.KEY_USER_ID);
         UserName = Preference.get(getActivity(),Preference.KEY_UserName);

        binding.txtUserName.setText(UserName);

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

        binding.RRjoin.setOnClickListener(v -> {

            AlertDaliogJoinCircle();

        });

        binding.RRCreate.setOnClickListener(v -> {

            AlertDaliogCircleCreate();

        });

        binding.RRjoinCircle.setOnClickListener(v -> {

            if(isCircleList)
            {
                binding.txtCircle.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(),R.drawable.drp_dwn_white), null);
                binding.llData.setVisibility(View.VISIBLE);
                binding.llListCircle.setVisibility(View.GONE);
                isCircleList=false;

            }else
            {
                binding.txtCircle.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(),R.drawable.up_down), null);
                binding.llData.setVisibility(View.GONE);
                binding.llListCircle.setVisibility(View.VISIBLE);
                isCircleList=true;
            }
          //  AlertDaliogJoinCircle();
        });

        binding.imgMap.setOnClickListener(v -> {

            bottomSheetFragment = new BottomFragmentMap(getActivity(),HomeFragment.this,mMap);
            bottomSheetFragment.show(getActivity().getSupportFragmentManager(), "BottomSheet");

        });

        binding.cardUserDetails.setOnClickListener(v -> {

            bottomSheetFragmentUser = new BottomFragmentAllUser(getActivity(),addressStreet);
            bottomSheetFragmentUser.show(getActivity().getSupportFragmentManager(), "BottomSheet");

        });

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            ApiGetCircleList();

        }else {

            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();

        }

        getCurrentLocation();

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

        binding.txtCircle.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(),R.drawable.drp_dwn_white), null);
        binding.llData.setVisibility(View.VISIBLE);
        binding.llListCircle.setVisibility(View.GONE);
        isCircleList=false;

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            ApiGetCircleList();

        }else {

            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        mMap = googleMap;

        mMap.clear();

        LatLng sydney = new LatLng(latitude, longitude);

        Marker mSydney = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Sydney")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));

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
        EditText edtCode;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(getActivity());
        promptsView = li.inflate(R.layout.alert_joincircle, null);
        RRjoinCircle = (RelativeLayout) promptsView.findViewById(R.id.RRjoinCircle);
        edtCode = (EditText) promptsView.findViewById(R.id.edtCode);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        RRjoinCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              String JoinCircle= edtCode.getText().toString();

                if(!JoinCircle.equalsIgnoreCase(""))
                {
                    if (sessionManager.isNetworkAvailable()) {

                        binding.progressBar.setVisibility(View.VISIBLE);

                        ApiMethodJoinCircle(JoinCircle);

                    }else {

                        Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Enter Mobile Number.", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void AlertDaliogCircleCreate() {

        LayoutInflater li;
        RelativeLayout RRContinueCreate;
        EditText edtCircleName;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(getActivity());
        promptsView = li.inflate(R.layout.alert_circle_add, null);
        RRContinueCreate = (RelativeLayout) promptsView.findViewById(R.id.RRContinueCreate);
        edtCircleName = (EditText) promptsView.findViewById(R.id.edtCircleName);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        RRContinueCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              String CricelName= edtCircleName.getText().toString();

                if(!CricelName.equalsIgnoreCase(""))
                {
                    if (sessionManager.isNetworkAvailable()) {

                        binding.progressBar.setVisibility(View.VISIBLE);

                        ApiMethodCircleCreate(CricelName);

                    }else {

                        Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Enter Mobile Number.", Toast.LENGTH_SHORT).show();
                }

                alertDialog_create.dismiss();

            }
        });

        alertDialog_create = alertDialogBuilder.create();
        alertDialog_create.show();

       alertDialog_create.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

            Preference.save(getActivity(),Preference.KEY_address,addressStreet);

            binding.txtAddress.setText(addressStreet+"");

            Log.e("region====", region);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressStreet;
    }



    private void setAdapter(ArrayList<CircleListModel.Result> modelList_my_circle) {

        //this.modelList_my_circle.add(new RatingModel("Corn"));

        mAdapter = new MyCircleListAdapter(getActivity(), this.modelList_my_circle);
        binding.recyclerMyCircle.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerMyCircle.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerMyCircle.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new MyCircleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CircleListModel.Result model) {

            }
        });
    }

    public void ApiMethodCircleCreate(String cricelName) {

        String UserId = Preference.get(getActivity(),Preference.KEY_USER_ID);
        String UserCode = Preference.get(getActivity(),Preference.KEY_UserCode);

        Call<CricleCreate> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_create_circle(UserId,cricelName, String.valueOf(latitude), String.valueOf(longitude),"Yes");
        call.enqueue(new Callback<CricleCreate>() {
            @Override
            public void onResponse(Call<CricleCreate> call, Response<CricleCreate> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    CricleCreate myclass= response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")){

                        binding.txtCircle.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(),R.drawable.up_down), null);
                        binding.llData.setVisibility(View.GONE);
                        binding.llListCircle.setVisibility(View.VISIBLE);

                        ApiGetCircleList();

                    }else {

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CricleCreate> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ApiMethodJoinCircle(String Code){

        String UserId = Preference.get(getActivity(),Preference.KEY_USER_ID);

        Call<CricleCreate> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_Join_circle(UserId,Code,String.valueOf(latitude), String.valueOf(longitude));
        call.enqueue(new Callback<CricleCreate>() {
            @Override
            public void onResponse(Call<CricleCreate> call, Response<CricleCreate> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    CricleCreate myclass= response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")){

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<CricleCreate> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void ApiGetCircleList() {

        String UserId = Preference.get(getActivity(),Preference.KEY_USER_ID);

        Call<CircleListModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_get_circle(UserId);
        call.enqueue(new Callback<CircleListModel>() {
            @Override
            public void onResponse(Call<CircleListModel> call, Response<CircleListModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    CircleListModel myclass= response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")){

                        modelList_my_circle = (ArrayList<CircleListModel.Result>) myclass.result;

                        setAdapter(modelList_my_circle);

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CircleListModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
