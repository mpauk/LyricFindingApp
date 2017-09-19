package com.example.mpauk.spotifygenius.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mpauk on 6/17/2017.
 */

public class SpotifyReceiver extends BroadcastReceiver {
    private String artistName;
    private String trackName;
    static final class BroadcastTypes {
        static final String SPOTIFY_PACKAGE = "com.spotify.music";
        static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        long timeSentInMs = intent.getLongExtra("timeSent",0L);
        String action = intent.getAction();
        if(action.equals(BroadcastTypes.METADATA_CHANGED)){
            if(intent.getStringExtra("artist")!=null){
                FloatingIcon.previousArtist = FloatingIcon.artist;
                FloatingIcon.artist = intent.getStringExtra("artist");
            }

            if(intent.getStringExtra("track")!=null) {
                FloatingIcon.previousTrack = FloatingIcon.track;
                FloatingIcon.track = intent.getStringExtra("track");
            }
            Log.d("artist name", FloatingIcon.artist);
            Log.d("track name", FloatingIcon.track);
        }
    }
}
