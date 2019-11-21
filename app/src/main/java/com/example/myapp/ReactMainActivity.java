package com.example.myapp;

import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView;

public class ReactMainActivity extends ReactActivity {
    @Override
    protected String getMainComponentName() {
        return "Beginner";
    }

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this, getMainComponentName()) {
            @Override
            protected ReactRootView createRootView() {
                return new RNGestureHandlerEnabledRootView(ReactMainActivity.this);
            }

            @Override
            protected Bundle getLaunchOptions() {
                Bundle launchOptions = new Bundle();
                launchOptions.putString("buildType", BuildConfig.BUILD_TYPE);
                return launchOptions;
            }
        };
    }

//    @Override
//    protected ReactActivityDelegate createReactActivityDelegate() {
//        return new ReactActivityDelegate(this, getMainComponentName()) {
//            @Override
//            protected Bundle getLaunchOptions() {
//                Bundle launchOptions = new Bundle();
//                launchOptions.putString("buildType", BuildConfig.BUILD_TYPE);
//                return launchOptions;
//            }
//        };
//    }
}

