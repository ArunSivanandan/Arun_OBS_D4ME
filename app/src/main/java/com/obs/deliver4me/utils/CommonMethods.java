package com.obs.deliver4me.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.obs.deliver4me.configs.Constants;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CommonMethods {
    public ProgressDialog getProgressDialog(Context context) {
        return new ProgressDialog(context);
    }
    public static ProgressDialog dialog;
    public static CommonMethods commonMethodsHandler = null;

    //Show Loader
    public void showLoader(ProgressDialog pd) {
        if (pd != null && !pd.isShowing()) {
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }
    }

    //Dismiss Loader
    public void dismissLoader(ProgressDialog pd) {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    public File uniqueCameraFileName() {
        String uniqueId = new BigInteger(290, new SecureRandom()).toString(32);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < uniqueId.length(); i++) {
            if (Character.isLetter(uniqueId.charAt(i))) {
                stringBuilder.append(Character.toUpperCase(uniqueId.charAt(i)));
            } else if (Character.isDigit(uniqueId.charAt(i))) {
                stringBuilder.append(Character.toUpperCase(uniqueId.charAt(i)));
            }
            switch (i) {
                case 8:
                    stringBuilder.deleteCharAt(i);
                    stringBuilder.append('-');
                    break;
                case 13:
                    stringBuilder.deleteCharAt(i);
                    stringBuilder.append('-');
                    break;
                case 18:
                    stringBuilder.deleteCharAt(i);
                    stringBuilder.append('-');
                    break;
                case 23:
                    stringBuilder.deleteCharAt(i);
                    stringBuilder.append('-');
                    break;
                case 36:
                    stringBuilder.deleteCharAt(i);
                    stringBuilder.append('-');
                    break;
                case 41:
                    stringBuilder.deleteCharAt(i);
                    stringBuilder.append('-');
                    break;
            }
        }
        uniqueId = stringBuilder.toString();
        return new File(getDefaultCameraPath(), uniqueId + ".png");
    }

    public String getDefaultCameraPath() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (path.exists()) {
            File test1 = new File(path, "Camera/");
            if (test1.exists()) {
                path = test1;
            } else {
                File test2 = new File(path, "100MEDIA/");
                if (test2.exists()) {
                    path = test2;
                } else {
                    File test3 = new File(path, "100ANDRO/");
                    if (test3.exists()) {
                        path = test3;
                    } else {
                        test1.mkdirs();
                        path = test1;
                    }
                }
            }
        } else {
            path = new File(path, "Camera/");
            path.mkdirs();
        }
        return path.getPath();
    }

    public void refreshGallery(Context context, File file) {
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file); //out is your file you saved/deleted/moved/copied
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////// CAMERA ///////////////////////////////////
    public File getDefaultFileName(Context context) {
        File imageFile = null;
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent) { // External storage path
            imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FILE_NAME + System.currentTimeMillis() + ".png");
        } else {  // Internal storage path
            imageFile = new File(context.getFilesDir() + File.separator + Constants.FILE_NAME + System.currentTimeMillis() + ".png");
        }
        return imageFile;
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

    public void getKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.obs.deliver4me", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}

