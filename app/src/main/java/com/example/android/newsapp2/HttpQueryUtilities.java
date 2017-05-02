package com.example.android.newsapp2;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on 3/31/2017.
 * <p>
 * This is all the Utilities needed to make an http query
 * All variables that could be, have been intentionally left generic so that the code can be copied
 * and pasted for future projects which need the same functionality and minimal changes would be
 * needed to update it, most of which are in extractInformationFromJson().
 */

public final class HttpQueryUtilities extends AppCompatActivity {

    // Tag for the log messages
    private static final String LOG_TAG = HttpQueryUtilities.class.getSimpleName();

    // A private empty constructor so that HttpQueryUtilities cannot be instantiated
    public HttpQueryUtilities() {
    }

    // Query the specified URL to retrieve data from that server.
    // Replace the List<OBJECT_TYPE> with the appropriate object type.
    public static List<Article> fetchJSONStringData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of the appropriate
        // Object type. Most variable name changes will be necessary in the
        // extractInformationFormJson() method to handle the appropriate query.
        // Return the list of {@link Article)
        return extractInformationFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given String URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     *  Make an HTTP request to the given URL and return a String (JSON) as the response.
     * @param url The URL object with the necessary request url
     * @return The JSON response from the server at the other end of the URL
     * @throws IOException if there is an issue retrieving the JSON response from the server
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     *
     * @param inputStream received from the server query
     * @return response from the server in String format
     * @throws IOException If there are any issues getting the stream from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of objects that has been built up from
     * parsing the given JSON response.
     * This is where many future changes will be needed to customize to given Object needs.
     */
    private static List<Article> extractInformationFromJson(String thisJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(thisJSON)) {
            return null;
        }

        // Create an empty ArrayList to start adding Objects to
        List<Article> listOfObjects = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject jsonRawResponse = new JSONObject(thisJSON);

            // Extract the JSONArray associated with the key called "articles",
            // which represents a list of articles.
            JSONArray jsonArticleArray = jsonRawResponse.getJSONArray("articles");

            // For each base item in the jsonArray, create an object to send back to the list
            for (int i = 0; i < jsonArticleArray.length(); i++) {

                // Get a single Article at position i within the jsonArticleArray
                JSONObject currentJSONArticle = jsonArticleArray.getJSONObject(i);

                String authors;
                String title;
                String description;
                String articleUrl;
                String articleImageUrl;
                String retrievedPublishedDate;

                title = currentJSONArticle.getString("title");
                authors = currentJSONArticle.getString("author");
                description = currentJSONArticle.getString("description");
                articleUrl = currentJSONArticle.getString("url");
                articleImageUrl = currentJSONArticle.getString("urlToImage");
                retrievedPublishedDate = currentJSONArticle.getString("publishedAt");

                // Create a new object with the args retrieved from the JSON response
                Article anObject = new Article(authors, title, description, articleUrl, articleImageUrl, retrievedPublishedDate);

                // Add the new Object to the list of Objects.
                listOfObjects.add(anObject);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the Article JSON results", e);
        }

        // Return the list of Objects
        return listOfObjects;
    }

}