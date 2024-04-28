package com.dima.solvaball.handlers;

public interface AppAdapter {

    void openPrivacy();

    GoogleAdapter getGoogleAdapter();

    FirebaseAdapter getFirebaseAdapter();

    AdAdapter getAdAdapter();

    void openStorePage();
}
