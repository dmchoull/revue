package com.github.dmchoull.revuesample;

import android.app.Application;
import com.github.dmchoull.revue.Revue;
import com.github.dmchoull.revue.RevueConfig;

public class SampleApplication extends Application {
    private final Revue revue = new Revue();

    public Revue getRevue() {
        return revue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        revue.init(this);
        revue.setConfig(new RevueConfig(2));
    }
}
