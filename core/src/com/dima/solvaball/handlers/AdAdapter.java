package com.dima.solvaball.handlers;

public interface AdAdapter {

    RewardCallback getRewardCallback();

    void setRewardCallback(RewardCallback callback);

    void showRewardAd();

    void showBannerAd();

    void hideBannerAd();

    void loadRewardAd();

    void showNoAds();

    void showError(String s);
}
