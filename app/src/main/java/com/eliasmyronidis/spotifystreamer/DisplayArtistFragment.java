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
    @InjectView(R.id.search_edit_text)EditText searchEditText;
    @InjectView(R.id.artist_listview) ListView artistsListView;

    private ArrayAdapter<Artist> artistsAdapter;
    private Artist artist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_display_artist, container);
        ButterKnife.inject(this, rootView);

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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (artistsPager.artists.items.isEmpty())
                                        setToastMessage(getString(R.string.no_artist_found_toast));
                                    else {
                                        showArtists(artistsPager);
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

        artist = new Artist();
        artistsAdapter = new ArtistsAdapter(getActivity(), new Artist());
        artistsListView.setAdapter(artistsAdapter);
        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                   @Override
                                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                       artist = artistsAdapter.getItem(position);
                                                       Intent mIntent = new Intent(getActivity(), TracksActivity.class);
                                                       mIntent.putExtra(Intent.EXTRA_TEXT, artist.id.toString());
                                                       startActivity(mIntent);
                                                   }
                                               }
        );

        return rootView;
    }

    private void showArtists(ArtistsPager artistPager) {

        artistsAdapter.clear();
        for (Artist artist : artistPager.artists.items) {
            artistsAdapter.add(artist);
        }
    }

    public void setToastMessage(String toastMessage) {
        Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
    }
}
