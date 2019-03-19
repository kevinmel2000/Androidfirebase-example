package com.example.ezeelinkindonesia.fcmezeepay;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Class get Notification from FCM
 *
 * Created by Franck Aragão on 2/9/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ezeelink.co.id/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        long[] v = {500,1000, 500, 1000};
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.firebase_logo);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher));
        mBuilder.setWhen(System.currentTimeMillis());

        mBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
        mBuilder.setContentText(remoteMessage.getNotification().getBody());

        mBuilder.setVibrate(v);
        mBuilder.setSound(uri);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());
    }
}
