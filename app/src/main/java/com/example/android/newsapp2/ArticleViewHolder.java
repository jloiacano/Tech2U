package com.example.android.newsapp2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by J on 3/31/2017.
 *
 * An {@link ArticleViewHolder} to make ListView scrolling more responsive
 */

class ArticleViewHolder{

    final ImageView mVHArticleImage;
    final TextView mVHArticleTitle;
    final TextView mVHArticleAuthor;
    final TextView mVHArticleTimePast;
    final TextView mVHArticlePublishedDate;
    final TextView mVHArticleDescription;

    /**
     * An ArticleViewHolder constructor
     * @param view the View of one Article to be reused
     */
    ArticleViewHolder(View view) {
        mVHArticleImage = (ImageView) view.findViewById(R.id.article_image);
        mVHArticleTitle = (TextView) view.findViewById(R.id.article_title);
        mVHArticleAuthor = (TextView) view.findViewById(R.id.article_author);
        mVHArticleTimePast = (TextView) view.findViewById(R.id.time_quantity);
        mVHArticlePublishedDate = (TextView) view.findViewById(R.id.article_published_date);
        mVHArticleDescription = (TextView) view.findViewById(R.id.article_description);
    }
}
