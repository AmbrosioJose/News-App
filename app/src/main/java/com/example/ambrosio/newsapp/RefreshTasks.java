package com.example.ambrosio.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.ambrosio.newsapp.model.DBHelper;
import com.example.ambrosio.newsapp.model.DatabaseUtils;
import com.example.ambrosio.newsapp.model.NewsItem;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ambrosio on 7/28/2017.
 */

//Deletes and repopulates the database with news items
public class RefreshTasks {
    public static final String ACTION_REFRESH = "refresh";


    public static void refreshArticles(Context context) {
        ArrayList<NewsItem> result = null;
        URL url = NetworkUtils.buildUrl();

        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();

        try {
            //deletes everything from database
            DatabaseUtils.deleteAll(db);

            //gets Response from url and then parses the results
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            result = NetworkUtils.parseJSON(json);

            //inserts data in ArrayList into database
            DatabaseUtils.insertMany(db, result);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
    }
}
