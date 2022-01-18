package com.my.localizadorapp.act;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityEditProfileBinding;
import com.my.localizadorapp.model.SignUpModel;
import com.my.localizadorapp.utils.FileUtil;
import com.my.localizadorapp.utils.RetrofitClients;
import com.my.localizadorapp.utils.SessionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    SessionManager sessionManager;

    private Bitmap bitmap;
    private Uri resultUri;
    //private SessionManager sessionManager;
    public static File UserProfile_img, codmpressedImage, compressActualFile;
    boolean isProfileImage=false;
    String Name="";
    String Mobile="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);

        sessionManager =new SessionManager(EditProfileActivity.this);

        binding.RRback.setOnClickListener(v -> {
            onBackPressed();
        });


        binding.RRUploadImage.setOnClickListener(v -> {

            Dexter.withActivity(EditProfileActivity.this)
                    .withPermissions(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).getIntent(EditProfileActivity.this);
                                startActivityForResult(intent, 1);
                            } else {
                                showSettingDialogue();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

        });

        binding.txtSave.setOnClickListener(v -> {

          Name=binding.txtName1.getText().toString();
          Mobile=binding.txtMobile.getText().toString();

         if(Name.equalsIgnoreCase(""))
         {
             Toast.makeText(this, "Please Enter Name..", Toast.LENGTH_SHORT).show();
         }else if(Mobile.equalsIgnoreCase(""))
         {
             Toast.makeText(this, "Please Enter Mobile.", Toast.LENGTH_SHORT).show();
         }else
         {
             if (sessionManager.isNetworkAvailable()) {

                 binding.progressBar.setVisibility(View.VISIBLE);

                 ApIUpdateMehod();

             }else {

                 Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
             }
         }

        });


        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            ApiMethodmyProfile();

        }else {

            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    public void ApiMethodmyProfile() {

        String UserId = Preference.get(EditProfileActivity.this,Preference.KEY_USER_ID);

        Call<SignUpModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .Api_get_profile(UserId);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    SignUpModel myclass= response.body();

                    String status = myclass.status;
                    String message = myclass.message;

                    if (status.equalsIgnoreCase("1")){

                        String UserName = myclass.result.userName;
                        String UserMobile = myclass.result.mobile;

                        binding.txtName1.setText(""+UserName);
                        binding.txtMobile.setText(UserMobile+"");


                        if(!myclass.result.image.equalsIgnoreCase(""))
                        {
                            Glide.with(EditProfileActivity.this).load(myclass.result.image).placeholder(R.drawable.user_default).error(R.drawable.user_default).into(binding.imgUser);
                        }

                        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSettingDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                openSetting();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();

    }

    private void openSetting() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);

                    UserProfile_img = FileUtil.from(this, resultUri);

                    Glide.with(this).load(bitmap).circleCrop().into(binding.imgUser);

                    isProfileImage = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    codmpressedImage = new Compressor(this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(UserProfile_img);
                    Log.e("ActivityTag", "imageFilePAth: " + codmpressedImage);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void ApIUpdateMehod(){

        String Userid= Preference.get(EditProfileActivity.this,Preference.KEY_USER_ID);

        MultipartBody.Part imgFile = null;

        if(isProfileImage)
        {
            if (UserProfile_img == null) {

            } else {
                RequestBody requestFileOne = RequestBody.create(MediaType.parse("image/*"),UserProfile_img);
                imgFile = MultipartBody.Part.createFormData("image",UserProfile_img.getName(), requestFileOne);
            }
        }

        RequestBody UserId = RequestBody.create(MediaType.parse("text/plain"), Userid);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), Name);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), Mobile);


        Call<SignUpModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .update_profile(UserId,name,mobile,imgFile);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                SignUpModel finallyPr = response.body();

                String status = finallyPr.status;

                if (status.equalsIgnoreCase("1"))
                {

                    binding.txtName1.setText(finallyPr.result.userName+"");
                    binding.txtMobile.setText(finallyPr.result.mobile+"");

                    if(!finallyPr.result.image.equalsIgnoreCase(""))
                    {
                        Glide.with(EditProfileActivity.this).load(finallyPr.result.image).circleCrop().placeholder(R.drawable.user_default).error(R.drawable.user_default).into(binding.imgUser);
                    }

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProfileActivity.this, finallyPr.message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}