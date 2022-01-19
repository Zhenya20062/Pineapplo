package com.euzhene.pineapplo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public  class MyWakefulReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // create the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel2")
//                .setSmallIcon(R.drawable.start)
//                .setContentTitle("You did it!")
//                .setContentText("It's my notification. Way to go!")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
//        notificationManagerCompat.notify(2, builder.build());

        playMusic(context); //start the notification sound
    }
    private void playMusic(Context context) {
        MediaPlayer player = MediaPlayer.create(context, R.raw.notif);
        player.start();
    }
}
