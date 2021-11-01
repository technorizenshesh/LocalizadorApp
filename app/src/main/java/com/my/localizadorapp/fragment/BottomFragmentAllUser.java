package com.my.localizadorapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.localizadorapp.OnItemClickListener;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.act.CircleDetailsActivity;
import com.my.localizadorapp.act.InviteNewFriend;
import com.my.localizadorapp.adapter.AvailableAdapter;
import com.my.localizadorapp.adapter.MemberListAdapter;
import com.my.localizadorapp.databinding.FragmentBottomBinding;
import com.my.localizadorapp.databinding.FragmentBottomUserBinding;
import com.my.localizadorapp.model.MemberListDataModel;
import com.my.localizadorapp.model.MemberListModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BottomFragmentAllUser extends BottomSheetDialogFragment {


    Context context;

    FragmentBottomUserBinding binding;
    String addressStreet="";
    private ArrayList<MemberListDataModel> modelListCircleDetails = new ArrayList<>();
    AvailableAdapter mAdapterCircleDetails;
    SessionManager sessionManager;


    public BottomFragmentAllUser(Context context,String Address) {
        this.context=context;
        this.addressStreet=Address;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setupDialog(Dialog dialog, int style) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bottom_user,null,false);

        sessionManager =new SessionManager(getActivity());

        binding.RRAddMember.setOnClickListener(v ->
        {

         Intent i = new Intent(getActivity(), InviteNewFriend.class);
          startActivity(i);

        });

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);
            ApiGetMemberList();
        }else {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        dialog.setContentView(binding.getRoot());
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

            }
        });
    }


    public void ApiGetMemberList() {

        String UserId = Preference.get(getActivity(),Preference.KEY_USER_ID);
        String UserCode = Preference.get(getActivity(),Preference.KEY_CircleCode);
        String UserName = Preference.get(getActivity(),Preference.KEY_UserName);

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

                        binding.llDetails.setVisibility(View.VISIBLE);

                        if(myclass.getOwnerDetail()!=null)
                        {
                            binding.txtUserName.setText(myclass.getOwnerDetail().getUserName());

                            String Address = getAddress(getActivity(),myclass.getOwnerDetail().getLat(),myclass.getOwnerDetail().getLon());
                            binding.txtAddress.setText(Address+"");

                        }else
                        {
                            binding.txtUserName.setText(UserName);
                            String Address ="";
                            binding.txtAddress.setText(Address+"");
                        }

                        if(myclass.getResult()!=null)
                        {
                            modelListCircleDetails = (ArrayList<MemberListDataModel>) myclass.getResult ();
                            setAdapterCircleDetails(modelListCircleDetails);
                        }


                    }else {

                        binding.llDetails.setVisibility(View.GONE);

                        Toast.makeText(getActivity(), myclass.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MemberListModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.llDetails.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String getAddress(Context context, String latitude1, String longitute1) {

        double latitude= Double.parseDouble(latitude1);
        double longitute= Double.parseDouble(longitute1);

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

            binding.txtAddress.setText(addressStreet + "");

            Log.e("region====", region);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressStreet;
    }


}
