package com.github.dmchoull.revuesample;

import android.app.Application;

import com.github.dmchoull.revue.Revue;
import com.github.dmchoull.revue.RevueConfig;
import com.squareup.leakcanary.LeakCanary;

public class SampleApplication extends Application {
    private final Revue revue = Revue.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        // Call init once in the application's onCreate so Revue can updated tracked data such as
        // times launched, etc.
        revue.init(this);

        // Optional: override default configuration values by setting your own instance of RevueConfig
        revue.setConfig(new RevueConfig(2));
    }
}
