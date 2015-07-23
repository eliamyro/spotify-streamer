package com.eliasmyronidis.spotifystreamer.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.eliasmyronidis.spotifystreamer.R;
import com.eliasmyronidis.spotifystreamer.fragments.MediaPlayerFragment;

/**
 * Created by Elias Myronidis on 23/7/15.
 */
public class NowPlayingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.now_playing);

        if (savedInstanceState == null) {

            if (MainActivity.mTwoPane == false) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                MediaPlayerFragment mediaPlayerFragment = MediaPlayerFragment.getInstance();
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(android.R.id.content, mediaPlayerFragment)
                        .addToBackStack(null).commit();
            } else {
                DialogFragment mediaPlayerFragment = MediaPlayerFragment.getInstance();
                mediaPlayerFragment.show(getSupportFragmentManager(), "MPFTAG");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
