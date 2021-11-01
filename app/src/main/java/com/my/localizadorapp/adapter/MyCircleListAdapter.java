package com.my.localizadorapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.my.localizadorapp.NewOnItemlisner;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.act.CircleDetailsActivity;
import com.my.localizadorapp.model.CircleListModel;
import com.my.localizadorapp.model.RatingModel;

import java.util.ArrayList;


public class MyCircleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<CircleListModel.Result> modelList;
    private OnItemClickListener mItemClickListener;
    private NewOnItemlisner newOnItemlisner;
    int pos=0;


    public MyCircleListAdapter(Context context, ArrayList<CircleListModel.Result> modelList,NewOnItemlisner newOnItemlisner) {
        this.mContext = context;
        this.modelList = modelList;
        this.newOnItemlisner = newOnItemlisner;
    }

    public void updateList(ArrayList<CircleListModel.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itme_my_circle, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final CircleListModel.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            if(pos==position)
            {
                genericViewHolder.RRCircle.setBackgroundColor(Color.parseColor("#F2F2F2"));

            }else
            {
                genericViewHolder.RRCircle.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            }

            genericViewHolder.RRDetails.setOnClickListener(v -> {

                Preference.save(mContext,Preference.KEY_Circle_ID,model.id);

                Preference.save(mContext,Preference.KEY_CircleName,model.circleName);
                Preference.save(mContext,Preference.KEY_CircleCode,model.code);

                mContext.startActivity(new Intent(mContext, CircleDetailsActivity.class));

            });

            genericViewHolder.RRCircle.setOnClickListener(v -> {

                pos= position;

                notifyDataSetChanged();

                newOnItemlisner.onItemClick(model.code,model.circleName);

                Preference.save(mContext,Preference.KEY_CircleCode,model.code);

                Preference.save(mContext,Preference.KEY_CircleName,model.circleName);

            });

            genericViewHolder.txtName.setText(model.circleName);

        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private CircleListModel.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, CircleListModel.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout RRDetails;
        RelativeLayout RRCircle;

        TextView txtName;

        public ViewHolder(final View itemView) {
            super(itemView);

          this.txtName=itemView.findViewById(R.id.txtName);
          this.RRDetails=itemView.findViewById(R.id.RRDetails);
          this.RRCircle=itemView.findViewById(R.id.RRCircle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }


}

