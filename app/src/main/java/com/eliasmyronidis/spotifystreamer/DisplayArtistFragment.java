package com.eliasmyronidis.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created by Elias Myronidis on 7/6/2015.
 */
public class DisplayArtistFragment extends Fragment {

    private ArrayAdapter<Artist> artistsAdapter;
    private ListView artistsListView;
    private EditText searchEditText;
    private Artist artist;
    private String searchedArtist = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_display_artist, container);

        if (savedInstanceState != null) {
            searchedArtist = savedInstanceState.getString("searched_artist");
            updateArtistList(searchedArtist);
        }

        searchEditText = (EditText) rootView.findViewById(R.id.search_edit_text);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchedArtist = searchEditText.getText().toString();

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    return updateArtistList(searchedArtist);
                }
                return false;
            }
        });

        artist = new Artist();
        artistsAdapter = new ArtistsAdapter(getActivity(), artist);
        artistsListView = (ListView) rootView.findViewById(R.id.artist_listview);
        artistsListView.setAdapter(artistsAdapter);
        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                artist = artistsAdapter.getItem(position);
                Intent mIntent = new Intent(getActivity(), TracksActivity.class);
                mIntent.putExtra(Intent.EXTRA_TEXT, artist.id.toString());
                startActivity(mIntent);
            }
        });

        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setToastMessage(String toastMessage) {
        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    private boolean updateArtistList(String artistString) {
        if (!artistString.equals("")) {
            if (DisplayArtistFragment.this.isNetworkAvailable() == true) {
                FetchArtistTask artistTask = new FetchArtistTask();
                artistTask.execute(artistString);
                return true;
            } else {
                setToastMessage(getResources().getString(R.string.no_internet_toast));
            }
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searched_artist", searchedArtist);
    }


    public class FetchArtistTask extends AsyncTask<String, Void, ArtistsPager> {

        private final String LOG_TAG = FetchArtistTask.class.getSimpleName();

        @Override
        protected ArtistsPager doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);
            return results;
        }

        @Override
        protected void onPostExecute(ArtistsPager result) {
            if (!result.artists.items.isEmpty() == true) {
                artistsAdapter.clear();
                for (Artist art : result.artists.items) {
                    artistsAdapter.add(art);
                }
            } else {
                setToastMessage(getResources().getString(R.string.no_artist_found_toast));

            }
        }
    }
}