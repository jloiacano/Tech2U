package com.example.android.newsapp2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by J on 4/3/2017.
 *
 * A simple attribution to NewsAPI.org for letting me use their API service (also, they require it)
 */

public class Attribution extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attribution);

        // Opens the NewsAPI.org website
        ImageView newsapi = (ImageView) findViewById(R.id.newsapi_logo);
        newsapi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openNewsApiWebsite = new Intent(Intent.ACTION_VIEW);
                openNewsApiWebsite.setData(Uri.parse("http://newsapi.org"));
                startActivity(openNewsApiWebsite);
            }
        });
    }
}
