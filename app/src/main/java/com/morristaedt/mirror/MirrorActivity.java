package com.morristaedt.mirror;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.morristaedt.mirror.configuration.ConfigurationSettings;
import com.morristaedt.mirror.modules.BirthdayModule;
import com.morristaedt.mirror.modules.CalendarModule;
import com.morristaedt.mirror.modules.ChoresModule;
import com.morristaedt.mirror.modules.CountdownModule;
import com.morristaedt.mirror.modules.DayModule;
import com.morristaedt.mirror.modules.ForecastModule;
import com.morristaedt.mirror.modules.MoodModule;
import com.morristaedt.mirror.modules.NewsModule;
import com.morristaedt.mirror.modules.XKCDModule;
import com.morristaedt.mirror.modules.YahooFinanceModule;
import com.morristaedt.mirror.receiver.AlarmReceiver;
import com.morristaedt.mirror.requests.YahooStockResponse;
import com.morristaedt.mirror.utils.WeekUtil;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

public class MirrorActivity extends ActionBarActivity {

    @NonNull
    private ConfigurationSettings mConfigSettings;

    private TextView mBirthdayText;
    private TextView mDayText;
    private TextView mWeatherSummary;
    private TextView mHelloText;
    private TextView mBikeTodayText;
    private TextView mStockText;
    private TextView mMoodText;
    private View mWaterPlants;
    private View mGroceryList;
    private ImageView mXKCDImage;
    private MoodModule mMoodModule;
    private TextView mNewsHeadline;
    private TextView mCalendarTitleText;
    private TextView mCalendarDetailsText;
    private TextView mCountdownText;

    private XKCDModule.XKCDListener mXKCDListener = new XKCDModule.XKCDListener() {
        @Override
        public void onNewXKCDToday(String url) {
            if (TextUtils.isEmpty(url)) {
                mXKCDImage.setVisibility(View.GONE);
            } else {
                Picasso.with(MirrorActivity.this).load(url).into(mXKCDImage);
                mXKCDImage.setVisibility(View.VISIBLE);
            }
        }
    };

    private YahooFinanceModule.StockListener mStockListener = new YahooFinanceModule.StockListener() {
        @Override
        public void onNewStockPrice(YahooStockResponse.YahooQuoteResponse quoteResponse) {
            if (quoteResponse == null) {
                mStockText.setVisibility(View.GONE);
            } else {
                mStockText.setVisibility(View.VISIBLE);
                mStockText.setText("$" + quoteResponse.symbol + " $" + quoteResponse.LastTradePriceOnly);
            }
        }
    };

    private ForecastModule.ForecastListener mForecastListener = new ForecastModule.ForecastListener() {
        @Override
        public void onWeatherToday(String weatherToday) {
            if (!TextUtils.isEmpty(weatherToday)) {
                mWeatherSummary.setVisibility(View.VISIBLE);
                mWeatherSummary.setText(weatherToday);
            }
        }

        @Override
        public void onShouldBike(boolean showToday, boolean shouldBike) {
            if (mConfigSettings.showBikingHint()) {
                mBikeTodayText.setVisibility(showToday ? View.VISIBLE : View.GONE);
                mBikeTodayText.setText(shouldBike ? R.string.bike_today : R.string.no_bike_today);
            } else {
                mBikeTodayText.setVisibility(View.GONE);
            }
        }
    };

    private NewsModule.NewsListener mNewsListener = new NewsModule.NewsListener() {
        @Override
        public void onNewNews(String headline) {
            if (TextUtils.isEmpty(headline)) {
                mNewsHeadline.setVisibility(View.GONE);
            } else {
                mNewsHeadline.setVisibility(View.VISIBLE);
                mNewsHeadline.setText(headline);
                mNewsHeadline.setSelected(true);
            }
        }
    };

