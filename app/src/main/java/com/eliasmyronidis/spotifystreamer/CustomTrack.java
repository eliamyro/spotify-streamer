package com.eliasmyronidis.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Elias Myronidis on 15/6/2015.
 */
public class CustomTrack implements Parcelable {

    private String trackName;
    private String albumName;
    private ArrayList<String> albumImages = new ArrayList<>();
    private ArrayList<String> imagesWidth = new ArrayList<>();
    private String previewUrl;

    public CustomTrack(Track track){
        trackName = track.name;
        albumName = track.album.name;
        for(Image image : track.album.images){
            if(image.url != null){
                albumImages.add(image.url);
                imagesWidth.add(image.width.toString());
            }
        }
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

    public ArrayList<String> getAlbumImages() {
        return albumImages;
    }

    public void setAlbumImages(ArrayList<String> albumImages) {
        this.albumImages = albumImages;
    }

    public ArrayList<String> getImagesWidth() {
        return imagesWidth;
    }

    public void setImagesWidth(ArrayList<String> imagesWidth) {
        this.imagesWidth = imagesWidth;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public CustomTrack(Parcel in){
        trackName = in.readString();
        albumName = in.readString();
        albumImages = in.createStringArrayList();
        imagesWidth = in.createStringArrayList();
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
        dest.writeStringList(albumImages);
        dest.writeStringList(imagesWidth);
        dest.writeString(previewUrl);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){


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
