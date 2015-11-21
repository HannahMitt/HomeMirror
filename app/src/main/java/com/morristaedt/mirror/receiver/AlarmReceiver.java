package com.morristaedt.mirror.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;

import com.morristaedt.mirror.MirrorActivity;

/**
 * Created by HannahMitt on 8/22/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final int REQUEST_CODE = 1001;

    private static final String WAKE_LOCK = "HomeMirrorWakeLock";
    private static final long MINUTES_10 = 10 * 60 * 1000;

    public static void startMirrorUpdates(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);

        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + MINUTES_10, MINUTES_10, alarmIntent);
    }

    public static void stopMirrorUpdates(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);

        alarmMgr.cancel(alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK);
        wakeLock.acquire();

        Intent mainActivityIntent = new Intent(context, MirrorActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mainActivityIntent);

        wakeLock.release();
    }
}
