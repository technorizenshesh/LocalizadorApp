package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.my.localizadorapp.Chat.MsgChatAct;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.adapter.ChatMemberListAdapter;
import com.my.localizadorapp.adapter.CircleSpinnerAdapter;
import com.my.localizadorapp.databinding.ActivityChatMessageBinding;
import com.my.localizadorapp.model.CircleListNewModel;
import com.my.localizadorapp.model.GetUserChatModel;
import com.my.localizadorapp.model.MemberListDataModel;
import com.my.localizadorapp.model.MemberListModel;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatMessageActivity extends AppCompatActivity {

    ArrayList<CircleListNewModel.Result> modelList_my = new ArrayList<>();
    ArrayList<CircleListNewModel.Result> modelList_my_circle = new ArrayList<>();
    ArrayList<CircleListNewModel.Result> modelList = new ArrayList<>();

    ArrayList<GetUserChatModel.Result> modelListMember = new ArrayList<>();
    ArrayList<GetUserChatModel.Result> modelListMemberNew = new ArrayList<>();

    ChatMemberListAdapter mAdapter;
    String Code="";
    SessionManager sessionManager;
    ActivityChatMessageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_chat_message);

        setUi();
    }

    private void setUi() {

        sessionManager = new SessionManager(ChatMessageActivity.this);

        if (sessionManager.isNetworkAvailable()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ApiGetCircleList();

        }else {
            Toast.makeText(ChatMessageActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        binding.spinnerSbcategoy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                Code = modelList.get(pos).getCode();


            if (sessionManager.isNetworkAvailable()) {

                    binding.progressBar.setVisibility(View.VISIBLE);
                    ApiGetMemberList(Code);

                }else {
                    Toast.makeText(ChatMessageActivity.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });


    }

    private void setAdapter(ArrayList<GetUserChatModel.Result> modelListMemberNew) {
        // modelList_my_circle.add(new RatingModel("Corn"));
        mAdapter = new ChatMemberListAdapter(ChatMessageActivity.this,modelListMemberNew);
        binding.recyclerChat.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerChat.setLayoutManager(new LinearLayoutManager(ChatMessageActivity.this));
        binding.recyclerChat.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new ChatMemberListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetUserChatModel.Result model) {

               startActivity(new Intent(ChatMessageActivity.this, MsgChatAct.class)
                        .putExtra("UserId",model.getId())
                        .putExtra("UserName",model.getUserName())
                        .putExtra("UserImage",model.getImage())
                        .putExtra("request_id",model.getId()));

            }
        });
    }

    public void ApiGetCircleList() {

        String UserId = Preference.get(ChatMessageActivity.this, Preference.KEY_USER_ID);

        Call<CircleListNewModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_get_circle_new(UserId);
        call.enqueue(new Callback<CircleListNewModel>() {
            @Override
            public void onResponse(Call<CircleListNewModel> call, Response<CircleListNewModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    CircleListNewModel myclass = response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")) {

                        modelList_my= (ArrayList<CircleListNewModel.Result>) myclass.getResult();

                        modelList_my_circle= (ArrayList<CircleListNewModel.Result>) myclass.getCircleData();

                        if(modelList_my!=null)
                        {
                            for(int i=0;i<modelList_my.size();i++)
                            {
                                modelList.add(modelList_my.get(i));
                            }
                        }
                        if(modelList_my_circle!=null)
                        {
                            for(int i=0;i<modelList_my_circle.size();i++)
                            {
                                modelList.add(modelList_my_circle.get(i));
                            }
                        }

                        CircleSpinnerAdapter customAdapter=new CircleSpinnerAdapter(ChatMessageActivity.this,modelList);
                        binding.spinnerSbcategoy.setAdapter(customAdapter);

                       // ApiGetMemberList(modelList.get(0).getCode());

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CircleListNewModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void ApiGetMemberList(String CircleCode)
    {
        modelListMember.clear();
        modelListMemberNew.clear();

        String UserId = Preference.get(ChatMessageActivity.this, Preference.KEY_USER_ID);

        Log.e("CircleCode---------",""+CircleCode);

        Call<GetUserChatModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_circle_user(CircleCode);
        call.enqueue(new Callback<GetUserChatModel>() {
            @Override
            public void onResponse(Call<GetUserChatModel> call, Response<GetUserChatModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    GetUserChatModel myclass= response.body();

                    if (myclass.getStatus().equalsIgnoreCase("1")){

                        if(myclass.getResult()!=null)
                        {

                            modelListMember = (ArrayList<GetUserChatModel.Result>) myclass.getResult();

                            for(int i=0;i<modelListMember.size();i++)
                            {
                                if(!UserId.equalsIgnoreCase(modelListMember.get(i).getId()))
                                {
                                    modelListMemberNew.add(modelListMember.get(i));
                                }
                            }

                            setAdapter(modelListMemberNew);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<GetUserChatModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}