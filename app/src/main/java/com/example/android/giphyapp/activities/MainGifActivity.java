package com.example.android.giphyapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.android.giphyapp.EndlessScrollListener;
import com.example.android.giphyapp.GifArrayAdapter;
import com.example.android.giphyapp.GiphyResponseParser;
import com.example.android.giphyapp.R;
import com.example.android.giphyapp.models.Gif;
import com.example.android.giphyapp.utils.HttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainGifActivity extends AppCompatActivity {

    private EditText etQuery;
    private GridView gvResults;
    private GifArrayAdapter adapter;
    private ArrayList<Gif> gifs = new ArrayList<>();
    private HttpClient httpClient = new HttpClient();
    private GiphyResponseParser giphyResponseParser = new GiphyResponseParser();
    private boolean searchFlag = false;
    private final static int DEFAULT_OFFSET = 0;
    private final static int MAX_GIFS_IN_RESPONSE = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gif);
        initializeView();
        loadGiphyApiTrending(DEFAULT_OFFSET);
        gvResults.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public void onLoadMore(int offset) {
                if (searchFlag) {
                    loadGiphyApiSearch(offset);
                } else {
                    int newOffset = offset * MAX_GIFS_IN_RESPONSE;
                    loadGiphyApiTrending(newOffset);
                }
            }
        });
    }

    public void initializeView() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        adapter = new GifArrayAdapter(this, gifs);
        gvResults.setAdapter(adapter);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), GifActivity.class);
                Gif gif = gifs.get(position);
                intent.putExtra("url", gif.getUrl());
                startActivity(intent);
            }
        });
    }

    public void onGifSearch(View view) {
        searchFlag = true;
        adapter.clear();
        loadGiphyApiSearch(DEFAULT_OFFSET);
        View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }
    }

    private void loadGiphyApiTrending(int offset) {
        httpClient.getTrendingGifs(offset, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                List<Gif> gifArrayList = giphyResponseParser.parseForGifs(response);
                adapter.addAll(gifArrayList);
            }
        });
    }

    public void loadGiphyApiSearch(int offset) {
        int newOffset = offset * MAX_GIFS_IN_RESPONSE;
        String searchQuery = etQuery.getText().toString();
        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                List<Gif> gifArrayList = giphyResponseParser.parseForGifs(response);
                adapter.addAll(gifArrayList);
            }
        };
        httpClient.getSearchGifs(newOffset, searchQuery, jsonHttpResponseHandler);
    }
}
