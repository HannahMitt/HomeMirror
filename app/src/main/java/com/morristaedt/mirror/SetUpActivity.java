package com.morristaedt.mirror;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.morristaedt.mirror.configuration.ConfigurationSettings;

public class SetUpActivity extends Activity {

    private static final long HOUR_MILLIS = 60 * 60 * 1000;
    private static final int METERS_MIN = 500;

    @NonNull
    private ConfigurationSettings mConfigSettings;

    private LocationManager mLocationManager;

    @Nullable
    private LocationListener mLocationListener;

    @Nullable
    private Location mLocation;

    private RadioGroup mTemperatureChoice;
    private CheckBox mBikingCheckbox;
    private CheckBox mMoodDetectionCheckbox;
    private CheckBox mShowNextCaledarEventCheckbox;
    private CheckBox mShowNewsHeadlineCheckbox;
    private CheckBox mXKCDCheckbox;
    private CheckBox mXKCDInvertCheckbox;
    private CheckBox mCountdownCheckbox;
    private CheckBox mNewCountdownCheckbox;
    private View mNewCountdownView;
    private View mLocationView;
    private View mColorShowView;
    private EditText mLatitude;
    private EditText mLongitude;
    private EditText mStockTickerSymbol;
    private EditText mCountdownDays;
    private EditText mCountdownHours;
    private EditText mCountdownMins;
    private EditText mCountdownSecs;
    private SeekBar mColorPickerRed;
    private SeekBar mColorPickerGreen;
    private SeekBar mColorPickerBlue;
    private TextView mColorShowerRed;
    private TextView mColorShowerGreen;
    private TextView mColorShowerBlue;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        mConfigSettings = new ConfigurationSettings(this);

        mTemperatureChoice = (RadioGroup) findViewById(R.id.temperature_group);
        mTemperatureChoice.check(mConfigSettings.getIsCelsius() ? R.id.celsius : R.id.farenheit);

        mColorPickerRed = (SeekBar) findViewById(R.id.ColorPickerRed);
        mColorPickerRed.setProgress(Color.red(mConfigSettings.getTextColor()));

        mColorPickerGreen = (SeekBar) findViewById(R.id.ColorPickerGreen);
        mColorPickerGreen.setProgress(Color.green(mConfigSettings.getTextColor()));

        mColorPickerBlue = (SeekBar) findViewById(R.id.ColorPickerBlue);
        mColorPickerBlue.setProgress(Color.blue(mConfigSettings.getTextColor()));

        mColorShowerRed = (TextView) findViewById(R.id.ColorShowerRed);
        mColorShowerRed.setText(String.format("%d", Color.red(mConfigSettings.getTextColor())));

        mColorShowerGreen = (TextView) findViewById(R.id.ColorShowerGreen);
        mColorShowerGreen.setText(String.format("%d", Color.green(mConfigSettings.getTextColor())));

        mColorShowerBlue = (TextView) findViewById(R.id.ColorShowerBlue);
        mColorShowerBlue.setText(String.format("%d", Color.blue(mConfigSettings.getTextColor())));

        mColorShowView = findViewById(R.id.colored_bar);
        mColorShowView.setBackgroundColor(mConfigSettings.getTextColor());

        mColorPickerRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mConfigSettings.setTextColorRed(progress);
                mColorShowerRed.setText(String.format("%d", progress));
                mColorShowView.setBackgroundColor(mConfigSettings.getTextColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mColorPickerGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mConfigSettings.setTextColorGreen(progress);
                mColorShowerGreen.setText(String.format("%d", progress));
                mColorShowView.setBackgroundColor(mConfigSettings.getTextColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        mColorPickerBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mConfigSettings.setTextColorBlue(progress);
                mColorShowerBlue.setText(String.format("%d", progress));
                mColorShowView.setBackgroundColor(mConfigSettings.getTextColor());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        mBikingCheckbox = (CheckBox) findViewById(R.id.biking_checkbox);
        mBikingCheckbox.setChecked(mConfigSettings.showBikingHint());

        mMoodDetectionCheckbox = (CheckBox) findViewById(R.id.mood_detection_checkbox);
        mMoodDetectionCheckbox.setChecked(mConfigSettings.showMoodDetection());

        mShowNextCaledarEventCheckbox = (CheckBox) findViewById(R.id.calendar_checkbox);
        mShowNextCaledarEventCheckbox.setChecked(mConfigSettings.showNextCalendarEvent());

        mShowNewsHeadlineCheckbox = (CheckBox) findViewById(R.id.headline_checkbox);
        mShowNewsHeadlineCheckbox.setChecked(mConfigSettings.showNewsHeadline());

        mXKCDCheckbox = (CheckBox) findViewById(R.id.xkcd_checkbox);
        mXKCDCheckbox.setChecked(mConfigSettings.showXKCD());

        mXKCDInvertCheckbox = (CheckBox) findViewById(R.id.xkcd_invert_checkbox);
        mXKCDInvertCheckbox.setChecked(mConfigSettings.invertXKCD());

        mLatitude = (EditText) findViewById(R.id.latitude);
        mLongitude = (EditText) findViewById(R.id.longitude);

        mLatitude.setText(String.valueOf(mConfigSettings.getLatitude()));
        mLongitude.setText(String.valueOf(mConfigSettings.getLongitude()));

        mLocationView = findViewById(R.id.location_view);
        setUpLocationMonitoring();

        mStockTickerSymbol = (EditText) findViewById(R.id.stock_name);
        mStockTickerSymbol.setText(mConfigSettings.getStockTickerSymbol());

        mCountdownCheckbox = (CheckBox) findViewById(R.id.countdown_checkbox);
        mCountdownCheckbox.setChecked(mConfigSettings.showCountdown());

        mNewCountdownCheckbox = (CheckBox) findViewById(R.id.countdown_new_checkbox);
        mNewCountdownCheckbox.setChecked(false);
        if (!mConfigSettings.showCountdown()) {
            mNewCountdownCheckbox.setVisibility(View.GONE);
        }

        mNewCountdownView = findViewById(R.id.new_countdown_view);
        mNewCountdownView.setVisibility(View.GONE);

        mCountdownDays = (EditText) findViewById(R.id.countdown_days);
        mCountdownHours = (EditText) findViewById(R.id.countdown_hours);
        mCountdownMins = (EditText) findViewById(R.id.countdown_mins);
        mCountdownSecs = (EditText) findViewById(R.id.countdown_secs);

        mCountdownCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    mNewCountdownCheckbox.setVisibility(View.VISIBLE);
                } else {
                    mNewCountdownCheckbox.setChecked(false);
                    mNewCountdownCheckbox.setVisibility(View.GONE);
                    mNewCountdownView.setVisibility(View.GONE);
                }
            }
        });

        mNewCountdownCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    mNewCountdownView.setVisibility(View.VISIBLE);
                } else {
                    mNewCountdownView.setVisibility(View.GONE);
                }
            }
        });


        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFields();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mStockTickerSymbol.getWindowToken(), 0);

                Intent intent = new Intent(SetUpActivity.this, MirrorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null && mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    private void setUpLocationMonitoring() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = mLocationManager.getBestProvider(criteria, true);

        try {
            mLocation = mLocationManager.getLastKnownLocation(provider);

            if (mLocation == null) {
                mLocationView.setVisibility(View.VISIBLE);
                mLocationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (location != null) {
                            Toast.makeText(SetUpActivity.this, R.string.found_location, Toast.LENGTH_SHORT).show();
                            mLocation = location;
                            mConfigSettings.setLatLon(String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()));
                            mLocationManager.removeUpdates(this);
                            if (mLocationView != null) {
                                mLocationView.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                mLocationManager.requestLocationUpdates(provider, HOUR_MILLIS, METERS_MIN, mLocationListener);
            } else {
                mLocationView.setVisibility(View.GONE);
            }
        } catch (IllegalArgumentException e) {
            Log.e("SetUpActivity", "Location manager could not use provider", e);
        }
    }

    private void saveFields() {
        mConfigSettings.setIsCelsius(mTemperatureChoice.getCheckedRadioButtonId() == R.id.celsius);
        mConfigSettings.setShowBikingHint(mBikingCheckbox.isChecked());
        mConfigSettings.setShowMoodDetection(mMoodDetectionCheckbox.isChecked());
        mConfigSettings.setShowNextCalendarEvent(mShowNextCaledarEventCheckbox.isChecked());
        mConfigSettings.setShowNewsHeadline(mShowNewsHeadlineCheckbox.isChecked());
        mConfigSettings.setXKCDPreference(mXKCDCheckbox.isChecked(), mXKCDInvertCheckbox.isChecked());
        mConfigSettings.setShowCountdown(mCountdownCheckbox.isChecked());
        if (mNewCountdownCheckbox.isChecked()){
            mConfigSettings.setCountdownTime(
                    Integer.parseInt("0"+mCountdownDays.getText().toString()),
                    Integer.parseInt("0"+mCountdownHours.getText().toString()),
                    Integer.parseInt("0"+mCountdownMins.getText().toString()),
                    Integer.parseInt("0"+mCountdownSecs.getText().toString()));
            mNewCountdownCheckbox.setChecked(false);
            mNewCountdownView.setVisibility(View.GONE);
        }

        if (mLocation == null) {
            mConfigSettings.setLatLon(mLatitude.getText().toString(), mLongitude.getText().toString());
        } else {
            mConfigSettings.setLatLon(String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()));
        }

        mConfigSettings.setStockTickerSymbol(mStockTickerSymbol.getText().toString());
    }
}
