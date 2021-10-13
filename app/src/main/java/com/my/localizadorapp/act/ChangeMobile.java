package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityChangeMobileBinding;
import com.my.localizadorapp.model.ChangeMobileModel;
import com.my.localizadorapp.model.SignUpModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeMobile extends AppCompatActivity {

    ActivityChangeMobileBinding binding;
    SessionManager sessionManager;
    String Mobile="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_mobile);

        sessionManager =new SessionManager(ChangeMobile.this);

        Intent intent=getIntent();

        if(intent!=null)
        {
            Mobile=intent.getStringExtra("mobile").toString();
            binding.edtNewMobile.setText(Mobile);
        }

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtSave.setOnClickListener(v -> {

             Mobile= binding.edtNewMobile.getText().toString();

            if(!Mobile.equalsIgnoreCase(""))
            {
                if (sessionManager.isNetworkAvailable()) {

                    binding.progressBar.setVisibility(View.VISIBLE);

                    ApiMethodChangeMobile();

                }else {

                    Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Please Enter Mobile Number.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ApiMethodChangeMobile() {

        String UserId = Preference.get(ChangeMobile.this,Preference.KEY_USER_ID);

        Call<ChangeMobileModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_change_mobile(UserId,Mobile);
        call.enqueue(new Callback<ChangeMobileModel>() {
            @Override
            public void onResponse(Call<ChangeMobileModel> call, Response<ChangeMobileModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    ChangeMobileModel myclass= response.body();

                    String status = myclass.getStatus();
                    String message = myclass.getMessage();

                    if (status.equalsIgnoreCase("1")){

                        Toast.makeText(ChangeMobile.this, message, Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(ChangeMobile.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ChangeMobileModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ChangeMobile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}