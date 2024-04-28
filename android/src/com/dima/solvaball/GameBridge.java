package com.dima.solvaball;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.dima.solvaball.handlers.AdAdapter;
import com.dima.solvaball.handlers.AppAdapter;
import com.dima.solvaball.handlers.FirebaseAdapter;
import com.dima.solvaball.handlers.GoogleAdapter;

public class GameBridge implements AppAdapter {

    private Context context;
    private GoogleAdapter googleAdapter;
    private FirebaseAdapter firebaseAdapter;
    private AdAdapter adAdapter;

    public GameBridge(Context context) {
        this.context = context;
    }

    @Override
    public void openPrivacy() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources()
                    .getString(R.string.privacy_url)));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Please install a web browser to view the site",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public GoogleAdapter getGoogleAdapter() {
        return googleAdapter;
    }

    public void setGoogleAdapter(GoogleAdapter adapter) {
        this.googleAdapter = adapter;
    }

    @Override
    public FirebaseAdapter getFirebaseAdapter() {
        return firebaseAdapter;
    }

    public void setFirebaseAdapter(FirebaseAdapter firebaseAdapter) {
        this.firebaseAdapter = firebaseAdapter;
    }

    @Override
    public AdAdapter getAdAdapter() {
        return adAdapter;
    }

    @Override
    public void openStorePage() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getResources()
                    .getString(R.string.game_url)));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Error, You don't have Play Store installed",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void setAdAdapter(AdAdapter adAdapter) {
        this.adAdapter = adAdapter;
    }
}
