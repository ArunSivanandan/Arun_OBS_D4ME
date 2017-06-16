package com.obs.deliver4me.dependencies.component;

import com.obs.deliver4me.configs.RunTimePermission;
import com.obs.deliver4me.configs.SessionManager;
import com.obs.deliver4me.customviews.CustomDialog;
import com.obs.deliver4me.datamodels.JsonResponse;
import com.obs.deliver4me.dependencies.module.AppContainerModule;
import com.obs.deliver4me.dependencies.module.ApplicationModule;
import com.obs.deliver4me.dependencies.module.NetworkModule;
import com.obs.deliver4me.interfaces.RequestCallback;
import com.obs.deliver4me.sprint1.views.activity.ChangePassword;
import com.obs.deliver4me.sprint1.views.activity.CreateProfile;
import com.obs.deliver4me.sprint1.views.activity.ForgotPasswordActivity;
import com.obs.deliver4me.sprint1.views.activity.HomeActivity;
import com.obs.deliver4me.sprint1.views.activity.LoginActivity;
import com.obs.deliver4me.sprint1.views.activity.ResetPassword;
import com.obs.deliver4me.sprint1.views.activity.SignupActivity;
import com.obs.deliver4me.sprint1.views.activity.SplashActivity;
import com.obs.deliver4me.sprint1.views.fragments.Settings;
import com.obs.deliver4me.utils.WebServiceUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Arun.S on 4/10/2017.
 */

@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class, AppContainerModule.class})
public interface AppComponent {

    /*ACTIVITY*/
    void inject(SplashActivity splashActivity);

    void inject(HomeActivity homeActivity);

    void inject(LoginActivity loginActivity);

    void inject(SignupActivity signupActivity);

    void inject(ForgotPasswordActivity forgotPasswordActivity);

    void inject(CreateProfile createProfile);

    void inject(ChangePassword changePassword);

    void inject(ResetPassword resetPassword);

    /*ADAPTERS*/


    /*JAVA CLASS*/
    void inject(SessionManager sessionManager);

    void inject(RunTimePermission runTimePermission);

    void inject(CustomDialog customDialog);

    /*FRAGMENTS*/
    void inject(Settings settings);

    /*MODEL*/
    void inject(JsonResponse jsonResponse);

    /*SERVICE*/
    void inject(RequestCallback requestCallback);

    void inject(WebServiceUtils webServiceUtils);
}
