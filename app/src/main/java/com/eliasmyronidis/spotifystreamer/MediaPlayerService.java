package com.eliasmyronidis.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import java.io.IOException;

/**
 * Created by Elias Myronidis on 10/7/15.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private MediaPlayer mediaPlayer;
    private final IBinder musicBind = new MediaPlayerBinder();
    private String mTrackUrl;
    private boolean nextPressed;

    private int trackDuration;
    private Handler mHandler = new Handler();
    public int currentPosition;

    private Intent mIntent;

    public Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            currentPosition = mediaPlayer.getCurrentPosition();
            mIntent = new Intent("seekbar_progress");
            mIntent.putExtra("track_progress", currentPosition);
            sendSeekbarProgress(mIntent);

            mHandler.postDelayed(this,200);
        }
    };
    public static boolean nowPlaying;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * If the track is not the same with the track playing then we set the media player.
     * On screen orientation the track doesn't stop!
     *
     * @param trackUrl the track's url.
     */
    public void startMediaPlayer(String trackUrl) {
        if (!setTrackUrl(trackUrl)) {
            setMediaPlayer();
        }
    }


    /**
     * Checks if the track playing is the same or if there is no track.
     *
     * @param trackUrl the track's url.
     * @return false if it's not the same or true if it's the same.
     */
    public boolean setTrackUrl(String trackUrl) {
        if (mTrackUrl == null || !mTrackUrl.equals(trackUrl)) {
            mTrackUrl = trackUrl;
            return false;
        } else {
            return true;
        }
    }

    /**
     * Sets the mediaplayer.
     */
    public void setMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();

        setListeners();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(mTrackUrl);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            mediaPlayer.release();
        }
    }

    /**
     * Sets the mediaPlayer listeners.
     */
    public void setListeners() {
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        trackDuration = mediaPlayer.getDuration();

        mIntent = new Intent("media_started");
        mIntent.putExtra("trackDuration", trackDuration);
        sendMediaStartedBroadcast(mIntent);

        mediaPlayer.start();

        mHandler.postDelayed(updateProgress, 200);
        nowPlaying = true;
    }

    private void sendMediaStartedBroadcast(Intent mIntent){
        LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
    }

    private void sendSeekbarProgress(Intent mIntent){
        LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
    }

    private void sendMediaCompletedBroadcast(Intent mIntent){
        LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mHandler.removeCallbacks(updateProgress);

        //TODO check if we need seekTo and SetProgress
        mediaPlayer.seekTo(0);
        mIntent = new Intent("media_completed");
        sendMediaCompletedBroadcast(mIntent);
        nowPlaying = false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public class MediaPlayerBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    public void playPauseTrack() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else if (!mediaPlayer.isPlaying() && nextPressed != true) {
            mediaPlayer.start();
            mHandler.postDelayed(updateProgress,200);
        } else {
            nextPressed = false;
            startMediaPlayer(mTrackUrl);
        }
    }

    public void nextTrack(String trackUrl) {
        startMediaPlayer(trackUrl);
    }

    public void previousTrack(String trackUrl){
        startMediaPlayer(trackUrl);
    }

    public void seekToPosition(int currentPosition){
        mHandler.removeCallbacks(updateProgress);
        mediaPlayer.seekTo(currentPosition);
        mHandler.postDelayed(updateProgress, 200);
    }
}
