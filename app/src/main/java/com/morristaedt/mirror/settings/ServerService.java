package com.morristaedt.mirror.settings;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.morristaedt.mirror.MirrorApplication;

public class ServerService extends Service {
    private SettingsWebServer server;

    @Override
    public void onCreate() {
        super.onCreate();
        server = new SettingsWebServer(this);
        server.startServer();
    }

    @Override
    public void onDestroy() {
        server.stopServer();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
