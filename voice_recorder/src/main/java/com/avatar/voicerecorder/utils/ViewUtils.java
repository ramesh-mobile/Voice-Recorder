package com.avatar.voicerecorder.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.avatar.voicerecorder.BuildConfig;
import com.avatar.voicerecorder.R;
import com.avatar.voicerecorder.RecorderApplication;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

/**
 * Created by ramesh on 26-08-20
 */


public class ViewUtils {

    private static final String TAG = "ViewUtils";

    public static void print(String TAG, String msg) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, msg);
    }

    public static void snackForPermission(Context context,View view) {
        final Snackbar mySnackbar = Snackbar.make(view, "Please enable app permissions.", Snackbar.LENGTH_LONG);
        mySnackbar.setAction("SETTING", (viewLocal) -> {
            mySnackbar.dismiss();
            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myAppSettings);

        });
        mySnackbar.show();

    }

    public static void requestionPermission(Activity activity, String[] requestedPerm) {
        print(TAG, "requestionPermission: "+requestedPerm);
        ActivityCompat.requestPermissions(activity, requestedPerm, Config.PERMISSION_REQUEST_CODE);
    }

    public static AlertDialog showOkAlertBox(Context context, String title, String msg) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Ok", (dialogInterface, which) -> dialogInterface.dismiss()).create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog.Builder showConfirmationBox(Context context, String title, String msg) {
        return (new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg));
    }

}