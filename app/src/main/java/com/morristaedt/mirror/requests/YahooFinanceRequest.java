package com.morristaedt.mirror.requests;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by HannahMitt on 8/23/15.
 */
public interface YahooFinanceRequest {

    @GET("/yql")
    YahooStockResponse getStockData(@Query("q") String query, @Query("env") String env, @Query("format") String format);
}
