package com.eliasmyronidis.spotifystreamer.activities;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import com.eliasmyronidis.spotifystreamer.fragments.MediaPlayerFragment;
import com.eliasmyronidis.spotifystreamer.R;
import com.eliasmyronidis.spotifystreamer.beans.CustomTrack;

import java.util.ArrayList;


public class MediaPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        if (getIntent() != null) {
            ArrayList<CustomTrack> customTrackList = getIntent().getParcelableArrayListExtra(MediaPlayerFragment.CUSTOM_TRACKS_LIST);
            int selectedTrack = getIntent().getIntExtra(MediaPlayerFragment.SELECTED_TRACK, 0);
            String artistName = getIntent().getStringExtra(MediaPlayerFragment.ARTIST_NAME);

//        Bundle arguments = new Bundle();
//        arguments.putParcelableArrayList(MediaPlayerFragment.CUSTOM_TRACKS_LIST, getIntent().getParcelableArrayListExtra(MediaPlayerFragment.CUSTOM_TRACKS_LIST));
//        arguments.putInt(MediaPlayerFragment.SELECTED_TRACK, getIntent().getIntExtra(MediaPlayerFragment.SELECTED_TRACK, 0));
//        arguments.putString(MediaPlayerFragment.ARTIST_NAME, getIntent().getStringExtra(MediaPlayerFragment.ARTIST_NAME));

            DialogFragment mediaPlayerFragment = MediaPlayerFragment.newInstance(customTrackList, selectedTrack, artistName);
//        mediaPlayerFragment.setArguments(arguments);
//        mediaPlayerFragment.show(getSupportFragmentManager(),"dialog");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.media_player_container, mediaPlayerFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
