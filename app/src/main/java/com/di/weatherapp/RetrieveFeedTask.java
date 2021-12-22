package com.di.weatherapp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

class RetrieveFeedTask extends AsyncTask<String, Void, JSONObject> {

    private Exception exception;

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            MainActivity.jsonStatic = json;


            JSONObject main = (JSONObject) json.get("main");
            return json;
        } finally {
            is.close();
        }
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        JSONObject json = new JSONObject();
        try {
            json = readJsonFromUrl(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    protected void onPostExecute(JSONObject feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
