package com.example.android.recipes;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipes>> {

    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?show-fields=byline%2Cthumbnail";

    private static final int RECIPES_LOADER_ID = 1;

    private RecipesAdapter mAdapter;
    private TextView emptyStateTextView;
    private ImageView emptyStateImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView recipesListView = (ListView) findViewById(R.id.list_item);
        View emptyLayout = (View) findViewById(R.id.empty_view);
        emptyStateImageView = (ImageView) findViewById(R.id.empty_image);
        emptyStateTextView = (TextView) findViewById(R.id.empty_text);
        recipesListView.setEmptyView(emptyLayout);

        mAdapter = new RecipesAdapter(this, new ArrayList<Recipes>());
        recipesListView.setAdapter(mAdapter);
        recipesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipes currentRecipes = mAdapter.getItem(position);
                Uri recipesUri = Uri.parse(currentRecipes.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, recipesUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(RECIPES_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.progress);
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_connection);
            emptyStateImageView.setImageResource(R.drawable.noconnection);
        }
    }

    @Override
    public Loader<List<Recipes>> onCreateLoader(int id, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String maxRecipes = sharedPrefs.getString(getString(R.string.settings_max_recipes_key),
                getString(R.string.settings_max_recipes_default));

        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("from-date", "2017-01-01");
        uriBuilder.appendQueryParameter("q", "recipes");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", maxRecipes);
        uriBuilder.appendQueryParameter("api-key", "0869c196-c93c-4456-b45c-d6df75e35f16");

        return new RecipesLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Recipes>> loader, List<Recipes> recipes) {
        View loadingIndicator = findViewById(R.id.progress);
        loadingIndicator.setVisibility(View.GONE);
        emptyStateTextView.setText(R.string.no_recipes);
        emptyStateImageView.setImageResource(R.drawable.news);
        mAdapter.clear();

        if (recipes != null && !recipes.isEmpty()) {
            View emptyLayout = (View) findViewById(R.id.empty_view);
            emptyLayout.setVisibility(View.GONE);
            mAdapter.addAll(recipes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipes>> loader) {
        loader.reset();
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
