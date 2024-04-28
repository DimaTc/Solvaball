package com.dima.solvaball.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;

public class FontGenerator {
    private static FontGenerator instance;
    private FreeTypeFontGenerator generator;

    private FreeTypeFontGenerator.FreeTypeFontParameter params;
    private HashMap<Integer, BitmapFont> cache;

    public FontGenerator() {
        cache = new HashMap<>();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arialbd.ttf"));
        params = new FreeTypeFontGenerator.FreeTypeFontParameter();
    }

    public static FontGenerator getInstance() {
        if (instance == null)
            instance = new FontGenerator();
        return instance;
    }

    public BitmapFont getFontBySize(int size) {
        BitmapFont cacheItem = cache.get(size);
        if (cacheItem == null) {
            params.size = size;
            cacheItem = generator.generateFont(params);
            cache.put(size, cacheItem);
        }
        return cacheItem;
    }

    public void dispose() {
        generator.dispose();
        instance = null;
    }

}
