package com.my.localizadorapp.act;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityPurchaseItemsBinding;
import com.my.localizadorapp.databinding.ActivityPurchaseItemsDetailsBinding;
import com.my.localizadorapp.model.ProductShopDeatils;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseItemsActivityDetails extends AppCompatActivity {

    ActivityPurchaseItemsDetailsBinding binding;
    private SessionManager sessionManager;
    private View promptsView;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_purchase_items_details);

        binding.RRback.setOnClickListener(v -> {

            onBackPressed();

        });

        binding.RRContinue.setOnClickListener(v -> {

            AlertDaliogRecharge();

        });

        sessionManager = new SessionManager(PurchaseItemsActivityDetails.this);

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            getShopDetails();

        }else {
            Toast.makeText(PurchaseItemsActivityDetails.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    public void getShopDetails() {

        String Product_id = Preference.get(PurchaseItemsActivityDetails.this, Preference.Product_id);

        Call<ProductShopDeatils> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_product_details(Product_id);
        call.enqueue(new Callback<ProductShopDeatils>() {
            @Override
            public void onResponse(Call<ProductShopDeatils> call, Response<ProductShopDeatils> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    ProductShopDeatils myclass = response.body();

                    String status = String.valueOf(myclass.getStatus());
                    String message = myclass.getMessage();

                    if (status.equalsIgnoreCase("1")) {

                        binding.txtMassaeg.setText(myclass.getResult().getName() + "");
                        binding.txtDescrip.setText(myclass.getResult().getDescription() + "");
                        binding.price.setText("$"+myclass.getResult().getPrice());

                    } else {
                        Toast.makeText(PurchaseItemsActivityDetails.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProductShopDeatils> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(PurchaseItemsActivityDetails.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void AlertDaliogRecharge() {

        LayoutInflater li;
        ImageView ivBack;
        CardForm cardForm;
        RelativeLayout RRDone;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(PurchaseItemsActivityDetails.this);
        promptsView = li.inflate(R.layout.activity_stripe_payment, null);
        ivBack = (ImageView) promptsView.findViewById(R.id.ivBack);
        cardForm = (CardForm) promptsView.findViewById(R.id.card_form);
        RRDone = (RelativeLayout) promptsView.findViewById(R.id.RRDone);

        alertDialogBuilder = new AlertDialog.Builder(PurchaseItemsActivityDetails.this, R.style.myFullscreenAlertDialogStyle);   //second argument

        alertDialogBuilder.setView(promptsView);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                //.mobileNumberExplanation("SMS is required on this number")
                .setup(PurchaseItemsActivityDetails.this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        RRDone.setOnClickListener(v -> {

            startActivity(new Intent(PurchaseItemsActivityDetails.this, HomeActivity.class));
            finish();

        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}