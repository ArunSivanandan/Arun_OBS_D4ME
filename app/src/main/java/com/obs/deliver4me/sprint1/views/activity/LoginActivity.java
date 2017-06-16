package com.obs.deliver4me.sprint1.views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.obs.deliver4me.R;
import com.obs.deliver4me.application.AppController;
import com.obs.deliver4me.configs.Constants;
import com.obs.deliver4me.configs.NetworkHandler;
import com.obs.deliver4me.configs.RunTimePermission;
import com.obs.deliver4me.configs.SessionManager;
import com.obs.deliver4me.customviews.CustomDialog;
import com.obs.deliver4me.datamodels.JsonResponse;
import com.obs.deliver4me.interfaces.ApiService;
import com.obs.deliver4me.interfaces.RequestCallback;
import com.obs.deliver4me.interfaces.RequestListener;
import com.obs.deliver4me.sprint1.models.LoginModel;
import com.obs.deliver4me.utils.CommonMethods;
import com.obs.deliver4me.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * Created by Arun.S on 6/13/2017.
 */

public class LoginActivity extends AppCompatActivity implements RequestListener {

    private TextView tvLogin, tvForgotPassword, tvSignup;
    private EditText edtEmail, edtPassword;
    private ImageView ivFacebookLogin;
    private String email = "", password = "";
    private LoginModel loginModel;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private ProgressDialog progressDialog;
    private String fbName = "", fbEmail = "", profileImg = "", userId = "", token = "";

