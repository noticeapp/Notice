package com.example.myapp;

import android.app.Application;

import androidx.annotation.NonNull;

import com.facebook.react.ReactApplication;
import com.swmansion.rnscreens.RNScreensPackage;
import com.swmansion.gesturehandler.react.RNGestureHandlerPackage;
import com.swmansion.reanimated.ReanimatedPackage;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.horcrux.svg.SvgPackage;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
    private static final String JS_BUNDLE_NAME = "index.bundle";
    private static final String JS_MAIN_MODULE_NAME = "index";

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.USE_DEVELOPER_SUPPORT;
        }

        @NonNull
        @Override
        protected String getJSMainModuleName() {
            return JS_MAIN_MODULE_NAME;
        }

        /**
         * Returns the name of the bundle in assets.
         */
        @NonNull
        @Override
        protected String getBundleAssetName() {
            return JS_BUNDLE_NAME;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new RNScreensPackage(),
                    new RNGestureHandlerPackage(),
                    new ReanimatedPackage(),
                    new SvgPackage(),
                    new NavigateProversionPackage()
            );
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
    }
}
