package com.morristaedt.mirror.modules;

import java.util.Calendar;

/**
 * Created by HannahMitt on 8/23/15.
 */
public class PlantsModule {

    public static boolean waterPlantsToday() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.SATURDAY;
    }
}
