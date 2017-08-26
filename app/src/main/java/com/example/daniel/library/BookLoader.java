package com.example.daniel.library;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Daniel on 6/22/2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Books>>{
    private String url;

    BookLoader(Context context, String url){
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {
        Log.i("INSIDE OF :", "loadInBackground");
        if(url == null){
            return null;
        }
        List<Books> result = QueryUtils.fetchBookData(url);
        return result;
    }
}
