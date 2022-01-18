package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityMyAccountBinding;
import com.my.localizadorapp.model.SignUpModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountActivity extends AppCompatActivity {

    ActivityMyAccountBinding binding;
    SessionManager sessionManager;

     String Mobile="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_account);

        sessionManager =new SessionManager(MyAccountActivity.this);

        binding.RRback.setOnClickListener(v -> {

            onBackPressed();

        });

        binding.txtMobile.setOnClickListener(v -> {

            startActivity(new Intent(MyAccountActivity.this,ChangeMobile.class).putExtra("mobile",Mobile));

        });

        binding.RREdit.setOnClickListener(v -> {
            startActivity(new Intent(MyAccountActivity.this,EditProfileActivity.class));
        });

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            ApiMethodmyProfile();

        }else {

            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    public void ApiMethodmyProfile() {

       String UserId = Preference.get(MyAccountActivity.this,Preference.KEY_USER_ID);

        Call<SignUpModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_get_profile(UserId);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    SignUpModel myclass= response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")){

                        String UserName = myclass.result.userName;
                         Mobile = myclass.result.mobile;

                        binding.txtName1.setText(""+UserName);
                        binding.txtMobile.setText("91-"+Mobile);

                        if(!myclass.result.image.equalsIgnoreCase(""))
                        {
                            Glide.with(MyAccountActivity.this).load(myclass.result.image).circleCrop().into(binding.imgUser);
                        }


                        Toast.makeText(MyAccountActivity.this, message, Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(MyAccountActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(MyAccountActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}