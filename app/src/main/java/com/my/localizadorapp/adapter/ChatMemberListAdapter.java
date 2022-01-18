package com.my.localizadorapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.my.localizadorapp.R;
import com.my.localizadorapp.model.GetAddressModel;
import com.my.localizadorapp.model.GetUserChatModel;
import com.my.localizadorapp.model.GetUserChatModel.Result;

import java.util.ArrayList;


public class ChatMemberListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<GetUserChatModel.Result> modelList;
    private OnItemClickListener mItemClickListener;


    public ChatMemberListAdapter(Context context, ArrayList<GetUserChatModel.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GetUserChatModel.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_chatmemebr_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GetUserChatModel.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.txtAddressType.setText(model.getUserName());
           // genericViewHolder.txtAddress.setText(model.getAddress());

        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private GetUserChatModel.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GetUserChatModel.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAddressType;
        TextView txtAddress;

        public ViewHolder(final View itemView) {
            super(itemView);

        this.txtAddressType=itemView.findViewById(R.id.txtAddressType);
        this.txtAddress=itemView.findViewById(R.id.txtAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


}

