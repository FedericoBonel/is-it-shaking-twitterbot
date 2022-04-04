package com.isitshaking.model;

import twitter4j.JSONArray;
import twitter4j.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class that represents a connection to the USGS RestAPI
 */
public class USGSConnection {
    private final String baseUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2022-04-04&limit=1&orderby=time";

    public USGSConnection() {
    }

    /**
     * Gets the last earthquake
     * @return The last earthquake that happened
     */
    public Earthquake getLastEarthquake() {
        URL finalURL = createUrl(baseUrl);
        try {
            String jsonResponse = makeGETHttpRequest(finalURL);
            List<Earthquake> lastEarthquake = extractEarthquakes(jsonResponse);
            return lastEarthquake.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Extracts earthquake objects from json response
     * @param jsonResponse String of a json response
     * @return A list of earthquakes contained in that json response
     */
    private List<Earthquake> extractEarthquakes(String jsonResponse) {
        JSONObject response = new JSONObject(jsonResponse);
        JSONArray earthquakes = response.getJSONArray("features");
        List<Earthquake> result = new ArrayList<Earthquake>();
        for(int i = 0; i < earthquakes.length(); i++)  {
            JSONObject earthquakeData = earthquakes.getJSONObject(i);
            JSONObject properties = earthquakeData.getJSONObject("properties");
            // id
            String badFormatId = properties.getString("ids");
            String id = badFormatId.substring(1, badFormatId.length()-1);
            // Location
            String location = properties.getString("place");
            // Url
            String url = properties.getString("url");
            // Magnitude
            Float magnitude = Float.parseFloat(properties.getString("mag"));
            // Time
            Date time = new Date(Long.parseLong(properties.getString("time")));
            result.add(new Earthquake(id, location, url, magnitude, time));
        }
        return result;
    }

    /**
     * Creates a url
     * @param url String of the url
     * @return a URL object corresponding to that string
     */
    private URL createUrl(String url) {
        URL finalURL;
        try {
            finalURL = new URL(url);
            return finalURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executes a get http request to the url that's being passed
     * @param url object representing the url where we want to send the request to
     * @return A string representing the json response
     * @throws IOException if an I/O error ocurrs
     */
    private String makeGETHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                System.out.println("Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Parses an input stream from an HTTP response into a json response
     * @return json response from that HTTP request
     * @throws IOException if an I/O error ocurrs
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}