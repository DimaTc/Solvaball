package com.dima.solvaball;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.firebase.analytics.FirebaseAnalytics;

import de.golfgl.gdxgamesvcs.GpgsClient;

public class AndroidLauncher extends AndroidApplication {


    private GpgsClient client;
    private AdManager adManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        RequestConfiguration.Builder confBuilder = new RequestConfiguration.Builder();
        RequestConfiguration conf = confBuilder.
                setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).build();
        MobileAds.setRequestConfiguration(conf);
        MobileAds.initialize(this);
        AdView adView = new AdView(this);
        client = new GpgsClient();
        client.initialize(this, false);
        GameBridge gameBridge = new GameBridge(this);
        adManager = new AdManager(this, adView);
        gameBridge.setGoogleAdapter(new GoogleBridge(client));
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseBridge firebaseBridge = new FirebaseBridge(mFirebaseAnalytics);
        gameBridge.setFirebaseAdapter(firebaseBridge);
        gameBridge.setAdAdapter(adManager);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        SolvaBallGame game = new SolvaBallGame(gameBridge);

//        initialize(game, config);

        //for ads
        RelativeLayout layout = new RelativeLayout(this);

        //Manual things that libgdx do automatically
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        //
        View gameView = initializeForView(game, config);
        layout.addView(gameView);

        //
        adView.setAdSize(AdSize.BANNER);
        adView.setVisibility(View.INVISIBLE);
        adView.setAdUnitId(getString(R.string.banner_ad));
//        adView.setAdUnitId(getString(R.string.banner_ad_debug));
        AdRequest.Builder builder = new AdRequest.Builder();
        AdRequest adRequest = builder.build();
        adView.loadAd(adRequest);
        adManager.loadRewardAd();
        //
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //
        layout.addView(adView, params);
        setContentView(layout);
        adjustViewForGame();


    }

    @Override
    protected void onResume() {
        super.onResume();
        adjustViewForGame();
        adManager.onResume();
    }

    private void adjustViewForGame() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY  // this flag do=Semi-transparent bars temporarily appear and then hide again
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  // Make Content Appear Behind the status  Bar
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  // it Make Content Appear Behind the Navigation Bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN  // hide status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    @Override
    protected void onPause() {
        adManager.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        adManager.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        client.onGpgsActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001)
            Toast.makeText(this, "Did work? - " + client.isSessionActive(), Toast.LENGTH_LONG).show();

//        if (requestCode == 9001) {
//            GoogleSignInResult res = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (res.isSuccess()) {
//                Log.d("ASD", "onActivityResult: " + res.getSignInAccount());
//            } else {
//                Log.e("Asd", "onActivityResult: " + res.getStatus());
//            }
//        }
    }


}
