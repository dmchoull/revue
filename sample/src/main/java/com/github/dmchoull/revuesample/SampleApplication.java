package com.github.dmchoull.revuesample;

import android.app.Application;
import com.github.dmchoull.revue.Revue;

public class SampleApplication extends Application {
    private final Revue revue = new Revue();

    public Revue getRevue() {
        return revue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        revue.init(this);
    }
}
