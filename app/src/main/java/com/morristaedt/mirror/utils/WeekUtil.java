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

    public static boolean afterFive() {
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return hourOfDay >= 17;
    }

    public static boolean isWeekdayBeforeFive() {
        return isWeekday() && !afterFive();
    }

    public static boolean isWeekdayAfterFive() {
        return isWeekday() && afterFive();
    }
}
