package com.dima.solvaball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.dima.solvaball.handlers.AdAdapter;
import com.dima.solvaball.handlers.RewardCallback;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;

public class AdManager implements AdAdapter {
    private static final int SHOW_BANNER_AD = 1;
    private static final int SHOW_REWARD_AD = 2;
    private static final int SHOW_NO_ADS = 3;
    private static final int HIDE_BANNER_AD = 0;
    private static final int LAST_LEVEL = 0;
    private static final int ERROR_MESSAGE = 4;
    private RewardedVideoAd rewardedVideoAd;
    private RewardCallback callback;
    private Context context;
    private AdView adView;
    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_BANNER_AD: {
                    adView.setVisibility(View.VISIBLE);
                    break;
                }
                case HIDE_BANNER_AD: {
                    adView.setVisibility(View.GONE);
                    break;
                }
                case SHOW_REWARD_AD:
                    if (rewardedVideoAd.isLoaded())
                        rewardedVideoAd.show();
                    else
                        Toast.makeText(context, "No Ads available", Toast.LENGTH_LONG).show();
                    break;
                case SHOW_NO_ADS:
                    Toast.makeText(context, "There no ads currently available", Toast.LENGTH_LONG).show();
                    break;
                case ERROR_MESSAGE:
                    if (msg.arg1 == LAST_LEVEL)
                        Toast.makeText(context, "You're at the last level", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };

    public AdManager(Context context, AdView adView) {
        this.context = context;
        this.adView = adView;
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        RewardManager rewardManager = new RewardManager(this, rewardedVideoAd);
        rewardedVideoAd.setRewardedVideoAdListener(rewardManager);

    }

    @Override
    public void loadRewardAd() {
        AdRequest.Builder builder = new AdRequest.Builder();
        rewardedVideoAd.loadAd(context.getString(R.string.reward_ad),
                builder.build());
//        rewardedVideoAd.loadAd(context.getString(R.string.reward_ad_debug),
//                builder.build());
    }

    @Override
    public void showNoAds() {
        handler.sendEmptyMessage(SHOW_NO_ADS);
    }

    @Override
    public void showError(String s) {
        Message msg = new Message();
        msg.what = ERROR_MESSAGE;
        msg.arg1 = LAST_LEVEL;
        handler.sendMessage(msg);
    }

    @Override
    public RewardCallback getRewardCallback() {
        return callback;
    }

    @Override
    public void setRewardCallback(RewardCallback callback) {
        this.callback = callback;
    }

    @Override
    public void showRewardAd() {
        handler.sendEmptyMessage(SHOW_REWARD_AD);

    }

    @Override
    public void showBannerAd() {
        handler.sendEmptyMessage(SHOW_BANNER_AD);
    }

    @Override
    public void hideBannerAd() {
        handler.sendEmptyMessage(HIDE_BANNER_AD);

    }

    public void onResume() {
        rewardedVideoAd.resume(context);
    }

    public void onPause() {
        rewardedVideoAd.pause(context);
    }

    public void onDestroy() {
        rewardedVideoAd.destroy(context);
    }


}
