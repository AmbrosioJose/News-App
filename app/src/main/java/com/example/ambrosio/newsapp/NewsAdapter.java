package com.example.ambrosio.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ambrosio.newsapp.model.Contract;
import com.squareup.picasso.Picasso;




/**
 * Created by Ambrosio on 6/27/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    private Cursor cursor;
    private ItemClickListener listener;
    private Context context;
    public static final String TAG = "myadapter";

    public NewsAdapter(Cursor cursor, ItemClickListener listener){
        this.cursor = cursor;
        this.listener=listener;

    }


    public interface ItemClickListener {
        //passes the item clicked index and the url of news item that was clicked on
        void onItemClick(int clickedItemIndex,String url);
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context= viewGroup.getContext();
        int layoutIdForListItem=R.layout.news_list_item;
        LayoutInflater inflater= LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately= false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NewsAdapterViewHolder holder = new NewsAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView nameTV;
        TextView descriptionTV;
        TextView timeTV;
        ImageView urlIV;
        long id;
        String name;
        String description;
        String time;
        String urlToImage;
        String url;



        NewsAdapterViewHolder(View view){
            super(view);
            nameTV = (TextView)view.findViewById(R.id.name);
            descriptionTV = (TextView)view.findViewById(R.id.description);
            timeTV = (TextView)view.findViewById(R.id.time);
            urlIV = (ImageView)view.findViewById(R.id.image);
            view.setOnClickListener(this);

            }

        public void bind(int pos) {
            cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_NEWS._ID));

            //gets the string value of title from database
            name = cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_TITLE));
            //gets the string value of description from database
            time= cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_PUBLISHED_AT));
            //gets the string value of publishedAt from database
            description= cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION));

            //gets the string value of urlToImage from database
            urlToImage= cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_URL_TO_IMAGE));

            //gets the string value of url from database
            url= cursor.getString(cursor.getColumnIndex(Contract.TABLE_NEWS.COLUMN_NAME_URL));

            //Picasso loads the image from the url to the Image view, urlIV.
            if(url != null) {
                Picasso.with(context)
                        .load(urlToImage)
                        .into(urlIV);
            }

            nameTV.setText(name);
            descriptionTV.setText(description);
            timeTV.setText(time);

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos,url);

        }
    }

}
