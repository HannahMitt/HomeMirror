package com.morristaedt.mirror.modules;

import android.os.AsyncTask;
import android.util.Log;

import com.morristaedt.mirror.requests.YahooFinanceRequest;
import com.morristaedt.mirror.requests.YahooStockResponse;

import java.math.BigDecimal;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by HannahMitt on 8/22/15.
 */
public class YahooFinanceModule {

    public interface StockListener {
        void onNewStockPrice(YahooStockResponse.YahooQuoteResponse quoteResponse);
    }

    /**
     * Fetch the the latest stock price, but only show it if its a large change
     *
     * @param listener
     */
    public static void getStockForToday(final String stockName, final StockListener listener) {

        new AsyncTask<Void, Void, YahooStockResponse>() {

            @Override
            protected YahooStockResponse doInBackground(Void... params) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint("http://query.yahooapis.com/v1/public")
                        .setErrorHandler(new ErrorHandler() {
                            @Override
                            public Throwable handleError(RetrofitError cause) {
                                Log.w("mirror", "Yahoo Finance error: " + cause);
                                return cause;
                            }
                        })
                        .build();

                YahooFinanceRequest service = restAdapter.create(YahooFinanceRequest.class);

                String query = "select * from yahoo.finance.quotes where symbol in (\"" + stockName + "\")";
                String env = "http://datatables.org/alltables.env";
                String format = "json";
                return service.getStockData(query, env, format);
            }

            @Override
            protected void onPostExecute(YahooStockResponse stockResponse) {
                if (stockResponse != null && stockResponse.getQuoteResponse() != null) {
                    YahooStockResponse.YahooQuoteResponse quoteResponse = stockResponse.getQuoteResponse();
                    if (quoteResponse.getPercentageChange().abs().compareTo(BigDecimal.valueOf(0.03)) >= 0) {
                        listener.onNewStockPrice(quoteResponse);
                        return;
                    }
                }
                listener.onNewStockPrice(null);
            }
        }.execute();

    }
}
