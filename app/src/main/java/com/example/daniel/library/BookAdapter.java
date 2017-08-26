package com.example.daniel.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Daniel on 6/22/2017.
 */

public class BookAdapter extends ArrayAdapter<Books> {
    private Context context;
    private int resource;
    private ArrayList<Books> books;

    public BookAdapter(Context context, int resource, ArrayList<Books> books) {
        super(context, resource, books);
        this.context = context;
        this.resource = resource;
        this.books = books;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView title = (TextView) listItemView.findViewById(R.id.title_textView);
        TextView author = (TextView) listItemView.findViewById(R.id.author_textView);
        TextView date = (TextView) listItemView.findViewById(R.id.date_textView);
        TextView description = (TextView) listItemView.findViewById(R.id.description_textView);
        ImageView preview = (ImageView) listItemView.findViewById(R.id.book_imageView);

        title.setText(books.get(position).getTitle());
        author.setText(books.get(position).getAuthor());
        date.setText(books.get(position).getDate());
        description.setText(books.get(position).getDescription());
        preview.setImageBitmap(books.get(position).getImg());


        return listItemView;
    }
}
