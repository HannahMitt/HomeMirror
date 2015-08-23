package com.morristaedt.mirror.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.morristaedt.mirror.MirrorActivity;

/**
 * Created by HannahMitt on 8/22/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String WAKE_LOCK = "HomeMirrorWakeLock";

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
