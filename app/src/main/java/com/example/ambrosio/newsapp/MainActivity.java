package com.example.ambrosio.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ambrosio.newsapp.model.Repository;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "mainactivity";
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private ProgressBar mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_news);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
//        mNewsAdapter=new NewsAdapter();
//        mRecyclerView.setAdapter(mNewsAdapter);
//        new NetworkTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        NetworkTask task = new NetworkTask();
        task.execute();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();

        if (itemNumber == R.id.search) {
            NetworkTask task = new NetworkTask();
            task.execute();
        }

        return true;
    }
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    class NetworkTask extends AsyncTask<String, Void, ArrayList<Repository>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Repository> doInBackground(String... params) {
            ArrayList<Repository> result = null;
            URL newsRequestUrl = NetworkUtils.buildUrl();

            try {
                String jsonNewsResponse = NetworkUtils.
                        getResponseFromHttpUrl(newsRequestUrl);
                result = NetworkUtils.parseJSON(jsonNewsResponse);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<Repository> newsData) {
            super.onPostExecute(newsData);
            mProgress.setVisibility(View.GONE);
            if (newsData != null) {
                NewsAdapter adapter = new NewsAdapter(newsData, new NewsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int clickedItemIndex) {
                        String url = newsData.get(clickedItemIndex).getUrl();
                        openWebPage(url);
                        Log.d(TAG, String.format("Url %s", url));
                    }
                });
                mRecyclerView.setAdapter(adapter);
            }
        }
    }
}





