package com.example.ambrosio.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.example.ambrosio.newsapp.model.DBHelper;
import com.example.ambrosio.newsapp.model.DatabaseUtils;
import com.example.ambrosio.newsapp.model.NewsItem;




public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>,NewsAdapter.ItemClickListener {
    static final String TAG = "mainactivity";
    private RecyclerView mRecyclerView;
    private static DBHelper helper;
    private Cursor cursor;
    private static SQLiteDatabase db;
    private ProgressBar mProgress;
    private int NEWS_LOADER_ID = 0;
    private Bundle bundleForLoader = null;
    private NewsAdapter adapter;
    final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_news);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);



        helper = new DBHelper(this);

        //gets a writable databasse
        db = helper.getWritableDatabase();

        //gets a shared preference file titled MyPrefsFile and opens it
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        //Attempts to retrieve a boolean from MyPrefsFile if it does not exist it will return true
        boolean isFirst=settings.getBoolean("my_first_time", true);

        //If its the first time using this app the loader will be executed
        //otherwise cursor will retrieve newsItems from database
        if (isFirst) {
            Log.d("Comments", "First time");
            //Executes loader which will delete and repopulate the database with news items
            //Then
            load();
            //puts and saves a boolean titled "my_fist_time" and sets it as false int MyPrefsFile.
            settings.edit().putBoolean("my_first_time", false).commit();

        }else{
            //cursor retrieves news items from database
            cursor = DatabaseUtils.getAllItems(db);
            //makes progress barr invisible
            mProgress.setVisibility(View.INVISIBLE);
            Log.d("Comments", "not Frist time");
        }



        ScheduleUtilities.scheduleRefresh(this);

    }

    //closes the database and cursor
    @Override
    protected void onStop() {
        super.onStop();

        if (db != null) db.close();
        if (cursor != null) cursor.close();

    }

    @Override
    protected void onStart() {
        super.onStart();
        helper = new DBHelper(MainActivity.this);
        db = helper.getReadableDatabase();

        cursor = DatabaseUtils.getAllItems(db);


        adapter = new NewsAdapter(cursor, this);

        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    // If the search button is pressed it will execute the loader
    //which will "refresh" the database and recyclerview
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();
        if (itemNumber == R.id.search) {
            load();
        }

        return true;
    }



    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {



        return new AsyncTaskLoader<Void>(this) {

            @Override
            public void onStartLoading() {
                super.onStartLoading();
                //makes progress bar visible
                mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public Void loadInBackground() {
                //Deletes all news items and repopulates them in database
                RefreshTasks.refreshArticles(MainActivity.this);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader,Void data) {
        mProgress.setVisibility(View.INVISIBLE);

        //gets a readable database
        db= new DBHelper(MainActivity.this).getReadableDatabase();
        //cursor gets all the news items from database
        cursor = DatabaseUtils.getAllItems(db);
        // new adapter created and set to the recycler view
        adapter = new NewsAdapter(cursor, this);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {
    }

    //once news item is clicked it will attempt to open the webpage
    @Override
    public void onItemClick(int clickedItemIndex, String url) {

        openWebPage(url);
        Log.d(TAG, String.format("Url %s + clickedItemIndex %s", url ,clickedItemIndex));
    }

    // calls the intent to open a web page to the url passed in
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    //Executes the loader
    public void load() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER_ID, bundleForLoader, this).forceLoad();

    }





}
