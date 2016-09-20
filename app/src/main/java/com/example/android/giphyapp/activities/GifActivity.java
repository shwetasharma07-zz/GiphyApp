package com.example.android.giphyapp.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.android.giphyapp.R;

public class GifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        String url = getIntent().getStringExtra("url");

        ImageView imageView = (ImageView) findViewById(R.id.overlayGif);
        Glide.with(this).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.mipmap.ic_launcher).centerCrop().into(imageView);

    }
}
