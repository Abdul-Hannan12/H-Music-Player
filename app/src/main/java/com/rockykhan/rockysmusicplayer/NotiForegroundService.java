package com.rockykhan.rockysmusicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

public class NotiForegroundService extends Service {

    private static final String CHANNEL_ID = "Media Player";
    private static final int NOTIFICATION_ID = 001;

//    Drawable drawable = ResourcesCompat.getDrawable(getResources() ,R.drawable.iconn, null);
//    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//    Bitmap largeIcon = bitmapDrawable.getBitmap();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            Intent notiIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notiIntent, 0);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notification = new Notification.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.small_noti_icon)
//                    .setLargeIcon(largeIcon)
                        .setContentIntent(pendingIntent)
                        .addAction(new Notification.Action(R.drawable.previous, "previous", null))
                        .addAction(new Notification.Action(R.drawable.play, "play", null))
                        .addAction(new Notification.Action(R.drawable.next, "next", null))
                        .addAction(new Notification.Action(R.drawable.cancel, "cancel", null))
                        .setStyle(new Notification.MediaStyle())
                        .build();

                notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "media player", NotificationManager.IMPORTANCE_LOW));
                notificationManager.notify(NOTIFICATION_ID, notification);

            } else {
                notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.small_noti_icon)
//                    .setLargeIcon(largeIcon)
                        .setContentIntent(pendingIntent)
                        .build();
            }

            startForeground(1, notification);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
