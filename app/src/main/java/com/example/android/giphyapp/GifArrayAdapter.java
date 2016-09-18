package com.example.android.giphyapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.giphyapp.models.Gif;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shwetasharma on 16-09-17.
 */
public class GifArrayAdapter extends ArrayAdapter<Gif> {
    public GifArrayAdapter (Context context, List<Gif> gifs) {
        super (context, android.R.layout.simple_list_item_1, gifs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gif gif = this.getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_gif_result, parent, false);

        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        imageView.setImageResource(0);

        String url = gif.getUrl();
        Picasso.with(getContext()).load(url).into(imageView);
        Picasso.with(getContext()).load(url).fit().placeholder(R.mipmap.ic_launcher).into(imageView);

        return convertView;

    }
}
