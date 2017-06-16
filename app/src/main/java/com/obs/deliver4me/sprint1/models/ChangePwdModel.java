package com.obs.deliver4me.sprint1.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arun.S on 6/16/2017.
 */

public class ChangePwdModel {
    @SerializedName("sucess")
    @Expose
    private String sucess;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("success")
    @Expose
    private String success;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSucess() {
        return sucess;
    }

    public void setSucess(String sucess) {
        this.sucess = sucess;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
