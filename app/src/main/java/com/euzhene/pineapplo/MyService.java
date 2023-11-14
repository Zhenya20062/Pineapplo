package com.euzhene.pineapplo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.euzhene.pineapplo.activities.MainActivity;

public class MyService extends Service {

    private final String CHANNEL_ID = "channel1";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        setAlarm();

        MainActivity.timerViewModel.Start();

        startForegroundWithNotification();
        Toast.makeText(getApplicationContext(), "Alarm will set in " + MainActivity.timerViewModel.GetTotalTimeInMillis(), Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "First Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }
    @Override
    public void onDestroy() {
        //alarmManager.cancel(pendingIntent2);
        stopForeground(true);
        stopSelf();

        super.onDestroy();
    }
    private void startForegroundWithNotification() {
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_arrow_drop_down)
                .setContentTitle("Pineapplo is running")
                .setContentIntent(getNotificationPendingIntent())
                .build();

        startForeground(1, notification);
    }
    private void setAlarm() {
        long totalTime = SystemClock.elapsedRealtime() + MainActivity.timerViewModel.GetTotalTimeInMillis();
        //long totalTime = System.currentTimeMillis() + MainActivity.timerViewModel.GetTotalTimeInMillis();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms())
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, totalTime, getBroadcastPendingIntent());
        }
        //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, totalTime, getBroadcastPendingIntent());
    }
    private PendingIntent getBroadcastPendingIntent() {
        Intent broadcastIntent = new Intent(getApplicationContext(), MyWakefulReceiver.class);
        return PendingIntent.getBroadcast(getApplicationContext(), 0, broadcastIntent, PendingIntent.FLAG_IMMUTABLE);
    }
    private PendingIntent getNotificationPendingIntent() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
