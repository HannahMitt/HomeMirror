package com.morristaedt.mirror.requests;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 */
public class OpenWeatherResponse {

    public ArrayList<Weather> weather;
    public Main main;

    public String getWeatherDescription() {
        if (weather != null && weather.size() > 0) {
            return TextUtils.join(", ", weather);
        }
        return "";
    }

    public class Weather {
        public String description;

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return getDescription();
        }
    }

    public class Main {
        float temp;

        public String getDisplayTemperature() {
            return String.valueOf(Math.round(temp)) + (char) 0x00B0;
        }
    }

}
