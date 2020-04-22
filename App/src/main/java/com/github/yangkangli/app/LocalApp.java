package com.github.yangkangli.app;

import android.app.Application;

import com.github.yangkangli.logger.ALogger;
import com.github.yangkangli.logger.adapter.ConsoleAdapter;

public class LocalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ALogger.addLogAdapter(new ConsoleAdapter.Builder().build());
    }
}
