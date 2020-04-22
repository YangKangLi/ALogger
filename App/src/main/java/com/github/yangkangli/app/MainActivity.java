package com.github.yangkangli.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.yangkangli.logger.ALogger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ALogger.d("MyTag", "aaa");

        ALogger.d(new Exception("aaa"));
    }
}
