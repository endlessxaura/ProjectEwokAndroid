package com.example.asap.projectewok.API;

import android.content.Context;
import android.util.Log;

import com.example.asap.projectewok.Helpers.CompletionHandler;
import com.example.asap.projectewok.Helpers.RequestMaker;
import com.example.asap.projectewok.Helpers.Requester;
import com.example.asap.projectewok.Models.GeolocationModel;
import com.example.asap.projectewok.Models.PictureModel;
import com.example.asap.projectewok.Models.ReviewModel;
import com.example.asap.projectewok.Models.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by asap on 8/18/16.
 */
public class ApiInterface extends Requester {
    //Properties
    public Object returns;
    public ReturnsHandler onCompleteWithReturns;
    public Authenticator auth;
    public CompletionHandler setComplete = new CompletionHandler() {
        //Run this after each request
        @Override
        public void handle(JSONObject JSON) {
            //PRE: A JSON from a request
            //POST: sets completed to true and passes the JSON to onComplete, if it exists
            if(onComplete != null){
                onComplete.handle(JSON);
            }
            if(onCompleteWithReturns != null){
                onCompleteWithReturns.handle(returns);
            }
            completed = true;
        }
    };

    //Constructors
    public ApiInterface(Context context){
        //PRE: NEEDS THE APPLICATION CONTEXT
        //POST: returns an ApiInterface for use
        auth = Authenticator.getAuthenticator(context);
        this.context = context;
    }

