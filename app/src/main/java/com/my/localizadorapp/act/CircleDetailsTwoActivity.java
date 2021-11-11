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
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.adapter.MemberListAdapter;
import com.my.localizadorapp.adapter.MemberListAdapterNew;
import com.my.localizadorapp.databinding.ActivityCircleDetailsBinding;
import com.my.localizadorapp.databinding.ActivityCircleDetailsTwoBinding;
import com.my.localizadorapp.model.DeleteModel;
import com.my.localizadorapp.model.MemberListDataModel;
import com.my.localizadorapp.model.MemberListModel;
import com.my.localizadorapp.model.UpdatedCircleModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CircleDetailsTwoActivity extends AppCompatActivity {

    ActivityCircleDetailsTwoBinding binding;
    private View promptsView;
    private AlertDialog alertDialog;
    String CircleName = "";
    SessionManager sessionManager;

    private ArrayList<MemberListDataModel> modelList = new ArrayList<>();
    MemberListAdapterNew mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_circle_details_two);

        sessionManager =new SessionManager(CircleDetailsTwoActivity.this);

        CircleName = Preference.get(CircleDetailsTwoActivity.this, Preference.KEY_CircleName);

      /*  String CircleCount =Preference.get(CircleDetailsTwoActivity.this,Preference.KEY_CircleCount);

        if(CircleCount.equalsIgnoreCase("1"))
        {
           binding.RRLeave.setVisibility(View.GONE);
        }
*/
        binding.txtCircleName.setText(CircleName);


        binding.RRUserProfile.setOnClickListener(v -> {

            startActivity(new Intent(CircleDetailsTwoActivity.this, MyAccountActivity.class));
        });

       binding.RRLeave.setOnClickListener(v -> {

           if (sessionManager.isNetworkAvailable()) {

               binding.progressBar.setVisibility(View.VISIBLE);

               //ApiDeleteCircle();

           }else {

               Toast.makeText(CircleDetailsTwoActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
           }

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
            Intent i = new Intent(CircleDetailsTwoActivity.this, InviteNewFriend.class);
            startActivity(i);

        });

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);
            ApiGetMemberList();
        }else {
            Toast.makeText(CircleDetailsTwoActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void AlertDaliogUpdateCircle() {

        LayoutInflater li;
        RelativeLayout RRContinueCreate;
        EditText edtCircleName;
        AlertDialog.Builder alertDialogBuilder;
        li = LayoutInflater.from(CircleDetailsTwoActivity.this);
        promptsView = li.inflate(R.layout.alert_update_circle, null);
        RRContinueCreate = (RelativeLayout) promptsView.findViewById(R.id.RRContinueCreate);
        edtCircleName = (EditText) promptsView.findViewById(R.id.edtCircleName);
        alertDialogBuilder = new AlertDialog.Builder(CircleDetailsTwoActivity.this);
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

                    Toast.makeText(CircleDetailsTwoActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();

                }

                alertDialog.dismiss();

            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    private void setAdapter(ArrayList<MemberListDataModel> modelList) {

        //this.modelList_my_circle.add(new RatingModel("Corn"));

        mAdapter = new MemberListAdapterNew(CircleDetailsTwoActivity.this,modelList);
        binding.recyclerMemebr.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerMemebr.setLayoutManager(new LinearLayoutManager(CircleDetailsTwoActivity.this));
        binding.recyclerMemebr.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new MemberListAdapterNew.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, MemberListDataModel model) {


            }
        });
    }

    public void ApiMethodUpdate(String cricelName) {

        String CircleId = Preference.get(CircleDetailsTwoActivity.this,Preference.KEY_Circle_ID);
        String UserCode = Preference.get(CircleDetailsTwoActivity.this,Preference.KEY_CircleCode);

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

                        Toast.makeText(CircleDetailsTwoActivity.this, myclass.message, Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(CircleDetailsTwoActivity.this, myclass.message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UpdatedCircleModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(CircleDetailsTwoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ApiGetMemberList() {

        String UserId = Preference.get(CircleDetailsTwoActivity.this,Preference.KEY_USER_ID);
        String UserName = Preference.get(CircleDetailsTwoActivity.this,Preference.KEY_UserName);
        String UserCode = Preference.get(CircleDetailsTwoActivity.this,Preference.KEY_CircleCode);

        Call<MemberListModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_get_member_detail(UserCode);
        call.enqueue(new Callback<MemberListModel>() {
            @Override
            public void onResponse(Call<MemberListModel> call, Response<MemberListModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    MemberListModel myclass= response.body();

                    if (myclass.getStatus().equalsIgnoreCase("1")){

                        if(myclass.getOwnerDetail()!=null)
                        {
                            binding.txtUser.setText(myclass.getOwnerDetail().getUserName());

                        }else
                        {
                            binding.txtUser.setText(UserName);
                        }

                        if(myclass.getResult()!=null)
                        {
                            modelList = (ArrayList<MemberListDataModel>) myclass.getResult();

                            setAdapter(modelList);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<MemberListModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }



}