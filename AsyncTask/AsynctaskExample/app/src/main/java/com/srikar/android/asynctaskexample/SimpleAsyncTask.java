package com.srikar.android.asynctaskexample;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class SimpleAsyncTask extends AsyncTask<Void, Integer, String> {
    private WeakReference<TextView> mTextView;
    private WeakReference<ProgressBar> mPb;
    int count = 1;

    SimpleAsyncTask(TextView tv, ProgressBar pb){
        mTextView = new WeakReference<>(tv);
        mPb = new WeakReference<>(pb);
    }
    @Override
    protected String doInBackground(Void... voids) {
        // Generate a random number between 0 and 10
        Random r = new Random();
        int n = r.nextInt(11);

        // Make the task take long enough that we have
        // time to rotate the phone while it is running
        int s = n * 200;
        int progress = s / 10;
        Log.d("time", Integer.toString(s));

        // Sleep for the random amount of time
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // To publish the progress we use this. It's broken now need to fix it.
        publishProgress(progress);

        // Return a String result
        return "Awake at last after sleeping for " + s + " milliseconds!";

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
            Log.d("async task", Integer.toString(values[0]));
            mPb.get().setProgress(values[0]);




    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mTextView.get().setText(s);

    }
}