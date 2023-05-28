package com.morristaedt.mirror.modules;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import com.morristaedt.mirror.configuration.ConfigurationSettings;
import com.morristaedt.mirror.requests.ForecastRequest;
import com.morristaedt.mirror.requests.ForecastResponse;
import com.morristaedt.mirror.requests.OpenWeatherRequest;
import com.morristaedt.mirror.requests.OpenWeatherResponse;
import com.morristaedt.mirror.utils.WeekUtil;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by HannahMitt on 8/22/15.
 */
public class ForecastModule {

    public interface ForecastListener {

        void onWeatherToday(String weatherToday);

        void onShouldBike(boolean showToday, boolean shouldBike);
    }

    /**
     * @param apiKey   The api key for the forecast.io weather api
     * @param units
     * @param lat
     * @param lon
     * @param listener
     */
    public static void getForecastIOHourlyForecast(final String apiKey, final String units, final String lat, final String lon, final ForecastListener listener) {
        new AsyncTask<Void, Void, ForecastResponse>() {

            @Override
            protected ForecastResponse doInBackground(Void... params) {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://api.forecast.io").build();
                ForecastRequest service = restAdapter.create(ForecastRequest.class);
                String excludes = "minutely,daily,flags";
                try {
                    return service.getHourlyForecast(apiKey, lat, lon, excludes, units, Locale.getDefault().getLanguage());
                } catch (RetrofitError error) {
                    Log.w("ForecastModule", "Forecast error: " + error.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ForecastResponse forecastResponse) {
                if (forecastResponse != null) {
                    if (forecastResponse.currently != null) {
                        listener.onWeatherToday(forecastResponse.currently.getDisplayTemperature() + " " + forecastResponse.currently.summary);
                    }
                    if (forecastResponse.hourly != null && forecastResponse.hourly.data != null && (ConfigurationSettings.isDemoMode() || WeekUtil.isWeekdayBeforeFive())) {
                        listener.onShouldBike(true, shouldBikeToday(forecastResponse.hourly.data));
                    } else {
                        listener.onShouldBike(false, true);
                    }
                }
            }

            private boolean shouldBikeToday(List<ForecastResponse.Hour> hours) {
                int dayOfMonthToday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                for (ForecastResponse.Hour hour : hours) {
                    Calendar hourCalendar = hour.getCalendar();
                    // Only check hourly forecast for today
                    if (hourCalendar.get(Calendar.DAY_OF_MONTH) == dayOfMonthToday) {
                        int hourOfDay = hourCalendar.get(Calendar.HOUR_OF_DAY);
                        if (hourOfDay >= 7 && hourOfDay <= 11) {
                            if (hour.precipProbability >= 0.3) {
                                return false;
                            }
                        } else if (hourOfDay >= 17 && hourOfDay <= 19 && hour.precipProbability >= 0.3) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }.execute();
    }

    /**
     * @param apiKey   The api key for the openweather api
     * @param units
     * @param lat
     * @param lon
     * @param listener
     */
    public static void getOpenWeatherForecast(final String apiKey, final String units, final String lat, final String lon, final ForecastListener listener) {
        new AsyncTask<Void, Void, OpenWeatherResponse>() {

            @Override
            protected OpenWeatherResponse doInBackground(Void... params) {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://api.openweathermap.org").build();
                OpenWeatherRequest service = restAdapter.create(OpenWeatherRequest.class);
                try {
                    return service.getCurrentForecast(apiKey, lat, lon, getOpenWeatherUnits(units), Locale.getDefault().getLanguage());
                } catch (RetrofitError error) {
                    Log.w("ForecastModule", "Forecast error: " + error.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(OpenWeatherResponse response) {
                if (response != null && response.main != null) {
                    listener.onWeatherToday(response.main.getDisplayTemperature() + " " + response.getWeatherDescription());
                }
            }
        }.execute();
    }

    @NonNull
    private static String getOpenWeatherUnits(String units) {
        if (units.equalsIgnoreCase(ForecastRequest.UNITS_SI)) {
            return OpenWeatherRequest.UNITS_METRIC;
        } else if (units.equalsIgnoreCase(ForecastRequest.UNITS_US)) {
            return OpenWeatherRequest.UNITS_IMPERIAL;
        }
        return units;
    }
}
