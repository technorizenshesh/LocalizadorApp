package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.my.localizadorapp.GPSTracker;
import com.my.localizadorapp.MainActivity;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityLoginBinding;
import com.my.localizadorapp.model.SignUpModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String Mobile="";
    GPSTracker gpsTracker;
    SessionManager sessionManager;
    String latitude="";
    String longitude="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        sessionManager =new SessionManager(LoginActivity.this);

        //Gps Lat Long
        gpsTracker=new GPSTracker(LoginActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
        }else{
            gpsTracker.showSettingsAlert();
        }

        binding.RRContinue.setOnClickListener(v -> {

             Mobile = binding.edtMobile.getText().toString();

            if(Mobile.length()>=10 )
            {
                if (sessionManager.isNetworkAvailable()) {

                    binding.progressBar.setVisibility(View.VISIBLE);

                    ApiMethodlogin();

                }else {

                    Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }

            }else
            {
                Toast.makeText(this, "Please Valid Mobile Number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ApiMethodlogin() {

        Call<SignUpModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_login(Mobile,"bhhjvhh",latitude,longitude,"1234");
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    SignUpModel myclass= response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")){

                        String UserId = myclass.result.id;
                        String UserName = myclass.result.userName;

                        sessionManager.saveUserId(UserId);
                        sessionManager.saveUserName(UserName);

                        Preference.save(LoginActivity.this,Preference.KEY_USER_ID,UserId);
                        Preference.save(LoginActivity.this,Preference.KEY_UserName,UserName);

                        startActivity(new Intent(LoginActivity.this,HomeActivity.class).putExtra("mobile",Mobile));

                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}