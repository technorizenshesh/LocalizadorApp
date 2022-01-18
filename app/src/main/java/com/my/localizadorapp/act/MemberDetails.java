package com.my.localizadorapp.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.localizadorapp.GPSTracker;
import com.my.localizadorapp.Preference;
import com.my.localizadorapp.R;
import com.my.localizadorapp.databinding.ActivityMemberDetailsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.my.localizadorapp.Upd.MyService.TAG;

public class MemberDetails extends AppCompatActivity implements OnMapReadyCallback {

    ActivityMemberDetailsBinding binding;
    private GoogleMap mMap;
    GPSTracker gpsTracker;
    double latitude = 0;
    double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_member_details);


        Intent intent=getIntent();
        if(intent!=null)
        {
            String UserName=intent.getStringExtra("name").toString();
            String lat=intent.getStringExtra("lat").toString();
            String lon=intent.getStringExtra("lon").toString();
            String Baterry=intent.getStringExtra("Batery").toString();

            double lat1= Double.parseDouble(lat);
            double lon1= Double.parseDouble(lon);

            String address = getAddress(this, lat1, lon1);

            binding.txtUserName.setText(UserName+"");
            binding.txtBatery.setText(Baterry+"");
            binding.txtAddress.setText(address+"");
        }
        //Gps Lat Long
        gpsTracker = new GPSTracker(MemberDetails.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);



        binding.cardUserDetails.setOnClickListener(v -> {

            getContactList();

        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        mMap.clear();

        LatLng sydney = new LatLng(latitude, longitude);

        Marker mSydney = mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("UserName")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.new_user)));

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))));

    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(14).build();
    }


    public String getAddress(Context context, double latitude, double longitute) {
        String addressStreet = "";
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitute, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addressStreet = addresses.get(0).getAddressLine(0);
            // address2 = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            //  city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String region = addresses.get(0).getAdminArea();

            Preference.save(this, Preference.KEY_address, addressStreet);

        //    Log.e("region====", region);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressStreet;
    }

    private void getContactList() {

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
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
                        Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }
}