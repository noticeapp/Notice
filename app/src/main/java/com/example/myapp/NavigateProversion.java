package com.example.myapp;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;

public class NavigateProversion extends ReactContextBaseJavaModule {
    NavigateProversion(ReactApplicationContext reactContext) {
        super(reactContext);
    }


    @Override
    public String getName() {
        return "NavigateToPro";
    }

    @ReactMethod
    void navigateToExample() {
        Activity activity = getCurrentActivity();
        if(activity != null){
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        }

    }
}
