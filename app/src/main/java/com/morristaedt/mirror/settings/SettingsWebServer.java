package com.morristaedt.mirror.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.HttpException;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by alex on 12/09/15.
 */
public class SettingsWebServer {
    public static final int PORT = 8080;

    private boolean serverIsRunning = false;
    private BasicHttpProcessor basicHttpProcessor;
    private BasicHttpContext basicHttpContext;
    private HttpService httpService ;
    private HttpRequestHandlerRegistry handlerRegistry;
    private ServerSocket serverSocket;

    private String settingsPagePath = "/";
    private String settingsSavePath = "/set_settings";

    public SettingsWebServer(Context context) {
        Log.d("SettingsWebServer", "Creating web server");
        basicHttpProcessor = new BasicHttpProcessor();
        basicHttpContext = new BasicHttpContext();

        basicHttpProcessor.addInterceptor(new ResponseDate());
        basicHttpProcessor.addInterceptor(new ResponseServer());
        basicHttpProcessor.addInterceptor(new ResponseContent());
        basicHttpProcessor.addInterceptor(new ResponseConnControl());

        httpService = new HttpService(basicHttpProcessor, new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory());

        handlerRegistry = new HttpRequestHandlerRegistry();
        handlerRegistry.register(settingsPagePath, new SettingsPageHandler());
        handlerRegistry.register(settingsSavePath, new SettingsSaveHandler(context));

        httpService.setHandlerResolver(handlerRegistry);
    }

    public void handleRequests() {
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setReuseAddress(true);

            while (serverIsRunning) {
                final Socket clientSocket = serverSocket.accept();
                DefaultHttpServerConnection serverConnection = new DefaultHttpServerConnection();
                serverConnection.bind(clientSocket, new BasicHttpParams());
                httpService.handleRequest(serverConnection, basicHttpContext);
                serverConnection.shutdown();
            }

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        }
        serverIsRunning = false;
    }

    public synchronized void startServer() {
        serverIsRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                handleRequests();
            }
        }).start();
    }

    public synchronized void stopServer() {
        serverIsRunning = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
