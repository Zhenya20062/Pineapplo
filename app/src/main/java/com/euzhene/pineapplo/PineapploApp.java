package com.euzhene.pineapplo;

import android.app.Application;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public class PineapploApp extends Application {
    public static final String TAG = "PresentationLayer";
    public MediaPlayer musicPlayer;


    @Override
    public void onCreate() {
        super.onCreate();
        File musicFile = new File(getCacheDir() + "/music");
        musicPlayer = MediaPlayer.create(this, Uri.fromFile(musicFile));
        musicPlayer.setLooping(true);
    }

    public void pausePlayer() {
        Log.d(TAG, "pausePlayer:");
        if (musicPlayer != null) musicPlayer.pause();
    }
}
