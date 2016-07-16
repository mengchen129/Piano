package com.example.mengchen.piano.utils;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class SysApplication extends Application {

    private List<Activity> mList = new LinkedList<>();
    private static SysApplication instance = new SysApplication();

    private SysApplication() {}

    public synchronized static SysApplication getInstance() {
        return instance;
    }
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
