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

    public static void getTimeRemaining(final Date countdownEnd, final CountdownListener listener){
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

        time /= 1000; //convert to secs
        if (time <= 0){
            return formattedTime;
        }
        formattedTime = time%60 + "s";

        time /= 60; //convert to mins
        if (time == 0){
            return formattedTime;
        }
        formattedTime = time%60 + "m " + formattedTime;

        time /= 60; //convert to hours
        if (time == 0) {
            return formattedTime;
        }
        formattedTime = time%60 + "h " + formattedTime;

        time /= 24; //convert to days
        if (time == 0) {
            return formattedTime;
        }
        formattedTime = time + "d " + formattedTime;

        return formattedTime;
    }

}
