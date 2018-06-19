package com.example.android.recipes;

import android.graphics.drawable.Drawable;
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

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String response = "response";
    private static final String results = "results";
    private static final String section = "sectionName";
    private static final String date = "webPublicationDate";
    private static final String title = "webTitle";
    private static final String url = "webUrl";
    private static final String fields = "fields";
    private static final String byline = "byline";
    private static final String thumbnail = "thumbnail";

    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static List<Recipes> fetchRecipes(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Recipes> recipes = extractFeatureFromJson(jsonResponse);
        return recipes;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

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


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

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

    private static List<Recipes> extractFeatureFromJson(String recipesJSON) {
        if (TextUtils.isEmpty(recipesJSON)) {
            return null;
        }
        List<Recipes> recipes = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(recipesJSON);
            JSONObject responseJson = baseJsonResponse.getJSONObject(response);
            JSONArray recipesArray = responseJson.getJSONArray(results);
            for (int i = 0; i < recipesArray.length(); i++) {
                JSONObject currentRecipes = recipesArray.getJSONObject(i);
                String recipesSection = currentRecipes.getString(section);
                String recipesDate = currentRecipes.getString(date);
                String recipesTitle = currentRecipes.getString(title);
                String recipesUrl = currentRecipes.getString(url);
                JSONObject field = currentRecipes.getJSONObject(fields);
                String recipesAuthor = field.getString(byline);
                String recipesThumbnail = field.getString(thumbnail);
                Recipes recipe = new Recipes(recipesTitle, recipesAuthor, recipesSection, recipesDate, recipesUrl, recipesThumbnail);
                recipes.add(recipe);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return recipes;
    }


}
