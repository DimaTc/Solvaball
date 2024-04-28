package com.dima.solvaball;

import android.app.Activity;
import android.content.Context;

import com.dima.solvaball.handlers.GoogleAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import de.golfgl.gdxgamesvcs.GpgsClient;


public class GoogleBridge implements GoogleAdapter {
    private static final String TAG = "GOOGLE ADAPTER";
    private GpgsClient client;
    private Context context;
    private GoogleSignInAccount signInAccount;
    private Activity activity;

    public GoogleBridge(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public GoogleBridge(GpgsClient client) {
        this.client = client;
    }

    @Override
    public boolean checkLoggedIn() {
        return false;
    }

    @Override
    public boolean signIn() {
//        GoogleSignInOptions options = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
//        GoogleSignInClient client = GoogleSignIn.getClient(context, options);
//        signInSilently();
        client.logIn();
        return false;   //placeholder for now
    }

}

