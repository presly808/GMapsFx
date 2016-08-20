package com.lynden.model;

import com.lynden.gmapsfx.javascript.object.LatLong;

/**
 * Created by macaque on 19.08.2016.
 */
public class Coordinates {

    private LatLong coordinates;
    private String title;

    public Coordinates(LatLong coordinates, String title) {
        this.coordinates = coordinates;
        this.title = title;
    }

    public LatLong getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLong coordinates) {
        this.coordinates = coordinates;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
