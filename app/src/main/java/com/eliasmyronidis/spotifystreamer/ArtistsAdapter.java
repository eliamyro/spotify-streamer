package com.eliasmyronidis.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Elias Myronidis on 8/6/2015.
 */
public class ArtistsAdapter extends ArrayAdapter<Artist> {

    public ArtistsAdapter(Context context, Artist artist) {
        super(context, R.layout.artist_list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.artist_list_item, parent, false);
        }

        TextView artistName = (TextView) convertView.findViewById(R.id.artist_name_textview);
        ImageView artistImage = (ImageView) convertView.findViewById(R.id.artist_image);
        artistName.setText(getItem(position).name);


        artistImage.setImageResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
        if (artistImage != null && getItem(position).images.size() != 0) {
            Picasso.with(getContext()).load(getItem(position).images.get(0).url).resize(200, 200).into(artistImage);
        }

        return convertView;
    }
}
