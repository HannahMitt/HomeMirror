package com.morristaedt.mirror.modules;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.morristaedt.mirror.R;

import java.util.Calendar;

/**
 * Created by HannahMitt on 8/22/15.
 */
public class TimeModule {

    public static String getTimeOfDayWelcome(@NonNull Resources resources) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int timeRes;
        if (hour < 4) {
            timeRes = R.string.late;
        } else if (hour < 12) {
            timeRes = R.string.good_morning;
        } else if (hour < 17) { // 5pm
            timeRes = R.string.good_afternoon;
        } else if (hour < 22) { // 10pm
            timeRes = R.string.good_evening;
        } else { // 10pm - midnight is bedtime
            timeRes = R.string.bedtime;
        }

        return resources.getString(timeRes, resources.getString(R.string.owners));
    }
}
