package com.eliasmyronidis.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;
import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Elias Myronidis on 13/6/2015.
 */
public class SimpleArtist implements Parcelable {
    private Artist artist;


    public SimpleArtist(Artist artist){
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }


    public SimpleArtist(Parcel in){
        this.artist = (Artist)in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(artist);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public SimpleArtist createFromParcel(Parcel source) {
            return new SimpleArtist(source);
        }

        @Override
        public SimpleArtist[] newArray(int size) {
            return new SimpleArtist[size];
        }
    };
}
