package com.obs.deliver4me.sprint1.views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.obs.deliver4me.R;
import com.obs.deliver4me.application.AppController;
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

import javax.inject.Inject;

/**
 * Created by Arun.S on 6/13/2017.
 */

public class ForgotPasswordActivity extends AppCompatActivity implements RequestListener {

    private TextView tvSubmitBtn, tvTitle;
    private EditText edtForgotEmail;
    private ImageView ivBackBtn;
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
        setContentView(R.layout.forgot_password_layout);
        AppController.getAppComponent().inject(this);
        progressDialog = new ProgressDialog(this);

        initViews();
    }

    private void initViews() {
        tvTitle =  (TextView) findViewById(R.id.tv_title);
        tvSubmitBtn = (TextView) findViewById(R.id.tv_forgot_submit);
        edtForgotEmail =  (EditText) findViewById(R.id.edt_forgot_email);
        ivBackBtn = (ImageView) findViewById(R.id.iv_back);

        tvTitle.setText(getResources().getString(R.string.forgot_pwd_screen_title));
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
                    case R.id.tv_forgot_submit:
                        callForgotPwdApi();
                        break;
                }
            }
        };
        tvSubmitBtn.setOnClickListener(onClickListener);
        ivBackBtn.setOnClickListener(onClickListener);
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

    private void callForgotPwdApi() {
        if (hasNetworkConnection()) {
            commonMethods.showLoader(progressDialog);
            apiService.getForgotPwdApi(edtForgotEmail.getText().toString()).enqueue(new RequestCallback(this));
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp) {
        commonMethods.dismissLoader(progressDialog);
        switch (jsonResp.getResponseCode()) {
            case 200:
                try {
                    loginModel = (LoginModel) webServiceUtils.getJsonObjectModel(jsonResp.getStrResponse(), LoginModel.class);
                    if (loginModel != null) {
                        if (!TextUtils.isEmpty(loginModel.getSucess().getMessage())) {
                            customDialog.showDialog(getSupportFragmentManager(),
                                    getResources().getString(R.string.success_msg),
                                    loginModel.getSucess().getMessage(),
                                    new CustomDialog.btnOkClick() {
                                        @Override
                                        public void clicked() {
                                            startActivity(new Intent(ForgotPasswordActivity.this, ResetPassword.class));
                                        }
                                    });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                loginModel = (LoginModel) webServiceUtils.getJsonObjectModel(jsonResp.getErrorMsg(), LoginModel.class);
                if (loginModel.getError() != null) {
                    customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.error_msg), loginModel.getError());
                }
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
}
