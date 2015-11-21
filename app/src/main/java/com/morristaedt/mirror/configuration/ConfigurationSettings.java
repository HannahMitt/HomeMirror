package com.morristaedt.mirror.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.morristaedt.mirror.BuildConfig;
import com.morristaedt.mirror.requests.ForecastRequest;

/**
 * Created by HannahMitt on 9/26/15.
 */
public class ConfigurationSettings {

    /**
     * Hardcode on to enable features outside of their regularly scheduled hours
     */
    private static final boolean DEMO_MODE = false;

    private static final String PREFS_MIRROR = "MirrorPrefs";

    private static final String FORECAST_UNITS = "forecast_units";
    private static final String BIKING_HINT = "biking_hint";
    private static final String USE_MOOD_DETECTION = "mood_detection";
    private static final String SHOW_CALENDAR = "show_calendar";
    private static final String SHOW_HEADLINE = "show_headline";
    private static final String SHOW_XKCD = "xkcd";
    private static final String INVERT_XKCD = "invert_xkcd";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String STOCK_TICKER = "stock_ticker";

    @NonNull
    private SharedPreferences mSharedPrefs;

    private String mForecastUnits;

    private boolean mShowBikingHint;
    private boolean mShowMoodDetection;
    private boolean mShowNextCalendarEvent;
    private boolean mShowNewsHeadline;
    private boolean mShowXKCD;
    private boolean mInvertXKCD;

    private String mLatitude;
    private String mLongitude;

    private String mStockTickerSymbol;

    public ConfigurationSettings(Context context) {
        mSharedPrefs = context.getSharedPreferences(PREFS_MIRROR, Context.MODE_PRIVATE);
        readPrefs();
    }

    private void readPrefs() {
        mForecastUnits = mSharedPrefs.getString(FORECAST_UNITS, ForecastRequest.UNITS_US);
        mShowBikingHint = mSharedPrefs.getBoolean(BIKING_HINT, false);
        mShowMoodDetection = mSharedPrefs.getBoolean(USE_MOOD_DETECTION, false);
        mShowNextCalendarEvent = mSharedPrefs.getBoolean(SHOW_CALENDAR, false);
        mShowNewsHeadline = mSharedPrefs.getBoolean(SHOW_HEADLINE, false);
        mShowXKCD = mSharedPrefs.getBoolean(SHOW_XKCD, false);
        mInvertXKCD = mSharedPrefs.getBoolean(INVERT_XKCD, false);

        mLatitude = mSharedPrefs.getString(LAT, "");
        mLongitude = mSharedPrefs.getString(LON, "");

        mStockTickerSymbol = mSharedPrefs.getString(STOCK_TICKER, "");
    }

    public void setIsCelsius(boolean isCelsius) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(FORECAST_UNITS, isCelsius ? ForecastRequest.UNITS_SI : ForecastRequest.UNITS_US);
        editor.apply();
    }

    public void setShowBikingHint(boolean show) {
        mShowBikingHint = show;
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean(BIKING_HINT, show);
        editor.apply();
    }

    public void setShowMoodDetection(boolean show) {
        mShowMoodDetection = show;
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean(USE_MOOD_DETECTION, show);
        editor.apply();
    }

    public void setShowNextCalendarEvent(boolean show) {
        mShowNextCalendarEvent = show;
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean(SHOW_CALENDAR, show);
        editor.apply();
    }

    public void setShowNewsHeadline(boolean show) {
        mShowNewsHeadline = show;
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean(SHOW_HEADLINE, show);
        editor.apply();
    }

    public void setXKCDPreference(boolean showXKCD, boolean invertXKCDColors) {
        mShowXKCD = showXKCD;
        mInvertXKCD = invertXKCDColors;
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean(SHOW_XKCD, showXKCD);
        editor.putBoolean(INVERT_XKCD, invertXKCDColors);
        editor.apply();
    }

    public void setLatLon(String latitude, String longitude) {
        mLatitude = latitude.trim();
        mLongitude = longitude.trim();

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(LAT, mLatitude);
        editor.putString(LON, mLongitude);
        editor.apply();
    }

    public void setStockTickerSymbol(String tickerSymbol) {
        mStockTickerSymbol = tickerSymbol.replace("$", "").trim();

        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString(STOCK_TICKER, mStockTickerSymbol);
        editor.apply();
    }

    public boolean getIsCelsius() {
        return ForecastRequest.UNITS_SI.equals(mForecastUnits);
    }

    public String getForecastUnits() {
        return mForecastUnits;
    }

    public boolean showBikingHint() {
        return mShowBikingHint;
    }

    public boolean showMoodDetection() {
        return mShowMoodDetection;
    }

    public boolean showNextCalendarEvent() {
        return mShowNextCalendarEvent;
    }

    public boolean showNewsHeadline() {
        return mShowNewsHeadline;
    }

    public boolean showXKCD() {
        return mShowXKCD;
    }

    public boolean invertXKCD() {
        return mInvertXKCD;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public boolean showStock() {
        return !TextUtils.isEmpty(mStockTickerSymbol);
    }

    public String getStockTickerSymbol() {
        return mStockTickerSymbol;
    }

    public static boolean isDebugBuild() {
        return BuildConfig.DEBUG;
    }

    /**
     * Whether we're ignoring timing rules for features
     *
     * @return
     */
    public static boolean isDemoMode() {
        return DEMO_MODE;
    }
}
