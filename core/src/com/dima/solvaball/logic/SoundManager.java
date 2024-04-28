package com.dima.solvaball.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

public class SoundManager {
    private static SoundManager instance;
    private ArrayList<Sound> sounds;
    private boolean active = true;

    private SoundManager() {
        sounds = new ArrayList<>();
        active = PreferencesData.getInstance().getSoundStatus();
        init();
    }

    public static SoundManager getInstance() {
        if (instance == null)
            instance = new SoundManager();
        return instance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        PreferencesData.getInstance().saveSoundStatus(active);
    }

    public void init() {
        try {
            for (int i = 0; i < Sounds.values().length; i++) {
                String soundFileName = Sounds.values()[i] + ".mp3";
                sounds.add(Gdx.audio.newSound(Gdx.files.internal("sounds/" + soundFileName)));
            }
        } catch (Exception e) {
            Gdx.app.error("Sound", "Could not load sounds");
        }
    }

    public void playSound(Sounds sound) {
        if (active)
            sounds.get(sound.ordinal()).play(1f);
    }

    public void dispose() {
        instance = null;
        for (Sound sound : sounds)
            sound.dispose();
    }

    public void toggleActive() {
        setActive(!active);
    }

    public enum Sounds {
        TILE,
        POWER_TILE,
        SAND_TILE,
        CLICK,
        ICE_TILE,
        BALL_CLICK
    }
}
