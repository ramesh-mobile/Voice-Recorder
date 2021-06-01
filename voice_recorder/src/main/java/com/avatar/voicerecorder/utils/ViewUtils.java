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

    static Context context = RecorderApplication.instance;

    static Activity currActivity = RecorderApplication.getCurrActivityInstance();

    public static ProgressDialog progressDialog;

    public static void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void e(String TAG, String msg, Throwable e) {
        Log.e(TAG, msg, e);
    }

    public static void e(String TAG, String msg) {
        Log.e(TAG, msg);
    }

    public static void error(String TAG, String msg) {
        Log.e(TAG, msg);
    }

    public static void v(String TAG, String msg) {
        Log.v(TAG, msg);
    }

    public static void d(String TAG, Object msg) {
        Log.d(TAG, msg + "");
    }

    public static void i(String TAG, String msg) {
        Log.i(TAG, msg);
    }

    public static void s(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    public static void livePrint(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    public static void print(String TAG, String msg) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, msg);
    }



    public static void snack(Activity activity, String msg) {
        try {
            print(TAG, msg + " curr act:" + currActivity);
            View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                print(TAG, msg + "curr act in error");
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            } catch (Exception e1) {
            }
        }
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
        //context = AvatarApplication.getCurrActivityInstance();
        return (new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg));
    }

    public static ProgressDialog showProgressDialog(Activity activity, String msg) {
        dismissProgressDialog();
        progressDialog = new ProgressDialog(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(msg);
        //progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static void setSupportActionBar(AppCompatActivity context, Toolbar toolbar) {
        context.setSupportActionBar(toolbar);
        if (context.getSupportActionBar() != null) {
            context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            context.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public static String intentToString(Intent intent) {
        if (intent == null) {
            return null;
        }

        return intent.toString() + " " + bundleToStringWithNewLine(intent.getExtras());
    }

    public static String bundleToString(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", ");
                }

                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToString((Bundle) value));
                } else {
                    out.append(value);
                }

                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }

    public static String bundleToStringWithNewLine(Bundle bundle) {
        StringBuilder out = new StringBuilder("Bundle[");

        if (bundle == null) {
            out.append("null");
        } else {
            boolean first = true;
            for (String key : bundle.keySet()) {
                if (!first) {
                    out.append(", \n");
                }
                out.append(key).append('=');

                Object value = bundle.get(key);

                if (value instanceof int[]) {
                    out.append(Arrays.toString((int[]) value));
                } else if (value instanceof byte[]) {
                    out.append(Arrays.toString((byte[]) value));
                } else if (value instanceof boolean[]) {
                    out.append(Arrays.toString((boolean[]) value));
                } else if (value instanceof short[]) {
                    out.append(Arrays.toString((short[]) value));
                } else if (value instanceof long[]) {
                    out.append(Arrays.toString((long[]) value));
                } else if (value instanceof float[]) {
                    out.append(Arrays.toString((float[]) value));
                } else if (value instanceof double[]) {
                    out.append(Arrays.toString((double[]) value));
                } else if (value instanceof String[]) {
                    out.append(Arrays.toString((String[]) value));
                } else if (value instanceof CharSequence[]) {
                    out.append(Arrays.toString((CharSequence[]) value));
                } else if (value instanceof Parcelable[]) {
                    out.append(Arrays.toString((Parcelable[]) value));
                } else if (value instanceof Bundle) {
                    out.append(bundleToStringWithNewLine((Bundle) value));
                } else {
                    out.append(value);
                }
                //out.append("\n");
                first = false;
            }
        }

        out.append("]");
        return out.toString();
    }

    public void bundleKeyValuePairPrint(Bundle bundle) {
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            print("LocalBroadcastManager", key.toString() + "" + value.toString());
        }
    }

    public static String printArguments(Object... argArr) {

        //Here I need to get access to arg1, arg2, arg3
        StringBuilder out = new StringBuilder("Arguments[");
        int i = 0;
        for (Object o : argArr) {
            if(o == null ) continue;
            if (i++ == 0)
                out.append(o.toString());
            else
                out.append(" , " + o.toString());
        }
        i = 0;
        print(TAG, "printArguments:" + out + "]");
        return out+"";
    }

}