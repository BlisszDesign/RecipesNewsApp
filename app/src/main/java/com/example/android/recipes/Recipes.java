package com.example.android.recipes;

import android.graphics.drawable.Drawable;

public class Recipes {

    private String mRecipesTitle;
    private String mRecipesAuthor;
    private String mRecipesSection;
    private String mRecipesDate;
    private String mRecipesUrl;
    private String mThumbnail;

    public Recipes(String title, String author, String section, String date, String url, String thumbnail) {
        mRecipesTitle = title;
        mRecipesAuthor = author;
        mRecipesSection = section;
        mRecipesDate = date;
        mRecipesUrl = url;
        mThumbnail = thumbnail;
    }

    public String getTitle() {
        return mRecipesTitle;
    }

    public String getAutor() {
        return mRecipesAuthor;
    }

    public String getSection() {
        return mRecipesSection;
    }

    public String getDate() {
        return mRecipesDate;
    }

    public String getUrl() {
        return mRecipesUrl;
    }

    public String getThumbnail() {
        return mThumbnail;
    }
}
