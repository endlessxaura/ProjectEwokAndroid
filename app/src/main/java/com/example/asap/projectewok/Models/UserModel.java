package com.example.asap.projectewok.Models;

import java.util.LinkedList;

/**
 * Created by asap on 8/18/16.
 */
public class UserModel {
    //Properties
    public int userID;
    public String firstName;
    public String lastName;
    public String email;
    public LinkedList<ReviewModel> reviews;
    public LinkedList<GeolocationModel> geolocations;

    //Constructor
    public UserModel(int userID, String firstName, String lastName, String email, LinkedList<ReviewModel> reviews, LinkedList<GeolocationModel> geolocations){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.reviews = reviews;
        this.geolocations = geolocations;
    }

    public UserModel(int userID, String firstName, String lastName, String email){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    //Functions
    //TODO: Get reviews

}
