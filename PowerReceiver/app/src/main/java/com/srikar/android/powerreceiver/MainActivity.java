package com.srikar.android.powerreceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CustomReceiver mReceiver = new CustomReceiver();
    private static final String ACTION_CUSTOM_BROADCAST =
            BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* A system broadcast is a message that the Android system sends when a system event occurs. Each system broadcast is wrapped in an Intent object:
         - The intent's action field contains event details such as android.intent.action.HEADSET_PLUG, which is sent when a wired headset is connected or disconnected.
         - The intent can contain other data about the event in its extra field, for example a boolean extra indicating whether a headset is connected or disconnected.


         A BroadcastReceiver is either a static receiver or a dynamic receiver, depending on how you register it:

        - To register a receiver statically, use the <receiver> element in your AndroidManifest.xml file. Static receivers are also called manifest-declared receivers.
        - To register a receiver dynamically, use the app context or activity context. The receiver receives broadcasts as long as the registering context is valid, meaning as long as the corresponding app or activity is running. Dynamic receivers are also called context-registered receivers.

        For this app, you're interested in two system broadcasts, ACTION_POWER_CONNECTED and ACTION_POWER_DISCONNECTED. The Android system sends these broadcasts when the device's power is connected or disconnected.

        Starting from Android 8.0 (API level 26 and higher), you can't use static receivers to receive most Android system broadcasts, with some exceptions. So for this task, you use dynamic receivers:*/

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_HEADSET_PLUG);

        // Register the receiver using the activity context.
        this.registerReceiver(mReceiver, filter);

        /*Registering for a local broadcast is similar to registering for a system broadcast, which you do using a dynamic receiver. For broadcasts sent using LocalBroadcastManager, static registration in the manifest is not allowed.
          If you register a broadcast receiver dynamically, you must unregister the receiver when it is no longer needed. In your app, the receiver only needs to respond to the custom broadcast when the app is running, so you can register the action in onCreate() and unregister it in onDestroy().*/
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver,
                        new IntentFilter(ACTION_CUSTOM_BROADCAST));
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mReceiver);
        super.onDestroy();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mReceiver);
    }

      /*In addition to responding to system broadcasts, your app can send and receive custom broadcasts. Use a custom broadcast when you want your app to take an action without launching an activity, for example when you want to let other apps know that data has been downloaded to the device.

       Android provides three ways for your app to send custom broadcasts:

      - Normal broadcasts are asynchronous. Receivers of normal broadcasts run in an undefined order, often at the same time. To send a normal broadcast, create a broadcast intent and pass it to sendBroadcast(Intent).
      - Local broadcasts are sent to receivers that are in the same app as the sender. To send a local broadcast, create a broadcast intent and pass it to LocalBroadcastManager.sendBroadcast.
      - Ordered broadcasts are delivered to one receiver at a time. As each receiver executes, it can propagate a result to the next receiver, or it can cancel the broadcast so that the broadcast is not passed to other receivers. To send an ordered broadcast, create a broadcast intent and pass it to sendOrderedBroadcast(Intent, String).*/


    public void sendCustomBroadcast(View view) {

        /*The broadcast message is wrapped in an Intent object. The Intent action string must provide the app's Java package name syntax and uniquely identify the broadcast event.
        For a custom broadcast, you define your own Intent action (a unique string). You can create Intent objects with custom actions and broadcast them yourself from your app using one of the methods above. The broadcasts are received by apps that have a BroadcastReceiver registered for that action.*/

        Intent customBroadcastIntent = new Intent(ACTION_CUSTOM_BROADCAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent);
    }
}
