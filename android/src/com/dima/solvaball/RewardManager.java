package com.dima.solvaball;

import android.util.Log;

import com.dima.solvaball.handlers.AdAdapter;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class RewardManager implements RewardedVideoAdListener {

    private final static String TAG = "REWARD MANAGER";
    private AdAdapter adAdapter;
    private RewardedVideoAd rewardedVideoAd;

    public RewardManager(AdAdapter adAdapter, RewardedVideoAd rewardedVideoAd) {
        this.adAdapter = adAdapter;
        this.rewardedVideoAd = rewardedVideoAd;
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        if (adAdapter.getRewardCallback() != null)
            adAdapter.getRewardCallback().rewardIsLoaded(true);

    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.d(TAG, "onRewardedVideoAdOpened: ");
    }

    @Override
    public void onRewardedVideoStarted() {
        if (adAdapter.getRewardCallback() != null)
            adAdapter.getRewardCallback().rewardIsLoaded(false);

        Log.d(TAG, "onRewardedVideoStarted: ");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.d(TAG, "onRewardedVideoAdClosed: ");
        adAdapter.loadRewardAd();

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        if (adAdapter.getRewardCallback() != null)
            adAdapter.getRewardCallback().onRewardCompleted();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.d(TAG, "onRewardedVideoAdLeftApplication: ");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.d(TAG, "onRewardedVideoAdFailedToLoad: ");
        if (adAdapter.getRewardCallback() != null)
            adAdapter.getRewardCallback().onRewardFailed();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.d(TAG, "onRewardedVideoCompleted: ");

    }
}
