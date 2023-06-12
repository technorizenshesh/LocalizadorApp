package com.my.localizadorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpdataModel {

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
        @SerializedName("user_name")
        @Expose
        public String userName;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("dob")
        @Expose
        public String dob;
        @SerializedName("gender")
        @Expose
        public String gender;

        @SerializedName("country_code")
        @Expose
        public String country_code;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("otp")
        @Expose
        public String otp;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("circle_name")
        @Expose
        public String circleName;
        @SerializedName("code")
        @Expose
        public String code;

    }
}

