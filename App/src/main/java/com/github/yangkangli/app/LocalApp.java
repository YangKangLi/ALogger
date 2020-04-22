package com.github.yangkangli.app;

import android.app.Application;
import android.util.Log;

import com.github.yangkangli.logger.ALogger;
import com.github.yangkangli.logger.adapter.ConsoleAdapter;
import com.github.yangkangli.logger.adapter.DiskAdapter;
import com.github.yangkangli.logger.strategy.DefaultLogStrategy;

import java.io.File;

public class LocalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 在这里初始化ALogger
        ALogger.setLogStrategy(new DefaultLogStrategy.Builder().setBordermaxLength(100).setShowStackTrace(true).build());

        File logsPath = getExternalFilesDir("Logs");
        Log.d("Adam", "externalFilesDir : " + logsPath);
        ALogger.addLogAdapter(new ConsoleAdapter.Builder().build(), new DiskAdapter.Builder().setLogFilePath(logsPath.getAbsolutePath()).build());
    }
}
