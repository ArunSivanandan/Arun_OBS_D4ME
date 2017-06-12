package com.obs.deliver4me.configs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.obs.deliver4me.application.AppController;

import java.util.ArrayList;

import javax.inject.Inject;


public class RunTimePermission {

    @Inject
    Context context;
    @Inject
    ArrayList<String> permissionList;
    private SharedPreferences preferences;

    public RunTimePermission() {
        AppController.getAppComponent().inject(this);
        preferences = context.getSharedPreferences("deliver4me_permission", Context.MODE_PRIVATE);
    }

    private boolean isFirstTimePermission() {
        return preferences.getBoolean("isFirstTimePermission", false);
    }

    public ArrayList<String> checkHasPermission(Activity context, String[] permissions) {
        permissionList.clear();
        if (isMarshmallow() && context != null && permissions != null) {
            for (String permission : permissions) {
                if ((ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)) {
                    permissionList.add(permission);
                }
            }
        }
        return permissionList;
    }

    public boolean isPermissionBlocked(Activity context, String[] permissions) {
        if (isMarshmallow() && context != null && permissions != null && isFirstTimePermission()) {
            for (String permission : permissions) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<String> onRequestPermissionsResult(String[] permissions, int[] grantResults) {
        permissionList.clear();
        if (grantResults != null && grantResults.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i]);
                }
            }
        }
        return permissionList;
    }

    private boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
