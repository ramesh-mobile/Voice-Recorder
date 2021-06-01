package com.avatar.voicerecorder;

import android.app.Activity;
import android.app.Application;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * Created by ramesh on 22-04-21
 */
public class RecorderApplication extends Application {

    public static RecorderApplication instance;

    public static Activity currActivityInstance;

    public static Fragment currFragmentInstance;



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static RecorderApplication getInstance(){return instance;}

    public static Activity getCurrActivityInstance(){return currActivityInstance;}

    public static Fragment getCurrFragmentnstance(){return currFragmentInstance;}

    public static void setCurrActivityInstance(Activity activityInstance){currActivityInstance = activityInstance;}

    public static void setCurrActivityInstance(FragmentActivity activityInstance){currActivityInstance = activityInstance;}

    public static void setCurrFragmentInstance(Fragment fragmentInstance){currFragmentInstance = fragmentInstance;}

}