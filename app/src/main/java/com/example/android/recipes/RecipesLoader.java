package com.example.android.recipes;

import android.content.Context;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class RecipesLoader extends AsyncTaskLoader<List<Recipes>> {
    private String mUrl;
    private List<Recipes> recipes;

    public RecipesLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Recipes> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        recipes = QueryUtils.fetchRecipes(mUrl);
        return recipes;
    }
}
