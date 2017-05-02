package com.example.android.newsapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by J on 4/3/2017.
 *
 * Handles the settings.
 */
public class SettingsActivity extends AppCompatActivity {

    // A tag for log messages
    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    // Where all the preferences happen
    public static class Tech2UPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // Not technically a preference, this opens an attribution page for the API provider
            Preference provider = findPreference(getString(R.string.settings_newsapi_key));
            provider.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {//
                    Intent openAttribution =
                            new Intent(getActivity(), Attribution.class);
                    startActivity(openAttribution);
                    return true;
                }
            });

            // If the user doesn't have an API key (necessary) opens the website to get one
            Preference getApiKey = findPreference(getString(R.string.settings_get_api_key));
            getApiKey.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent openAPIgetterWebPage = new Intent(Intent.ACTION_VIEW);
                    openAPIgetterWebPage.setData(Uri.parse("https://newsapi.org/register"));
                    startActivity(openAPIgetterWebPage);

                    return true;
                }
            });

            // Sets the summary to the API key value
            Preference enteredApiKey = findPreference(getString(R.string.settings_api_key_key));
            bindPreferenceSummaryToValue(enteredApiKey);

            // Checks the preferences for which news sources are 'checked' to follow
            MultiSelectListPreference sourceForCheckBoxes = (MultiSelectListPreference)
                    findPreference(getString(R.string.multicheck_news_sources));
            Set<String> currentValues = sourceForCheckBoxes.getValues();
            Set<String> setString = checkForSelectAll(currentValues);
            sourceForCheckBoxes.setValues(setString);

            // Sets the summary to the desired sort order
            Preference sortBy = findPreference(getString(R.string.settings_sort_by_key));
            bindPreferenceSummaryToValue(sortBy);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }

        // For setting the summary value of the setting on the Settings page
        private void bindPreferenceSummaryToValue(Preference preference) {
            String preferenceString = null;
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            if (preference.equals(findPreference(getString(R.string.settings_api_key_key)))) {
                preferenceString = getString(R.string.settings_entered) + preferences.getString(preference.getKey(), "");
            }
            else if (preference.equals(findPreference(getString(R.string.settings_sort_by_key)))) {
                preferenceString = preferences.getString(preference.getKey(), "");
            }
            onPreferenceChange(preference, preferenceString);
        }

        // Checks the check boxes in the "Choose News Sources to View" dialog to find out if
        // "VIEW ALL SOURCES" is checked, and if it is, check all the checkboxes
        private Set<String> checkForSelectAll(Set<String> theSet){

            Set<String> theSetToReturn = new HashSet<>();

            String allSelected = getString(R.string.source_select_all_key);
            String arstechnicaSelected = getString(R.string.source_ars_technica_key);
            String engadgetSelected = getString(R.string.source_engadget_key);
            String techcrunchSelected = getString(R.string.source_techcrunch_key);
            String techradarSelected = getString(R.string.source_techradar_key);
            String tnwSelected = getString(R.string.source_tnw_key);
            String vergeSelected = getString(R.string.source_verge_key);

           if (theSet.contains(allSelected)){
                theSetToReturn.add(allSelected);
                theSetToReturn.add(arstechnicaSelected);
                theSetToReturn.add(engadgetSelected);
                theSetToReturn.add(techcrunchSelected);
                theSetToReturn.add(techradarSelected);
                theSetToReturn.add(tnwSelected);
                theSetToReturn.add(vergeSelected);

            } else if (theSet.size() == 6){
                theSetToReturn.add(allSelected);
                theSetToReturn.add(arstechnicaSelected);
                theSetToReturn.add(engadgetSelected);
                theSetToReturn.add(techcrunchSelected);
                theSetToReturn.add(techradarSelected);
                theSetToReturn.add(tnwSelected);
                theSetToReturn.add(vergeSelected);
            }
            // might be more efficient/concise to use an iterator here
            // must learn about those 'next' (pun intended)
            else {
                if (theSet.contains(arstechnicaSelected)) {
                    theSetToReturn.add(arstechnicaSelected);
                }
                if (theSet.contains(engadgetSelected)) {
                    theSetToReturn.add(engadgetSelected);
                }
                if (theSet.contains(techcrunchSelected)) {
                    theSetToReturn.add(techcrunchSelected);
                }
                if (theSet.contains(techradarSelected)) {
                    theSetToReturn.add(techradarSelected);
                }
                if (theSet.contains(tnwSelected)) {
                    theSetToReturn.add(tnwSelected);
                }
                if (theSet.contains(vergeSelected)) {
                    theSetToReturn.add(vergeSelected);
                }
            }
            return theSetToReturn;
        }
    }
}