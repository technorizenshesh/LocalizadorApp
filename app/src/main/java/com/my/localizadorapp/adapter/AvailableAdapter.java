package com.my.localizadorapp.adapter;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.model.MemberListDataModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AvailableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<MemberListDataModel> modelList;
    private OnItemClickListener mItemClickListener;


    public AvailableAdapter(Context context, ArrayList<MemberListDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        Log.e("skgajdgakjsdkja","modelList = " + modelList);
    }

    public void updateList(ArrayList<MemberListDataModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_all_user_available, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final MemberListDataModel model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.txtUserName.setText(model.getUserDetail().getUserName());

            if (model.getUserDetail().getLat() != null && model.getUserDetail().getLon() != null) {
                double lat = Double.parseDouble(model.getUserDetail().getLat());
                double lon = Double.parseDouble(model.getUserDetail().getLon());

                String Address = getAddress(mContext, lat, lon);

                genericViewHolder.txtAddress.setText(Address);
            }

            if(model.getBattery()!=null)
            {
                genericViewHolder.txtBatery.setText(model.getBattery()+"% ");
            }
        }

    }

    @Override
    public int getItemCount() {
        return modelList == null ? 0 : modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private MemberListDataModel getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, MemberListDataModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUserName;
        TextView txtAddress;
        TextView txtBatery;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.txtUserName = itemView.findViewById(R.id.txtUserName);
            this.txtAddress = itemView.findViewById(R.id.txtAddress);
            this.txtBatery = itemView.findViewById(R.id.txtBatery);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


    public String getAddress(Context context, double latitude, double longitute) {
        String addressStreet = "";
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

            Preference.save(mContext, Preference.KEY_address, addressStreet);

            Log.e("region====", region);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressStreet;
    }

}

