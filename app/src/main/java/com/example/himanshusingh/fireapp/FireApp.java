package com.example.himanshusingh.fireapp;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Himanshu Singh on 07-Feb-17.
 */

public class FireApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

    }
}
