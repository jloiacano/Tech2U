package com.example.android.newsapp2;

import android.app.LoaderManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on 3/31/2017.
 * <p>
 * A {@link NewsResultsActivity} to handle the input from MainActivity which is a URL,
 * deal with connectivity to the internet, and handle the response back, if there is one, handle all
 * the callbacks from bach thread processes, and then display all the results
 */

public class NewsResultsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>>{

    // Tag for the log messages
    private final static String LOG_TAG = NewsResultsActivity.class.getSimpleName();

    //
    private String mSearchUrl = "";

    private static final int ARTICLE_LOADER_ID = 1;

    // An Adapter to handle the list of articles
    private ArticleAdapter mArticleAdapter;

    private ImageView mEmptyImageView;
    private TextView mEmptyTextView;
    private Button mEmptyButtonView;
    private ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list);

        // Get the extras that were put in the bundle from the intent to start this in LocationAdapter
        Bundle thisBundlesExtras = getIntent().getExtras();

        if (thisBundlesExtras != null) {
            mSearchUrl = thisBundlesExtras.getString("url");
        }

        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.article_listview);

        // Find a reference to the Layout, ImageView and TextView supplied in case there is no data
        // retrieved from the the api to populate the ListView
        RelativeLayout mEmptyLayout = (RelativeLayout) findViewById(R.id.alternate_results_layout);
        mEmptyImageView = (ImageView) findViewById(R.id.alternate_results_imageview);
        mEmptyTextView = (TextView) findViewById(R.id.alternate_results_textview);
        mEmptyButtonView = (Button) findViewById(R.id.set_api_key_button);

        // Set the Button view to gone so it doesnt show up with the spinner
        mEmptyButtonView.setVisibility(View.GONE);

        // Find the reference to the ProgressBar spinner and set it to the global member variable
        mSpinner = (ProgressBar) findViewById(R.id.article_loading_spinner);

        // let Android set the empty view for us
        articleListView.setEmptyView(mEmptyLayout);


        // Create a new adapter that takes an empty list of articles as input
        mArticleAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mArticleAdapter);

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().
                        getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            mSpinner.setVisibility(View.GONE);
            mEmptyImageView.setImageResource(R.drawable.no_net);
            mEmptyTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),
                    R.color.noNetColor));
            mEmptyTextView.setText(R.string.no_net);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new ArticleLoader(this, mSearchUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        //clear the spinner when there is data to populate the list.
        mSpinner.setVisibility(View.GONE);

        // Clear the adapter of previous data
        mArticleAdapter.clear();

        // set the emptyState text
        mEmptyImageView.setImageResource(R.drawable.search);
        mEmptyTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),
                R.color.noResultsColor));
        mEmptyTextView.setText(R.string.no_articles);
        mEmptyButtonView.setVisibility(View.VISIBLE);

        // When the button is clicked Copy the API key so that it can be pasted in Settings
        // which is opened along with a Toast describing what to do
        mEmptyButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString
                                (R.string.settings_api_key_key), getString(R.string.js_api_key));
                clipboard.setPrimaryClip(clip);

                Intent goToSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(goToSettings);


                Toast.makeText(getApplicationContext(),
                        getString(R.string.copy_api_toast),
                        Toast.LENGTH_LONG).show();
            }
        });

        // If there is a valid list of {@link Article}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            mArticleAdapter.addAll(articles);
        } else {
            Log.v(LOG_TAG, getResources().getString(R.string.null_results));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        mArticleAdapter.clear();
    }
}