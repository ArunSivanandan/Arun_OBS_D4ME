package com.obs.deliver4me.sprint1.views.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.deliver4me.BuildConfig;
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
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;



/**
 * Created by Arun.S on 6/15/2017.
 */

public class CreateProfile extends AppCompatActivity implements RequestListener {

    private TextView tvTitle, tvSkip, tvCreateProfileSave;
    private ImageView ivBackBtn, ivMemberImg;
    private EditText edtUsername, edtPhoneNo, edtEmail, edtAbout;
    private RadioGroup radio_group;
    private RadioButton memberGroup;
    private RelativeLayout rltMemberContent, rltVehicleContent;
    private File imageFile = null;
    private Uri imageUri;
    private String path = "", userName = "", email = "", phoneNo = "", about = "";
    private Context context;
    private ProgressDialog progressDialog;
    private ChangePwdModel changePwdModel;
    private Bitmap photo;

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
        setContentView(R.layout.create_profile_layout);
        AppController.getAppComponent().inject(this);
        progressDialog = new ProgressDialog(this);
        context = this;

        initviews();
    }

    private void initviews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSkip = (TextView) findViewById(R.id.tv_skip_txt);
        tvCreateProfileSave = (TextView) findViewById(R.id.tv_create_profile_save);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtEmail = (EditText) findViewById(R.id.edt_mail);
        edtPhoneNo = (EditText) findViewById(R.id.edt_phone_no);
        edtAbout = (EditText) findViewById(R.id.edt_about);
        ivBackBtn = (ImageView) findViewById(R.id.iv_back);
        ivMemberImg = (ImageView) findViewById(R.id.iv_member_image);
        radio_group = (RadioGroup) findViewById(R.id.radio_create_profile_group);
        memberGroup = (RadioButton) findViewById(R.id.member_Group);
        rltMemberContent = (RelativeLayout) findViewById(R.id.rlt_member_content);
        rltVehicleContent = (RelativeLayout) findViewById(R.id.rlt_vehicle_content);

        ivBackBtn.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.create_profile_title));

        initEventListener();
    }

    private void initEventListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_member_image:
                        getPhoto();
                        break;
                    case R.id.tv_skip_txt:
                        startActivity(new Intent(CreateProfile.this, HomeActivity.class));
                        break;
                    case R.id.tv_create_profile_save:
                        userName = edtUsername.getText().toString();
                        email = edtEmail.getText().toString();
                        phoneNo = edtPhoneNo.getText().toString();
                        about = edtAbout.getText().toString();
                        callMemberApi();
                        break;
                }
            }
        };
        tvCreateProfileSave.setOnClickListener(onClickListener);
        tvSkip.setOnClickListener(onClickListener);
        ivMemberImg.setOnClickListener(onClickListener);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.member_Group:
                        rltMemberContent.setVisibility(View.VISIBLE);
                        rltVehicleContent.setVisibility(View.GONE);
                        break;
                    case R.id.vehicle_Group:
                        rltVehicleContent.setVisibility(View.VISIBLE);
                        rltMemberContent.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void getPhoto() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(CreateProfile.this.getResources().getString(R.string.upload_option));
        alertDialog.setItems(Constants.EDIT_PROFILE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                if (pos == 0) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    imageFile = commonMethods.uniqueCameraFileName();
                    Log.e("image", "image clicked");

                    imageUri = FileProvider.getUriForFile(CreateProfile.this, BuildConfig.APPLICATION_ID + ".provider", imageFile);
                    Log.e("image file", imageFile.toString());
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(cameraIntent, 1);
                    commonMethods.refreshGallery(context, imageFile);
                } else {
                    imageFile = commonMethods.getDefaultFileName(context);

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, Constants.REQUEST_CODE_GALLERY);
                }
            }
        });
        AlertDialog alertbox = alertDialog.create();
        alertbox.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && permission.size() > 0) {
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    startCropImage();
                    break;
                case Constants.REQUEST_CODE_GALLERY:
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        startCropImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    path = result.getUri().getPath();
                    Log.e("Show Path", "   " + path);
                    if (!TextUtils.isEmpty(path)) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            ivMemberImg.setImageBitmap(bitmap);
                            Log.e("Show bitmap", "   " + bitmap);
                        } catch (OutOfMemoryError error) {
                            error.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("Show Path Empty", "path is empty");
                    }
                    break;
                case 300:
                    checkAllPermission(Constants.PERMISSIONS_STORAGE);
                    break;
                default:
                    break;
            }
        }
    }

    private void startCropImage() {
        if (imageFile != null) {
            CropImage.activity(Uri.fromFile(imageFile)).setAspectRatio(2, 2).start(CreateProfile.this);
        }
    }

    private void copyStream(InputStream input, FileOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(CreateProfile.this, permission);
        if (blockedPermission != null && blockedPermission.size() > 0) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(CreateProfile.this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                callPermissionSettings();
            } else {
                ActivityCompat.requestPermissions(CreateProfile.this, permission, 150);
            }
        }
    }

    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", CreateProfile.this.getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
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

    private void callMemberApi() {
        if (hasNetworkConnection()) {
            commonMethods.showLoader(progressDialog);
            Map<String, String> query = new HashMap<>();
            query.put("userName", userName);
            query.put("phoneNumber", phoneNo);
            query.put("email", email);
            query.put("about", about);

            Map<String, File> imageQuery = new HashMap<>();
            if (imageFile != null) {
                Log.e("imageFile", "ImageFile is not null");
                imageQuery.put("image", imageFile);
            } else {
                Log.e("imageFile", "ImageFile is null");
            }
            apiService.createProfileMemberApi(query, imageQuery).enqueue(new RequestCallback(this));
        }
    }

    @Override
    public void onSuccess(JsonResponse jsonResp) {
        switch (jsonResp.getRequestCode()) {
            case Constants.POST_PROFILE_MEMBER:
                onSuccessProfileMember(jsonResp);
                break;
            case Constants.POST_PROFILE_VEHICLE:
                onSuccessProfileVehicle(jsonResp);
                break;
            default:
                commonMethods.dismissLoader(progressDialog);
                Log.e("onSuccess CreatePro Error", "default switch called");
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

    public void onSuccessProfileMember(JsonResponse jsonResponse) {
        commonMethods.dismissLoader(progressDialog);
        switch (jsonResponse.getResponseCode()) {
            case 200:
                try {
                    changePwdModel = (ChangePwdModel) webServiceUtils.getJsonObjectModel(jsonResponse.getStrResponse(), ChangePwdModel.class);
                    if (changePwdModel != null) {
                        if (!TextUtils.isEmpty(changePwdModel.getSucess())) {
                            customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.success_msg), changePwdModel.getSucess());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                changePwdModel = (ChangePwdModel) webServiceUtils.getJsonObjectModel(jsonResponse.getErrorMsg(), ChangePwdModel.class);
                if (!TextUtils.isEmpty(changePwdModel.getError())) {
                    customDialog.showDialog(getSupportFragmentManager(), getResources().getString(R.string.error_msg), changePwdModel.getError());
                }
                break;
        }
    }

    public void onSuccessProfileVehicle(JsonResponse jsonResponse) {
        commonMethods.dismissLoader(progressDialog);
        switch (jsonResponse.getResponseCode()) {
            case 200:
                break;
            default:
                break;
        }
    }
}
