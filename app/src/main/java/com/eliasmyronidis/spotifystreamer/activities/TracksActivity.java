package com.eliasmyronidis.spotifystreamer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.eliasmyronidis.spotifystreamer.R;
import com.eliasmyronidis.spotifystreamer.beans.CustomTrack;
import com.eliasmyronidis.spotifystreamer.fragments.MediaPlayerFragment;
import com.eliasmyronidis.spotifystreamer.fragments.TracksFragment;

import java.util.ArrayList;


public class TracksActivity extends AppCompatActivity implements TracksFragment.TracksClickCallback{

    private static final String MEDIA_PLAYER_FRAGMENT_TAG = "MPFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        if(savedInstanceState==null){

            Bundle arguments = new Bundle();
            arguments.putString(TracksFragment.SPOTIFY_ID, getIntent().getStringExtra(TracksFragment.SPOTIFY_ID));
            arguments.putString(TracksFragment.ARTIST_NAME, getIntent().getStringExtra(TracksFragment.ARTIST_NAME));

            TracksFragment fragment = new TracksFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_tracks_container, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
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

    @Override
    public void onItemSelected(ArrayList<CustomTrack> customTracksList, int position, String artistName) {
        if (MainActivity.mTwoPane == false) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment().newInstance(customTracksList, position, artistName);
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(android.R.id.content, mediaPlayerFragment)
                    .addToBackStack(null).commit();
        } else {
            DialogFragment mediaPlayerFragment = MediaPlayerFragment.newInstance(customTracksList, position, artistName);
            mediaPlayerFragment.show(getSupportFragmentManager(), MEDIA_PLAYER_FRAGMENT_TAG);
        }
    }
}
