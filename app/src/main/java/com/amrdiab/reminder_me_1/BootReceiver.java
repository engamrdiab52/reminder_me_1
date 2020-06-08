package com.amrdiab.reminder_me_1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Calendar;

import static com.amrdiab.reminder_me_1.MainActivity.NUMBER_OF_TIMES;
import static com.amrdiab.reminder_me_1.MainActivity.SHARED_PREFS_1;

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager;
        PendingIntent pendingIntent;

        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 15);

      /*  if(calendar.getTimeInMillis()<System.currentTimeMillis()){
            calendar.add(Calendar.DAY_OF_YEAR,1);
            Toast.makeText(context, "tomorrow", Toast.LENGTH_LONG).show();
        }*/

        Intent intentAlarm = new Intent(context, AlertReceiver.class);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_1,Context.MODE_PRIVATE);
        int i=  sharedPreferences.getInt(NUMBER_OF_TIMES,0);
        intentAlarm.putExtra("NO OF TIMES",i);
        pendingIntent = PendingIntent.getBroadcast(context
                , 101, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, "BOOT RECEIVER", Toast.LENGTH_LONG).show();
    }
}
