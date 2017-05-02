package com.example.android.newsapp2;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by J on 3/31/2017.
 *
 * Loads a list of articles by using an AsyncTask to perform the network
 * request to the designated news source's api
 */

class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private final String mUrl;

    /**
     * Constructs a new {@link ArticleLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    ArticleLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Background thread operation
     */
    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of articles.
        return HttpQueryUtilities.fetchJSONStringData(mUrl);
    }
}
