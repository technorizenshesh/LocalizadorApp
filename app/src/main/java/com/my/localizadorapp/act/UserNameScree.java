package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityUserNameScreeBinding;

public class UserNameScree extends AppCompatActivity {

    ActivityUserNameScreeBinding binding;
    String Mobile="";
    String UserName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_name_scree);
      /*  AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
*/
        Intent intent =getIntent();

        if(intent!=null)
        {
            Mobile = intent.getStringExtra("mobile1").toString();
        }

        binding.RRContinue.setOnClickListener(v ->{

            UserName = binding.edtFirstName.getText().toString();

            if(!UserName.equalsIgnoreCase(""))
            {
                startActivity(new Intent(UserNameScree.this,
                        CircleAddScreen.class).putExtra("mobile",Mobile).putExtra("UserName",UserName));

            } else {

                Toast.makeText(this, "Please Enter User Name.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.txtSkip.setOnClickListener(v -> {

            startActivity(new Intent(UserNameScree.this,HomeActivity.class));

        });
    }


}