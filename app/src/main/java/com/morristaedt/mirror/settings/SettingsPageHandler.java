package com.morristaedt.mirror.settings;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by alex on 12/09/15.
 */
public class SettingsPageHandler implements HttpRequestHandler {
    private static final String PAGE_HTML = "<html><head><title>HomeMirror Settings</title></head>" +
            "<body>" +
            "<form action=\"set_settings\" method =\"post\">" +
            "<h3>Forecast.io API Key</h3>" +
            "<input type=\"text\" name=\"dark_sky_api_key\"></input><br />\n" +
            "<input type=\"submit\" value=\"Save settings\">\n" +
            "</form>" +
            "</body>";


    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        HttpEntity entity = new EntityTemplate(new ContentProducer() {
            public void writeTo(final OutputStream outstream) throws IOException {
                OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");
                writer.write(PAGE_HTML);
                writer.flush();
            }
        });
        httpResponse.setHeader("Content-Type", "text/html");
        httpResponse.setEntity(entity);

    }
}
