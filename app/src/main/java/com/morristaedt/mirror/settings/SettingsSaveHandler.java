package com.morristaedt.mirror.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.morristaedt.mirror.MirrorActivity;
import com.morristaedt.mirror.MirrorApplication;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by alex on 13/09/15.
 */
public class SettingsSaveHandler implements HttpRequestHandler {

    public static final String DARK_SKY_API_KEY = "dark_sky_api_key";
    private Context context;

    public SettingsSaveHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MirrorApplication.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) httpRequest;
        HttpEntity entity = request.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        String out = reader.readLine();
        while (out != null && !out.equals("")) {
            String[] splitOutput = out.split("=");
        if (splitOutput.length == 2) {
            if (splitOutput[0].equals(DARK_SKY_API_KEY)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(DARK_SKY_API_KEY, splitOutput[1]);
                editor.apply();
            }
        }

            //Get the next line from the reader, will be null if empty and loop will terminate
            out = reader.readLine();
        }

        // Return settings page again
        new SettingsPageHandler().handle(httpRequest, httpResponse, httpContext);

        // Force refresh of main screen
        Intent mainActivityIntent = new Intent(context, MirrorActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mainActivityIntent);

    }
}
