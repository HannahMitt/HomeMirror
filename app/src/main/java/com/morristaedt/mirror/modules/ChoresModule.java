package com.morristaedt.mirror.modules;

import com.morristaedt.mirror.configuration.ConfigurationSettings;

import java.util.Calendar;

/**
 * Created by HannahMitt on 8/23/15.
 */
public class ChoresModule {

    public static boolean waterPlantsToday() {
        //TODO configuration
        if (!ConfigurationSettings.isDebugBuild()) {
            return false;
        }

        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.SATURDAY;
    }

    public static boolean makeGroceryListToday() {
        //TODO configuration
        if (!ConfigurationSettings.isDebugBuild()) {
            return false;
        }

        Calendar now = Calendar.getInstance();
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY) {
            return true;
        }
        if (dayOfWeek == Calendar.MONDAY && now.get(Calendar.HOUR_OF_DAY) < 15) {
            return true;
        }
        return false;
    }
}
