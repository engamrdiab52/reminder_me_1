package com.amrdiab.reminder_me_1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText editText ;
    Calendar calendar;
    AlarmManager alarmManager;
    MediaPlayer player = null;
    int no_of_times;

    Button enter ;
    Button enable;
    Button disable;
    Button play;
    Button pause;
    Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextNumber);
        enter = findViewById(R.id.enter);
        enable = findViewById(R.id.enable);
        //enable.setFocusable(true);
    //    enable.setFocusableInTouchMode(true);

        disable = findViewById(R.id.disable);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);

      //  enable.setEnabled(false);
       // disable.setEnabled(false);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 30);
        // this condition to prevent the alarm from fire as soon as it starts
        /*if(calendar.getTimeInMillis()<System.currentTimeMillis()){
            calendar.add(Calendar.DAY_OF_YEAR,1);
            Toast.makeText(this, "tomorrow", Toast.LENGTH_LONG).show();
        }*/

        // enable alarm by taking the value from the shared preferences

      /*  Intent intent = new Intent(this,AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this
                ,101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);*/

    }

    public void on_enter(View view) {
        String s =editText.getText().toString();
        if (s.equals("")){
            Toast.makeText(this, "enter no between 1 and 1000", Toast.LENGTH_SHORT).show();
        }else {
            no_of_times = Integer.parseInt(s);
        }
        editText.setEnabled(false);
        enter.setEnabled(false);
        //enable.requestFocus();
        enable.setEnabled(true);

       // enable.setText("ENTER");

    }

    public void on_play(View view) {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.song_1);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play.setEnabled(true);
                    stopPlayer();
                }
            });
        }
        player.start();
        play.setEnabled(false);
        pause.setEnabled(true);
        Toast.makeText(this, "media player play", Toast.LENGTH_SHORT).show();
    }

    public void on_pause(View view) {
        pause.setEnabled(false);
        play.setEnabled(true);
        if (player != null) {
            player.pause();
            Toast.makeText(this, "media player pause", Toast.LENGTH_SHORT).show();
        }
    }

    public void on_stop(View view) {
        play.setEnabled(true);
        pause.setEnabled(true);
        stopPlayer();
    }

    private void stopPlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            Toast.makeText(this, "MediaPlayer released", Toast.LENGTH_SHORT).show();
        }
    }

    public void on_enable(View view) {
        String s =editText.getText().toString();
        if (s.equals("")){
            Toast.makeText(this, "enter no between 1 and 1000", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(this, AlertReceiver.class);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            intent.putExtra("NO OF TIMES", no_of_times);
            // will reset the alarm anager with the same pending intent
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this
                    , 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // if (alarmManager == null) throw new AssertionError();
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.i("intent to receiver::", Integer.toString(no_of_times));
            enable.setEnabled(false);
            enter.setEnabled(false);
            editText.setEnabled(false);
            disable.setEnabled(true);
        }
    }

    public void on_disable(View view) {
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this
                    , 101, intent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent !=null)   {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //if (alarmManager == null) throw new AssertionError();//
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
                Log.i("ALARM MANGER::::", "canceled");
            }
            enter.setEnabled(true);
            enable.setEnabled(true);
            editText.setEnabled(true);
            disable.setEnabled(false);

            Log.i("ALARM MANGER::::", "disabled");
            Toast.makeText(this, "the alarm has been removed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "no alarm to disable", Toast.LENGTH_SHORT).show();
        }
    }

    public void on_stopNow(View view) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
    }
}