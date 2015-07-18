package com.eliasmyronidis.spotifystreamer.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Elias Myronidis on 13/6/2015.
 */
public class CustomArtist implements Parcelable {
    private String artistName;
    private String spotifyId;
    private String artistImageUrl;

    public CustomArtist(Artist artist){
        artistName = artist.name;
        spotifyId = artist.id;
        artistImageUrl = getArtistImageUrl(artist.images);
    }


    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getArtistImageUrl() {
        return artistImageUrl;
    }

    public void setArtistImageUrl(String artistImageUrl) {
        this.artistImageUrl = artistImageUrl;
    }

    private String getArtistImageUrl(List<Image> images) {
        int i = 0;
        boolean isFound = false;

        while (i < images.size() && isFound == false) {
            if (images.get(i).url != null) {
                if (images.get(i).width == 200) {
                    artistImageUrl = images.get(i).url;
                    isFound = true;
                }
            }
            i++;
        }
        if (isFound == false && images.size()!=0)
            artistImageUrl = images.get(0).url;

        return artistImageUrl;
    }

    public CustomArtist(Parcel in){
        artistName = in.readString();
        spotifyId = in.readString();
        artistImageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeString(spotifyId);
        dest.writeString(artistImageUrl);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public CustomArtist createFromParcel(Parcel source) {
            return new CustomArtist(source);
        }

        @Override
        public CustomArtist[] newArray(int size) {
            return new CustomArtist[size];
        }
    };
}
