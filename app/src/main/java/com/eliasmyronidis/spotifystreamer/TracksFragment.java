package com.eliasmyronidis.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by Elias Myronidis on 11/6/2015.
 */
public class TracksFragment extends Fragment {

    private Track track;
    private TracksAdapter tracksAdapter;
    private ListView tracksListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tracks, container, false);

        track = new Track();
        tracksAdapter = new TracksAdapter(getActivity(), track);
        tracksListView = (ListView)rootView.findViewById(R.id.track_listview);
        tracksListView.setAdapter(tracksAdapter);

        Intent mIntent = getActivity().getIntent();
        if(mIntent != null && mIntent.hasExtra(Intent.EXTRA_TEXT)){
            String spotifiId = mIntent.getStringExtra(Intent.EXTRA_TEXT);
            updateTrackList(spotifiId);
        }
        return rootView;
    }

    private void updateTrackList(String spotifiId){
        FetchTracksTask tracksTask = new FetchTracksTask();
        tracksTask.execute(spotifiId);
    }

    public class FetchTracksTask extends AsyncTask<String, Void, Tracks>{

        private final String LOG_TAG = FetchTracksTask.class.getSimpleName();

        @Override
        protected Tracks doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            Map<String, Object> countryMap = new HashMap<>();
            countryMap.put("country", getString(R.string.country_iso));
            Tracks tracks = spotify.getArtistTopTrack(params[0], countryMap);

            return tracks;
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            super.onPostExecute(tracks);

            if(!tracks.tracks.isEmpty() == true){
                tracksAdapter.clear();
                for(Track tr :tracks.tracks){
                    tracksAdapter.add(tr);
                }
            } else {
                Toast.makeText(getActivity(),getString(R.string.no_tracks_toast), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
