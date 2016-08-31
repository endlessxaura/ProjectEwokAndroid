package com.example.asap.projectewok.Models;

import java.sql.Time;

/**
 * Created by asap on 8/8/16.
 */
public class GeolocationModel {
    //Properties
    public int geolocationID;
    public double latitude;
    public double longitude;
    public String name;
    public String description;
    public double averageRating;
    public int locationID;
    public String locationType;
    //TODO: Reviews
    //TODO: Location

    //Constructors
    public GeolocationModel(int gID, double lat, double lon, String name, double avR, String desc, int lID, String lType){
        geolocationID = gID;
        latitude = lat;
        longitude = lon;
        this.name = name;
        averageRating = avR;
        description = desc;
        locationID = lID;
        locationType = lType;
    }

    public GeolocationModel(int gID, double lat, double lon, String name, double avR, String desc){
        geolocationID = gID;
        latitude = lat;
        longitude = lon;
        this.name = name;
        averageRating = avR;
        description = desc;
    }

    public GeolocationModel(int gID, double lat, double lon, String name, double avR){
        geolocationID = gID;
        latitude = lat;
        longitude = lon;
        this.name = name;
        averageRating = avR;
    }
}
