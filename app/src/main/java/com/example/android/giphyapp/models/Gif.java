package com.example.android.giphyapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shwetasharma on 16-09-17.
 */
public class Gif {
    private String url;

    public Gif(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
