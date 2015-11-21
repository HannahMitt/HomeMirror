package com.morristaedt.mirror;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.RadioGroup;
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
    private View mLocationView;
    private EditText mLatitude;
    private EditText mLongitude;
    private EditText mStockTickerSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        mConfigSettings = new ConfigurationSettings(this);

        mTemperatureChoice = (RadioGroup) findViewById(R.id.temperature_group);
        mTemperatureChoice.check(mConfigSettings.getIsCelsius() ? R.id.celsius : R.id.farenheit);

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
        } catch (IllegalArgumentException e) {
            Log.e("SetUpActivity", "Location manager could not use provider", e);
        }

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
    }

    private void saveFields() {
        mConfigSettings.setIsCelsius(mTemperatureChoice.getCheckedRadioButtonId() == R.id.celsius);
        mConfigSettings.setShowBikingHint(mBikingCheckbox.isChecked());
        mConfigSettings.setShowMoodDetection(mMoodDetectionCheckbox.isChecked());
        mConfigSettings.setShowNextCalendarEvent(mShowNextCaledarEventCheckbox.isChecked());
        mConfigSettings.setShowNewsHeadline(mShowNewsHeadlineCheckbox.isChecked());
        mConfigSettings.setXKCDPreference(mXKCDCheckbox.isChecked(), mXKCDInvertCheckbox.isChecked());

        if (mLocation == null) {
            mConfigSettings.setLatLon(mLatitude.getText().toString(), mLongitude.getText().toString());
        } else {
            mConfigSettings.setLatLon(String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()));
        }

        mConfigSettings.setStockTickerSymbol(mStockTickerSymbol.getText().toString());
    }
}
