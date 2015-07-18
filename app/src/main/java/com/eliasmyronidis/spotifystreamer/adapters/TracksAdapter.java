package com.eliasmyronidis.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eliasmyronidis.spotifystreamer.beans.CustomTrack;
import com.eliasmyronidis.spotifystreamer.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Elias Myronidis on 11/6/2015.
 */
public class TracksAdapter extends ArrayAdapter<CustomTrack> {
    public TracksAdapter(Context context, ArrayList<CustomTrack> customTracksList) {
        super(context, R.layout.track_list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.track_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.trackName.setText(getItem(position).getTrackName());
        holder.albumName.setText(getItem(position).getAlbumName());
        String previewUrl = getItem(position).getPreviewUrl(); // preview url for spotify stage 2.


        holder.albumImage.setImageResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);

        String smallImage = getItem(position).getSmallImageUrl();
        String largeImage = getItem(position).getLargeImageUrl(); // large image for spotify stage 2

        Picasso.with(getContext()).load(smallImage).resize(100, 100).into(holder.albumImage);
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.track_name_textview) TextView trackName;
        @Bind(R.id.track_album_textview) TextView albumName;
        @Bind(R.id.album_image) ImageView albumImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
