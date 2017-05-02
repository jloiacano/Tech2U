package com.example.android.newsapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String NEWS_API_URL = "https://newsapi.org/v1/articles?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the preferences
        SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the necessary keys of the preferences
        Set<String> setToReturn = new HashSet<>();
        Set<String> viewsToShow = appPrefs.getStringSet(getString(R.string.multicheck_news_sources), setToReturn);
        String selectAllKey = getString(R.string.source_select_all_key);
        String arstechKey = getString(R.string.source_ars_technica_key);
        String engadgetKey = getString(R.string.source_engadget_key);
        String techcrunchKey = getString(R.string.source_techcrunch_key);
        String techradarKey = getString(R.string.source_techradar_key);
        String tnwKey = getString(R.string.source_tnw_key);
        String vergeKey = getString(R.string.source_verge_key);

        // Get the layouts of all the news sources that can be displayed
        LinearLayout arsTechnica = (LinearLayout) findViewById(R.id.ars_technica_linear_layout);
        LinearLayout engadget = (LinearLayout) findViewById(R.id.engadget_linear_layout);
        LinearLayout techCrunch = (LinearLayout) findViewById(R.id.techcrunch_linear_layout);
        LinearLayout techRadar = (LinearLayout) findViewById(R.id.techradar_linear_layout);
        LinearLayout theNextWeb = (LinearLayout) findViewById(R.id.tnw_linear_layout);
        LinearLayout verge = (LinearLayout) findViewById(R.id.verge_linear_layout);


        // Check the preference keys to see which sources should be displayed
        if (viewsToShow.contains(selectAllKey)) {
            // Do nothing and leave all the Views as they are
        } else {
            if (!viewsToShow.contains(arstechKey)) {
                arsTechnica.setVisibility(View.GONE);
            }
            if (!viewsToShow.contains(engadgetKey)) {
                engadget.setVisibility(View.GONE);
            }
            if (!viewsToShow.contains(techcrunchKey)) {
                techCrunch.setVisibility(View.GONE);
            }
            if (!viewsToShow.contains(techradarKey)) {
                techRadar.setVisibility(View.GONE);
            }
            if (!viewsToShow.contains(tnwKey)) {
                theNextWeb.setVisibility(View.GONE);
            }
            if (!viewsToShow.contains(vergeKey)) {
                verge.setVisibility(View.GONE);
            }

            // If there are no Views, show the alternate "go to settings" page with reasons.
            if (viewsToShow.isEmpty()) {
                LinearLayout goToSettings = (LinearLayout) findViewById(R.id.go_to_settings);
                goToSettings.setVisibility(View.VISIBLE);

                if (!appPrefs.getString(getString(R.string.settings_api_key_key),
                        getString(R.string.settings_api_key_key)).equals("")) {
                    TextView goToSettingsAPITextView = (TextView)
                            findViewById(R.id.go_to_settings_api_textview);
                    goToSettingsAPITextView.setVisibility(View.GONE);
                } else {
                    TextView goToSettingsSourcesTextView = (TextView)
                            findViewById(R.id.go_to_settings_sources_textview);
                    goToSettingsSourcesTextView.setVisibility(View.GONE);
                }


            }
        }

        // Set onClickListeners for each of the sources to load the appropriate list of articles
        arsTechnica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticles("ars-technica");
            }
        });

        engadget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticles("engadget");
            }
        });

        techCrunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticles("techcrunch");
            }
        });

        techRadar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticles("techradar");
            }
        });

        theNextWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticles("the-next-web");
            }
        });

        verge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticles("the-verge");
            }
        });
    }

    // Build the URI and open the appropriate news source list
    private void openArticles(String newsSource) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String apiKey = sharedPrefs.getString(
                getString(R.string.settings_api_key_key),
                getString(R.string.settings_enter_api_key));
        String sortBy = sharedPrefs.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default));

        Uri baseUri = Uri.parse(NEWS_API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("source", newsSource);
        uriBuilder.appendQueryParameter("sortBy", sortBy);
        uriBuilder.appendQueryParameter("apiKey", apiKey);

        Intent openNewsResultsView =
                new Intent(getApplicationContext(), NewsResultsActivity.class);
        Bundle theNewsSource = new Bundle();
        theNewsSource.putString("url", uriBuilder.toString());
        openNewsResultsView.putExtras(theNewsSource);
        startActivity(openNewsResultsView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

