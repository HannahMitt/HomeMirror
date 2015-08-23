package com.morristaedt.mirror.requests;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by HannahMitt on 8/23/15.
 */
public interface ForecastRequest {

    @GET("/forecast/{apikey}/{lat},{lon}")
    ForecastResponse getHourlyForecast(@Path("apikey") String apiKey, @Path("lat") double lat, @Path("lon") double lon, @Query("exclude") String exclude, @Query("units") String units);
}
