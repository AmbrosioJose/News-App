package com.example.ambrosio.newsapp;

/**
 * Created by Ambrosio on 7/28/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;


public class ScheduleUtilities {

    private static final int SCHEDULE_INTERVAL_ONE_MINUTE = 60;
    private static final int SYNC_FLEXTIME_SECONDS = 10;
    private static final String NEWS_JOB_TAG = "news_job_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleRefresh(@NonNull final Context context){
        if(sInitialized) return;

        //Creates FirebaseJobDispatcher which requires a GoogleplayDriver
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        //this schedules the our job which is: NewsFireBaseJobService
        Job constraintRefreshJob = dispatcher.newJobBuilder()
                .setService(NewsFirebaseJobService.class)
                // this is a unique id for our taskn
                .setTag(NEWS_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                //This will make it run between 60 seconds and 70 seconds
                .setTrigger(Trigger.executionWindow(SCHEDULE_INTERVAL_ONE_MINUTE,
                        SCHEDULE_INTERVAL_ONE_MINUTE + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        // initializes the job and adds it to the dispatcher
        dispatcher.schedule(constraintRefreshJob);
        sInitialized = true;

    }
}
