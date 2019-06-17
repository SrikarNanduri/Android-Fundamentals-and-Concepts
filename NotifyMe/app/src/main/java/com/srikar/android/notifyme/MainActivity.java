package com.srikar.android.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // These are the Ids that are created to keep track of notifications and other actions of broadcast receivers.
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final String ACTION_UPDATE_NOTIFICATION = "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";
    private static final String CLEAR_NOTIFICATION = "com.example.android.notifyme.CLEAR_NOTIFICATION";
    private static final int NOTIFICATION_ID = 0;


    private NotificationManager mNotifyManager;
    private NotificationReceiver mReceiver = new NotificationReceiver();
    private ClearNotificationReceiver mClearReceiver = new ClearNotificationReceiver();
    private Button button_notify;
    private Button button_cancel;
    private Button button_update;
    private Button button_update_inbox_style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));
        registerReceiver(mClearReceiver,new IntentFilter(CLEAR_NOTIFICATION));

        button_notify = findViewById(R.id.notify);
        button_notify.setOnClickListener(view -> sendNotification());
        button_update = findViewById(R.id.update);
        button_update.setOnClickListener(view -> updateNotification());

        button_update_inbox_style = findViewById(R.id.update_inbox);
        button_update_inbox_style.setOnClickListener(view -> updateNotificationInboxStyle());

        button_cancel = findViewById(R.id.cancel);
        button_cancel.setOnClickListener(view -> cancelNotification());
        createNotificationChannel();
        setNotificationButtonState(true, false, false, false);
    }


    // This method is handling the setDeleteIntent calls, on what should be done if the notifications are cleared by clear all button on notification bar.
    PendingIntent cleared(){
        Intent clear = new Intent(CLEAR_NOTIFICATION);
        return PendingIntent.getBroadcast(this, NOTIFICATION_ID, clear, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    // This is creating the notification and notifying it to the user.
    public void sendNotification() {
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent);
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        setNotificationButtonState(false, true, true, true);
    }

    // This method is creating a notification channel, as per android version Oreo notifications are grouped and the user has been given the control of what should be displayed and what it should do.
    public void createNotificationChannel()
    {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }


    // This method is building the notification with all the contents, icons, summary etc.
    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android)
                // this line is handling what the notification should do when it is clicked.
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                // this line checks if the user cleared the notification manually without clearing using clancle button. so that it will automatically update the visibility of the buttons.
                .setDeleteIntent(cleared())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

    }

    // This method updates the existing notification in BigPictureStyle. We are using "NOTIFICATION_ID" to keep track of the notifications.
    public void updateNotification() {
        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(),R.drawable.mascot_1);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!"));
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        setNotificationButtonState(false, false, true, false);
    }


    // This method updates the existing notification in InboxStyle. We are using "NOTIFICATION_ID" to keep track of the notifications.
    public void updateNotificationInboxStyle() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.InboxStyle()
                .addLine("First line")
                .addLine("Second line")
                .addLine("Last line")
                .setBigContentTitle("Notification Updated!")
                .setSummaryText("+1 more"));
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        setNotificationButtonState(false, false, true, false);
    }

    // As the name suggests it clears notification that has been raised.
    public void cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID);
        setNotificationButtonState(true, false, false, false);
    }


    // This method deals with the visibility of the buttons. Because we shouldn't allow user to click clear notification button without even raising a notification. It is a bad UI pratice.
    void setNotificationButtonState(Boolean isNotifyEnabled,
                                    Boolean isUpdateEnabled,
                                    Boolean isCancelEnabled,
                                    Boolean isInboxed) {
        button_notify.setEnabled(isNotifyEnabled);
        button_update.setEnabled(isUpdateEnabled);
        button_cancel.setEnabled(isCancelEnabled);
        button_update_inbox_style.setEnabled(isInboxed);
    }


    // This is a custom broadcast receiever which is executed when the user clicks update notification button from the notification bar.
    public class NotificationReceiver extends BroadcastReceiver{

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }


    // This is a custom broadcast receiever which executes when the user cleares the notifications using clear all.
    public class ClearNotificationReceiver extends BroadcastReceiver{

        public ClearNotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            setNotificationButtonState(true, false, false, false);
        }
    }


    @Override
    protected void onDestroy() {
        //unregister the broadcast receivers
        unregisterReceiver(mReceiver);
        unregisterReceiver(mClearReceiver);
        super.onDestroy();
    }
}
