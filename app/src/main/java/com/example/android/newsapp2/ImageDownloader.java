package com.example.android.newsapp2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by J on 3/31/2017.
 *
 * A basic {@link ImageDownloader} for downloading images from the web
 */

class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

    // A tag for the log messages
    private static final String LOG_TAG = ImageDownloader.class.getSimpleName();

    // Stores the ImageView to set the returned image to
    private final ImageView mTheImage;

    /**
     * Constructor to retrieve the image from the internet
     * @param imageView is the view to be filled by the retrieved image
     */
    ImageDownloader(ImageView imageView) {mTheImage = imageView;}


    @Override
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap imageToReturn = null;
        //try to get the image at the url parameter
        try {
            InputStream inStream = new java.net.URL(url).openStream();
            imageToReturn = BitmapFactory.decodeStream(inStream);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error retrieving image", e);
        }
        return imageToReturn;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        // Once we have the image, post it there.
        mTheImage.setImageBitmap(bitmap);
    }
}
