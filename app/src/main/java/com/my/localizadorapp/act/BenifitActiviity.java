package com.my.localizadorapp.act;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.braintreepayments.cardform.view.CardForm;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityBenifitActiviityBinding;

public class BenifitActiviity extends AppCompatActivity {

    ActivityBenifitActiviityBinding binding;
    String name="";

    private View promptsView;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_benifit_activiity);

        Intent intent=getIntent();

        if(intent !=null)
        {
            name=intent.getStringExtra("Name").toString();
            binding.txtName.setText(name);
        }

    binding.RRback.setOnClickListener(v -> {
        onBackPressed();
    });

        binding.RRtrack.setOnClickListener(v -> {

            AlertDaliogRecharge();

        });
    }


    private void AlertDaliogRecharge() {

        LayoutInflater li;
        ImageView ivBack;
        CardForm cardForm;
        RelativeLayout RRDone;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(BenifitActiviity.this);
        promptsView = li.inflate(R.layout.activity_stripe_payment, null);
        ivBack = (ImageView) promptsView.findViewById(R.id.ivBack);
        cardForm = (CardForm) promptsView.findViewById(R.id.card_form);
        RRDone = (RelativeLayout) promptsView.findViewById(R.id.RRDone);

        alertDialogBuilder = new AlertDialog.Builder(BenifitActiviity.this, R.style.myFullscreenAlertDialogStyle);   //second argument

        alertDialogBuilder.setView(promptsView);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                //.mobileNumberExplanation("SMS is required on this number")
                .setup(BenifitActiviity .this);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        RRDone.setOnClickListener(v -> {
            finish();
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}