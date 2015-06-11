package com.eliasmyronidis.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Elias Myronidis on 11/6/2015.
 */
public class TracksAdapter extends ArrayAdapter<Track> {
    public TracksAdapter(Context context, Track track) {
        super(context, R.layout.track_list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.track_list_item, parent, false);
        }

        TextView trackName = (TextView) convertView.findViewById(R.id.track_name_textview);
        TextView albumName = (TextView) convertView.findViewById(R.id.track_album_textview);
        ImageView trackImage = (ImageView) convertView.findViewById(R.id.track_image);
        trackName.setText(getItem(position).name);
        albumName.setText(getItem(position).album.name);
        String previewUrl = getItem(position).preview_url; // preview url for spotify stage 2.


        trackImage.setImageResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
        int imageWidth = 0;
        String smallImage = null;
        String largeImage = null; // large image for spotify stage 2
        if (trackImage != null && getItem(position).album.images.size() != 0) {
            for(int i=0; i<getItem(position).album.images.size(); i++){
                imageWidth = getItem(position).album.images.get(i).width;
                if(imageWidth == 200){
                    smallImage = getItem(position).album.images.get(i).url;
                }else if(imageWidth == 600){
                    largeImage = getItem(position).album.images.get(i).url;
                } else {
                    // We haven't found image with the desired width and we take the first image.
                    smallImage = getItem(position).album.images.get(0).url;
                    largeImage = getItem(position).album.images.get(0).url;
                }
                Picasso.with(getContext()).load(smallImage).resize(200,200).into(trackImage);
            }

        }

        return convertView;
    }
}
