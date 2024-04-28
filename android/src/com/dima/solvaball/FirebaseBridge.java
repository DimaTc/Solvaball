package com.dima.solvaball;

import android.os.Bundle;

import com.dima.solvaball.handlers.FirebaseAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseBridge implements FirebaseAdapter {

    private FirebaseAnalytics analytics;

    public FirebaseBridge(FirebaseAnalytics analytics) {
        this.analytics = analytics;
    }

    @Override
    public void logSkippedLevel(int level, int retries, int lostCount) {
        Bundle bundle = new Bundle();
        bundle.putString("level", "level_" + level);
        bundle.putInt("retries", retries);
        bundle.putInt("lost_count", lostCount);
        analytics.logEvent("skip_level", bundle);
    }

    @Override
    public void lostLevel(int level) {
        Bundle bundle = new Bundle();
        bundle.putString("level", "level_" + level);
        analytics.logEvent("lost_level", bundle);

    }

    @Override
    public void wonLevel(int level, int retries, int lostCount) {
        Bundle bundle = new Bundle();
        bundle.putString("level", "level_" + level);
        bundle.putInt("retries", retries);
        bundle.putInt("lost_count", lostCount);
        analytics.logEvent("won_level", bundle);

    }

    @Override
    public void retryLevel(int level, int count) {
        Bundle bundle = new Bundle();
        bundle.putString("level", "level_" + level);
        bundle.putInt("retries", count);
        analytics.logEvent("retry_level", bundle);

    }
}
