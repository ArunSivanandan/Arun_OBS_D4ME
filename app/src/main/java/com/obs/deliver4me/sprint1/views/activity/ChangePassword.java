package com.obs.deliver4me.sprint1.views.activity;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.obs.deliver4me.configs.Constants;
import com.obs.deliver4me.configs.NetworkHandler;
import com.obs.deliver4me.configs.RunTimePermission;
import com.obs.deliver4me.configs.SessionManager;
import com.obs.deliver4me.customviews.CustomDialog;
import com.obs.deliver4me.datamodels.JsonResponse;
import com.obs.deliver4me.interfaces.ApiService;
import com.obs.deliver4me.interfaces.RequestCallback;
import com.obs.deliver4me.interfaces.RequestListener;
import com.obs.deliver4me.sprint1.models.ChangePwdModel;
import com.obs.deliver4me.utils.CommonMethods;
import com.obs.deliver4me.utils.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * Created by Arun.S on 6/14/2017.
 */

public class ChangePassword extends AppCompatActivity implements RequestListener {

    private View view;
    private Context context;
    private EditText edtCurrentPwd, edtNewPwd, edtConfirmPwd;
    private TextView tvTitle, tvSubmitBtn;
    private ImageView ivBackBtn;
    private ChangePwdModel changePwdModel;
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
        setContentView(R.layout.change_password_layout);
        AppController.getAppComponent().inject(this);
        progressDialog = new ProgressDialog(this);

        initViews();
    }

    private void initViews() {
        edtCurrentPwd = (EditText) findViewById(R.id.edt_current_pwd);
        edtNewPwd = (EditText) findViewById(R.id.edt_new_pwd);
        edtConfirmPwd = (EditText) findViewById(R.id.edt_confirm_pwd);
        tvSubmitBtn = (TextView) findViewById(R.id.tv_submitBtn);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivBackBtn = (ImageView) findViewById(R.id.iv_back);

        tvTitle.setText(getResources().getString(R.string.txt_change_pwd_title));
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
                    case R.id.tv_submitBtn:
                        callChangePwdApi();
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

    private void callChangePwdApi() {
        if (hasNetworkConnection()) {
            commonMethods.showLoader(progressDialog);
            apiService.postChangePwdApi(getPostParmas()).enqueue(new RequestCallback(this));
        }
    }

    private RequestBody getPostParmas() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("currentPassword", edtCurrentPwd.getText().toString());
            jsonObject.put("newPassword", edtNewPwd.getText().toString());
            jsonObject.put("confirmPassword", edtConfirmPwd.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return RequestBody.create(Constants.JSON, jsonObject.toString());
    }

    @Override
    public void onSuccess(JsonResponse jsonResp) {
        commonMethods.dismissLoader(progressDialog);
        switch (jsonResp.getResponseCode()) {
            case 200:
                try {
                    changePwdModel = (ChangePwdModel) webServiceUtils.getJsonObjectModel(jsonResp.getStrResponse(), ChangePwdModel.class);
                    if (changePwdModel != null) {
                        if (!TextUtils.isEmpty(changePwdModel.getSucess())) {
                            customDialog.showDialog(getSupportFragmentManager(),
                                    getResources().getString(R.string.success_msg),
                                    changePwdModel.getSucess(),
                                    new CustomDialog.btnOkClick() {
                                        @Override
                                        public void clicked() {
                                            sessionManager.clear();
                                            sessionManager.clearAll();
                                            startActivity(new Intent(ChangePassword.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        }
                                    });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                changePwdModel = (ChangePwdModel) webServiceUtils.getJsonObjectModel(jsonResp.getErrorMsg(), ChangePwdModel.class);
                if (!TextUtils.isEmpty(changePwdModel.getError())) {
                    customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.error_msg), changePwdModel.getError());
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
