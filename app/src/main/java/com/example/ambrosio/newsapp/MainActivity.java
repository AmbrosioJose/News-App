package com.example.ambrosio.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mNewsTextView;
    private ProgressBar mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsTextView= (TextView) findViewById(R.id.tv_news_data);
        mProgress= (ProgressBar) findViewById(R.id.progressBar);
        //mSearchBoxEdit=(EditText) findViewById(R.id.search_box);

        new NetworkTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();
        if(itemNumber == R.id.search){

            mNewsTextView.setText("");
            new NetworkTask().execute();
            return true;

            //makes toast
//            Toast.makeText(this, "A toast to you!", Toast.LENGTH_LONG).show();


        }

        return super.onOptionsItemSelected(item);
    }

    public class NetworkTask extends AsyncTask<String, Void, String>{

        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            URL newsRequestUrl= NetworkUtils.buildUrl();

            try{
                String jsonNewsResponse = NetworkUtils.
                        getResponseFromHttpUrl(newsRequestUrl);


                return jsonNewsResponse;
            }catch (Exception e){
                e.printStackTrace();
            return null;
            }

        }

        @Override
        protected void onPostExecute(String newsData){
            //with search
//            if(newsData != null && !newsData.equals("")){
//                mNewsTextView.setText(newsData);
//            }

//            without search
            super.onPostExecute(newsData);
            mProgress.setVisibility(View.GONE);
            if(newsData == null){
                mNewsTextView.setText("Sorry, not news was recieved");
            }else{
                mNewsTextView.append(newsData);
            }
        }
    }
}
