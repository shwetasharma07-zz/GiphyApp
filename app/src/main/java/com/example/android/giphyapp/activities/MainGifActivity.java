package com.example.android.giphyapp.activities;

import android.content.Context;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.giphyapp.GifArrayAdapter;
import com.example.android.giphyapp.GifBuilder;
import com.example.android.giphyapp.R;
import com.example.android.giphyapp.models.Gif;
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

    ArrayList<Gif> gifs = new ArrayList<>();
    GifArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gif);
        initializeView();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.giphy.com/v1/gifs/trending";

        RequestParams parameters = new RequestParams();
        parameters.put("api_key", "dc6zaTOxFJmzC");

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
    }

    public void onGifSearch(View view) {
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
        View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }

    }
}
