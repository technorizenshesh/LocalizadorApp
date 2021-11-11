package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.my.localizadorapp.Preference;
import com.my.localizadorapp.ProductOnItemlisner;
import com.my.localizadorapp.R;
import com.my.localizadorapp.adapter.HomeCategoryAdapter;
import com.my.localizadorapp.adapter.HomeProductAdapter;
import com.my.localizadorapp.databinding.ActivityPurchaseItemsBinding;
import com.my.localizadorapp.model.CategoryModel;
import com.my.localizadorapp.model.CircleListModel;
import com.my.localizadorapp.model.ProductShopDeatils;
import com.my.localizadorapp.model.ProductShopModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseItemsActivity extends AppCompatActivity implements ProductOnItemlisner {

    HomeCategoryAdapter mAdapterCategory;
    private ArrayList<CategoryModel.Result> modelListCategory = new ArrayList<>();

    ActivityPurchaseItemsBinding binding;
    private SessionManager sessionManager;

    HomeProductAdapter mAdapter;
    private ArrayList<ProductShopModel.Result> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_purchase_items);

        sessionManager = new SessionManager(PurchaseItemsActivity.this);

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            getCategory();

        }else {
            Toast.makeText(PurchaseItemsActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });
    }


    private void setAdapterOne(ArrayList<CategoryModel.Result> modelListCategory) {

        mAdapterCategory = new HomeCategoryAdapter(PurchaseItemsActivity.this,modelListCategory,PurchaseItemsActivity.this);
        binding.recyclerCategory.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PurchaseItemsActivity.this);
        binding.recyclerCategory.setLayoutManager(new LinearLayoutManager(PurchaseItemsActivity.this, LinearLayoutManager.HORIZONTAL, true));
        binding.recyclerCategory.setAdapter(mAdapterCategory);

        mAdapterCategory.SetOnItemClickListener(new HomeCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CategoryModel.Result model) {

            }
        });
    }

    private void setAdapter(ArrayList<ProductShopModel.Result> modelList) {

        mAdapter = new HomeProductAdapter(PurchaseItemsActivity.this,modelList);
        binding.recycleProduct.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PurchaseItemsActivity.this);
        binding.recycleProduct.setLayoutManager(new GridLayoutManager(PurchaseItemsActivity.this,2));
        binding.recycleProduct.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new HomeProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ProductShopModel.Result model) {

                Preference.save(PurchaseItemsActivity.this, Preference.Product_id,model.getId());

                startActivity(new Intent(PurchaseItemsActivity.this, PurchaseItemsActivityDetails.class));

            }
        });
    }


    public void getCategory() {

        Call<CategoryModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_category();
        call.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    CategoryModel myclass = response.body();

                    String status = String.valueOf(myclass.getStatus());
                    String message = myclass.getMessage();

                    if (status.equalsIgnoreCase("1")) {

                        modelListCategory = (ArrayList<CategoryModel.Result>) myclass.getResult();

                        setAdapterOne(modelListCategory);

                        ApiGetProductList("1");

                    } else {
                        Toast.makeText(PurchaseItemsActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(PurchaseItemsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ApiGetProductList(String id) {
        modelList.clear();
        Call<ProductShopModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_product_by_category(id);
        call.enqueue(new Callback<ProductShopModel>() {
            @Override
            public void onResponse(Call<ProductShopModel> call, Response<ProductShopModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    ProductShopModel myclass = response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")) {
                        binding.txtEmty.setVisibility(View.GONE);
                        modelList = (ArrayList<ProductShopModel.Result>) myclass.getResult();

                        setAdapter(modelList);
                    }
                } catch (Exception e) {
                    binding.txtEmty.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductShopModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.txtEmty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(String id) {

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            ApiGetProductList(id);

        }else {
            Toast.makeText(PurchaseItemsActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }
}