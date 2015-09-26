package com.morristaedt.mirror.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by HannahMitt on 9/26/15.
 */
public class ConfigurationSettings {

    private static final String PREFS_MIRROR = "MirrorPrefs";

    private static final String LAT = "lat";
    private static final String LON = "lon";

    @NonNull
    private SharedPreferences mSharedPrefs;

    private String mLatitide;
    private String mLongitude;

    public ConfigurationSettings(Context context) {
        mSharedPrefs = context.getSharedPreferences(PREFS_MIRROR, Context.MODE_PRIVATE);
        readPrefs();
    }

    private void readPrefs() {
        mLatitide = mSharedPrefs.getString(LAT, "");
        mLongitude = mSharedPrefs.getString(LON, "");
    }

    public void setLatLon(String latitude, String longitude) {
        mLatitide = latitude.trim();
        mLongitude = longitude.trim();

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(LAT, mLatitide);
        editor.putString(LON, mLongitude);
        editor.apply();
    }

    public String getLatitude() {
        return mLatitide;
    }

    public String getLongitude() {
        return mLongitude;
    }
}
