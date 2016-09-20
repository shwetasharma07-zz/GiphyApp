package com.example.android.giphyapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.giphyapp.EndlessScrollListener;
import com.example.android.giphyapp.GifArrayAdapter;
import com.example.android.giphyapp.GifBuilder;
import com.example.android.giphyapp.R;
import com.example.android.giphyapp.models.Gif;
import com.example.android.giphyapp.utils.HttpClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainGifActivity extends AppCompatActivity {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    boolean searchFlag = false;

    ArrayList<Gif> gifs = new ArrayList<>();
    GifArrayAdapter adapter;

    HttpClient httpClient = new HttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gif);
        initializeView();

        getTrendingGifs(httpClient);

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (searchFlag) {
                    customLoadMoreDataFromGiphyApiSearch(page);
                } else {
                    customLoadMoreDataFromGiphyApiTrending(page);
                }
                return true;
            }
        });
    }

    private void getTrendingGifs(HttpClient httpClient) {
        httpClient.getTrendingGifs(0, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                GifBuilder builder = new GifBuilder();
                List<Gif> gifArrayList = builder.build(response);
                adapter.addAll(gifArrayList);
            }
        });
    }

    public void customLoadMoreDataFromGiphyApiTrending(int offset) {
        int newOffset = offset * 25;
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.giphy.com/v1/gifs/trending";

        RequestParams parameters = new RequestParams();
        parameters.put("api_key", "dc6zaTOxFJmzC");
        parameters.put("offset", newOffset);

        client.get(url, parameters, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                GifBuilder builder = new GifBuilder();
                List<Gif> gifArrayList = builder.build(response);
                adapter.addAll(gifArrayList);
            }
        });

    }

    public void customLoadMoreDataFromGiphyApiSearch(int offset) {
        int newOffset = offset * 25;
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.giphy.com/v1/gifs/search";
        String searchQuery = etQuery.getText().toString();

        RequestParams parameters = new RequestParams();
        parameters.put("api_key", "dc6zaTOxFJmzC");
        parameters.put("q", searchQuery);
        parameters.put("offset", newOffset);

        client.get(url, parameters, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                GifBuilder builder = new GifBuilder();
                List<Gif> gifArrayList = builder.build(response);
                adapter.addAll(gifArrayList);
            }
        });

    }

    public void initializeView() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        adapter = new GifArrayAdapter(this, gifs);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), GifActivity.class);

                Gif gif = gifs.get(position);
                i.putExtra("url", gif.getUrl());

                startActivity(i);

            }

        });
    }

    public void onGifSearch(View view) {
        searchFlag = true;
        String searchQuery = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.giphy.com/v1/gifs/search";

        RequestParams parameters = new RequestParams();
        parameters.put("q", searchQuery);
        parameters.put("api_key", "dc6zaTOxFJmzC");

        client.get(url, parameters, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                adapter.clear();

                GifBuilder builder = new GifBuilder();
                List<Gif> gifArrayList = builder.build(response);
                adapter.addAll(gifArrayList);
            }
        });

//        gvResults.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) {
//                customLoadMoreDataFromGiphyApiSearch(page);
//                return true;
//            }
//        });

        View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }

    }
}
