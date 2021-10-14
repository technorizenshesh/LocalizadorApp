package com.my.localizadorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AddAddressModel {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("address_type")
        @Expose
        public String addressType;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
    }

}

