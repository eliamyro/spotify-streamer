package com.eliasmyronidis.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by eliamyro on 10/7/15.
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private MediaPlayer mediaPlayer;
    private String songUrl;
    private final IBinder musicBind = new MediaPlayerBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();
        initMusicPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            return START_STICKY;
    }

    public void initMusicPlayer(){
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

//    @Override
//    public boolean onUnbind(Intent intent) {
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        return false;
//    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public void setSong(String url){
        songUrl = url;
    }

    public class MediaPlayerBinder extends Binder {
        public MediaPlayerService getService(){
            return MediaPlayerService.this;
        }
    }

    public void playSong(){
        mediaPlayer.reset();
        try{
            mediaPlayer.setDataSource(songUrl);
        } catch (Exception e){

        }

        mediaPlayer.prepareAsync();
    }


}
