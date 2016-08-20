package com.lynden.model;

import com.lynden.gmapsfx.javascript.object.LatLong;

/**
 * Created by macaque on 19.08.2016.
 */
public class Coordinates {

    private double latitude;
    private double longitude;
    private String title;

    public Coordinates(double latitude, double longitude, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
