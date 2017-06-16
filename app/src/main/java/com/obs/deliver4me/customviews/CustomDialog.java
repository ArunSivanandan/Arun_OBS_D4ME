package com.obs.deliver4me.customviews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.obs.deliver4me.R;
import com.obs.deliver4me.application.AppController;
import com.obs.deliver4me.utils.CTypeface;

import javax.inject.Inject;

@SuppressLint("ValidFragment")
public class CustomDialog extends BaseDialogFragment {

    @Inject
    CTypeface cTypeface;

    private String title;
    private String message;
    private String btnPositiveBtnTxt;
    private String btnNegativeBtnTxt;
    private String btnCofirmTxt;
    private int customDialogImageSrc;
    private TextView dialogTitle;
    private TextView dialogMessage;
    private TextView allowTxt;
    private TextView denyTxt;
    private TextView okTxt;
    private LinearLayout lltAllowDenyBtn;
    private ImageView customDialogImage;
    private boolean isPermissionDialog = false;
    private btnOkClick okClickListener;
    private btnAllowClick allowClickListener;
    private btnDenyClick denyClickListener;
    private static CustomDialog dialog = null;

    public interface btnOkClick {
        void clicked();
    }

    public interface btnAllowClick {
        void clicked();
    }

    public interface btnDenyClick {
        void clicked();
    }

    public CustomDialog(){
    }

    public CustomDialog(String title, String message, int customDialogImageSrc, String btnCofirmTxt, btnOkClick okClickListener, boolean isError) {
        this.title=title;
        this.message = message;
        this.customDialogImageSrc = customDialogImageSrc;
        this.btnCofirmTxt = btnCofirmTxt;
        this.okClickListener = okClickListener;
        this.mActivity = null;
        isPermissionDialog = false;
        setLayoutId(R.layout.activity_custom_dialog);
    }

    public CustomDialog(String title, String message, btnOkClick okClickListener) {
        this.title=title;
        this.message = message;
        this.okClickListener = okClickListener;
        this.mActivity = null;
        isPermissionDialog = false;
        btnCofirmTxt = "OK";
        setLayoutId(R.layout.activity_custom_dialog);
    }

    public CustomDialog(String title, String message, int customDialogImageSrc, String btnPositiveBtnTxt, String btnNegativeBtnTxt, btnAllowClick allowClickListener, btnDenyClick denyClickListener) {
        this.title=title;
        this.message = message;
        this.customDialogImageSrc = customDialogImageSrc;
        this.btnPositiveBtnTxt = btnPositiveBtnTxt;
        this.btnNegativeBtnTxt = btnNegativeBtnTxt;
        this.allowClickListener = allowClickListener;
        this.denyClickListener = denyClickListener;
        this.mActivity = null;
        isPermissionDialog = true;
        setLayoutId(R.layout.activity_custom_dialog);
    }

    @Override
    public void initViews(View v) {
        super.initViews(v);
        AppController.getAppComponent().inject(this);
        this.dialogTitle = (TextView) v.findViewById(R.id.dialogTextTitle);
        this.dialogMessage = (TextView) v.findViewById(R.id.dialogTextMessage);
        this.allowTxt = (TextView) v.findViewById(R.id.customAllowTxt);
        this.denyTxt = (TextView) v.findViewById(R.id.customDenyTxt);
        this.okTxt = (TextView) v.findViewById(R.id.customOkTxt);
        lltAllowDenyBtn = (LinearLayout) v.findViewById(R.id.llt_allow_deny_btn);
//        customDialogImage = (ImageView) v.findViewById(R.id.customDialogImage);

        this.dialogTitle.setText(title);
        this.dialogMessage.setText(message);

        this.dialogMessage.setMovementMethod(new ScrollingMovementMethod());
        if (isPermissionDialog) {
            this.allowTxt.setVisibility(View.VISIBLE);
            this.denyTxt.setVisibility(View.VISIBLE);
            lltAllowDenyBtn.setVisibility(View.VISIBLE);
            this.okTxt.setVisibility(View.GONE);
            this.allowTxt.setText(btnPositiveBtnTxt);
            this.denyTxt.setText(btnNegativeBtnTxt);
        } else {
            lltAllowDenyBtn.setVisibility(View.GONE);
            this.allowTxt.setVisibility(View.GONE);
            this.denyTxt.setVisibility(View.GONE);
            this.okTxt.setVisibility(View.VISIBLE);
            this.okTxt.setText(btnCofirmTxt);
        }

        initEvent(v);
        setCancelable(false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    private void initEvent(View v) {
        allowTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allowClickListener != null) {
                    allowClickListener.clicked();
                }
                dismiss();
            }
        });

        denyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (denyClickListener != null) {
                    denyClickListener.clicked();
                }
                dismiss();
            }
        });

        okTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okClickListener != null) {
                    okClickListener.clicked();
                }
                dismiss();
            }
        });
    }

    public void showDialog(FragmentManager fm, String title, String message, int customDialogImage, String btnCofirmTxt, btnOkClick okClickListener) {
        CustomDialog dialog = new CustomDialog(title, message, customDialogImage, btnCofirmTxt, okClickListener, false);
        dialog.show(fm, "");
    }

    public void showDialog(FragmentManager fm, String title, String message) {
        CustomDialog dialog = new CustomDialog(title, message, null);
        dialog.show(fm, "");
    }

    public void showDialog(FragmentManager fm, String title, String message, btnOkClick okClickListener) {
        CustomDialog dialog = new CustomDialog(title, message, okClickListener);
        dialog.show(fm, "");
    }

    public void showDialog(FragmentManager fm, String message, int customDialogImage, String btnPositiveBtnTxt, String btnNegativeBtnTxt, btnAllowClick allowClickListener, btnDenyClick denyClickListener) {
        CustomDialog dialog = new CustomDialog("SUCCESS",message, customDialogImage, btnPositiveBtnTxt, btnNegativeBtnTxt, allowClickListener, denyClickListener);
        dialog.show(fm, "");
    }

//    public void showDialog(FragmentManager fm, String message, boolean isError) {
//        if (dialog != null && dialog.getDialog() != null && dialog.getDialog().isShowing()) {
//            return;
//        }
//        if (isError) {
//            dialog = new CustomDialog("FAILURE",message, R.drawable.cross, "TRY AGAIN", null, isError);
//            dialog.show(fm, "");
//        } else {
//            dialog = new CustomDialog("SUCCESS",message, R.drawable.tick, getString(R.string.continue_text), null, isError);
//            dialog.show(fm, "");
//        }
//    }

//    public void showTCDialog(FragmentManager fm, String title, String message, String btnCofirmTxt, btnOkClick okClickListener) {
//        CustomDialog dialog = new CustomDialog(title, message, btnCofirmTxt, okClickListener);
//        dialog.show(fm, "");
//    }
}
