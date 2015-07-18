package com.eliasmyronidis.spotifystreamer;

import java.util.concurrent.TimeUnit;

/**
 * Created by Elias Myronidis on 12/7/15.
 */
public class Utility {


    public static String getTimeFormated(int trackLength){

        String time;

        time = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long)trackLength),
                TimeUnit.MILLISECONDS.toSeconds((long)trackLength) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)trackLength)));
        return time;
    }
}

