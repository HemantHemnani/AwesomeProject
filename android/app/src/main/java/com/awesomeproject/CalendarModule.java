package com.awesomeproject;

import android.content.Intent;
import android.util.Log;
import java.util.Map;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.common.MapBuilder;

public class CalendarModule extends ReactContextBaseJavaModule {
    private final String TAG = CalendarModule.class.getSimpleName();


     CalendarModule(ReactApplicationContext context) {
        super(context);
    }

    // add to CalendarModule.java
    @Override
    public String getName() {
        return "CalendarModule";
    }


//    @Override
//    public Map getExportedCustomDirectEventTypeConstants() {
//        return MapBuilder.of("onLocationChange", MapBuilder.of("registrationName", "onLocationChange"));
//    }

    @ReactMethod
    public void createCalendarEvent(String name, String location) {
        Log.d(TAG   , "CCCCCCCreate event called with name: " + name
                + " and location: " + location);
    }


    @ReactMethod
    public void showMap(String showMap) {

         Log.d(TAG   , "CCCCCShowMapp>>>>>"+showMap);

    }

    @ReactMethod
    void navigateToExample() {
        ReactApplicationContext context = getReactApplicationContext();
        Log.e(TAG   , "CCCCCCCnavigateToExample>>>>>navigateToExample");
        Intent intent = new Intent(context, TestKotlinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}