    //Functions
    // ///////////////////////
    /////////////////////////
    //////////USERS//////////
    /////////////////////////
    /////////////////////////
    public void getUser(int userID){
        returns = null;
        completed = false;
        CompletionHandler setUser = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null) {
                    try {
                        JSONObject userJSON = JSON.getJSONObject("user");
                        int userID = userJSON.getInt("userID");
                        String firstName = userJSON.getString("firstName");
                        String lastName = userJSON.getString("lastName");
                        String email = userJSON.getString("email");
                        returns = new UserModel(userID, firstName, lastName, email);
                    }
                    catch (Exception e) {
                        Log.w("Error Reports", e.getMessage());
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester = new RequestMaker(context, "GET", "users/" + String.valueOf(userID), null);
        requester.run(setUser);
    }

    ////////////////////////////////
    ////////////////////////////////
    ////////////////////////////////
    //////////GEOLOCATIONS//////////
    ////////////////////////////////
    ////////////////////////////////
    ////////////////////////////////
    public void getRawGeolocations(int radius, double latitude, double longitude, String unit, String locationType, String name, String operatingTime){
        //PRE: radius, lat, and long must be provided, all others can be null. Filters according to settings.
        //      Operating time must be in the format HH:MM:SS (military time). This checks if it is open at that time.
        //POST: sets returns to a GeoJSON representing the geolocations
        returns = null;
        completed = false;
        String dataString = "radius=" + String.valueOf(radius) + "&latitude=" + String.valueOf(latitude) + "&longitude=" + String.valueOf(longitude);
        if(unit != null) {
            dataString += "&unit=" + unit;
        }
        if(locationType != null){
            dataString += "&locationType=" + locationType;
        }
        if(name != null){
            dataString += "&name=" + name;
        }
        if(operatingTime != null){
            dataString += "&operatingTime=" + operatingTime;
        }
        requester = new RequestMaker(context, "geolocations", dataString);
        CompletionHandler setRawGeolocations = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    returns = JSON;
                }
                setComplete.handle(JSON);
            }
        };
        requester.run(setRawGeolocations);
    }

    public void getRawGeolocations(String locationType, String name, String operatingTime){
        //PRE: radius, lat, and long must be provided, all others can be null. Filters according to settings.
        //      Operating time must be in the format HH:MM:SS (military time). This checks if it is open at that time.
        //POST: sets returns to a GeoJSON representing the geolocations
        returns = null;
        completed = false;
        String dataString = "";
        if(locationType != null){
            dataString += "&locationType=" + locationType;
        }
        if(name != null){
            dataString += "&name=" + name;
        }
        if(operatingTime != null){
            dataString += "&operatingTime=" + operatingTime;
        }
        if(!dataString.isEmpty()){
            dataString = dataString.substring(1);
        }
        requester = new RequestMaker(context, "geolocations", dataString);
        CompletionHandler setRawGeolocations = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    returns = JSON;
                }
                setComplete.handle(JSON);
            }
        };
        requester.run(setRawGeolocations);
    }

    public void getRawGeolocations(int radius, double latitude, double longitude, String unit){
        //PRE: All the above variables must be provided, though unit can be null. Default unit is miles, "m". "k" for kilometers.
        //POST: sets returns to a GeoJSON representing the geolocations
        returns = null;
        completed = false;
        String dataString = "radius=" + String.valueOf(radius) + "&latitude=" + String.valueOf(latitude) + "&longitude=" + String.valueOf(longitude);
        if(unit != null) {
            dataString += "&unit=" + unit;
        }
        requester = new RequestMaker(context, "geolocations", dataString);
        CompletionHandler setRawGeolocations = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    returns = JSON;
                }
                setComplete.handle(JSON);
            }
        };
        requester.run(setRawGeolocations);
    }

    public void getRawGeolocations(){
        //POST: sets returns to a GeoJSON representing the geolocations
        returns = null;
        completed = false;
        requester = new RequestMaker(context, "geolocations");
        CompletionHandler setRawGeolocations = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    returns = JSON;
                }
                setComplete.handle(JSON);
            }
        };
        requester.run(setRawGeolocations);
    }

    public void getGeolocations(int radius, double latitude, double longitude, String unit, String locationType, String name, String operatingTime){
        //PRE: radius, lat, and long must be provided, all others can be null. Filters according to settings.
        //      Operating time must be in the format HH:MM:SS (military time). This checks if it is open at that time.
        //POST: sets returns to a linked list of geolocations
        returns = null;
        completed = false;
        String dataString = "radius=" + String.valueOf(radius) + "&latitude=" + String.valueOf(latitude) + "&longitude=" + String.valueOf(longitude);
        if(unit != null) {
            dataString += "&unit=" + unit;
        }
        if(locationType != null){
            dataString += "&locationType=" + locationType;
        }
        if(name != null){
            dataString += "&name=" + name;
        }
        if(operatingTime != null){
            dataString += "&operatingTime=" + operatingTime;
        }
        dataString += "&GeoJSON=0";
        requester = new RequestMaker(context, "geolocations", dataString);
        CompletionHandler setGeolocations = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    try{
                        JSONArray geolocationsJSON = JSON.getJSONArray("geolocations");
                        LinkedList<GeolocationModel> geolocations = new LinkedList<GeolocationModel>();
                        for(int i = 0; i < geolocationsJSON.length(); i++){
                            JSONObject geolocationJSON =  (JSONObject)geolocationsJSON.get(i);
                            int geolocationID = geolocationJSON.getInt("geolocationID");
                            double latitude = geolocationJSON.getDouble("latitude");
                            double longitude = geolocationJSON.getDouble("longitude");
                            String name = geolocationJSON.getString("name");
                            double averageRating = geolocationJSON.getDouble("averageRating");
                            String description = geolocationJSON.optString("description");
                            int locationID = geolocationJSON.optInt("location_id");
                            String locationType = geolocationJSON.optString("location_type");
                            GeolocationModel geolocation = new GeolocationModel(geolocationID, latitude, longitude, name, averageRating, description, locationID, locationType);
                            geolocations.add(geolocation);
                        }
                        returns = geolocations;
                    }
                    catch(Exception e) {
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error ReportS", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester.run(setGeolocations);
    }

    public void getGeolocations(String locationType, String name, String operatingTime){
        //PRE: radius, lat, and long must be provided, all others can be null. Filters according to settings.
        //      Operating time must be in the format HH:MM:SS (military time). This checks if it is open at that time.
        //POST: sets returns to a linked list of geolocations
        returns = null;
        completed = false;
        String dataString = "GeoJSON=0";
        if(locationType != null){
            dataString += "&locationType=" + locationType;
        }
        if(name != null){
            dataString += "&name=" + name;
        }
        if(operatingTime != null){
            dataString += "&operatingTime=" + operatingTime;
        }
        requester = new RequestMaker(context, "geolocations", dataString);
        CompletionHandler setGeolocations = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    try{
                        JSONArray geolocationsJSON = JSON.getJSONArray("geolocations");
                        LinkedList<GeolocationModel> geolocations = new LinkedList<GeolocationModel>();
                        for(int i = 0; i < geolocationsJSON.length(); i++){
                            JSONObject geolocationJSON =  (JSONObject)geolocationsJSON.get(i);
                            int geolocationID = geolocationJSON.getInt("geolocationID");
                            double latitude = geolocationJSON.getDouble("latitude");
                            double longitude = geolocationJSON.getDouble("longitude");
                            String name = geolocationJSON.getString("name");
                            double averageRating = geolocationJSON.getDouble("averageRating");
                            String description = geolocationJSON.optString("description");
                            int locationID = geolocationJSON.optInt("location_id");
                            String locationType = geolocationJSON.optString("location_type");
                            GeolocationModel geolocation = new GeolocationModel(geolocationID, latitude, longitude, name, averageRating, description, locationID, locationType);
                            geolocations.add(geolocation);
                        }
                        returns = geolocations;
                    }
                    catch(Exception e) {
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error ReportS", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester.run(setGeolocations);
    }

    public void getGeolocations(int radius, double latitude, double longitude, String unit){
        //PRE: All the above variables must be provided, though unit can be null. Default unit is miles, "m". "k" for kilometers.
        //POST: sets returns to a linked list of geolocations
        returns = null;
        completed = false;
        String dataString = "radius=" + String.valueOf(radius) + "&latitude=" + String.valueOf(latitude) + "&longitude=" + String.valueOf(longitude);
        if(unit != null) {
            dataString += "&unit=" + unit;
        }
        dataString += "&GeoJSON=0";
        requester = new RequestMaker(context, "geolocations", dataString);
        CompletionHandler setGeolocations = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    try{
                        JSONArray geolocationsJSON = JSON.getJSONArray("geolocations");
                        LinkedList<GeolocationModel> geolocations = new LinkedList<GeolocationModel>();
                        for(int i = 0; i < geolocationsJSON.length(); i++){
                            JSONObject geolocationJSON =  (JSONObject)geolocationsJSON.get(i);
                            int geolocationID = geolocationJSON.getInt("geolocationID");
                            double latitude = geolocationJSON.getDouble("latitude");
                            double longitude = geolocationJSON.getDouble("longitude");
                            String name = geolocationJSON.getString("name");
                            double averageRating = geolocationJSON.getDouble("averageRating");
                            String description = geolocationJSON.optString("description");
                            int locationID = geolocationJSON.optInt("location_id");
                            String locationType = geolocationJSON.optString("location_type");
                            GeolocationModel geolocation = new GeolocationModel(geolocationID, latitude, longitude, name, averageRating, description, locationID, locationType);
                            geolocations.add(geolocation);
                        }
                        returns = geolocations;
                    }
                    catch(Exception e) {
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error ReportS", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester.run(setGeolocations);
    }

    public void getGeolocations(){
        //POST: sets returns to a linked list of geolocations
        returns = null;
        completed = false;
        requester = new RequestMaker(context, "geolocations", "GeoJSON=0");
        CompletionHandler setGeolocations = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    try{
                        JSONArray geolocationsJSON = JSON.getJSONArray("geolocations");
                        LinkedList<GeolocationModel> geolocations = new LinkedList<GeolocationModel>();
                        for(int i = 0; i < geolocationsJSON.length(); i++){
                            JSONObject geolocationJSON =  (JSONObject)geolocationsJSON.get(i);
                            int geolocationID = geolocationJSON.getInt("geolocationID");
                            double latitude = geolocationJSON.getDouble("latitude");
                            double longitude = geolocationJSON.getDouble("longitude");
                            String name = geolocationJSON.getString("name");
                            double averageRating = geolocationJSON.getDouble("averageRating");
                            String description = geolocationJSON.optString("description");
                            int locationID = geolocationJSON.optInt("location_id");
                            String locationType = geolocationJSON.optString("location_type");
                            GeolocationModel geolocation = new GeolocationModel(geolocationID, latitude, longitude, name, averageRating, description, locationID, locationType);
                            geolocations.add(geolocation);
                        }
                        returns = geolocations;
                    }
                    catch(Exception e) {
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error ReportS", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester.run(setGeolocations);
    }

    public void getGeolocation(int geolocationID){
        //PRE: geolocationID must match an ID in the database
        //POST: sets returns to a GeolocationModel for the data
        returns = null;
        completed = false;
        requester = new RequestMaker(context, "geolocations/" + String.valueOf(geolocationID), "GeoJSON=0");
        CompletionHandler setGeolocation = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    try{
                        JSONObject geolocationJSON = JSON.getJSONObject("geolocation");
                        int geolocationID = geolocationJSON.getInt("geolocationID");
                        double latitude = geolocationJSON.getDouble("latitude");
                        double longitude = geolocationJSON.getDouble("longitude");
                        String name = geolocationJSON.getString("name");
                        double averageRating = geolocationJSON.getDouble("averageRating");
                        String description = geolocationJSON.optString("description");
                        int locationID = geolocationJSON.optInt("location_id");
                        String locationType = geolocationJSON.optString("location_type");
                        returns = new GeolocationModel(geolocationID, latitude, longitude, name, averageRating, description, locationID, locationType);
                    }
                    catch(Exception e){
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error ReportS", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester.run(setGeolocation);
    }

    public void createNewGeolocation(double latitude, double longitude, double submitterLatitude, double submitterLongitude, String name, String description, Integer locationID, String locationType){
        //PRE: latitude, submitterLatitude, longitude, and submitterLongitude must be doubles. Coordinates must be within half a mile of eachother. Name must be a string.
        //POST: creates the location in the DB
        returns = null;
        completed = false;
        String dataString = "latitude=" + latitude + "&longitude=" + longitude + "&submitterLatitude=" + submitterLatitude + "&submitterLongitude=" + submitterLongitude + "&name=" + name;
        if(description != null){
            dataString += "&description=" + description;
        }
        if(locationID != null){
            dataString += "&locationID=" + locationID;
        }
        if(locationType != null){
            dataString += "&locationType=" + locationType;
        }
        requester = new RequestMaker(context, "POST", "geolocations", dataString);
        CompletionHandler getGeolocationAfterCreation = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if (JSON != null && requester.error == null) {
                    try{
                        int geolocationID = JSON.getInt("ID");
                        getGeolocation(geolocationID);
                    }
                    catch (Exception e){
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error ReportS", "From server: " + requester.error);
                    }
                }
                else{
                    setComplete.handle(JSON);
                }
            }
        };
        if(auth.token != null){
            requester.authorize(auth.token);
        }
        requester.run(getGeolocationAfterCreation);
    }

    public void updateGeolocation(GeolocationModel geolocation, double submitterLatitude, double submitterLongitude){
        //PRE: A geolocation model to update must be provided. This model must already exist in the database. The submitter's location cannot be more than half a mile away.
        //POST: Updates the geolocation in the database with the model. Any errors will be contained in the requester.
        returns = null;
        completed = false;
        String dataString = "latitude=" + geolocation.latitude + "&longitude=" + geolocation.longitude + "&name=" + geolocation.name;
        if(geolocation.description != null){
            dataString += "&description=" + geolocation.description;
        }
        requester = new RequestMaker(context, "PUT", "geolocations/" + geolocation.geolocationID, dataString);
        if(auth.token != null){
            requester.authorize(auth.token);
        }
        requester.run(setComplete);
    }

    public void validateLocation(GeolocationModel geolocation, double submitterLatitude, double submitterLongitude, boolean valid){
        //PRE: A geolocation model to update must be provided. This model must already exist in the database. The submitter's location cannot be more than half a mile away.
        //POST: Validates the location on behalf of the user. If more than 50% of validations report invalid, destroys the geolocation.
        returns = null;
        completed = false;
        String dataString = "valid=" + (valid ? 1 : 0) + "&submitterLatitude=" + submitterLatitude + "&submitterLongitude=" + submitterLongitude;
        requester = new RequestMaker(context, "POST", "geolocations/" + geolocation.geolocationID, dataString);
        if(auth.token != null){
            requester.authorize(auth.token);
        }
        requester.run(setComplete);
    }

    ///////////////////////////
    ///////////////////////////
    //////////REVIEWS//////////
    ///////////////////////////
    ///////////////////////////
    public void getReviews(Integer geolocationID, Integer userID){
        //PRE: both geolocationID and userID can be used to narrow the search
        //POST: makes a request based on the above and sets reviews to an array of review models
        returns = null;
        completed = false;
        String dataString = "";
        if(geolocationID != null){
            dataString += "&geolocationID=" + geolocationID;
        }
        if(userID != null){
            dataString += "&userID=" + userID;
        }
        CompletionHandler setReviews = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    try{
                        JSONArray reviewsJSON = JSON.getJSONArray("reviews");
                        LinkedList<ReviewModel> reviews = new LinkedList<ReviewModel>();
                        for(int i = 0; i < reviewsJSON.length(); i++){
                            JSONObject reviewJSON = reviewsJSON.getJSONObject(i);
                            int reviewID = reviewJSON.getInt("reviewID");
                            int userID = reviewJSON.getInt("userID");
                            int geolocationID = reviewJSON.getInt("geolocationID");
                            int rating = reviewJSON.getInt("rating");
                            String comment = reviewJSON.optString("comment");
                            ReviewModel review = new ReviewModel(reviewID, userID, geolocationID, rating, comment);
                            reviews.add(review);
                        }
                        Log.w("Reports", reviews.toString());
                        returns = reviews;
                    }
                    catch(Exception e){
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error Reports", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        if(!dataString.isEmpty()){
            dataString = dataString.substring(1);
        }
        requester = new RequestMaker(context, "reviews", dataString);
        Log.w("Reports", "Running request...");
        requester.run(setReviews);
    }

    public void getReviews(){
        //POST: makes a request based on the above and sets reviews to an array of review models
        returns = null;
        completed = false;
        CompletionHandler setReviews = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    try{
                        JSONArray reviewsJSON = JSON.getJSONArray("reviews");
                        LinkedList<ReviewModel> reviews = new LinkedList<ReviewModel>();
                        for(int i = 0; i < reviewsJSON.length(); i++){
                            JSONObject reviewJSON = reviewsJSON.getJSONObject(i);
                            int reviewID = reviewJSON.getInt("reviewID");
                            int userID = reviewJSON.getInt("userID");
                            int geolocationID = reviewJSON.getInt("geolocationID");
                            int rating = reviewJSON.getInt("rating");
                            String comment = reviewJSON.optString("comment");
                            ReviewModel review = new ReviewModel(reviewID, userID, geolocationID, rating, comment);
                            reviews.add(review);
                        }
                        returns = reviews;
                    }
                    catch(Exception e){
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error Reports", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester = new RequestMaker(context, "reviews");
        requester.run(setReviews);
    }

    public void getReview(int reviewID){
        //PRE: reviewID must match a reviewID in the database
        //POST: makes a request and sets returns to a specific review model
        returns = null;
        completed = false;
        CompletionHandler setReview = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    try{
                        JSONObject reviewJSON = JSON.getJSONObject("review");
                        int reviewID = reviewJSON.getInt("reviewID");
                        int userID = reviewJSON.getInt("userID");
                        int geolocationID = reviewJSON.getInt("geolocationID");
                        int rating = reviewJSON.getInt("rating");
                        String comment = reviewJSON.optString("comment");
                        returns = new ReviewModel(reviewID, userID, geolocationID, rating, comment);
                    }
                    catch(Exception e){
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error Reports", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester = new RequestMaker(context, "reviews/" + reviewID);
        requester.run(setReview);
    }

    public void createNewReview(int geolocationID, int rating, String comment){
        //PRE: geolocationID must match a geolocation in the DB. rating must be between 0 and 5.
        //POST: creates a geolocation in the database and sets returns to it
        returns = null;
        completed = false;
        String dataString = "geolocationID=" + geolocationID + "&rating=" + rating;
        if(comment != null){
            dataString += "&comment=" + comment;
        }
        CompletionHandler setReviewAfterCreation = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null && requester.error == null){
                    try{
                        int reviewID = JSON.getInt("ID");
                        getReview(reviewID);
                    }
                    catch (Exception e){
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error Reports", "From server: " + requester.error);
                    }
                }
                else{
                    setComplete.handle(JSON);
                }
            }
        };
        requester = new RequestMaker(context, "POST", "reviews", dataString);
        if(auth.token != null){
            requester.authorize(auth.token);
        }
        requester.run(setReviewAfterCreation);
    }

    public void updateReview(ReviewModel review){
        returns = null;
        completed = false;
        String dataString = "rating=" + review.rating;
        if(review.comment != null){
            dataString += "&comment=" + review.comment;
        }
        requester = new RequestMaker(context, "PUT", "reviews/" + review.reviewID, dataString);
        if(auth.token != null){
            requester.authorize(auth.token);
        }
        requester.run(setComplete);
    }

    public void destroyReview(ReviewModel review){
        returns = null;
        completed = false;
        requester = new RequestMaker(context, "DELETE", "reviews/" + review.reviewID, null);
        if(auth.token != null){
            requester.authorize(auth.token);
        }
        requester.run(setComplete);
    }

    public void getPictures(Integer ID, String model){
        returns = null;
        completed = false;
        String dataString = "";
        if(ID != null){
            dataString += "&id=" + ID;
        }
        if(model != null){
            dataString += "&model=" + model;
        }
        if(!dataString.isEmpty()){
            dataString = dataString.substring(1);
        }
        CompletionHandler setPictures = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    try{
                        LinkedList<PictureModel> pictures = new LinkedList<PictureModel>();
                        JSONArray picturesJSON = JSON.getJSONArray("pictures");
                        for(int i = 0; i < picturesJSON.length(); i++){
                            JSONObject pictureJSON = picturesJSON.getJSONObject(1);
                            int pictureID = pictureJSON.getInt("pictureID");
                            String attachedType = pictureJSON.getString("attached_type");
                            int attachedID = pictureJSON.getInt("attached_id");
                            String filePath = pictureJSON.getString("filePath");
                            PictureModel picture = new PictureModel(pictureID, attachedType, attachedID, filePath);
                            pictures.add(picture);
                        }
                        returns = pictures;
                    }
                    catch (Exception e){
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error Reports", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester = new RequestMaker(context, "GET", "pictures", dataString);
        requester.run(setPictures);
    }

    public void getPictures(){
        returns = null;
        completed = false;
        CompletionHandler setPictures = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    try{
                        LinkedList<PictureModel> pictures = new LinkedList<PictureModel>();
                        JSONArray picturesJSON = JSON.getJSONArray("pictures");
                        for(int i = 0; i < picturesJSON.length(); i++){
                            JSONObject pictureJSON = picturesJSON.getJSONObject(1);
                            int pictureID = pictureJSON.getInt("pictureID");
                            String attachedType = pictureJSON.getString("attached_type");
                            int attachedID = pictureJSON.getInt("attached_id");
                            String filePath = pictureJSON.getString("filePath");
                            PictureModel picture = new PictureModel(pictureID, attachedType, attachedID, filePath);
                            pictures.add(picture);
                        }
                        returns = pictures;
                    }
                    catch (Exception e){
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error Reports", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester = new RequestMaker(context, "GET", "pictures", null);
        requester.run(setPictures);
    }

    public void getPicture(int pictureID, Boolean model) {
        returns = null;
        completed = false;
        String dataString = "";
        if (model != null) {
            dataString += "model=" + (model ? 1 : 0);
        }
        else{
            dataString += "model=0";
        }
        CompletionHandler setPicture = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if (JSON != null) {
                    try {
                        JSONObject pictureJSON = JSON.getJSONObject("picture");
                        int pictureID = pictureJSON.getInt("pictureID");
                        String attachedType = pictureJSON.getString("attached_type");
                        int attachedID = pictureJSON.getInt("attached_id");
                        String filePath = pictureJSON.getString("filePath");
                        returns = new PictureModel(pictureID, attachedType, attachedID, filePath);
                    } catch (Exception e) {
                        Log.w("Error Reports", e.getMessage());
                        Log.w("Error Reports", "From server: " + requester.error);
                    }
                }
                setComplete.handle(JSON);
            }
        };
        requester = new RequestMaker(context, "pictures/" + pictureID, dataString);
        requester.run(setPicture);
    }

    public void getPicture(int itemID, String model){
        returns = null;
        completed = false;
        String dataString = "id=" + itemID + "&model=" + model;
        //TODO: PICTURE REQUEST MAKER
    }
}
