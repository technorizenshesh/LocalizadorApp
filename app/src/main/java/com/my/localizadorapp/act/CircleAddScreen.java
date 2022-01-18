package com.my.localizadorapp.act;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.my.localizadorapp.Chat.SessionManagerTwo;
import com.my.localizadorapp.GPSTracker;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityCircleAddScreenBinding;
import com.my.localizadorapp.model.SignUpModel;
import com.my.localizadorapp.model.SignUpdataModel;
import com.my.localizadorapp.utils.Constant;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CircleAddScreen extends AppCompatActivity {

    ActivityCircleAddScreenBinding binding;
    String Mobile="";
    String UserName="";
    String CircleName ="";
    SessionManager sessionManager;
    GPSTracker gpsTracker;
    String latitude="";
    String longitude="";

    String Battery="";
    private String token="";

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float) scale;
            Battery = String.valueOf(batteryPct);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_circle_add_screen);

       registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
       try {
         token=  getToken(this);
       }catch (Exception e){

       }

        //Gps Lat Long
        gpsTracker=new GPSTracker(this);
        if(gpsTracker.canGetLocation()){
            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());
           // latitude = String.valueOf(gpsTracker.getLatitude());
          //  longitude = String.valueOf(gpsTracker.getLongitude());

        }else{
            gpsTracker.showSettingsAlert();
        }

        sessionManager =new SessionManager(CircleAddScreen.this);

        Intent intent=getIntent();
        if(intent!=null)
        {
            Mobile=intent.getStringExtra("mobile").toString();
            UserName=intent.getStringExtra("UserName").toString();
        }

        binding.RRContinue.setOnClickListener(v -> {

            CircleName =binding.edtCircleName.getText().toString();

            if(!CircleName.equalsIgnoreCase(""))
            {
                if (sessionManager.isNetworkAvailable()) {

                    binding.progressBar.setVisibility(View.VISIBLE);

                    ApiMethodSignUp();

                }else {

                    Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }

            }else
            {
                Toast.makeText(this, "Please Enter Circle Name", Toast.LENGTH_SHORT).show();
            }
            //startActivity(new Intent(CircleAddScreen.this,HomeActivity.class));
        });

        binding.txtSkip.setOnClickListener(v -> {

            if (sessionManager.isNetworkAvailable()) {

                binding.progressBar.setVisibility(View.VISIBLE);

                ApiMethodSignUp();

            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }

           // startActivity(new Intent(CircleAddScreen.this,HomeActivity.class));

        });
    }

    static   String getToken(Context context) {
        final String[] tokenr = new String[1];
             FirebaseApp.initializeApp(context);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                          //  Log.w(TAG, "" + getString(R.string.fetching_fcm_token_failed), task.getException());
                            return;
                        }
                        // Get new FCM registration token
                         tokenr[0] = task.getResult();

                    });

        return tokenr[0];
    }

    public void ApiMethodSignUp()
    {
        Log.e( "Tokennnn" ,token);

        Call<SignUpdataModel> call = RetrofitClients.getInstance().getApi()
                .Api_signup(UserName,CircleName,Mobile,token,latitude,longitude,"1234","yes",Battery);
        call.enqueue(new Callback<SignUpdataModel>() {
            @Override
            public void onResponse(Call<SignUpdataModel> call, Response<SignUpdataModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    SignUpdataModel myclass= response.body();

                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("RESPONSE1", "RESPONSE" + dataResponse);

                    if (myclass.status.equalsIgnoreCase("1")){

                        String UserId = myclass.result.id;
                        String UserName = myclass.result.userName;
                        String UserCode = myclass.result.code;

                        sessionManager.saveUserId(UserId);
                        sessionManager.saveUserName(UserName);

                        ///////
                        String responseString = new Gson().toJson(response.body());
                        SessionManagerTwo.writeString(CircleAddScreen.this, Constant.USER_INFO, responseString);
                       ///////

                        Preference.save(CircleAddScreen.this,Preference.KEY_USER_ID,UserId);
                         Preference.save(CircleAddScreen.this,Preference.KEY_UserName,UserName);
                         Preference.save(CircleAddScreen.this,Preference.KEY_CircleCode,UserCode);

                        Preference.save(CircleAddScreen.this,Preference.KEY_CircleName,myclass.result.circleName);
                        Preference.save(CircleAddScreen.this,Preference.KEY_CircleCode,myclass.result.code);


                        startActivity(new Intent(CircleAddScreen.this,HomeActivity.class));

                        Toast.makeText(CircleAddScreen.this, myclass.message, Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(CircleAddScreen.this,  myclass.message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SignUpdataModel> call, Throwable t){
                call.cancel();
                Log.e("error", String.valueOf(t));
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(CircleAddScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}