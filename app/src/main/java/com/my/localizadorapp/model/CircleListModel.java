package com.my.localizadorapp.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CircleListModel {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
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
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("circle_name")
        @Expose
        public String circleName;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
    }
}

