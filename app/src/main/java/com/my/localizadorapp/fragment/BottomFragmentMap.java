package com.my.localizadorapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.my.localizadorapp.OnItemClickListener;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.FragmentBottomBinding;


public class BottomFragmentMap extends BottomSheetDialogFragment {


    Context context;

    FragmentBottomBinding binding;
    private OnItemClickListener mItemClickListener;
    GoogleMap map;

    public BottomFragmentMap(Context context,OnItemClickListener mItemClickListener,GoogleMap map) {
        this.context=context;
        this.mItemClickListener = mItemClickListener;
        this.map = map;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setupDialog(Dialog dialog, int style) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bottom,null,false);

        binding.llAuto.setOnClickListener(v -> {

            mItemClickListener.onItemClick(1,map);

        });

        binding.llStreet.setOnClickListener(v -> {
            mItemClickListener.onItemClick(2,map);

        });

        binding.llSatellite.setOnClickListener(v -> {
            mItemClickListener.onItemClick(3,map);

        });


        dialog.setContentView(binding.getRoot());

        // setContentView(contentView);
        //((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

}