    @Inject
    SessionManager sessionManager;
    @Inject
    RunTimePermission runTimePermission;
    @Inject
    CustomDialog customDialog;
    @Inject
    NetworkHandler networkHandler;
    @Inject
    ApiService apiService;
    @Inject
    WebServiceUtils webServiceUtils;
    @Inject
    CommonMethods commonMethods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        AppController.getAppComponent().inject(this);
        sessionManager.setFbUser(false);
        progressDialog = new ProgressDialog(this);
        initViews();
        facebookLogin();
    }

    private void initViews() {
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvSignup = (TextView) findViewById(R.id.tv_signup);
        tvForgotPassword = (TextView) findViewById(R.id.tv_forgotpwd);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        ivFacebookLogin = (ImageView) findViewById(R.id.iv_facebook_login);

        initEventListener();
    }

    private void initEventListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_login:
                        validate();
                        break;
                    case R.id.tv_forgotpwd:
                        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                        break;
                    case R.id.tv_signup:
                        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                        break;
                    case R.id.iv_facebook_login:
                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                        break;
                }
            }
        };
        tvLogin.setOnClickListener(onClickListener);
        tvForgotPassword.setOnClickListener(onClickListener);
        tvSignup.setOnClickListener(onClickListener);
        ivFacebookLogin.setOnClickListener(onClickListener);
    }

    private void facebookLogin() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (loginResult != null && loginResult.getAccessToken() != null && loginResult.getAccessToken().getToken() != null) {
                    accessToken = loginResult.getAccessToken();
                    token = accessToken.getToken();
                    sessionManager.setFbUser(true);
                    getFacebookUserProfile(accessToken);
                    Log.v("login result", "  " + String.valueOf(loginResult.getAccessToken()));
                } else {
                    Log.v("login result", "login result is null.");
                }
            }

            @Override
            public void onCancel() {
                Log.e("Cancel", "true");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("Error", "true");
                if (exception instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }
        });
    }

    private void getFacebookUserProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = response.getJSONObject();
                try {
                    fbName = jsonObject.getString("name");
                    fbEmail = jsonObject.getString("email");
                    userId = jsonObject.getString("id");
                    if (jsonObject.has("picture")) {
                        profileImg = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                    }
                    callFBRegisterApi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, first_name,last_name, email, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void validate() {
        if (isValidUsername()) {
            email = edtEmail.getText().toString().trim();
            password = edtPassword.getText().toString().trim();
            callLoginApi();
        }
    }

    public boolean isValidUsername() {
        if (edtEmail.getText().toString().length() > 1) {
            return isPasswordEmpty();
        } else {
            customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.error_msg), getResources().getString(R.string.empty_email));
            return false;
        }
    }

    public boolean isPasswordEmpty() {
        if (edtPassword.getText().toString().length() > 1) {
            return true;
        } else {
            customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.error_msg), getResources().getString(R.string.empty_password));
            return false;
        }
    }

    private boolean hasNetworkConnection() {
        try {
            if (!networkHandler.isNetworkAvailable()) {
                customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.error_msg), getResources().getString(R.string.no_internet_connection));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void callFBRegisterApi() {
        if (hasNetworkConnection()) {
            commonMethods.showLoader(progressDialog);
            apiService.fbRegisterApi(getFBRegisterParam()).enqueue(new RequestCallback(this));
        }
    }

    private void callFBLoginApi() {
        if (hasNetworkConnection()) {
            apiService.fbLoginApi(getFBLoginParam()).enqueue(new RequestCallback(this));
        }
    }

    private void callLoginApi() {
        if (hasNetworkConnection()) {
            commonMethods.showLoader(progressDialog);
            apiService.loginTokenApi(getCreateLoginParam()).enqueue(new RequestCallback(this));
        }
    }

    private RequestBody getCreateLoginParam() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RequestBody.create(Constants.JSON, jsonObject.toString());
    }

    private RequestBody getFBRegisterParam() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", fbName);
            jsonObject.put("email", fbEmail);
            jsonObject.put("providerName", "Facebook");
            jsonObject.put("providerToken", token);
            jsonObject.put("providerId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RequestBody.create(Constants.JSON, jsonObject.toString());
    }

    private RequestBody getFBLoginParam() {
        JSONObject jsonObject = new JSONObject();
        try {
            Log.v("fb email", "  " + fbEmail);
            Log.v("fb providerId", "  " + userId);
            jsonObject.put("email", fbEmail);
            jsonObject.put("providerId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RequestBody.create(Constants.JSON, jsonObject.toString());
    }

    @Override
    public void onSuccess(JsonResponse jsonResp) {
        Log.v("RequestCodeSuccess", " " + jsonResp.getRequestCode());
        Log.v("ResponseCodeSuccess", " " + jsonResp.getResponseCode());
        Log.v("ResponseMsgSuccess", " " + jsonResp.getErrorMsg());
        switch (jsonResp.getRequestCode()) {
            case Constants.POST_AUTH_LOGIN_TOKEN:
                onPostAuthLoginSuccess(jsonResp);
                break;
            case Constants.POST_FB_REGISTER:
                onPostFBRegisterSuccess(jsonResp);
                break;
            case Constants.POST_FB_LOGIN:
                onPostAuthLoginSuccess(jsonResp);
                break;

        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp) {
        Log.v("ResponseCodeFail", " " + jsonResp.getResponseCode());
        Log.v("ResponseMsgFail", " " + jsonResp.getErrorMsg());
        commonMethods.dismissLoader(progressDialog);
        if (!this.isFinishing()) {
            customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.error_msg), getResources().getString(R.string.connection_timeout));
        }
    }

    public void onPostFBRegisterSuccess(JsonResponse jsonResponse) {
        Log.v("ResponseCode", " " + jsonResponse.getResponseCode());
        Log.v("ResponseMsg", " " + jsonResponse.getErrorMsg());
        switch (jsonResponse.getResponseCode()) {
            case 200:
                callFBLoginApi();
                break;
            case 400:
                callFBLoginApi();
            default:
                commonMethods.dismissLoader(progressDialog);
                Log.e("OnSuccessFBreg Error", "Switch Case Error Occured!");
                break;
        }
    }

    public void onPostAuthLoginSuccess(JsonResponse jsonResponse) {
        commonMethods.dismissLoader(progressDialog);
        switch (jsonResponse.getResponseCode()) {
            case 200:
                try {
                    loginModel = (LoginModel) webServiceUtils.getJsonObjectModel(jsonResponse.getStrResponse(), LoginModel.class);
                    if (loginModel != null) {
                        if (loginModel.getSucess().getToken() != null) {
                            sessionManager.setToken(loginModel.getSucess().getToken());
                            Log.i("LoginAccessToken", loginModel.getSucess().getToken());
                        }
                        customDialog.showDialog(getSupportFragmentManager(),
                                getResources().getString(R.string.success_msg),
                                getResources().getString(R.string.login_success_msg),
                                new CustomDialog.btnOkClick() {
                                    @Override
                                    public void clicked() {
                                        if (loginModel.getSucess().getFirstLogin() != null) {
                                            if (loginModel.getSucess().getFirstLogin()) {
                                                startActivity(new Intent(LoginActivity.this, CreateProfile.class));
                                                finish();
                                            } else {
//                                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                startActivity(new Intent(LoginActivity.this, CreateProfile.class));
                                                finish();
                                            }
                                        }
                                    }
                                });
                    }
                    Log.e("login Response Role", " " + loginModel.getSucess().getRole());
                    Log.e("login Response Token", " " + loginModel.getSucess().getToken());
//                    Log.e("Signup Response FirstLogin", " " + loginModel.getSuccess().getFirstLogin());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                loginModel = (LoginModel) webServiceUtils.getJsonObjectModel(jsonResponse.getErrorMsg(), LoginModel.class);
                if (loginModel.getError() != null) {
                    customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.error_msg), loginModel.getError());
                }
                break;
        }
    }
}
