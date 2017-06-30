package com.example.ambrosio.newsapp;

import android.net.Uri;
import android.util.Log;

import com.example.ambrosio.newsapp.model.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by Ambrosio on 6/19/2017.
 */

public class NetworkUtils {


    //Goal is to build this:
    // https://newsapi.org/v1/articles?
    // source=the-next-web&sortBy=latest&apiKey=e8af304f19cf454ca08413b868e1de73
    private static final String BASE_URL=
            "https://newsapi.org/v1/articles";

    private static final String source="the-next-web";
    private static final String sort="latest";
    private static final String key="e8af304f19cf454ca08413b868e1de73";

    private static final String SOURCE_PARAM="source";
    private static final String SORTBY_PARAM="sortBy";
    private static final String APIKEY_PARAM="apiKey";

    public static URL buildUrl(){
        Uri builtUri=
                Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SOURCE_PARAM,source)
                    .appendQueryParameter(SORTBY_PARAM,sort)
                    .appendQueryParameter(APIKEY_PARAM,key)
                .build();
        URL url=null;

        try{
            url=new URL (builtUri.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(TAG, "Built Uri: " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else {
                return null;
            }
        } finally{
            urlConnection.disconnect();
        }
    }
    public static ArrayList<Repository> parseJSON(String json) throws JSONException {
        ArrayList<Repository> result = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray items = main.getJSONArray("articles");

        for(int i = 0; i < items.length(); i++){
            JSONObject item = items.getJSONObject(i);
            String author = item.getString("author");
            String title = item.getString("title");
            String description = item.getString("description");
            String url = item.getString("url");
            String urlToImage = item.getString("urlToImage");
            String publishedAt= item.getString("publishedAt");
            Repository repo = new Repository( author,  title,  description,  url,  urlToImage,  publishedAt);
            result.add(repo);
        }
        return result;
    }


}
