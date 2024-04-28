package com.dima.solvaball.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesData {
    private static final String PREF_NAME = "e5518b31";
    private static final String LAST_LEVEL = "79f637f0";
    private static final String SOUND = "be92047f";
    private static PreferencesData instance;
    private Preferences preferences;

    private PreferencesData() {
        preferences = Gdx.app.getPreferences(PREF_NAME);
    }

    public static PreferencesData getInstance() {
        if (instance == null)
            instance = new PreferencesData();
        return instance;
    }

    public void saveLastLevel(int level) {
        preferences.putInteger(LAST_LEVEL, level);
        preferences.flush();
    }

    public int getLastLevel() {
        return preferences.getInteger(LAST_LEVEL, 1);
    }

    public void saveSoundStatus(boolean enabled) {
        preferences.putBoolean(SOUND, enabled);
        preferences.flush();
    }

    public boolean getSoundStatus() {
        return preferences.getBoolean(SOUND, true);
    }
}
