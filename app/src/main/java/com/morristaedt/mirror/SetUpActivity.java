package com.morristaedt.mirror;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.morristaedt.mirror.configuration.ConfigurationSettings;

public class SetUpActivity extends Activity {

    @NonNull
    private ConfigurationSettings mConfigSettings;

    private CheckBox mMoodDetection;
    private EditText mLatitude;
    private EditText mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        mConfigSettings = new ConfigurationSettings(this);

        mMoodDetection = (CheckBox) findViewById(R.id.mood_detection);
        mMoodDetection.setChecked(mConfigSettings.showMoodDetection());

        mLatitude = (EditText) findViewById(R.id.latitude);
        mLongitude = (EditText) findViewById(R.id.longitude);

        mLatitude.setText(String.valueOf(mConfigSettings.getLatitude()));
        mLongitude.setText(String.valueOf(mConfigSettings.getLongitude()));

        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFields();
                Intent intent = new Intent(SetUpActivity.this, MirrorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveFields() {
        mConfigSettings.setShowMoodDetection(mMoodDetection.isChecked());
        mConfigSettings.setLatLon(mLatitude.getText().toString(), mLongitude.getText().toString());
    }
}
