package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.adapter.AllContactAdapter;
import com.my.localizadorapp.databinding.ActivityAllContactListBinding;
import com.my.localizadorapp.model.GetAllContackModel;

import java.util.ArrayList;
import java.util.Arrays;

import static com.my.localizadorapp.Upd.MyService.TAG;

public class AllContactList extends AppCompatActivity {

    ArrayList<GetAllContackModel> modelList = new ArrayList<GetAllContackModel>();
    ArrayList<String> NewList = new ArrayList<String>();
    AllContactAdapter mAdapter;

    ActivityAllContactListBinding binding;
    private String strList="";
    private String UserCode="";
    ContentResolver cr;
    Cursor cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_all_contact_list);

        UserCode = Preference.get(AllContactList.this,Preference.KEY_CircleCode);


        modelList.clear();
        binding.progressBar.setVisibility(View.VISIBLE);
        modelList=getContactList();

        if(!modelList.isEmpty())
        {
            setAdapter(modelList);
        }

        binding.RRShare.setOnClickListener(v -> {

            for(int i=0;i<modelList.size();i++)
            {
                if(modelList.get(i).isSelected())
                {
                    NewList.add(modelList.get(i).getPhoneNumber());
                }
            }

            Log.e("cart_id_array>>>>", "" + NewList);

            strList = NewList.toString();

            strList = strList.replace("[", "")
                    .replace("]", "")
                    .replace(" ", "");

            Log.e("strList>>>", strList);

            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.putExtra("address", strList);
            i.putExtra("sms_body", "Please Join the Grp Circle Code : "+UserCode);
            i.setType("vnd.android-dir/mms-sms");
            startActivity(i);

        });

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void setAdapter(ArrayList<GetAllContackModel> modelList) {

        // modelList_my_circle.add(new RatingModel("Corn"));
        mAdapter = new AllContactAdapter(AllContactList.this,modelList);
        binding.recyclerMyAllContact.setHasFixedSize(true);
        // use a linear layout manager
        binding.recyclerMyAllContact.setLayoutManager(new LinearLayoutManager(AllContactList.this));
        binding.recyclerMyAllContact.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new AllContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GetAllContackModel model) {

            }
        });
    }


    private ArrayList<GetAllContackModel> getContactList() {

         cr = getContentResolver();
         cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        modelList.add(new GetAllContackModel(name,phoneNo));
                      //  binding.txtName.setText(name);

                        Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNo);
                    }

                    setAdapter(modelList);

                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

        binding.progressBar.setVisibility(View.GONE);
        binding.RRShare.setVisibility(View.VISIBLE);

        return modelList;
    }
}