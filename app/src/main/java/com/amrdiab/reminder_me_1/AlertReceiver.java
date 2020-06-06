package com.amrdiab.reminder_me_1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;


public class AlertReceiver extends BroadcastReceiver {
        int no_of_times;
    @Override
    public void onReceive(Context context, Intent intent) {
        no_of_times =intent.getIntExtra("NO OF TIMES",0);
        Intent serviceIntent = new Intent(context, ExampleService.class);
        serviceIntent.putExtra("no of times",no_of_times);
        ContextCompat.startForegroundService(context, serviceIntent);
        Log.i("recevier to service",Integer.toString(no_of_times));


   }
}
