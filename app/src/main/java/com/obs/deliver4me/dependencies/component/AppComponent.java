package com.obs.deliver4me.dependencies.component;

import com.obs.deliver4me.configs.RunTimePermission;
import com.obs.deliver4me.configs.SessionManager;
import com.obs.deliver4me.customviews.CustomDialog;
import com.obs.deliver4me.dependencies.module.AppContainerModule;
import com.obs.deliver4me.dependencies.module.ApplicationModule;
import com.obs.deliver4me.dependencies.module.NetworkModule;
import com.obs.deliver4me.sprint1.views.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Arun.S on 4/10/2017.
 */

@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class, AppContainerModule.class})
public interface AppComponent {

    /*ACTIVITY*/
    void inject(MainActivity mainActivity);


    /*ADAPTERS*/


    /*JAVA CLASS*/
    void inject(SessionManager sessionManager);

    void inject(RunTimePermission runTimePermission);

    void inject(CustomDialog customDialog);

    /*FRAGMENTS*/


    /*MODEL*/


    /*SERVICE*/
}
