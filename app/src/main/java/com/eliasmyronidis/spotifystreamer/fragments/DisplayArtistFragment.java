package com.eliasmyronidis.spotifystreamer.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.eliasmyronidis.spotifystreamer.R;
import com.eliasmyronidis.spotifystreamer.adapters.ArtistsAdapter;
import com.eliasmyronidis.spotifystreamer.beans.CustomArtist;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
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
    private static final String SELECTED_KEY = "selected_key";
    @Bind(R.id.searchView) SearchView searchView;
    @Bind(R.id.artist_listview) ListView mArtistsListView;

    private ArrayAdapter<CustomArtist> artistsAdapter;
    private ArrayList<CustomArtist> customArtistsList;
    private int mPosition;

    public interface ClickCallback {
        public void onItemSelected(String spotifyId, String artistName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_display_artist, container, false);
        ButterKnife.bind(this, rootView);

        artistsAdapter = new ArtistsAdapter(getActivity(), new ArrayList<CustomArtist>());
        mArtistsListView.setAdapter(artistsAdapter);
        mArtistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        CustomArtist customArtist = artistsAdapter.getItem(position);

                                                        ((ClickCallback) getActivity()).onItemSelected(customArtist.getSpotifyId(), customArtist.getArtistName());

                                                        mPosition = position;
                                                    }
                                                }
        );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchedArtist = searchView.getQuery().toString();
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
                                    setSnackbarMessage(getString(R.string.no_artist_found_toast));
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
                                    setSnackbarMessage(getString(R.string.no_internet_toast));
                                }
                            });
                        }
                    }
                });
                return true;
            }



        @Override
        public boolean onQueryTextChange (String newText){
            return false;
        }
    }

    );


    if(savedInstanceState!=null&&savedInstanceState.containsKey(SELECTED_KEY))

    {
        mPosition = savedInstanceState.getInt(SELECTED_KEY);
    }

    return rootView;
}

    private void showArtists(ArrayList<CustomArtist> customArtistsList) {

        artistsAdapter.clear();
        if (customArtistsList != null) {
            artistsAdapter.addAll(customArtistsList);
        }

    }

    private void setSnackbarMessage(String snackbarMessage) {
        if (getActivity() != null)
            Snackbar.make(getView(), snackbarMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (customArtistsList != null) {
            outState.putParcelableArrayList(getString(R.string.artist_list), customArtistsList);
        }

        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            customArtistsList = savedInstanceState.getParcelableArrayList(getString(R.string.artist_list));
            showArtists(customArtistsList);

            if (mPosition != ListView.INVALID_POSITION)
                mArtistsListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
