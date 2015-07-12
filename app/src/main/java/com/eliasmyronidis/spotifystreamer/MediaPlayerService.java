package com.eliasmyronidis.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Elias Myronidis on 10/7/15.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private MediaPlayer mediaPlayer;
    private final IBinder musicBind = new MediaPlayerBinder();
    private ImageButton playPauseButton;
    private String mTrackUrl;
    private boolean nextPressed;
    private SeekBar seekbar;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private int trackDuration;
    private Handler mHandler = new Handler();


    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            int currentPosition = mediaPlayer.getCurrentPosition();
            seekbar.setProgress(currentPosition);
            startTimeTextView.setText(Utility.getTimeFormated(currentPosition));
            mHandler.postDelayed(this,1000);
        }
    };


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
        playPauseButton.setImageResource(android.R.drawable.ic_media_pause);

        trackDuration = mediaPlayer.getDuration();
        seekbar.setMax(trackDuration);

        endTimeTextView.setText(Utility.getTimeFormated(trackDuration));
        mediaPlayer.start();
        mHandler.postDelayed(updateProgress, 1000);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayer.seekTo(0);
        playPauseButton.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    /**
     * Sets the views of the media player.
     *
     * @param mediaPlayerView the views of the media player.
     */
    public void setMediaPlayerViews(View mediaPlayerView) {
        playPauseButton = (ImageButton) mediaPlayerView.findViewById(R.id.play_button);
        seekbar = (SeekBar)mediaPlayerView.findViewById(R.id.track_duration_seekbar);

        startTimeTextView = (TextView)mediaPlayerView.findViewById(R.id.start_time_textview);
        endTimeTextView = (TextView)mediaPlayerView.findViewById(R.id.end_time_textview);
        endTimeTextView.setText(Utility.getTimeFormated(trackDuration));

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
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
        } else if (!mediaPlayer.isPlaying() && nextPressed != true) {
            mediaPlayer.start();
            playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
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

}
