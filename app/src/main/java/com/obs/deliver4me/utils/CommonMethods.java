package com.obs.deliver4me.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.obs.deliver4me.configs.Constants;

public class CommonMethods {
    public ProgressDialog getProgressDialog(Context context) {
        return new ProgressDialog(context);
    }
    public static ProgressDialog dialog;
    public static CommonMethods commonMethodsHandler = null;

    public static CommonMethods defaultHandler() {
        if (commonMethodsHandler == null) {
            commonMethodsHandler = new CommonMethods();
        }
        return commonMethodsHandler;
    }

    //Show Loader
    public void showLoader(ProgressDialog pd) {
        pd.setMessage("Please wait. Loading...");
        pd.setCancelable(false);
        pd.show();
    }

    //Show Loader
    public void showInitialSyncLoader(ProgressDialog pd) {
        pd.setMessage("Please wait. Initial Sync is in progress");
        pd.setCancelable(false);
        pd.show();
    }

    // Show Progress Dialog
//    public void showProgressDialog(Context context) {
////    if (dialog == null) {
//        dialog = new ProgressDialog(context, R.style.share_dialog);
//        dialog.show();
//        dialog.setContentView(R.layout.dialog_progress);
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
////    } else if (dialog.isShowing()) {
////      dialog.dismiss();
////      dialog = null;
////      showProgressDialog(context);
////    }
//    }

    // Hide Progress Dialog
    public void hideProgressDialog(Context context) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


    //Dismiss Loader
    public void dismissLoader(ProgressDialog pd) {
        pd.dismiss();
    }

    //Hide Keypad
    public void hideKeypadFragment(Context context) {
        try {
            ((AppCompatActivity) context).getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Log.e(Constants.LOGGER_TAG, Constants.LOGGER_ERROR, e);
        }
    }

    //hide keypad
    public static void hide_softkeyboard(Context mcontext) {
        try {
            // keyboard hide if view is visible
            View view = ((Activity) mcontext).getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) mcontext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

