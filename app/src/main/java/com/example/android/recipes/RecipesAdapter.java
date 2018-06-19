package com.example.android.recipes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecipesAdapter extends ArrayAdapter<Recipes> {

    private Context mContext;

    public RecipesAdapter(Context context, List<Recipes> recipes) {
        super(context, 0, recipes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Recipes currentRecipes = getItem(position);
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentRecipes.getTitle());
        TextView section = (TextView) listItemView.findViewById(R.id.section);
        section.setText(currentRecipes.getSection());
        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(currentRecipes.getAutor());
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(currentRecipes.getDate());
        ImageView thumbnail = (ImageView) listItemView.findViewById(R.id.image);
        String imageUrl = currentRecipes.getUrl();
        Picasso.get()
                .load(currentRecipes.getThumbnail())
                .into(thumbnail);

        SimpleDateFormat dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EE dd MMM yyyy", Locale.ENGLISH);

        try {
            Date dateNews = dateFormatJSON.parse(currentRecipes.getDate());
            String recipeDate = dateFormat2.format(dateNews);
            date.setText(recipeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return listItemView;
    }
}
