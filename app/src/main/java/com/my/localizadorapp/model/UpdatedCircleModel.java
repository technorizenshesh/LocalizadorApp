package com.my.localizadorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatedCircleModel {

    @SerializedName("result")
    @Expose
    public String result;
    @SerializedName("circle_name")
    @Expose
    public String circleName;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;
}
