package com.my.localizadorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MemberListModel {

    @SerializedName("result")
    @Expose
    private List<MemberListDataModel> result = null;
    @SerializedName("owner_detail")
    @Expose
    private OwnerDetailModel ownerDetail;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<MemberListDataModel> getResult() {
        return result;
    }

    public void setResult(List<MemberListDataModel> result) {
        this.result = result;
    }

    public OwnerDetailModel getOwnerDetail() {
        return ownerDetail;
    }

    public void setOwnerDetail(OwnerDetailModel ownerDetail) {
        this.ownerDetail = ownerDetail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
