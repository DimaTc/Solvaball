package com.dima.solvaball.logic;

import com.dima.solvaball.handlers.AdAdapter;
import com.dima.solvaball.handlers.AppAdapter;
import com.dima.solvaball.handlers.FirebaseAdapter;
import com.dima.solvaball.handlers.GoogleAdapter;

public class PlatformLogic {
    private static final PlatformLogic ourInstance = new PlatformLogic();
    private AppAdapter adapter;

    private PlatformLogic() {
    }

    public static PlatformLogic getInstance() {
        return ourInstance;
    }

    public void setGameAdapter(AppAdapter adapter) {
        this.adapter = adapter;
    }

    public void showPrivacyPolicy() {
        adapter.openPrivacy();
    }

    public GoogleAdapter getGoogleBridge() {
        if (adapter != null)
            return adapter.getGoogleAdapter();
        return null;
    }

    public FirebaseAdapter getFirebaseBridge() {
        if (adapter != null)
            return adapter.getFirebaseAdapter();
        return null;
    }

    public AdAdapter getAdAdapter() {
        return adapter.getAdAdapter();
    }

    public void showStorePage() {
        adapter.openStorePage();
    }
}
