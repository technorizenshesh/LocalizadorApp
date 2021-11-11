package com.my.localizadorapp.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.my.localizadorapp.NewOnItemlisner;
import com.my.localizadorapp.ProductOnItemlisner;
import com.my.localizadorapp.R;
import com.my.localizadorapp.model.CategoryModel;
import com.my.localizadorapp.model.ProductShopModel;
import com.my.localizadorapp.utils.RetrofitClients;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Context mContext;
    private ArrayList<CategoryModel.Result> modelList;
    private OnItemClickListener mItemClickListener;
    private ProductOnItemlisner newOnItemlisner;
    int pos =0;

    public HomeCategoryAdapter(Context context, ArrayList<CategoryModel.Result> modelList,ProductOnItemlisner newOnItemlisner) {
        this.mContext = context;
        this.modelList = modelList;
        this.newOnItemlisner = newOnItemlisner;
    }

    public void updateList(ArrayList<CategoryModel.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final CategoryModel.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;


         //   String IsFav= model.getIsFav().toString();

            genericViewHolder.txtName.setText(model.getCategoryName());

            genericViewHolder.llCategory.setOnClickListener(v -> {
                pos=position;
                notifyDataSetChanged();
                newOnItemlisner.onItemClick(model.getId());

            });

            genericViewHolder.txtName.setOnClickListener(v -> {
                pos=position;
                notifyDataSetChanged();
                newOnItemlisner.onItemClick(model.getId());
            });

            if(pos==position)
            {
                genericViewHolder.card.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bg_SPLASH));

                genericViewHolder.txtName.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            }else {

                genericViewHolder.card.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

                genericViewHolder.txtName.setTextColor(ContextCompat.getColor(mContext, R.color.black));

            }



            //  Picasso.get().load(model.getImage()).into(genericViewHolder.img1);

        }

    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private CategoryModel.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, CategoryModel.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private CardView card;
        private LinearLayout llCategory;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.txtName=itemView.findViewById(R.id.txtName);
            this.llCategory=itemView.findViewById(R.id.llCategory);
            this.card=itemView.findViewById(R.id.card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }




}

