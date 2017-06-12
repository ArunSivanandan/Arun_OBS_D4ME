package com.obs.deliver4me.application;

import android.app.Application;

import com.obs.deliver4me.configs.Constants;
import com.obs.deliver4me.dependencies.component.AppComponent;
import com.obs.deliver4me.dependencies.component.DaggerAppComponent;
import com.obs.deliver4me.dependencies.module.ApplicationModule;
import com.obs.deliver4me.dependencies.module.NetworkModule;

/**
 * Created by Arun.S on 4/10/2017.
 */

public class AppController extends Application {
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // Dagger%COMPONENT_NAME%
        appComponent = DaggerAppComponent.builder()
                // list of modules that are part of this component need to be created here too
                .applicationModule(new ApplicationModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .networkModule(new NetworkModule(Constants.BASE_URL))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
