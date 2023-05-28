package com.morristaedt.mirror.modules;

import android.app.Activity;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CountdownModule {

    public interface CountdownListener {

        void onCountdownUpdate(String timeLeft);
    }

    private static Timer timer;

    public static void getTimeRemaining(final Date countdownEnd, final CountdownListener listener) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                long timeLeft = countdownEnd.getTime() - System.currentTimeMillis();
                listener.onCountdownUpdate(formatTime(timeLeft));
            }
        }, 0, 1000);
    }

    private static String formatTime(long time) {
        String formattedTime = "Time is up!";
        //convert to secs
        time /= 1000;
        if (time <= 0) {
            return formattedTime;
        }
        formattedTime = time % 60 + "s";
        //convert to mins
        time /= 60;
        if (time == 0) {
            return formattedTime;
        }
        formattedTime = time % 60 + "m " + formattedTime;
        //convert to hours
        time /= 60;
        if (time == 0) {
            return formattedTime;
        }
        formattedTime = time % 60 + "h " + formattedTime;
        //convert to days
        time /= 24;
        if (time == 0) {
            return formattedTime;
        }
        formattedTime = time + "d " + formattedTime;
        return formattedTime;
    }
}
