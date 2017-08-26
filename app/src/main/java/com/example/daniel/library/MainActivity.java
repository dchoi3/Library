package com.example.daniel.library;

import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
                            implements LoaderManager.LoaderCallbacks<List<Books>>,
                                SearchView.OnQueryTextListener {
    private BookAdapter bookAdapter;
    private ArrayList<Books> booksArrayList;
    private TextView mEmptyTextView;
    private ProgressBar mProgressBar;
    private static final int LIBRARY_LOADER_ID = 1;
    private String urlLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Vibrator vb = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final ListView bookListView = (ListView) findViewById(R.id.book_listView);
        urlLink = "https://www.googleapis.com/books/v1/volumes?q=&maxResults=40";

        bookAdapter = new BookAdapter(this, R.layout.book_item_view, new ArrayList<Books>());
        bookListView.setAdapter(bookAdapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vb.vibrate(10);
                Books book = bookAdapter.getItem(position);
                String title;
                if(book.getTitle()!= null)title = book.getTitle();
                else title = "No title found";
                Snackbar.make(view, title, Snackbar.LENGTH_LONG).show();
            }
        });
        bookListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                vb.vibrate(50);
                Books currentBook = bookAdapter.getItem(position);
                if(currentBook.getPreviewURL()!= null) {
                    Uri bookUri = Uri.parse(currentBook.getPreviewURL());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                    startActivity(websiteIntent);
                }else{
                    Snackbar.make(view, "URL not found!", Snackbar.LENGTH_LONG).show();
                }
                return false;
            }
        });

        booksArrayList = new ArrayList<Books>();

        mEmptyTextView = (TextView) findViewById(R.id.emptyView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        bookListView.setEmptyView(mEmptyTextView);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LIBRARY_LOADER_ID, null, this);
        }else{
            mProgressBar.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_internet_connection);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            //Toast.makeText(this, "bloop", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        Log.i("Inside:", " onCreateLoader");
        bookAdapter.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyTextView.setText("");
        return new BookLoader(this, urlLink);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> data) {
        Log.i("Inside:", " onLoadFinished");
        bookAdapter.clear();
        if(data != null && !data.isEmpty()){
            bookAdapter.addAll(data);
        }
        mProgressBar.setVisibility(View.GONE);
        mEmptyTextView.setText(R.string.no_results);
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        Log.i("Inside:", " onLoadReset");
        bookAdapter.clear();
    }

    @Override
    public boolean onQueryTextSubmit(String q) {
        urlLink = "https://www.googleapis.com/books/v1/volumes?q="+q+"&maxResults=40";
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.restartLoader(LIBRARY_LOADER_ID, null, this);
        //Toast.makeText(getApplicationContext(), "query= "+query, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
