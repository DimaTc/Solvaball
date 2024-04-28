package com.dima.solvaball.handlers;

public interface RewardCallback {

    void onRewardCompleted();

    void onRewardFailed();

    void rewardIsLoaded(boolean loaded);
}
