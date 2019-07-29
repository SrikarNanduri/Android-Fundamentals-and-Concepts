package com.srikar.android.jobschedulerexample;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class ExampleJobService extends JobService {

    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        doBackgroundWork(jobParameters);
        return true; // If task is short and can be executed in the scope of the method then we can return false here. But for our case we are running it in a background thread so we return true, right now it is running on UI thread.

    }


    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Log.d(TAG, "run: " + i);
                    if (jobCancelled) {
                        return;
                    }

                    try {
                        Thread.sleep(1000);     // Basically we are putting the thread to sleep for 1sec and logging the i value. we can check that if wifi is off it wont run and if it's not charging it won't run.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "Job finished");
                jobFinished(params, false); // We need to tell the app that the Job is finished by calling jobFinished or else it will continue to run in background and that will effect the performance.
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {         // when job gets cancelled this will be called.
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return false;         // Boolean indicates if we want to reschedule or not.
    }
}
