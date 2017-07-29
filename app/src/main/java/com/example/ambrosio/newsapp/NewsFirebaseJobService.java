package com.example.ambrosio.newsapp;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Ambrosio on 7/28/2017.
 */

public class NewsFirebaseJobService extends JobService  {
    AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                Toast.makeText(NewsFirebaseJobService.this, "News refreshed", Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                //deletes and repopulates news items in database
                RefreshTasks.refreshArticles(NewsFirebaseJobService.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {

                    jobFinished(job, false);

                super.onPostExecute(o);

            }
        };


        mBackgroundTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        //if the background task is running it won't be cancelled
        if (mBackgroundTask != null) mBackgroundTask.cancel(false);
        return true;
    }
}
