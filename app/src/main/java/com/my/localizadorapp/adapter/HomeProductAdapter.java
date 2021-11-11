package com.my.localizadorapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.my.localizadorapp.R;
import com.my.localizadorapp.model.ProductShopModel;


import java.util.ArrayList;


public class HomeProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    int pos = 0;
    private Context mContext;
    private ArrayList<ProductShopModel.Result> modelList;
    private OnItemClickListener mItemClickListener;

    public HomeProductAdapter(Context context, ArrayList<ProductShopModel.Result> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<ProductShopModel.Result> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final ProductShopModel.Result model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

          genericViewHolder.txtMassaeg.setText(model.getName()+"");
          genericViewHolder.price.setText(model.getPrice()+"");

        }

    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private ProductShopModel.Result getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, ProductShopModel.Result model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMassaeg;
        private TextView price;
        private ImageView massage;


        public ViewHolder(final View itemView) {
            super(itemView);

            this.txtMassaeg=itemView.findViewById(R.id.txtMassaeg);
            this.price=itemView.findViewById(R.id.price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));

                }
            });
        }
    }


}

