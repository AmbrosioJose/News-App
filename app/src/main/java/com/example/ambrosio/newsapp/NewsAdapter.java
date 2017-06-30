package com.example.ambrosio.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.ambrosio.newsapp.model.Repository;

import java.util.ArrayList;

/**
 * Created by Ambrosio on 6/27/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    private ArrayList<Repository> mNewsData;
    ItemClickListener listener;

    public NewsAdapter(ArrayList<Repository> mNewsData, ItemClickListener listener){
        this.mNewsData= mNewsData;
        this.listener=listener;
    }

    public NewsAdapter() {

    }

    public interface ItemClickListener {
        void onItemClick(int clickedItemIndex);
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context= viewGroup.getContext();
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
        return mNewsData.size();
    }


    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView name;
        TextView description;
        TextView time;

        NewsAdapterViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            time = (TextView) view.findViewById(R.id.time);
            view.setOnClickListener(this);
            }

        public void bind(int pos) {
            Repository repo = mNewsData.get(pos);
            name.setText(repo.getTitle());
            description.setText(repo.getDescription());
            time.setText(repo.getPublishedAt());

            }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        }
    }

}
