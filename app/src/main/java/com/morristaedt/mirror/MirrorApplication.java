package com.morristaedt.mirror;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.morristaedt.mirror.receiver.AlarmReceiver;

/**
 * Created by HannahMitt on 8/22/15.
 */
public class MirrorApplication extends Application {

    private static final long MINUTES_10 = 10 * 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + MINUTES_10, MINUTES_10, alarmIntent);
    }
}
