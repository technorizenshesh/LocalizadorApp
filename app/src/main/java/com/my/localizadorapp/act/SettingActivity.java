package com.my.localizadorapp.act;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    private View promptsView;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_setting);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.llFaQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, FaqScreen.class);
                startActivity(i);
            }
        });

        binding.SendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, SendFeedbaackReport.class);
                startActivity(i);
            }
        });

        binding.llMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, SendFeedbaackReport.class);
                startActivity(i);
            }
        });

        binding.llManageMeber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, ManageMebersCircles.class);
                startActivity(i);
            }
        });

        binding.llTodolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, ToDoList.class);
                startActivity(i);
            }
        });

        binding.llTodolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, ToDoList.class);
                startActivity(i);
            }
        });

        binding.llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, NotificationScree.class);
                startActivity(i);
            }
        });

        binding.llBateryAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, BaterryAlertScreen.class);
                startActivity(i);
            }
        });

        binding.llInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, ManageInvitations.class);
                startActivity(i);
            }
        });

        binding.llEmergancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingActivity.this, EmergancyContact.class);
                startActivity(i);
            }
        });

        binding.llDistanceUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDaliog();
                
            }
        });
    }

    private void AlertDaliog() {

        LayoutInflater li;
        RelativeLayout RRkg;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(SettingActivity.this);
        promptsView = li.inflate(R.layout.alert_distacne, null);
        RRkg = (RelativeLayout) promptsView.findViewById(R.id.RRkg);
        alertDialogBuilder = new AlertDialog.Builder(SettingActivity.this);
        alertDialogBuilder.setView(promptsView);

        RRkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}