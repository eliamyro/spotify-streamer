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
        int imageWidth = 0;
        String smallImage = null;
        String largeImage = null; // large image for spotify stage 2
        if (holder.albumImage != null && getItem(position).getAlbumImages().size() != 0) {
//            for (int i = 0; i < getItem(position).getAlbumImages().size(); i++) {
//                imageWidth = getItem(position).getAlbumImages().get(i).width;
//                if (imageWidth == 200) {
//                    smallImage = getItem(position).album.images.get(i).url;
//                } else if (imageWidth == 600) {
//                    largeImage = getItem(position).album.images.get(i).url;
//                } else {
//                    // We haven't found image with the desired width and we take the first image.
//                    smallImage = getItem(position).album.images.get(0).url;
//                    largeImage = getItem(position).album.images.get(0).url;
//                }
                Picasso.with(getContext()).load(getItem(position).getAlbumImages().get(0)).resize(200, 200).into(holder.albumImage);
            //}
        }
        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.track_name_textview)TextView trackName;
        @InjectView(R.id.track_album_textview)TextView albumName;
        @InjectView(R.id.album_image)ImageView albumImage;

        public ViewHolder(View view){
            ButterKnife.inject(this,view);
        }

    }
}
