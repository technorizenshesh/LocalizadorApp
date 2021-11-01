package com.my.localizadorapp.act;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityInviteNewFriendBinding;

public class InviteNewFriend extends AppCompatActivity {

    ActivityInviteNewFriendBinding binding;
    String UserCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_invite_new_friend);

         UserCode = Preference.get(InviteNewFriend.this,Preference.KEY_CircleCode);

        binding.txtCode.setText(UserCode);

       binding.RRShare.setOnClickListener(v -> {
           onShareClicked();
       });

       binding.RRback.setOnClickListener(v -> {
          onBackPressed();
       });
    }

    private void onShareClicked() {

        String link = "https://play.google.com/store/apps/details?id=com.recharge2mePlay.recharge2me : Code - "+UserCode;

        Uri uri = Uri.parse(link);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link.toString());
        intent.putExtra(Intent.EXTRA_TITLE, "Recharge2me");

        startActivity(Intent.createChooser(intent, "Share Link"));

    }
}