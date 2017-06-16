package com.obs.deliver4me.sprint1.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.obs.deliver4me.R;
import com.obs.deliver4me.application.AppController;
import com.obs.deliver4me.configs.SessionManager;

import javax.inject.Inject;

/**
 * Created by Arun.S on 6/12/2017.
 */

public class SplashActivity extends AppCompatActivity {

    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        AppController.getAppComponent().inject(this);
        sessionManager.setFbUser(false);

        getIntentValues();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callActivityIntent();
            }
        }, 3000);
    }

    private void getIntentValues() {

    }

    private void callActivityIntent() {
        if (!TextUtils.isEmpty(sessionManager.getToken())) {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            Log.i("LoginAccessToken", " " + sessionManager.getToken());
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }
}
