package com.euzhene.pineapplo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.euzhene.pineapplo.activities.MainActivity;

public class MyWakefulReceiver extends BroadcastReceiver {
    public static final int SUCCESS_REQUEST_CODE = 135;

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = new NotificationCompat.Builder(context, MyService.CHANNEL_ID)
                .setSmallIcon(R.drawable.start)
                .setContentTitle("You did it!")
                .setContentText("Way to go!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, SUCCESS_REQUEST_CODE, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE))
                .build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(2, notification);

        playMusic(context);
        context.stopService(new Intent(context, MyService.class));
    }

    private void playMusic(Context context) {
        Log.d(PineapploApp.TAG, "playMusic: done");
        MediaPlayer player = MediaPlayer.create(context, R.raw.notif);
        player.start();
        ((PineapploApp) context.getApplicationContext()).pausePlayer();
    }
}
