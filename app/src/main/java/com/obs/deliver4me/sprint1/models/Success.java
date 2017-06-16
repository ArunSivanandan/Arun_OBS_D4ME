
package com.obs.deliver4me.sprint1.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Success {

    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("firstLogin")
    @Expose
    private Boolean firstLogin;
    @SerializedName("token")
    @Expose
    private String token;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
