package com.obs.deliver4me.dependencies.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.obs.deliver4me.configs.Constants;
import com.obs.deliver4me.configs.NetworkHandler;
import com.obs.deliver4me.configs.RunTimePermission;
import com.obs.deliver4me.configs.SessionManager;
import com.obs.deliver4me.customviews.CustomDialog;
import com.obs.deliver4me.datamodels.JsonResponse;
import com.obs.deliver4me.utils.CTypeface;
import com.obs.deliver4me.utils.CommonMethods;
import com.obs.deliver4me.utils.WebServiceUtils;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Arun.S on 4/10/2017.
 */
@Module(includes = ApplicationModule.class)
public class AppContainerModule {

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    RunTimePermission providesRunTimePermission() {
        return new RunTimePermission();
    }

    @Provides
    @Singleton
    SessionManager providesSessionManager() {
        return new SessionManager();
    }

    @Provides
    @Singleton
    CTypeface providesCTypeface(){
        return new CTypeface();
    }

    @Provides
    @Singleton
    Context providesContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    ArrayList<String> providesStringArrayList() {
        return new ArrayList<>();
    }

    @Provides
    @Singleton
    CommonMethods providesCommonMethods(){
        return new CommonMethods();
    }

    @Provides
    @Singleton
    CustomDialog providesCustomDialog(){
        return new CustomDialog();
    }

    @Provides
    @Singleton
    NetworkHandler providesNetworkHandler(Application application) {
        return new NetworkHandler(application);
    }

    @Provides
    @Singleton
    JsonResponse providesJsonResponse() {
        return new JsonResponse();
    }

    @Provides
    @Singleton
    WebServiceUtils providesWebServiceUtils() {
        return new WebServiceUtils();
    }
}
