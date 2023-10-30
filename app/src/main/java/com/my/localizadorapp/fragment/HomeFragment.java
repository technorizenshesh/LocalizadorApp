package com.my.localizadorapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.localizadorapp.GPSTracker;
import com.my.localizadorapp.NewAddOnItemlisner;
import com.my.localizadorapp.NewOnItemlisner;
import com.my.localizadorapp.OnItemClickListener;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.Upd.UpdateLocationService;
import com.my.localizadorapp.act.CircleDetailsActivity;
import com.my.localizadorapp.act.HomeActivity;
import com.my.localizadorapp.act.InviteNewFriend;
import com.my.localizadorapp.act.MemberDetails;
import com.my.localizadorapp.act.NotificationScree;
import com.my.localizadorapp.adapter.AvailableAdapter;
import com.my.localizadorapp.adapter.MyCircleListAdapter;
import com.my.localizadorapp.adapter.MyCircleListAdd_Adapter;
import com.my.localizadorapp.databinding.HomeFragmentBinding;
import com.my.localizadorapp.model.CircleListModel;
import com.my.localizadorapp.model.CricleCreate;
import com.my.localizadorapp.model.MemberListDataModel;
import com.my.localizadorapp.model.MemberListModel;
import com.my.localizadorapp.utils.Constant;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback, OnItemClickListener,NewOnItemlisner, NewAddOnItemlisner {

    HomeFragmentBinding binding;
    private GoogleMap mMap;
    private View promptsView;
    private AlertDialog alertDialog;
    private AlertDialog alertDialog_create;
    int PERMISSION_ID = 44;
    BottomFragmentMap bottomSheetFragment;
    BottomFragmentAllUser bottomSheetFragmentUser;
    GPSTracker gpsTracker;

    boolean isCircleList = false;
    boolean isMemberDetails = false;

    double latitude = 0;
    double longitude = 0;
    String UserName = "";
    SessionManager sessionManager;
    String addressStreet = "";

    ArrayList<CircleListModel.Result> modelList_my_circle = new ArrayList<>();
    ArrayList<CircleListModel.CircleDatum> modelList_my_circleAdd = new ArrayList<>();
    MyCircleListAdapter mAdapter;
    MyCircleListAdd_Adapter mAdapterNew;
    String CircleName ="";
    String CircleCode ="";
    String UserId = "";
    String Battery="";

    private ArrayList<MemberListDataModel> modelListCircleDetails = new ArrayList<>();
    AvailableAdapter mAdapterCircleDetails;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float) scale;
            Battery = String.valueOf(batteryPct);

            binding.txtBatery.setText(String.valueOf(batteryPct) + "%");

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);


                AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        getActivity().registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        Preference.save(getActivity(), Preference.KEY_battery, Battery);

        //Gps Lat Long
        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }

        sessionManager = new SessionManager(getActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);

        UserId = Preference.get(getActivity(), Preference.KEY_USER_ID);
        CircleName = Preference.get(getActivity(), Preference.KEY_CircleName);
        CircleCode = Preference.get(getActivity(), Preference.KEY_CircleCode);
        UserName = Preference.get(getActivity(), Preference.KEY_UserName);


        binding.txtCircle.setText(CircleName);

        binding.imgNotification.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), NotificationScree.class));
        });

        binding.imgShare.setOnClickListener(v -> {

           String Address= getAddress(getActivity(),gpsTracker.getLatitude(),gpsTracker.getLongitude());

            String url = "http://maps.google.com/maps?daddr="+Address;

          Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = String.valueOf(Uri.parse(url));
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Location");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
           // AlertDaliog();

        });

        binding.txtSkip.setOnClickListener(v -> {

            binding.llJoinAlert.setVisibility(View.GONE);
        });

        binding.RRjoin.setOnClickListener(v -> {

            AlertDaliogJoinCircle();

        });

        binding.RRCreate.setOnClickListener(v ->{

            AlertDaliogCircleCreate();

        });

        binding.RRjoinCircle.setOnClickListener(v -> {

            if (isCircleList) {
                binding.txtCircle.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.baseline_keyboard_arrow_down), null);
                binding.llData.setVisibility(View.VISIBLE);
                binding.llListCircle.setVisibility(View.GONE);
                isCircleList = false;

            } else {
                binding.txtCircle.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.baseline_keyboard_arrow_up), null);
                binding.llData.setVisibility(View.GONE);
                binding.llListCircle.setVisibility(View.VISIBLE);
                isCircleList = true;
            }
            //  AlertDaliogJoinCircle();
        });

        binding.imgMap.setOnClickListener(v ->{

            bottomSheetFragment = new BottomFragmentMap(getActivity(), HomeFragment.this, mMap);
            bottomSheetFragment.show(getActivity().getSupportFragmentManager(), "BottomSheet");

        });

        binding.cardUserDetails.setOnClickListener(v -> {

            if (isMemberDetails) {

                binding.imgMember.setImageResource(R.drawable.baseline_keyboard_arrow_up);
                binding.RRMeberDetails.setVisibility(View.GONE);
                collapse(binding.RRMeberDetails);

                isMemberDetails = false;

            } else {
                binding.imgMember.setImageResource(R.drawable.baseline_keyboard_arrow_down);
                binding.RRMeberDetails.setVisibility(View.VISIBLE);
                expand(binding.RRMeberDetails);
                isMemberDetails = true;
            }

           /* bottomSheetFragmentUser = new BottomFragmentAllUser(getActivity(), addressStreet);
            bottomSheetFragmentUser.show(getActivity().getSupportFragmentManager(), "BottomSheet");
*/
        });

          binding.RRAddMember.setOnClickListener(v -> {

              Intent i = new Intent(getActivity(), InviteNewFriend.class);
              startActivity(i);

          });

       /* if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);

            ApiGetCircleList();
        } else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
*/
        getCurrentLocation();

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

      //  ContextCompat.startForegroundService(getActivity(),new Intent(getActivity(), UpdateLocationService.class));


        binding.txtCircle.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.baseline_keyboard_arrow_down), null);
        binding.llData.setVisibility(View.VISIBLE);
        binding.llListCircle.setVisibility(View.GONE);
        isCircleList = false;

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);
            ApiGetCircleList();

        } else {

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
                .title(UserName)
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.new_user)));

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

                String JoinCircle = edtCode.getText().toString();

                if (!JoinCircle.equalsIgnoreCase("")) {
                    if (sessionManager.isNetworkAvailable()) {

                        binding.progressBar.setVisibility(View.VISIBLE);

                        ApiMethodJoinCircle(JoinCircle);

                    } else {

                        Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Enter Valid Code.", Toast.LENGTH_SHORT).show();
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

                String CricelName = edtCircleName.getText().toString();

                if (!CricelName.equalsIgnoreCase("")) {
                    if (sessionManager.isNetworkAvailable()) {

                        binding.progressBar.setVisibility(View.VISIBLE);

                        ApiMethodCircleCreate(CricelName);

                    } else {

                        Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
                    }
                } else {
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
        if (position == 1) {
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
            bottomSheetFragment.dismiss();

        } else if (position == 2) {
            if (mMap != null) {

                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }

            bottomSheetFragment.dismiss();

        } else if (position == 3) {
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            }
            bottomSheetFragment.dismiss();

        } else {
            if (mMap != null) {

                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }
        }
    }


    public void getCurrentLocation() {
        String loc = "";
        if (gpsTracker.canGetLocation()) {
            loc = getAddress(getActivity(), gpsTracker.getLatitude(), gpsTracker.getLongitude());
        }
        Log.e("Location=====", loc);
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

            Preference.save(getActivity(), Preference.KEY_address, addressStreet);

           // binding.txtAddress.setText(addressStreet + "");

            Log.e("region====", region);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressStreet;
    }


    private void setAdapter(ArrayList<CircleListModel.Result> modelList_my_circle) {

        //this.modelList_my_circle.add(new RatingModel("Corn"));

        mAdapter = new MyCircleListAdapter(getActivity(), this.modelList_my_circle,HomeFragment.this);
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

   private void setAdapterCircleAdd(ArrayList<CircleListModel.CircleDatum> modelList_my_circleAdd) {

        //this.modelList_my_circleAdd.add(new RatingModel("Corn"));
       mAdapterNew = new MyCircleListAdd_Adapter(getActivity(),modelList_my_circleAdd,HomeFragment.this);
        binding.recyclerCircleAdd.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerCircleAdd.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerCircleAdd.setAdapter(mAdapterNew);
       mAdapterNew.SetOnItemClickListener(new MyCircleListAdd_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CircleListModel.CircleDatum model) {

            }
        });

    }

    public void ApiMethodCircleCreate(String cricelName) {

        String UserId = Preference.get(getActivity(), Preference.KEY_USER_ID);
        String UserCode = Preference.get(getActivity(), Preference.KEY_CircleCode);

        Call<CricleCreate> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_create_circle(UserId, cricelName, String.valueOf(latitude), String.valueOf(longitude), "Yes",Battery);
        call.enqueue(new Callback<CricleCreate>() {
            @Override
            public void onResponse(Call<CricleCreate> call, Response<CricleCreate> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    CricleCreate myclass = response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")) {

                        binding.txtCircle.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.baseline_keyboard_arrow_up), null);
                        binding.llData.setVisibility(View.GONE);
                        binding.llListCircle.setVisibility(View.VISIBLE);

                        ApiGetCircleList();

                    } else {

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

        String UserId = Preference.get(getActivity(), Preference.KEY_USER_ID);

        Call<CircleListModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_get_circle(UserId);
        call.enqueue(new Callback<CircleListModel>() {
            @Override
            public void onResponse(Call<CircleListModel> call, Response<CircleListModel> response) {
                try {
                    binding.progressBar.setVisibility(View.GONE);

                     if (response.body()!=null){

                         if (response.body().status.equalsIgnoreCase("1")) {
                             CircleListModel myclass = response.body();
                             Preference.save(getActivity(),Preference.KEY_CircleCount, String.valueOf(myclass.circleCount));
                             modelList_my_circle = (ArrayList<CircleListModel.Result>) myclass.result;
                             modelList_my_circleAdd = (ArrayList<CircleListModel.CircleDatum>) myclass.getCircleData();
                             if(modelList_my_circle!=null)
                             {
                                 setAdapter(modelList_my_circle);
                                 ApiGetMemberList(modelList_my_circle.get(0).getCode());

                                 //ApiGetMemberList(CircleCode);
                             }

                             if(modelList_my_circleAdd!=null)
                             {
                                 setAdapterCircleAdd(modelList_my_circleAdd);
                             }


                         } else {

                             //Toast.makeText(getActivity(), "NO Data ", Toast.LENGTH_SHORT).show();
                             Toast.makeText(getActivity(), getString(R.string.no_circle_found), Toast.LENGTH_SHORT).show();

                         }
                     }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CircleListModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getString(R.string.no_circle_found), Toast.LENGTH_SHORT).show();

                // Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void allINGoogleMap(ArrayList<Marker> markers){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))));
        mMap.animateCamera(cu);
    }

    private void setAdapterCircleDetails(ArrayList<MemberListDataModel> modelListCircleDetails) {

        //this.modelList_my_circle.add(new RatingModel("Corn"));

        mAdapterCircleDetails = new AvailableAdapter(getActivity(),modelListCircleDetails);
        binding.recyclerMemebr.setHasFixedSize(true);

        // use a linear layout manager
        binding.recyclerMemebr.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerMemebr.setAdapter(mAdapterCircleDetails);
        mAdapterCircleDetails.SetOnItemClickListener(new AvailableAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, MemberListDataModel model) {

                startActivity(new Intent(getActivity(), MemberDetails.class)
                        .putExtra("name",model.getUserDetail().getUserName())
                        .putExtra("lat",model.getUserDetail().getLat())
                        .putExtra("lon",model.getUserDetail().getLon())
                        .putExtra("Batery",model.getBattery()));
            }
        });
    }




    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 5;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 5
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 5){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    @Override
    public void onItemClick(String code,String CIrcleName) {
        CircleName = CIrcleName;
        binding.txtCircle.setText(CircleName);
        binding.llListCircle.setVisibility(View.GONE);
        binding.llData.setVisibility(View.VISIBLE);

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            Log.e("bhbjhv","aya"+code);

            ApiGetMemberList(code);

        }else{
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClick1(String code, String CircleName) {

        CircleName = CircleName;
        binding.txtCircle.setText(CircleName);
        binding.llListCircle.setVisibility(View.GONE);
        binding.llData.setVisibility(View.VISIBLE);

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            Log.e("bhbjhv","aya"+code);

            ApiGetMemberList(code);

        }else{
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    public void ApiGetMemberList(String CircleCode) {

        Log.e("CircleCode---------",""+CircleCode);

        Call<MemberListModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_get_member_detail(CircleCode);
        call.enqueue(new Callback<MemberListModel>() {
            @Override
            public void onResponse(Call<MemberListModel> call, Response<MemberListModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    MemberListModel myclass= response.body();

                    if (myclass.getStatus().equalsIgnoreCase("1")){

                        if(myclass.getResult()!=null)
                        {
                            Log.e("TAG", "onResponse: -------------"+myclass.getOwnerDetail().toString() );
                            Log.e("TAG", "onResponse: -------------"+myclass.getOwnerDetail().getLat() );
                            Log.e("TAG", "onResponse: -------------"+myclass.getOwnerDetail().getLat() );

                            double OwnerLat= Double.parseDouble(myclass.getOwnerDetail().getLat());
                            double OwnerLon= Double.parseDouble(myclass.getOwnerDetail().getLon());
                            Glide.with(requireActivity()).load(Constant.BASE_URL_IMAGE+myclass.getOwnerDetail().
                                            getImage()).placeholder(getActivity().getDrawable(R.drawable.user))
                                    .circleCrop().into(binding.user);

                            String Address = getAddress(getActivity(), OwnerLat, OwnerLon);

                            binding.txtUserName.setText(myclass.getOwnerDetail().getUserName());
                            binding.txtAddress.setText(Address);

                            modelListCircleDetails.clear();
                            modelListCircleDetails = (ArrayList<MemberListDataModel>) myclass.getResult ();
                            setAdapterCircleDetails(modelListCircleDetails);



                            if(!modelListCircleDetails.isEmpty())
                            {
                                ArrayList<Marker> markers = new ArrayList<>();
                                mMap.clear();

                                for(int i=0;i<modelListCircleDetails.size();i++)
                                {
                                    LatLng sydney = new LatLng(Double.parseDouble(modelListCircleDetails.get(i).getLat()),
                                            Double.parseDouble(modelListCircleDetails.get(i).getLon()));
                                    Marker mSydney = mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .title(UserName)
                                            .snippet("Population: 4,627,300")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.new_user)));
                                    markers.add(mSydney);
                                }

                                LatLng sydney = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

                                Marker mark1 = mMap.addMarker(new MarkerOptions()
                                        .position(sydney)
                                        .title(UserName)
                                        .snippet("Population: 4,627,300")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));
                                markers.add(mark1);
                                allINGoogleMap(markers);
                            }
                        }
                    }else {
                        modelListCircleDetails.clear();
                        setAdapterCircleDetails(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<MemberListModel> call, Throwable t) {
                modelListCircleDetails.clear();
                setAdapterCircleDetails(null);
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
    public void ApiMethodJoinCircle(String Code) {
        String UserId = Preference.get(getActivity(), Preference.KEY_USER_ID);
        Call<CricleCreate> call = RetrofitClients.getInstance().getApi().Api_Join_circle(
                UserId, Code, String.valueOf(latitude), String.valueOf(longitude),Battery);
        call.enqueue(new Callback<CricleCreate>() {
            @Override
            public void onResponse(Call<CricleCreate> call, @NonNull Response<CricleCreate> response) {
                try {
                    binding.progressBar.setVisibility(View.GONE);

                    if (response.body()!=null){
                    String status = response.body().status;
                    String message = response.body().message;
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.no_circle_found), Toast.LENGTH_SHORT).show();
                    }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                binding.progressBar.setVisibility(View.VISIBLE);
                ApiGetCircleList();
            }
            @Override
            public void onFailure(Call<CricleCreate> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getString(R.string.no_circle_found), Toast.LENGTH_SHORT).show();
                // Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
