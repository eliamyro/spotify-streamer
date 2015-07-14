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
import android.util.Log;
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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Elias Myronidis on 27/6/2015.
 */
public class MediaPlayerFragment extends DialogFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    @Bind(R.id.track_duration_seekbar) SeekBar mSeekbar;
    @Bind(R.id.artist_name_textview) TextView artistNameTextView;
    @Bind(R.id.album_name_textview) TextView albumNameTextView;
    @Bind(R.id.album_artwork_imageview) ImageView albumArtworkImageView;
    @Bind(R.id.track_name_textview) TextView trackNameTextView;
    @Bind(R.id.play_button) ImageButton playButton;
    @Bind(R.id.next_button) ImageButton nextButton;
    @Bind(R.id.previous_button) ImageButton previousButton;

    ArrayList<CustomTrack> customTracksList;

    private String artistName;
    private TextView startTimeTextView;
    private int selectedTrack;
    public static final String CUSTOM_TRACKS_LIST = "custom_tracks_list";
    public static final String SELECTED_TRACK = "selected_track";
    public static final String ARTIST_NAME = "artist_name";
    public static final String CURRENT_TRACK_POSITION = "current_track_position";

    private MediaPlayerService mediaPlayerService;
    private Intent intentService;
    private boolean musicBound = false;
    View rootView;

    private int position;
    private int currentTrackPosition;

    public static MediaPlayerFragment newInstance(ArrayList<CustomTrack> trackList, int track, String name) {

        MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(MediaPlayerFragment.CUSTOM_TRACKS_LIST, trackList);
        arguments.putInt(MediaPlayerFragment.SELECTED_TRACK, track);
        arguments.putString(MediaPlayerFragment.ARTIST_NAME, name);
        mediaPlayerFragment.setArguments(arguments);
        return mediaPlayerFragment;
    }


    // connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaPlayerBinder binder = (MediaPlayerBinder) service;
            mediaPlayerService = binder.getService();
            mediaPlayerService.setMediaPlayerViews(getView());
            mediaPlayerService.startMediaPlayer(customTracksList.get(selectedTrack).getPreviewUrl());
            mSeekbar.setMax(30000);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_TRACK, selectedTrack);
        currentTrackPosition = mediaPlayerService.getCurrentPosition();
        outState.putInt(CURRENT_TRACK_POSITION, currentTrackPosition);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            selectedTrack = savedInstanceState.getInt(SELECTED_TRACK);
            currentTrackPosition = savedInstanceState.getInt(CURRENT_TRACK_POSITION);

            setTrackInfo();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_media_player, container, false);
        ButterKnife.bind(this, rootView);

        if (intentService == null) {
            intentService = new Intent(getActivity(), MediaPlayerService.class);
            getActivity().startService(intentService);
            getActivity().bindService(intentService, musicConnection, Context.BIND_AUTO_CREATE);

        }

        Bundle arguments = getArguments();
        if (arguments != null) {
            customTracksList = arguments.getParcelableArrayList(MediaPlayerFragment.CUSTOM_TRACKS_LIST);
            selectedTrack = arguments.getInt(MediaPlayerFragment.SELECTED_TRACK);
            artistName = arguments.getString(MediaPlayerFragment.ARTIST_NAME);
        }

        playButton.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(this);
        mSeekbar.setProgress(currentTrackPosition);


        nextButton = (ImageButton) rootView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);

        previousButton = (ImageButton) rootView.findViewById(R.id.previous_button);
        previousButton.setOnClickListener(this);

        setTrackInfo();

        return rootView;
    }


    public void setTrackInfo() {

        // Disables the previous button if it's the first track.
        if(selectedTrack==0){
            previousButton.setEnabled(false);
        } else if(selectedTrack==1){
            previousButton.setEnabled(true);
        }

        // Disables the next button if it's the last track.
        if(selectedTrack==customTracksList.size()-1)
            nextButton.setEnabled(false);
        else if(selectedTrack==customTracksList.size()-2)
            nextButton.setEnabled(true);



        // set's track info.
        Picasso.with(getActivity()).load(customTracksList.get(selectedTrack).getLargeImageUrl()).into(albumArtworkImageView);
        artistNameTextView.setText(artistName);
        albumNameTextView.setText(customTracksList.get(selectedTrack).getAlbumName());
        trackNameTextView.setText(customTracksList.get(selectedTrack).getTrackName());

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_button:
                mediaPlayerService.playPauseTrack();
                break;

            case R.id.next_button:

                if (selectedTrack != customTracksList.size() - 1) {
                    selectedTrack++;
                    mediaPlayerService.nextTrack(customTracksList.get(selectedTrack).getPreviewUrl());
                    setTrackInfo();
                }
                break;

            case R.id.previous_button:

                if (selectedTrack != 0) {
                    selectedTrack--;
                    mediaPlayerService.previousTrack(customTracksList.get(selectedTrack).getPreviewUrl());
                    setTrackInfo();
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        if (musicBound) {
            getActivity().unbindService(musicConnection);
            musicBound = false;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        if(fromUser)
//            mediaPlayer.seekTo(progress);

        position = progress;

    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayerService.seekToPosition(position);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
