package com.srikar.android.jobschedulerexample;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int JOBID = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    // We need to set the service in manifest file for the scheduler to work check the manifest file.
    public void scheduleJob(View v) {       // A Job is scheduled when button is clicked.
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);     // Jobinfo takes a component name as a parameter
        JobInfo info = new JobInfo.Builder(123, componentName)  // JobInfo takes in all the requirements that are necessary for the Job.
                .setRequiresCharging(true)      // Requires the phone to be charging for the Job to run.
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)     // Requires Wifi as networkType
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    public void cancelJob(View v) {
        JobScheduler scheduler =(JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.cancel(123);
        Log.d(TAG, "Job cancelled");
    }
}
