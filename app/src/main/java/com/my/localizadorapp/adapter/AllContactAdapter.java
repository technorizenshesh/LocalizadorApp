package com.my.localizadorapp.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.my.localizadorapp.R;
import com.my.localizadorapp.model.GetAddressModel;
import com.my.localizadorapp.model.GetAllContackModel;

import java.util.ArrayList;


public class AllContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetAllContackModel> modelList;
    private OnItemClickListener mItemClickListener;


    public AllContactAdapter(Context context, ArrayList<GetAllContackModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GetAllContackModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_my_all_contact, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetAllContackModel model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.txtName.setText(model.getName());
            genericViewHolder.txtPhone.setText(model.getPhoneNumber());

            genericViewHolder.CardView.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);

            genericViewHolder.CardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setSelected(!model.isSelected());
                    genericViewHolder.CardView.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private GetAllContackModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetAllContackModel model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtPhone;
         CardView CardView;

        public ViewHolder(final View itemView) {
            super(itemView);

        this.txtPhone=itemView.findViewById(R.id.txtPhone);
        this.txtName=itemView.findViewById(R.id.txtName);
        this.CardView=itemView.findViewById(R.id.CardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


}