    private MoodModule.MoodListener mMoodListener = new MoodModule.MoodListener() {
        @Override
        public void onShouldGivePositiveAffirmation(final String affirmation) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMoodText.setVisibility(affirmation == null ? View.GONE : View.VISIBLE);
                    mMoodText.setText(affirmation);
                }
            });
        }
    };

    private CalendarModule.CalendarListener mCalendarListener = new CalendarModule.CalendarListener() {
        @Override
        public void onCalendarUpdate(String title, String details) {
            mCalendarTitleText.setVisibility(title != null ? View.VISIBLE : View.GONE);
            mCalendarTitleText.setText(title);
            mCalendarDetailsText.setVisibility(details != null ? View.VISIBLE : View.GONE);
            mCalendarDetailsText.setText(details);

            //Make marquee effect work for long text
            mCalendarTitleText.setSelected(true);
            mCalendarDetailsText.setSelected(true);
        }
    };

    private CountdownModule.CountdownListener mCountdownListener = new CountdownModule.CountdownListener() {
        @Override
        public void onCountdownUpdate(final String timeLeft) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCountdownText.setVisibility(View.VISIBLE);
                    mCountdownText.setText(timeLeft);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);
        mConfigSettings = new ConfigurationSettings(this);
        AlarmReceiver.startMirrorUpdates(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mBirthdayText = (TextView) findViewById(R.id.birthday_text);
        mDayText = (TextView) findViewById(R.id.day_text);
        mWeatherSummary = (TextView) findViewById(R.id.weather_summary);
        mHelloText = (TextView) findViewById(R.id.hello_text);
        mWaterPlants = findViewById(R.id.water_plants);
        mGroceryList = findViewById(R.id.grocery_list);
        mBikeTodayText = (TextView) findViewById(R.id.can_bike);
        mStockText = (TextView) findViewById(R.id.stock_text);
        mMoodText = (TextView) findViewById(R.id.mood_text);
        mXKCDImage = (ImageView) findViewById(R.id.xkcd_image);
        mNewsHeadline = (TextView) findViewById(R.id.news_headline);
        mCalendarTitleText = (TextView) findViewById(R.id.calendar_title);
        mCalendarDetailsText = (TextView) findViewById(R.id.calendar_details);
        mCountdownText = (TextView) findViewById(R.id.countdown_text);

        if (mConfigSettings.invertXKCD()) {
            //Negative of XKCD image
            float[] colorMatrixNegative = {
                    -1.0f, 0, 0, 0, 255, //red
                    0, -1.0f, 0, 0, 255, //green
                    0, 0, -1.0f, 0, 255, //blue
                    0, 0, 0, 1.0f, 0 //alpha
            };
            ColorFilter colorFilterNegative = new ColorMatrixColorFilter(colorMatrixNegative);
            mXKCDImage.setColorFilter(colorFilterNegative); // not inverting for now
        }

        setViewState();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mMoodModule != null) {
            mMoodModule.release();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setViewState();
    }

    private void colorTextViews(ViewGroup mview){
        for (int i = 0; i < mview.getChildCount(); i++) {
            View view = mview.getChildAt(i);
            if (view instanceof ViewGroup)
                colorTextViews((ViewGroup) view);
            else if (view instanceof TextView) {
                ((TextView) view).setTextColor(mConfigSettings.getTextColor());
            }
        }
    }

    private void setViewState() {
        colorTextViews((ViewGroup) findViewById(R.id.main_layout));

        String birthday = BirthdayModule.getBirthday();
        if (TextUtils.isEmpty(birthday)) {
            mBirthdayText.setVisibility(View.GONE);
        } else {
            mBirthdayText.setVisibility(View.VISIBLE);
            mBirthdayText.setText(getString(R.string.happy_birthday, birthday));
        }

        mDayText.setText(DayModule.getDay());
//        mHelloText.setText(TimeModule.getTimeOfDayWelcome(getResources())); // not in current design

        mWaterPlants.setVisibility(ChoresModule.waterPlantsToday() ? View.VISIBLE : View.GONE);
        mGroceryList.setVisibility(ChoresModule.makeGroceryListToday() ? View.VISIBLE : View.GONE);

        // Get the API key for whichever weather service API key is available
        // These should be declared as a string in xml
        int forecastApiKeyRes = getResources().getIdentifier("dark_sky_api_key", "string", getPackageName());
        int openWeatherApiKeyRes = getResources().getIdentifier("open_weather_api_key", "string", getPackageName());

        if (forecastApiKeyRes != 0) {
            ForecastModule.getForecastIOHourlyForecast(getString(forecastApiKeyRes), mConfigSettings.getForecastUnits(), mConfigSettings.getLatitude(), mConfigSettings.getLongitude(), mForecastListener);
        } else if (openWeatherApiKeyRes != 0) {
            ForecastModule.getOpenWeatherForecast(getString(openWeatherApiKeyRes), mConfigSettings.getForecastUnits(), mConfigSettings.getLatitude(), mConfigSettings.getLongitude(), mForecastListener);
        }

        if (mConfigSettings.showNewsHeadline()) {
            NewsModule.getNewsHeadline(mNewsListener);
        } else {
            mNewsHeadline.setVisibility(View.GONE);
        }

        if (mConfigSettings.showXKCD()) {
            XKCDModule.getXKCDForToday(mXKCDListener);
        } else {
            mXKCDImage.setVisibility(View.GONE);
        }

        if (mConfigSettings.showNextCalendarEvent()) {
            CalendarModule.getCalendarEvents(this, mCalendarListener);
        } else {
            mCalendarTitleText.setVisibility(View.GONE);
            mCalendarDetailsText.setVisibility(View.GONE);
        }

        if (mConfigSettings.showStock() && (ConfigurationSettings.isDemoMode() || WeekUtil.isWeekdayAfterFive())) {
            YahooFinanceModule.getStockForToday(mConfigSettings.getStockTickerSymbol(), mStockListener);
        } else {
            mStockText.setVisibility(View.GONE);
        }

        if (mConfigSettings.showMoodDetection()) {
            mMoodModule = new MoodModule(new WeakReference<Context>(this));
            mMoodModule.getCurrentMood(mMoodListener);
        } else {
            mMoodText.setVisibility(View.GONE);
        }

        if (mConfigSettings.showCountdown()){
            CountdownModule.getTimeRemaining(mConfigSettings.getCountdownEnd(), mCountdownListener);
        } else {
            mCountdownText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlarmReceiver.stopMirrorUpdates(this);
        Intent intent = new Intent(this, SetUpActivity.class);
        startActivity(intent);
    }
}
