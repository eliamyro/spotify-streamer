package com.eliasmyronidis.spotifystreamer.fragments;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.eliasmyronidis.spotifystreamer.R;
import com.eliasmyronidis.spotifystreamer.activities.MainActivity;
import com.eliasmyronidis.spotifystreamer.activities.MediaPlayerActivity;
import com.eliasmyronidis.spotifystreamer.adapters.TracksAdapter;
import com.eliasmyronidis.spotifystreamer.beans.CustomTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Elias Myronidis on 11/6/2015.
 */
public class TracksFragment extends Fragment {

    @InjectView(R.id.track_listview)
    ListView tracksListView;
    public static final String SPOTIFY_ID = "spotify_id";
    public static final String ARTIST_NAME = "artist_name";

    private ArrayAdapter<CustomTrack> tracksAdapter;
    private ArrayList<CustomTrack> customTracksList;
    private String spotifiId;
    private String artistName;

    private static final String MEDIA_PLAYER_FRAGMENT_TAG = "MPFTAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_tracks, container, false);
        ButterKnife.inject(this, rootView);


        Bundle arguments = getArguments();
        if (arguments != null) {
            spotifiId = arguments.getString(TracksFragment.SPOTIFY_ID);
            artistName = arguments.getString(TracksFragment.ARTIST_NAME);
        }

//        Intent mIntent = getActivity().getIntent();
//        if (mIntent != null && mIntent.hasExtra(SPOTIFY_ID) && mIntent.hasExtra(ARTIST_NAME)) {
//            spotifiId = mIntent.getStringExtra(SPOTIFY_ID);
//            artistName = mIntent.getStringExtra(ARTIST_NAME);
//        }

        // http://stackoverflow.com/questions/18320713/getsupportactionbar-from-inside-of-fragment-actionbarcompat
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(artistName);

        tracksAdapter = new TracksAdapter(getActivity(), new ArrayList<CustomTrack>());
        tracksListView.setAdapter(tracksAdapter);
        tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                  @Override
                                                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                      if (MainActivity.mTwoPane == false) {
                                                          FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                          MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment().newInstance(customTracksList, position, artistName);
                                                          android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                                                          transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                                          transaction.replace(android.R.id.content, mediaPlayerFragment)
                                                                  .addToBackStack(null).commit();
                                                      } else {
                                                          DialogFragment mediaPlayerFragment = MediaPlayerFragment.newInstance(customTracksList, position, artistName);
                                                          mediaPlayerFragment.show(getActivity().getSupportFragmentManager(), MEDIA_PLAYER_FRAGMENT_TAG);
                                                      }
                                                  }
                                              }

        );

        if (savedInstanceState == null) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotifyService = api.getService();

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String country = sharedPref.getString(getString(R.string.pref_countries_key),
                    getString(R.string.pref_countries_default));

            Map<String, Object> countryMap = new HashMap<>();
            countryMap.put("country", country);
            spotifyService.getArtistTopTrack(spotifiId, countryMap, new Callback<Tracks>() {
                @Override
                public void success(final Tracks tracks, Response response) {
                    customTracksList = new ArrayList<CustomTrack>();
                    for (Track track : tracks.tracks) {
                        customTracksList.add(new CustomTrack(track));
                    }
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (customTracksList.isEmpty()) {
                                    setSnackbarMessage(getString(R.string.no_tracks_toast));
                                } else {
                                    showTopTracks(customTracksList);
                                }
                            }
                        });
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setSnackbarMessage(getString(R.string.no_internet_toast));
                            }
                        });
                    }
                }
            });
        }
        return rootView;
    }

    private void showTopTracks(ArrayList<CustomTrack> customTracksList) {
        tracksAdapter.clear();
        if (customTracksList != null) {
            tracksAdapter.addAll(customTracksList);
        }
    }

    public void setSnackbarMessage(String snackbarMessage) {
        if (getActivity() != null)
            Snackbar.make(getView(), snackbarMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (customTracksList != null) {
            outState.putParcelableArrayList(getString(R.string.tracks_list), customTracksList);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            customTracksList = savedInstanceState.getParcelableArrayList(getString(R.string.tracks_list));
            showTopTracks(customTracksList);
        }
    }
}
