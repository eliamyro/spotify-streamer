package com.eliasmyronidis.spotifystreamer;

import android.content.Intent;
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
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Elias Myronidis on 11/6/2015.
 */
public class TracksFragment extends Fragment {

    private Track track;
    private TracksAdapter tracksAdapter;
    private ListView tracksListView;
    private String spotifiId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tracks, container, false);

        track = new Track();
        tracksAdapter = new TracksAdapter(getActivity(), track);
        tracksListView = (ListView)rootView.findViewById(R.id.track_listview);
        tracksListView.setAdapter(tracksAdapter);

        Intent mIntent = getActivity().getIntent();
        if(mIntent != null && mIntent.hasExtra(Intent.EXTRA_TEXT)){
            spotifiId = mIntent.getStringExtra(Intent.EXTRA_TEXT);
            updateTrackList(spotifiId);
        }


        SpotifyApi api = new SpotifyApi();
        SpotifyService spotifyService = api.getService();

        Map<String, Object> countryMap = new HashMap<>();
        countryMap.put("country", "GR");
        spotifyService.getArtistTopTrack(spotifiId, countryMap, new Callback<Tracks>() {
            @Override
            public void success(final Tracks tracks, Response response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!tracks.tracks.isEmpty() == true){
                            tracksAdapter.clear();
                            for(Track tr :tracks.tracks){
                                tracksAdapter.add(tr);
                            }
                        } else {
                            setToastMessage(getString(R.string.no_tracks_toast));
                        }
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setToastMessage(getString(R.string.no_internet_toast));
                        }
                    });
                }
            }
        });




        return rootView;
    }

    private void updateTrackList(String spotifiId){

    }

    public void setToastMessage(String toastMessage) {
        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }


}
