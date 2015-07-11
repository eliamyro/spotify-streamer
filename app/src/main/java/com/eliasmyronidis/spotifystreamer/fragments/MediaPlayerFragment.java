package com.eliasmyronidis.spotifystreamer.fragments;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.eliasmyronidis.spotifystreamer.MediaPlayerService;
import com.eliasmyronidis.spotifystreamer.R;
import com.eliasmyronidis.spotifystreamer.beans.CustomTrack;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import com.eliasmyronidis.spotifystreamer.MediaPlayerService.MediaPlayerBinder;

/**
 * Created by Elias Myronidis on 27/6/2015.
 */
public class MediaPlayerFragment extends DialogFragment implements View.OnClickListener{

  //  implements View.OnClickListener, MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener

    private MediaPlayer mediaPlayer;
    int selectedTrack;
    ArrayList<CustomTrack> customTracksList;
    TextView artistNameTextView;
    private TextView albumNameTextView;
    private ImageView albumeArtworkImageView;
    private TextView trackNameTextView;
    private String artistName;
    boolean isPlaying = false;
    private ImageButton playButton;
    private SeekBar seekbar;
    public TextView startTimeTextView;
    private TextView endTimeTextView;
    private Handler mHandler = new Handler();

    public static final String CUSTOM_TRACKS_LIST = "custom_tracks_list";
    public static final String SELECTED_TRACK = "selected_track";
    public static final String ARTIST_NAME = "artist_name";


    private MediaPlayerService mediaPlayerService;
    private Intent intentService;
    private boolean musicBound = false;

    public static MediaPlayerFragment newInstance(ArrayList<CustomTrack> trackList, int track, String name) {
        MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(MediaPlayerFragment.CUSTOM_TRACKS_LIST, trackList);
        arguments.putInt(MediaPlayerFragment.SELECTED_TRACK, track);
        arguments.putString(MediaPlayerFragment.ARTIST_NAME, name);
        mediaPlayerFragment.setArguments(arguments);
        return mediaPlayerFragment;
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if(playIntent == null){
//            playIntent = new Intent(getActivity(), MediaPlayerService.class);
//            mediaPlayerService.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
//            mediaPlayerService.startService(playIntent);
//        }
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if(intentService == null){
            intentService = new Intent(getActivity().getBaseContext(), MediaPlayerService.class);
            getActivity().getBaseContext().startService(intentService);
            getActivity().getBaseContext().bindService(intentService, musicConnection, Context.BIND_AUTO_CREATE);

        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            customTracksList = arguments.getParcelableArrayList(MediaPlayerFragment.CUSTOM_TRACKS_LIST);
            selectedTrack = arguments.getInt(MediaPlayerFragment.SELECTED_TRACK);
            artistName = arguments.getString(MediaPlayerFragment.ARTIST_NAME);
        }

//        customTracksList = getActivity().getIntent().getParcelableArrayListExtra(CUSTOM_TRACKS_LIST);
//        selectedTrack = getActivity().getIntent().getIntExtra(SELECTED_TRACK, 0);
//        artistName = getActivity().getIntent().getStringExtra(ARTIST_NAME);


        artistNameTextView = (TextView) rootView.findViewById(R.id.artist_name_textview);
        albumNameTextView = (TextView) rootView.findViewById(R.id.album_name_textview);
        albumeArtworkImageView = (ImageView) rootView.findViewById(R.id.album_artwork_imageview);
        trackNameTextView = (TextView) rootView.findViewById(R.id.track_name_textview);
        playButton = (ImageButton) rootView.findViewById(R.id.play_button);
        playButton.setOnClickListener(this);
        startTimeTextView = (TextView) rootView.findViewById(R.id.start_time_textview);
        endTimeTextView = (TextView) rootView.findViewById(R.id.end_time_textview);

        seekbar = (SeekBar) rootView.findViewById(R.id.track_duration_seekbar);
//        seekbar.setOnSeekBarChangeListener(this);

        ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);

        ImageButton previousButton = (ImageButton) rootView.findViewById(R.id.previous_button);
        previousButton.setOnClickListener(this);

       // setTrackInfo();

     //   mediaPlayer = new MediaPlayer();

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    // connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerBinder binder = (MediaPlayerBinder) service;
            //get service
            mediaPlayerService = binder.getService();

            //pass list
            mediaPlayerService.setSong(customTracksList.get(selectedTrack).getPreviewUrl());
            mediaPlayerService.playSong();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    //user song select
    public void songPicked(View view){
       // mediaPlayerService.setSong(Integer.parseInt(view.getTag().toString()));
       mediaPlayerService.playSong();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                if (isPlaying == false) {
                    isPlaying = true;
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                } else {
                    isPlaying = false;
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                }

               // if (isPlaying == true)
                   // playTrack();
             //   else
                  //  pauseTrack();
                break;

            case R.id.next_button:
                //nextTrack();
                break;

            case R.id.previous_button:
             //   previousTrack();
                break;
        }
    }



//        albumNameTextView.setText(customTracksList.get(selectedTrack).getAlbumName());
//        Picasso.with(getActivity()).load(customTracksList.get(selectedTrack).getLargeImageUrl()).into(albumeArtworkImageView);
//        trackNameTextView.setText(customTracksList.get(selectedTrack).getTrackName());
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        mediaPlayer.release();
//    }

//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////        if(fromUser)
////            mediaPlayer.seekTo(progress);
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//        mHandler.removeCallbacks(updateTimeTask);
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        mHandler.removeCallbacks(updateTimeTask);
//        // int totalDuration = mp.getDuration();
//        int currentPosition = seekBar.getProgress();
//
//        // forward or backward to certain seconds
//        mediaPlayer.seekTo(currentPosition);
//
//        // update timer progress again
//        updateSeekBar();
//    }

//    @Override
//    public void run() {
//        int currentPosition= 0;
//        int total = 30000;
//        while (mediaPlayer!=null && currentPosition<total) {
//            try {
//                Thread.sleep(1000);
//                currentPosition= mediaPlayer.getCurrentPosition();
//
//            } catch (InterruptedException e) {
//                return;
//            } catch (Exception e) {
//                return;
//            }
//            seekbar.setProgress(currentPosition);
//
//        }
//    }

//    public void updateSeekBar() {
//        mHandler.postDelayed(updateTimeTask, 1000);
//    }

//    private Runnable updateTimeTask = new Runnable() {
//        @Override
//        public void run() {
//            int total = 30000;
//            int currentTime = mediaPlayer.getCurrentPosition();
//
//            startTimeTextView.setText("0:" + Integer.toString(currentTime / 1000));
//            seekbar.setProgress(currentTime);
//            mHandler.postDelayed(this, 1000);
//        }
//    };
}
