package com.my.localizadorapp.act;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.my.localizadorapp.Chat.SessionManagerTwo;
import com.my.localizadorapp.GPSTracker;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivitySignUpBinding;
import com.my.localizadorapp.model.SignUpModel;
import com.my.localizadorapp.utils.Constant;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    GPSTracker gpsTracker;
    SessionManager sessionManager;
    String latitude="";
    String longitude="";

    String Mobile="",country_code="";
    String token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        MobileAds.initialize(this, initializationStatus -> {
        });
        sessionManager =new SessionManager(SignUpActivity.this);

        //Gps Lat Long
        gpsTracker=new GPSTracker(SignUpActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
        }else{
            gpsTracker.showSettingsAlert();
        }

        binding.RRContinue.setOnClickListener(v -> {
             Mobile = binding.edtMobile.getText().toString();
            country_code = binding.txtCountry.getSelectedCountryCode().toString();
            if(Mobile.length()>=6)
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
           /*Intent i = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(i);*/
        });

        binding.txtLogin.setOnClickListener(v -> {

            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
        });

        try {
            try {
                FirebaseApp.initializeApp(this);
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.e(TAG, "onCreate:FirebaseMessaging "+task.getException().toString());
                                return;
                            }
                            token = task.getResult();
                            Log.e(TAG, "onCreate:FirebaseMessaging "+token);

                        });

            }catch (Exception e){
                Log.e(TAG, "onCreate:FirebaseMessaging "+e.toString());

            }        }catch (Exception e){

        }
    }


    public void ApiMethodlogin() {
        Call<SignUpModel> call = RetrofitClients.getInstance().getApi().Api_login(Mobile,
                token,latitude,longitude,"1234",country_code);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    SignUpModel myclass= response.body();
                    String responseString = new Gson().toJson(response.body());

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")){
                        SessionManagerTwo.writeString(SignUpActivity.this, Constant.USER_INFO, responseString);
                        String UserId = myclass.result.id;
                        String UserName = myclass.result.userName;
                        String UserImage = myclass.result.image;
                        sessionManager.saveUserId(UserId);
                        sessionManager.saveUserName(UserName);
                        Preference.save(SignUpActivity.this, Preference.key_image,UserImage);
                        Preference.save(SignUpActivity.this,Preference.KEY_USER_ID,UserId);
                        Preference.save(SignUpActivity.this,Preference.KEY_UserName,UserName);
                        Preference.save(SignUpActivity.this,Preference.KEY_CircleName,myclass.result.circleName);
                        Preference.save(SignUpActivity.this,Preference.KEY_CircleCode,myclass.result.code);
                        startActivity(new Intent(SignUpActivity.this,HomeActivity.class).putExtra("mobile",Mobile));

                      //  Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();

                    }else {

                        //startActivity(new Intent(SignUpActivity.this,OtpScreenActivity.class).putExtra("mobile",Mobile));
                        startActivity(new Intent(SignUpActivity.this,UserNameScree.class)
                                .putExtra("mobile1",Mobile));
                        Preference.save(SignUpActivity.this, Preference.Country,binding.txtCountry.getSelectedCountryCode());

                        //     Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}