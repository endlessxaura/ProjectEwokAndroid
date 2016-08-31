package com.example.asap.projectewok.Models;

import com.example.asap.projectewok.Models.GeolocationModel;
import com.example.asap.projectewok.Models.UserModel;

/**
 * Created by asap on 8/18/16.
 */
public class ReviewModel {
    //Properties
    public int reviewID;
    public int userID;
    public int geolocationID;
    public int rating;
    public String comment;
    public UserModel user;
    public GeolocationModel geolocation;

    //Constructors
    public ReviewModel(int reviewID, int userID, int geolocationID, int rating, String comment){
        this.reviewID = reviewID;
        this.userID = userID;
        this.geolocationID = geolocationID;
        this.rating = rating;
        this.comment = comment;
    }
}
