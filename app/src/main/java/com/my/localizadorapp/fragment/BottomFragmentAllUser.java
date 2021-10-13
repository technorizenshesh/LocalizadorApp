package com.my.localizadorapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.localizadorapp.OnItemClickListener;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.act.InviteNewFriend;
import com.my.localizadorapp.databinding.FragmentBottomBinding;
import com.my.localizadorapp.databinding.FragmentBottomUserBinding;


public class BottomFragmentAllUser extends BottomSheetDialogFragment {


    Context context;

    FragmentBottomUserBinding binding;
    String addressStreet="";

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

        String UserId = Preference.get(getActivity(),Preference.KEY_USER_ID);
        String UserName = Preference.get(getActivity(),Preference.KEY_UserName);
        String Address = Preference.get(getActivity(),Preference.KEY_address);
        String battery = Preference.get(getActivity(),Preference.KEY_battery);

         binding.txtUserName.setText(UserName);
         binding.txtAddress.setText(Address+"");
        binding.txtBatery.setText(String.valueOf(battery) + "%");

        binding.RRAddMember.setOnClickListener(v ->
        {

         Intent i = new Intent(getActivity(), InviteNewFriend.class);
          startActivity(i);

        });


        dialog.setContentView(binding.getRoot());
    }

}
