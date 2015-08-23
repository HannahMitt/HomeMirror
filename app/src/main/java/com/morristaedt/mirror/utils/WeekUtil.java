package com.morristaedt.mirror.utils;

import java.util.Calendar;

/**
 * Created by HannahMitt on 8/23/15.
 */
public class WeekUtil {

    public static boolean isWeekday() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }
}
