package com.eliasmyronidis.spotifystreamer;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements DisplayArtistFragment.ClickCallback{

    private boolean mTwoPane;
    private static final String TRACKS_FRAGMENT_TAG = "TFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.top_tracks_container)!=null){
            mTwoPane = true;

            if(savedInstanceState==null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.top_tracks_container, new TracksFragment(), TRACKS_FRAGMENT_TAG)
                        .commit();
            }
        }else {
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String spotifyId, String artistName) {
        if(mTwoPane==true){
            Bundle args = new Bundle();
            args.putString(TracksFragment.SPOTIFY_ID, spotifyId);
            args.putString(TracksFragment.ARTIST_NAME, artistName);

            TracksFragment fragment = new TracksFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.top_tracks_container, fragment, TRACKS_FRAGMENT_TAG).commit();
        } else {
            Intent mIntent = new Intent(this, TracksActivity.class);
            mIntent.putExtra(TracksFragment.SPOTIFY_ID, spotifyId);
            mIntent.putExtra(TracksFragment.ARTIST_NAME, artistName);
            startActivity(mIntent);
        }
    }
}
