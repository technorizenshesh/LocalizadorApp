package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityEmergancyContactBinding;

public class EmergancyContact extends AppCompatActivity {

    ActivityEmergancyContactBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_emergancy_contact);

      String CheckIt = Preference.get(EmergancyContact.this,Preference.key_On_off);

      if(CheckIt.equalsIgnoreCase("on"))
      {
          binding.SwithON.setChecked(true);

      }else
      {
          binding.SwithON.setChecked(false);
      }

       binding.RRback.setOnClickListener(v -> {

           onBackPressed();

       });

       binding.RRASend.setOnClickListener(v -> {
           Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
           sharingIntent.setType("text/plain");
           String shareBody = "Hello sir Please Urgent Call me +910731456698";
           sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
           sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
           startActivity(Intent.createChooser(sharingIntent, "Share via"));
         }
       );

        binding.SwithON.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Preference.save(EmergancyContact.this,Preference.key_On_off,"on");

                }else
                {
                    Preference.save(EmergancyContact.this,Preference.key_On_off,"off");
                }
            }
        });

    }
}