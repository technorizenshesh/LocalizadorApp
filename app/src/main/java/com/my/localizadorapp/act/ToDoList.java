package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityToDoListBinding;

public class ToDoList extends AppCompatActivity {

    ActivityToDoListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_to_do_list);

       binding.RRback.setOnClickListener(v -> {
           onBackPressed();
       });
    }
}