package com.example.ambrosio.newsapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.example.ambrosio.newsapp.model.Contract.TABLE_NEWS.*;

/**
 * Created by Ambrosio on 7/28/2017.
 */

public class DatabaseUtils {

    //gets all news items from db ordered by date in descending order
    public static Cursor getAllItems(SQLiteDatabase db) {
        return db.query(
                Contract.TABLE_NEWS.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contract.TABLE_NEWS.COLUMN_NAME_PUBLISHED_AT + " DESC"
        );
    }

    //fills the database with News items store in an ArrayList
    public static void insertMany(SQLiteDatabase db, ArrayList<NewsItem> articles) {

        db.beginTransaction();
        try {
            for (NewsItem a : articles) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME_AUTHOR, a.getAuthor());
                cv.put(COLUMN_NAME_TITLE, a.getTitle());
                cv.put(COLUMN_NAME_DESCRIPTION, a.getDescription());
                cv.put(COLUMN_NAME_URL, a.getUrl());
                cv.put(COLUMN_NAME_URL_TO_IMAGE, a.getUrlToImage());
                cv.put(COLUMN_NAME_PUBLISHED_AT, a.getPublishedAt());
                db.insertWithOnConflict(TABLE_NAME, null, cv,SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

//    public int updateNewsItem(SQLiteDatabase db, String author, String title, String description, String url, String urlToImage, String publishedAt,long id){
//
//
//        ContentValues cv = new ContentValues();
//        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_AUTHOR, author);
//        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_TITLE, title);
//        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION, description);
//        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_URL, url);
//        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_URL_TO_IMAGE, urlToImage);
//        cv.put(Contract.TABLE_NEWS.COLUMN_NAME_PUBLISHED_AT, publishedAt);
//
//        return db.update(Contract.TABLE_NEWS.TABLE_NAME, cv, Contract.TABLE_NEWS._ID + "=" + id, null);
//    }
}
