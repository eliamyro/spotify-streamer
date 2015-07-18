package com.eliasmyronidis.spotifystreamer.beans;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Elias Myronidis on 15/6/2015.
 */
public class CustomTrack implements Parcelable {

    private String trackName;
    private String albumName;
    private String smallImageUrl;
    private String largeImageUrl;
    private String previewUrl;

    public CustomTrack(Track track) {
        trackName = track.name;
        albumName = track.album.name;
        smallImageUrl = getSmallImageUrl(track);
        largeImageUrl = getLargeImageUrl(track);
        previewUrl = track.preview_url;
    }


    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    private String getSmallImageUrl(Track track) {
        int i = 0;
        boolean isFound = false;

        while (i < track.album.images.size() && isFound == false) {
            if (track.album.images.get(i).url != null) {
                if (track.album.images.get(i).width == 200) {
                    smallImageUrl = track.album.images.get(i).url;
                    isFound = true;
                }
            }
            i++;
        }
        if (isFound == false)
            smallImageUrl = track.album.images.get(0).url;

        return smallImageUrl;
    }

    private String getLargeImageUrl(Track track) {
        int i = 0;
        boolean isFound = false;

        while (i < track.album.images.size() && isFound == false) {
            if (track.album.images.get(i).url != null) {
                if (track.album.images.get(i).width == 600) {
                    largeImageUrl = track.album.images.get(i).url;
                    isFound = true;
                }
            }
            i++;
        }
        if (isFound == false)
            largeImageUrl = track.album.images.get(0).url;

        return largeImageUrl;
    }

    public CustomTrack(Parcel in) {
        trackName = in.readString();
        albumName = in.readString();
        smallImageUrl = in.readString();
        largeImageUrl = in.readString();
        previewUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trackName);
        dest.writeString(albumName);
        dest.writeString(smallImageUrl);
        dest.writeString(largeImageUrl);
        dest.writeString(previewUrl);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel source) {
            return new CustomTrack(source);
        }

        @Override
        public CustomTrack[] newArray(int size) {
            return new CustomTrack[size];
        }
    };
}
