package com.eliasmyronidis.spotifystreamer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Elias Myronidis on 27/6/2015.
 */
public class MediaPlayerFragment extends Fragment implements View.OnClickListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mediaPlayer;
    int selectedTrack;
    ArrayList<CustomTrack> customTracksList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        customTracksList = getActivity().getIntent().getParcelableArrayListExtra("tracks_list");
        selectedTrack = getActivity().getIntent().getIntExtra("selected_track", 0);
        String artistName = getActivity().getIntent().getStringExtra("artist_name");

        String trackName = customTracksList.get(selectedTrack).getTrackName();

        TextView artistNameTextView = (TextView)rootView.findViewById(R.id.artist_name_textview);
        TextView albumNameTextView = (TextView)rootView.findViewById(R.id.album_name_textview);
        ImageView albumeArtworkImageView = (ImageView)rootView.findViewById(R.id.album_artwork_imageview);
        TextView trackNameTextView = (TextView)rootView.findViewById(R.id.track_name_textview);
        ImageButton playButton = (ImageButton)rootView.findViewById(R.id.play_button);
        playButton.setOnClickListener(this);

        ImageButton stopButton = (ImageButton)rootView.findViewById(R.id.pause_button);
        stopButton.setOnClickListener(this);

        ImageButton nextButton = (ImageButton)rootView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(customTracksList.get(selectedTrack).getPreviewUrl());
            mediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        artistNameTextView.setText(artistName);
        albumNameTextView.setText(customTracksList.get(selectedTrack).getAlbumName());
        Picasso.with(getActivity()).load(customTracksList.get(selectedTrack).getLargeImageUrl()).into(albumeArtworkImageView);
        trackNameTextView.setText(trackName.toString());

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_button:
                playTrack();
                break;

            case R.id.pause_button:
                pauseTrack();
                break;

            case R.id.next_button:
                nextTrack();
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    private void playTrack(){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(customTracksList.get(selectedTrack).getPreviewUrl());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseTrack(){
        mediaPlayer.pause();
    }

    private void nextTrack(){
        selectedTrack++;
        playTrack();
    }
}
