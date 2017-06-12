package com.obs.deliver4me.configs;

/**
 * Created by Arun.S on 4/10/2017.
 */

import android.content.SharedPreferences;

import com.obs.deliver4me.application.AppController;

import javax.inject.Inject;

public class SessionManager {

    @Inject
    SharedPreferences sharedPreferences;


    public SessionManager() {
        //Injecting Dependency
        AppController.getAppComponent().inject(this);
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    // Store UserName
    public void setUsername(String username) {
        sharedPreferences.edit().putString("username", username).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString("username", "");
    }


    // Store Slide Menu Position
    public void setSlideMenuPosition(int position) {
        sharedPreferences.edit().putInt("menuposition", position).apply();
    }

    public int getSlideMenuPosition() {
        return sharedPreferences.getInt("menuposition", 0);
    }

}

