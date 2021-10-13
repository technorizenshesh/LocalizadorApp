package com.my.localizadorapp.act;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityCircleDetailsBinding;
import com.my.localizadorapp.model.CricleCreate;
import com.my.localizadorapp.model.UpdatedCircleModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CircleDetailsActivity extends AppCompatActivity {

    ActivityCircleDetailsBinding binding;
    private View promptsView;
    private AlertDialog alertDialog;
    String CircleName = "";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_circle_details);

        sessionManager =new SessionManager(CircleDetailsActivity.this);

        CircleName = Preference.get(CircleDetailsActivity.this, Preference.KEY_CircleName);
        binding.txtCircleName.setText(CircleName);


        binding.RRUserProfile.setOnClickListener(v -> {

            startActivity(new Intent(CircleDetailsActivity.this, MyAccountActivity.class));
        });

        binding.RRback.setOnClickListener(v ->
        {
            onBackPressed();
        });

        binding.RRupdateCircle.setOnClickListener(v ->
        {
            AlertDaliogUpdateCircle();
        });

        binding.RRInvite.setOnClickListener(v ->
        {
            Intent i = new Intent(CircleDetailsActivity.this, InviteNewFriend.class);
            startActivity(i);

        });
    }

    private void AlertDaliogUpdateCircle() {

        LayoutInflater li;
        RelativeLayout RRContinueCreate;
        EditText edtCircleName;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(CircleDetailsActivity.this);
        promptsView = li.inflate(R.layout.alert_update_circle, null);
        RRContinueCreate = (RelativeLayout) promptsView.findViewById(R.id.RRContinueCreate);
        edtCircleName = (EditText) promptsView.findViewById(R.id.edtCircleName);
        alertDialogBuilder = new AlertDialog.Builder(CircleDetailsActivity.this);
        alertDialogBuilder.setView(promptsView);

        edtCircleName.setText(CircleName);

        RRContinueCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String CricelName = edtCircleName.getText().toString();

                if (sessionManager.isNetworkAvailable()) {

                    binding.progressBar.setVisibility(View.VISIBLE);

                    ApiMethodUpdate(CricelName);

                }else {

                    Toast.makeText(CircleDetailsActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();

                }

                alertDialog.dismiss();

            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void ApiMethodUpdate(String cricelName) {

        String CircleId = Preference.get(CircleDetailsActivity.this,Preference.KEY_Circle_ID);
        String UserCode = Preference.get(CircleDetailsActivity.this,Preference.KEY_UserCode);

        Call<UpdatedCircleModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_update_circle_name(CircleId,cricelName);
        call.enqueue(new Callback<UpdatedCircleModel>() {
            @Override
            public void onResponse(Call<UpdatedCircleModel> call, Response<UpdatedCircleModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    UpdatedCircleModel myclass= response.body();

                    if (myclass.status.equalsIgnoreCase("1")){

                        binding.txtCircleName.setText(myclass.circleName);

                        Toast.makeText(CircleDetailsActivity.this, myclass.message, Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(CircleDetailsActivity.this, myclass.message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UpdatedCircleModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(CircleDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}