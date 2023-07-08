package com.my.localizadorapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.my.localizadorapp.R;
import com.my.localizadorapp.model.FAQModel;

import java.util.ArrayList;


public class FAQAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<FAQModel.Result> modelList;
    private OnItemClickListener mItemClickListener;


    public FAQAdapter(Context context, ArrayList<FAQModel.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<FAQModel.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_faq, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final FAQModel.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.txtName.setText(model.question);
            genericViewHolder.txt_ans.setText(model.answer);
            genericViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (genericViewHolder.txt_ans.getVisibility() == View.VISIBLE)
                        genericViewHolder.txt_ans.setVisibility(View.GONE);
                    else genericViewHolder.txt_ans.setVisibility(View.VISIBLE);
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

    private FAQModel.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, FAQModel.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txt_ans;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.txtName = itemView.findViewById(R.id.txt_question);
            this.txt_ans = itemView.findViewById(R.id.txt_ans);

         /*   itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(),
                            modelList.get(getAdapterPosition()));

                }
            });*/
        }
    }


}

