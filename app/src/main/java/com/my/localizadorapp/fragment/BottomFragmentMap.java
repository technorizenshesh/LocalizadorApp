package com.my.localizadorapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;

import androidx.core.content.ContextCompat;
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

    public BottomFragmentMap(Context context, OnItemClickListener mItemClickListener, GoogleMap map) {
        this.context = context;
        this.mItemClickListener = mItemClickListener;
        this.map = map;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setupDialog(Dialog dialog, int style) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom, null, false);

        binding.llAuto.setOnClickListener(v -> {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    binding.RRNORMAL.setBackgroundResource(R.drawable.map_border);
                    binding.RRTERRAIN.setBackgroundResource(R.drawable.map_border2);
                    binding.RRSATELLITE.setBackgroundResource(R.drawable.map_border2);

                    binding.txtOne.setTextColor(ContextCompat.getColor(context, R.color.black));
                    binding.txtTwo.setTextColor(ContextCompat.getColor(context, R.color.gray));
                    binding.txtThree.setTextColor(ContextCompat.getColor(context, R.color.gray));

                    mItemClickListener.onItemClick(1, map);

                }

            }, 3000);


        });

        binding.llStreet.setOnClickListener(v -> {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    binding.RRNORMAL.setBackgroundResource(R.drawable.map_border2);
                    binding.RRTERRAIN.setBackgroundResource(R.drawable.map_border);
                    binding.RRSATELLITE.setBackgroundResource(R.drawable.map_border2);

                    binding.txtOne.setTextColor(ContextCompat.getColor(context, R.color.gray));
                    binding.txtTwo.setTextColor(ContextCompat.getColor(context, R.color.black));
                    binding.txtThree.setTextColor(ContextCompat.getColor(context, R.color.gray));

                    mItemClickListener.onItemClick(2, map);

                }

            }, 3000);


        });

        binding.llSatellite.setOnClickListener(v -> {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    binding.RRNORMAL.setBackgroundResource(R.drawable.map_border2);
                    binding.RRTERRAIN.setBackgroundResource(R.drawable.map_border2);
                    binding.RRSATELLITE.setBackgroundResource(R.drawable.map_border);

                    binding.txtOne.setTextColor(ContextCompat.getColor(context, R.color.gray));
                    binding.txtTwo.setTextColor(ContextCompat.getColor(context, R.color.gray));
                    binding.txtThree.setTextColor(ContextCompat.getColor(context, R.color.black));

                    mItemClickListener.onItemClick(3, map);
                }

            }, 3000);

        });
        
        dialog.setContentView(binding.getRoot());

        // setContentView(contentView);
        //((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

}
