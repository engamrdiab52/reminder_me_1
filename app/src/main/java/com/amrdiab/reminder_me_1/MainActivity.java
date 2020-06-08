package com.amrdiab.reminder_me_1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    PendingIntent pendingIntent;
    SharedPreferences.Editor editor;

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
    Button stopNow;

    boolean enter_enabled,enable_enabled,disable_enabled,editText_enabled,
            play_enabled,pause_enabled,stop_enabled,stopNow_enabled;

    public static final String SHARED_PREFS_1= "shared_prefs_1";
    public static final String ENTER = "enter";
    public static final String ENABLE="enable";
    public static final String DISABLE = "disable";
    public static final String PLAY = "play";
    public static final String PAUSE = "pause";
    public static final String STOP = "stop";
    public static final String STOPNOW= "stopnow";
    public static final String NUMBER_OF_TIMES = "no_of_times";
    public static final String EDIT_TEXT = "edit_text";

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
        enable = findViewById(R.id.enable);
        stopNow = findViewById(R.id.stopNow);
        enter = findViewById(R.id.enter);
        stop.setEnabled(false);

      //  enable.setEnabled(false);
       // disable.setEnabled(false);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 15);
        // this condition to prevent the alarm from fire as soon as it starts

/*        if(calendar.getTimeInMillis()<System.currentTimeMillis()){
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
      loadData();
      updateData();
    }

    public void on_enter(View view) {
        String s =editText.getText().toString();
        if (s.equals("")){
            Toast.makeText(this, "enter no between 1 and 1000", Toast.LENGTH_SHORT).show();
        }else {
            no_of_times = Integer.parseInt(s);
        }
        editText.setEnabled(false);
        editText.setHint( Integer.toString(no_of_times));
        enter.setEnabled(false);
        //enable.requestFocus();
        enable.setEnabled(true);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_1,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(NUMBER_OF_TIMES,no_of_times);
        editor.apply();

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
        stop.setEnabled(true);
        pause.setEnabled(true);
        Toast.makeText(this, "media player play", Toast.LENGTH_SHORT).show();
    }

    public void on_pause(View view) {
        pause.setEnabled(false);
        play.setEnabled(true);
        stop.setEnabled(true);
        if (player != null) {
            player.pause();
            Toast.makeText(this, "media player pause", Toast.LENGTH_SHORT).show();
        }
    }

    public void on_stop(View view) {
        play.setEnabled(true);
        pause.setEnabled(true);
        stop.setEnabled(false);
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
            sharedPreferences = getSharedPreferences(SHARED_PREFS_1,MODE_PRIVATE);
            int i = sharedPreferences.getInt(NUMBER_OF_TIMES,0);
            Intent intent = new Intent(this, AlertReceiver.class);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            intent.putExtra("NO OF TIMES", i);
            // will reset the alarm anager with the same pending intent
            pendingIntent = PendingIntent.getBroadcast(this
                    , 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // if (alarmManager == null) throw new AssertionError();
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.i("intent to receiver::", Integer.toString(i));
            enable.setEnabled(false);
            enter.setEnabled(false);
            editText.setEnabled(false);
            disable.setEnabled(true);
        }
    }

    public void on_disable(View view) {
        enter.setEnabled(true);
        enable.setEnabled(true);
        editText.setEnabled(true);
        disable.setEnabled(false);
        stopNow.setEnabled(true);
        stop.setEnabled(false);
        editText.setHint("");

        Intent intent = new Intent(this, AlertReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this
                    , 101, intent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent !=null)   {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
                Log.i("ALARM MANGER::::", "canceled");



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
public void saveData(){
        sharedPreferences =getSharedPreferences(SHARED_PREFS_1,MODE_PRIVATE);
        editor  =sharedPreferences.edit();
        editor.putBoolean(ENABLE,enable.isEnabled());
        editor.putBoolean(ENTER,enter.isEnabled());
        editor.putBoolean(STOP,stop.isEnabled());
        editor.putBoolean(PAUSE,pause.isEnabled());
        editor.putBoolean(STOPNOW,stopNow.isEnabled());
        editor.putBoolean(DISABLE,disable.isEnabled());
        editor.putBoolean(PLAY,play.isEnabled());
        editor.putBoolean(EDIT_TEXT,editText.isEnabled());
        editor.putInt(NUMBER_OF_TIMES,no_of_times);
        editor.apply();
}
public void loadData(){
         sharedPreferences = getSharedPreferences(SHARED_PREFS_1,MODE_PRIVATE);

        enter_enabled =sharedPreferences.getBoolean(ENTER,true);
        enable_enabled = sharedPreferences.getBoolean(ENABLE,true);
        disable_enabled = sharedPreferences.getBoolean(DISABLE,true);
        play_enabled = sharedPreferences.getBoolean(PLAY,true);
        pause_enabled = sharedPreferences.getBoolean(PAUSE,true);
        stop_enabled = sharedPreferences.getBoolean(STOP,true);
        stopNow_enabled= sharedPreferences.getBoolean(STOPNOW,true);
        editText_enabled = sharedPreferences.getBoolean(EDIT_TEXT,true);
}
public void updateData(){
        enter.setEnabled(enable_enabled);
        enable.setEnabled(enable_enabled);
        stop.setEnabled(stop_enabled);
        stopNow.setEnabled(stopNow_enabled);
        disable.setEnabled(disable_enabled);
        play.setEnabled(play_enabled);
        pause.setEnabled(pause_enabled);
        editText.setEnabled(editText_enabled);
        sharedPreferences = getSharedPreferences(SHARED_PREFS_1,MODE_PRIVATE);
        int i = sharedPreferences.getInt(NUMBER_OF_TIMES,0);
        editText.setHint(Integer.toString(i));
     }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
        updateData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
        updateData();
    }
}