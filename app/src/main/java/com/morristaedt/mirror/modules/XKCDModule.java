package com.morristaedt.mirror.modules;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.morristaedt.mirror.requests.XKCDRequest;
import com.morristaedt.mirror.requests.XKCDResponse;

import java.util.Calendar;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by HannahMitt on 8/22/15.
 */
public class XKCDModule {

    public interface XKCDListener {
        void onNewXKCDToday(String url);
    }

    /**
     * Fetch the the latest xkcd comic, but only show it if its new today
     *
     * @param listener
     */
    public static void getXKCDForToday(final XKCDListener listener) {
        new AsyncTask<Void, Void, XKCDResponse>() {

            @Override
            protected XKCDResponse doInBackground(Void... params) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint("http://xkcd.com")
                        .setErrorHandler(new ErrorHandler() {
                            @Override
                            public Throwable handleError(RetrofitError cause) {
                                Log.w("mirror", "XKCD error: " + cause);
                                return null;
                            }
                        })
                        .build();

                XKCDRequest service = restAdapter.create(XKCDRequest.class);
                return service.getLatestXKCD();
            }

            @Override
            protected void onPostExecute(XKCDResponse xkcdResponse) {
                Calendar today = Calendar.getInstance();
                if (!TextUtils.isEmpty(xkcdResponse.img) && xkcdResponse.day == today.get(Calendar.DAY_OF_MONTH) && xkcdResponse.month == (today.get(Calendar.MONTH) + 1) && xkcdResponse.year == today.get(Calendar.YEAR)) {
                    listener.onNewXKCDToday(xkcdResponse.img);
                } else {
                    listener.onNewXKCDToday(null);
                }
            }
        }.execute();

    }
}
