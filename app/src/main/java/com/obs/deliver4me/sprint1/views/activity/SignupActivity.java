package com.obs.deliver4me.sprint1.views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * Created by Arun.S on 6/14/2017.
 */

public class SignupActivity extends AppCompatActivity implements RequestListener {

    private TextView tvTitle, tvLogin, tvSubmit;
    private ImageView ivBackBtn;
    private EditText edtUsername, edtEmail, edtPassword, edtConfirmPassword;
    private String username = "", email = "", password = "", confirmPassword = "";
    private LoginModel loginModel;
    private ProgressDialog progressDialog;

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
        setContentView(R.layout.registration_layout);
        AppController.getAppComponent().inject(this);
        progressDialog = new ProgressDialog(this);

        initViews();
    }

    private void initViews() {
        tvLogin = (TextView) findViewById(R.id.tv_signup_login);
        tvSubmit = (TextView) findViewById(R.id.tv_signup_submit);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtEmail = (EditText) findViewById(R.id.edt_mail);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);
        ivBackBtn = (ImageView) findViewById(R.id.iv_back);

        tvTitle.setText(getResources().getString(R.string.signup_screen_title));
        initEventListener();
    }

    private void initEventListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_back:
                        finish();
                        break;
                    case R.id.tv_signup_login:
                        finish();
                        break;
                    case R.id.tv_signup_submit:
                        username = edtUsername.getText().toString();
                        email = edtEmail.getText().toString();
                        password = edtPassword.getText().toString();
                        confirmPassword = edtConfirmPassword.getText().toString();
                        if (isValidate(getSupportFragmentManager())) {
                            callSignupApi();
                        }
                        break;
                }
            }
        };
        tvLogin.setOnClickListener(onClickListener);
        tvSubmit.setOnClickListener(onClickListener);
        ivBackBtn.setOnClickListener(onClickListener);
    }

    private boolean isValidate(final FragmentManager fm) {
        boolean isValid = true;
        if (isEmpty(username) && isEmpty(email) && isEmpty(password) && isEmpty(confirmPassword)) {
            isValid = false;
            customDialog.showDialog(fm, getResources().getString(R.string.error_msg), getResources().getString(R.string.empty_fields));
        } else if (isEmpty(username)) {
            isValid = false;
            customDialog.showDialog(fm, getResources().getString(R.string.error_msg), getResources().getString(R.string.empty_username));
        } else if (isEmpty(email)) {
            isValid = false;
            customDialog.showDialog(fm, getResources().getString(R.string.error_msg), getResources().getString(R.string.empty_email));
        } else if (!isValidEmail(email)) {
            isValid = false;
            customDialog.showDialog(fm, getResources().getString(R.string.error_msg), getResources().getString(R.string.Invalid_email));
        } else if (isEmpty(password)) {
            isValid = false;
            customDialog.showDialog(fm, getResources().getString(R.string.error_msg), getResources().getString(R.string.empty_password));
        } else if (!isValidPassword(password)) {
            isValid = false;
            customDialog.showDialog(fm, getResources().getString(R.string.error_msg), getResources().getString(R.string.Invalid_Password));
        } else if (isEmpty(confirmPassword)) {
            isValid = false;
            customDialog.showDialog(fm, getResources().getString(R.string.error_msg), getResources().getString(R.string.empty_confirm_password));
        } else if (!password.equals(confirmPassword)) {
            isValid = false;
            customDialog.showDialog(fm, getResources().getString(R.string.error_msg), getResources().getString(R.string.Invalid_confirm_password));
        } else {
            isValid = true;
        }

        return isValid;
    }

    private boolean isEmpty(String value) {
        boolean isValue = false;
        if (TextUtils.isEmpty(value)) {
            isValue = true;
        }
        return isValue;
    }

    private boolean isValidPassword(String password) {
        return password.matches(Constants.PASSWORD_VALIDITY);
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    private void callSignupApi() {
        if (hasNetworkConnection()) {
            commonMethods.showLoader(progressDialog);
            apiService.registerUserApi(getCreatePostParam()).enqueue(new RequestCallback(this));
        }
    }

    private void callLoginApi() {
        if (hasNetworkConnection()) {
            apiService.loginTokenApi(getCreateLoginParam()).enqueue(new RequestCallback(this));
        }
    }

    private RequestBody getCreatePostParam() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", username);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("confirmPassword", confirmPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RequestBody.create(Constants.JSON, jsonObject.toString());
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

    @Override
    public void onSuccess(JsonResponse jsonResp) {
        switch (jsonResp.getRequestCode()) {
            case Constants.POST_REGISTER_USER:
                onPostRegisterUserSuccess(jsonResp);
                break;
            case Constants.POST_AUTH_LOGIN_TOKEN:
                onPostAuthLoginSuccess(jsonResp);
                break;
            default:
                commonMethods.dismissLoader(progressDialog);
                Log.e("OnSuccess Error", "Switch Case Error Occured!");
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp) {
        commonMethods.dismissLoader(progressDialog);
        if (!this.isFinishing()) {
            customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.error_msg), getResources().getString(R.string.connection_timeout));
        }
    }

    public void onPostRegisterUserSuccess(JsonResponse jsonResponse) {
        switch (jsonResponse.getResponseCode()) {
            case 200:
                try {
                    loginModel = (LoginModel) webServiceUtils.getJsonObjectModel(jsonResponse.getStrResponse(), LoginModel.class);
                    callLoginApi();
                    Log.e("Signup Response Role", " " + loginModel.getSuccess().getRole());
                    Log.e("Signup Response Token", " " + loginModel.getSuccess().getToken());
//                    Log.e("Signup Response FirstLogin", " " + loginModel.getSuccess().getFirstLogin());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                commonMethods.dismissLoader(progressDialog);
                loginModel = (LoginModel) webServiceUtils.getJsonObjectModel(jsonResponse.getErrorMsg(), LoginModel.class);
                if (loginModel.getError() != null) {
                    customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.error_msg), loginModel.getError());
                }
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
                        if (loginModel.getSucess().getFirstLogin() != null) {
                            if (loginModel.getSucess().getFirstLogin()) {
                                startActivity(new Intent(SignupActivity.this, CreateProfile.class));
                                finish();
                            } else {
                                startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                                finish();
                            }
                        }
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
