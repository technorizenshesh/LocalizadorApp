package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivitySendFeedbaackReportBinding;

public class SendFeedbaackReport extends AppCompatActivity {

    ActivitySendFeedbaackReportBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_send_feedbaack_report);

         binding.RRback.setOnClickListener(v -> {
             onBackPressed();
         });
    }
}