package com.eliasmyronidis.spotifystreamer;

import android.content.Intent;
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

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Elias Myronidis on 7/6/2015.
 */
public class DisplayArtistFragment extends Fragment {
    @InjectView(R.id.search_edit_text)
    EditText searchEditText;
    @InjectView(R.id.artist_listview)
    ListView artistsListView;

    private static final String SPOTIFY_ID = "spotify_id";
    private static final String ARTIST_NAME = "artist_name";

    private ArrayAdapter<CustomArtist> artistsAdapter;
    private ArrayList<CustomArtist> customArtistsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_display_artist, container, false);
        ButterKnife.inject(this, rootView);

        artistsAdapter = new ArtistsAdapter(getActivity(), new ArrayList<CustomArtist>());
        artistsListView.setAdapter(artistsAdapter);
        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                   @Override
                                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                       CustomArtist customArtist = artistsAdapter.getItem(position);
                                                       Intent mIntent = new Intent(getActivity(), TracksActivity.class);
                                                       mIntent.putExtra(SPOTIFY_ID, customArtist.getSpotifyId());
                                                       mIntent.putExtra(ARTIST_NAME, customArtist.getArtistName());
                                                       startActivity(mIntent);
                                                   }
                                               }
        );

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchedArtist = searchEditText.getText().toString();
                    SpotifyApi api = new SpotifyApi();
                    SpotifyService spotifyService = api.getService();
                    spotifyService.searchArtists(searchedArtist, new Callback<ArtistsPager>() {
                        @Override
                        public void success(final ArtistsPager artistsPager, final Response response) {

                            customArtistsList = new ArrayList<CustomArtist>();
                            for (Artist artist : artistsPager.artists.items) {
                                customArtistsList.add(new CustomArtist(artist));
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (artistsPager.artists.items.isEmpty())
                                        setToastMessage(getString(R.string.no_artist_found_toast));
                                    else {
                                        showArtists(customArtistsList);
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
                }
                return false;
            }
        });
        return rootView;
    }

    private void showArtists(ArrayList<CustomArtist> customArtistsList) {

        artistsAdapter.clear();
        if (customArtistsList != null) {
            artistsAdapter.addAll(customArtistsList);
        }

    }

    public void setToastMessage(String toastMessage) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (customArtistsList != null) {
            outState.putParcelableArrayList(getString(R.string.artist_list), customArtistsList);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            customArtistsList = savedInstanceState.getParcelableArrayList(getString(R.string.artist_list));
            showArtists(customArtistsList);
        }
    }
}
