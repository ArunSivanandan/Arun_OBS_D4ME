
package com.obs.deliver4me.sprint1.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("success")
    @Expose
    private Success success;
    @SerializedName("sucess")
    @Expose
    private Sucess sucess;
    @SerializedName("error")
    @Expose
    private String error;

    public Sucess getSucess() {
        return sucess;
    }

    public void setSucess(Sucess sucess) {
        this.sucess = sucess;
    }

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
