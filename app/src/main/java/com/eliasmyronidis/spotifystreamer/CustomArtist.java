package com.eliasmyronidis.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Elias Myronidis on 13/6/2015.
 */
public class CustomArtist implements Parcelable {
    private String artistName;
    private String spotifyId;
    private ArrayList<String> artistImages = new ArrayList<>();


    public CustomArtist(Artist artist){
        artistName = artist.name;
        spotifyId = artist.id;
        for(Image image : artist.images){
            if(image.url != null){
                artistImages.add(image.url);
            }
        }
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

    public ArrayList<String> getArtistImages() {
        return artistImages;
    }

    public void setArtistImages(ArrayList<String> artistImages) {
        this.artistImages = artistImages;
    }

    public CustomArtist(Parcel in){
        artistName = in.readString();
        spotifyId = in.readString();
        artistImages = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(artistName);
        dest.writeString(spotifyId);
        dest.writeStringList(artistImages);
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
