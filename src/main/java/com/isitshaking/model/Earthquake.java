package com.isitshaking.model;

import java.util.Date;

/**
 * Abstraction of a earthquake
 */
public class Earthquake {
    private final String id;
    private final String location;
    private final String url;
    private final Float magnitude;
    private final Date time;

    public Earthquake(String id, String location, String url, Float magnitude, Date time) {
        this.id = id;
        this.location = location;
        this.url = url;
        this.magnitude = magnitude;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getUrl() {
        return url;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public Date getTime() {
        return time;
    }

    public String buildEarthquakeTweet() {
        StringBuilder tweet = new StringBuilder();
        tweet.append("It sure did shake! Last #earthquake was at: ").append(time)
                .append("\n in: ").append(location)
                .append("\nIts magnitude was: ").append(magnitude.toString())
                .append("\nGet more information at: ").append(url);
        return tweet.toString();
    }
}
