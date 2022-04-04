package com.isitshaking.model;

import java.util.Date;

/**
 * Abstraction of a earthquake
 */
public class Earthquake {
    private final String id;
    private final String location;
    private final String url;
    private final float magnitude;
    private final Date time;

    public Earthquake(String id, String location, String url, float magnitude, Date time) {
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
}
