package com.example.android.giphyapp.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

 public class HttpClient extends AsyncHttpClient {

    public void getTrendingGifs(int offset, JsonHttpResponseHandler responseHandler) {
        RequestParams parameters = new RequestParams();
        parameters.put("api_key", "dc6zaTOxFJmzC");
        parameters.put("offset", offset);

        super.get("http://api.giphy.com/v1/gifs/trending", parameters, responseHandler);
    }

    public void getSearchGifs(int offset, String searchQuery, JsonHttpResponseHandler responseHandler) {
        RequestParams parameters = new RequestParams();
        parameters.put("api_key", "dc6zaTOxFJmzC");
        parameters.put("q", searchQuery);
        parameters.put("offset", offset);

        super.get("http://api.giphy.com/v1/gifs/search", parameters, responseHandler);
    }
}
