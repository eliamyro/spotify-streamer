package com.eliasmyronidis.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Elias Myronidis on 8/6/2015.
 */
public class ArtistsAdapter extends ArrayAdapter<CustomArtist> {

    public ArtistsAdapter(Context context, ArrayList<CustomArtist> customArtistsList) {
        super(context, R.layout.artist_list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.artist_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.artistName.setText(getItem(position).getArtistName());
        holder.artistImage.setImageResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
        if (holder.artistImage != null && getItem(position).getArtistImages().size() != 0) {
            Picasso.with(getContext()).load(getItem(position).getArtistImages().get(0)).resize(200, 200).into(holder.artistImage);
        }
        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.artist_name_textview)TextView artistName;
        @InjectView(R.id.artist_image)ImageView artistImage;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
