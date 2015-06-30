package com.eliasmyronidis.spotifystreamer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Elias Myronidis on 27/6/2015.
 */
public class MediaPlayerFragment extends Fragment implements View.OnClickListener, MediaPlayer.OnPreparedListener {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        customTracksList = getActivity().getIntent().getParcelableArrayListExtra("tracks_list");
        selectedTrack = getActivity().getIntent().getIntExtra("selected_track", 0);
        artistName = getActivity().getIntent().getStringExtra("artist_name");

        artistNameTextView = (TextView) rootView.findViewById(R.id.artist_name_textview);
        albumNameTextView = (TextView) rootView.findViewById(R.id.album_name_textview);
        albumeArtworkImageView = (ImageView) rootView.findViewById(R.id.album_artwork_imageview);
        trackNameTextView = (TextView) rootView.findViewById(R.id.track_name_textview);
        playButton = (ImageButton) rootView.findViewById(R.id.play_button);
        playButton.setOnClickListener(this);

        ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);

        ImageButton previousButton = (ImageButton) rootView.findViewById(R.id.previous_button);
        previousButton.setOnClickListener(this);

        setTrackInfo();

        mediaPlayer = new MediaPlayer();

        return rootView;
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

                if (isPlaying == true)
                    playTrack();
                else
                    pauseTrack();
                break;

            case R.id.next_button:
                nextTrack();
                break;

            case R.id.previous_button:
                previousTrack();
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    private void playTrack() {
        if (isPlaying == true) {
            //playButton.setImageResource(android.R.drawable.ic_media_pause);
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(customTracksList.get(selectedTrack).getPreviewUrl());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void pauseTrack() {
        mediaPlayer.pause();
    }

    private void nextTrack() {
        selectedTrack++;
        setTrackInfo();
        playTrack();
    }

    private void previousTrack() {
        selectedTrack--;
        setTrackInfo();
        playTrack();
    }

    private void setTrackInfo() {
        artistNameTextView.setText(artistName);
        albumNameTextView.setText(customTracksList.get(selectedTrack).getAlbumName());
        Picasso.with(getActivity()).load(customTracksList.get(selectedTrack).getLargeImageUrl()).into(albumeArtworkImageView);
        trackNameTextView.setText(customTracksList.get(selectedTrack).getTrackName());
    }
}
