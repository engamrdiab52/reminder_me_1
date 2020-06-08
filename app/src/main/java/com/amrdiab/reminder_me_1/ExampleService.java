package com.amrdiab.reminder_me_1;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.amrdiab.reminder_me_1.App.CHANNEL_ID;

public class ExampleService extends Service {
    MediaPlayer player;
    int count=0;
    int no_of_times = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service::::","created");
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.song_1);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (count < no_of_times) {
                        mp.start();
                        Log.i("SONG:::::",Integer.toString(count));
                        count++;
                    }else {
                        count = 0;
                        stopPlayer();
                    }
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         no_of_times = intent.getIntExtra("no of times",0);
         Log.i("service onstart command",Integer.toString(no_of_times));
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("ALARM")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
               player.start();
        return START_NOT_STICKY;

    }
    private void stopPlayer() {
        Log.i("before stop player",Integer.toString(no_of_times));
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            Toast.makeText(this, "MediaPlayer released", Toast.LENGTH_SHORT).show();
            stopSelf();
            Log.i("SERVICE::::","stopself");
        }}
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayer();
        Log.i("SERVICE::::","destroyed");
       // stopSelf();
    }
}
