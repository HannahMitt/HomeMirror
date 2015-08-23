package com.morristaedt.mirror;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.morristaedt.mirror.modules.ForecastModule;
import com.morristaedt.mirror.modules.TimeModule;
import com.morristaedt.mirror.modules.XKCDModule;
import com.morristaedt.mirror.modules.YahooFinanceModule;
import com.morristaedt.mirror.requests.YahooStockResponse;
import com.squareup.picasso.Picasso;

public class MirrorActivity extends ActionBarActivity {

    private TextView mWeatherSummary;
    private TextView mHelloText;
    private TextView mStockText;
    private ImageView mXKCDImage;
    private TextView mBikeTodayText;

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
        public void onShouldBike(boolean shouldBike) {
            mBikeTodayText.setText(shouldBike ? R.string.bike_today : R.string.no_bike_today);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mWeatherSummary = (TextView) findViewById(R.id.weather_summary);
        mHelloText = (TextView) findViewById(R.id.hello_text);
        mBikeTodayText = (TextView) findViewById(R.id.can_bike);
        mStockText = (TextView) findViewById(R.id.stock_text);
        mXKCDImage = (ImageView) findViewById(R.id.xkcd_image);

        setViewState();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setViewState();
    }

    private void setViewState() {
        mHelloText.setText(TimeModule.getTimeOfDayWelcome(getResources()));
        ForecastModule.getHourlyForecast(getResources(), 40.681045, -73.9931749, mForecastListener);
        XKCDModule.getXKCDForToday(mXKCDListener);
        YahooFinanceModule.getStockForToday("ETSY", mStockListener);
    }
}
