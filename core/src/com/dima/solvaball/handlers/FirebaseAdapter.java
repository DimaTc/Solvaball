package com.dima.solvaball.handlers;

public interface FirebaseAdapter {

    void logSkippedLevel(int level, int retries, int lostCount);

    void lostLevel(int level);

    void wonLevel(int level, int retries, int lostCount);

    void retryLevel(int level, int count);
}
