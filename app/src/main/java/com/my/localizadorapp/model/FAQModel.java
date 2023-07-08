package com.my.localizadorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FAQModel implements Serializable {
    @SerializedName("result")
    @Expose
    public ArrayList<Result> result;
    @SerializedName("message")
    @Expose
    public  String message;
    @SerializedName("status")
    @Expose
    public
    String status;

    public static class Result   implements Serializable {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("question")
        @Expose
        public String question;
        @SerializedName("answer")
        @Expose
        public String answer;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
    }
}
