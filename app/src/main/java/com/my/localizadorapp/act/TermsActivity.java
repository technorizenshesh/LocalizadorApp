package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityTermsBinding;
import com.my.localizadorapp.model.PrivacyModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsActivity extends AppCompatActivity {

    ActivityTermsBinding binding;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_terms);



        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        sessionManager=new SessionManager(this);
        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            getApiPrivacy();
        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    public void getApiPrivacy() {

        Call<PrivacyModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_Terms();
        call.enqueue(new Callback<PrivacyModel>() {
            @Override
            public void onResponse(Call<PrivacyModel> call, Response<PrivacyModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    PrivacyModel myclass= response.body();

                    String status = myclass.getStatus();
                    String message = myclass.getMessage();

                    if (status.equalsIgnoreCase("1")){

                        String test = myclass.getResult().getText();

                        binding.txtTerms.setText(test);

                        Toast.makeText(TermsActivity.this, message, Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(TermsActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PrivacyModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(TermsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}