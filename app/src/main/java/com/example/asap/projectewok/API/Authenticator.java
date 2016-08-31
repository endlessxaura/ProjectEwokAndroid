package com.example.asap.projectewok.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.asap.projectewok.Helpers.CompletionHandler;
import com.example.asap.projectewok.Helpers.RequestMaker;
import com.example.asap.projectewok.Helpers.Requester;
import com.example.asap.projectewok.Models.GeolocationModel;
import com.example.asap.projectewok.Models.ReviewModel;
import com.example.asap.projectewok.Models.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by asap on 8/18/16.
 */
public class Authenticator extends Requester {
    /**
     Basic usage:
     get the authenticator using the getAuthenticator function
     Always be sure to pass in the APPLICATION CONTEXT, not the activity context

     When logging in, use an authenticate method and
     check the requester for when it finishes. The
     requester contains an "error" property that will be set
     if there are any errors. This also contains a "completed"
     property that will tell you when all the tasks are completed.

     Note that a token expires after 60 minutes but may be refreshed
     within a 2 week window.

     When registering, use the register method or
     the registerAndAuthenticate method (per your desire).
     Handle it otherwise like an authentication.

     You may refresh the token periodically to prevent the user
     from being logged out too soon.

     When the user logs out, destroy the token.

     To check if the user already has a valid token, you may call
     getUser(). User will be nil if it isn't valid or a dictionary if it is
     valid. Make sure to try to refresh the token when you do this.
     You can always call refreshAndGetUser to avoid this.

     Valid, in most other cases, can tell you when the token is good, BUT! cannot always be
     garunteed to be set unless you call get user or authenticate. For this reason,
     always check if valid is or is not true, instead of checking if it is or is not false.

     valid == false BAD
     valid != true GOOD

     Note that user may already be set! The authenticator automatically gets the user when it
     is initialized. Be sure to check if the user is there before calling getUser needlessly

     As with all requesters, this also have a onComplete, setComplete, and completed variables
     that allow you to asynchronously run functions.
     */

    //Properties
    private static String PREFERENCEPATH = "com.asap.projectewok.Authentication";       //The path to the preferences file
    private static Authenticator sharedInstance;            //The shared instance of the authenticator
    public String token;                                    //The token for the user
    public UserModel user;                                  //The user's model, can be null
    public boolean valid;                                   //True if the token is good, false otherwise
    private SharedPreferences preferences;                  //The authentication preferences of the user

    //Constructors
    private Authenticator(Context context){
        //PRE: An application context
        //POST: creates an authenticator and loads a token from perferences if there is one
        this.context = context;
        preferences = context.getSharedPreferences(PREFERENCEPATH, Context.MODE_PRIVATE);
        token = preferences.getString("token", null);
        if(token == null){
            valid = false;
        }
        else{
            refreshAndGetUser();
        }
    }

    public static synchronized Authenticator getAuthenticator(Context context){
        //USE THIS TO GET THE SHARED INSTANCE; ALWAYS USE AN APPLICATION CONTEXT
        if(sharedInstance == null){
            sharedInstance = new Authenticator(context);
        }
        return sharedInstance;
    }

    public void authenticate(String email, String password){
        //PRE: a formatted email (_@_._) and a password must be provided
        //POST: Sets up an authentication request and sets the token to any returned token
        completed = false;
        String dataString = "email=" + email + "&password=" + password;
        requester = new RequestMaker(context, "POST", "authenticate", dataString);
        CompletionHandler setToken = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    token = JSON.optString("token", null);
                    if(token != null){
                        valid = true;
                    }
                    else{
                        valid = false;
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", token);
                    editor.commit();
                }
                else{
                    valid = false;
                }
                setComplete.handle(JSON);
            }
        };
        requester.run(setToken);
    }

    public void register(String email, String password, String confirmed, String firstName, String lastName){
        //PRE: All the information must be provided; confirmed and password must match
        //POST: registers the user in the API
        completed = false;
        String dataString = "email=" + email + "&password=" + password + "&password_confirmation=" + confirmed + "&firstName=" + firstName + "&lastName=" + lastName;
        requester = new RequestMaker(context, "POST", "register", dataString);
        requester.run(setComplete);
    }

    public void registerAndAuthenticate(final String email, final String password, String confirmed, String firstName, String lastName) {
        //PRE: All the information must be provided; confirmed and password must match
        //POST: registers the user in the API
        completed = false;
        String dataString = "email=" + email + "&password=" + password + "&password_confirmation=" + confirmed + "&firstName=" + firstName + "&lastName=" + lastName;
        requester = new RequestMaker(context, "POST", "register", dataString);
        CompletionHandler authenticateAfterRegistration = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                authenticate(email, password);
            }
        };
        requester.run(authenticateAfterRegistration);
    }

    public void refreshToken(){
        //PRE: a token must be within the authentication
        //POST: refreshes the token if it is valid
        completed = false;
        requester = new RequestMaker(context, "POST", "refreshToken", null);
        CompletionHandler setToken = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                Log.w("Reports", "handling refresh request");
                if(JSON != null){
                    token = JSON.optString("token", null);
                    if(token != null){
                        Log.w("Reports", "Refreshed Token: " + token);
                        valid = true;
                    }
                    else{
                        Log.w("Reports", "Token failed to refresh");
                        valid = false;
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", token);
                    editor.apply();
                }
                setComplete.handle(JSON);
            }
        };
        if(token != null){
            Log.w("Reports", "sending refresh request");
            requester.authorize(token);
            requester.run(setToken);
        }
        else{
            setComplete.handle(null);
        }
    }

    public void getUser(){
        //PRE: A token must be present for this request
        //POST: gets the user from the API and sets the user model of this to the obtained data
        completed = false;
        requester = new RequestMaker(context, "user");
        CompletionHandler setUser = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    JSONObject userJSON = JSON.optJSONObject("user");
                    if(userJSON != null){
                        try{
                            int userID = userJSON.getInt("userID");
                            String firstName = userJSON.getString("firstName");
                            String lastName = userJSON.getString("lastName");
                            String email = userJSON.getString("email");
                            JSONArray reviewsJSON = userJSON.optJSONArray("reviews");
                            LinkedList<ReviewModel> reviews = null;
                            if(reviewsJSON != null){
                                reviews = new LinkedList<ReviewModel>();
                                for(int i = 0; i < reviewsJSON.length(); i++){
                                    JSONObject reviewJSON = (JSONObject)reviewsJSON.get(i);
                                    int reviewID = reviewJSON.getInt("reviewID");
                                    int reviewUserID = reviewJSON.getInt("userID");
                                    int geolocationID = reviewJSON.getInt("geolocationID");
                                    int rating = reviewJSON.getInt("rating");
                                    String comment = reviewJSON.optString("comment");
                                    ReviewModel review = new ReviewModel(reviewID, reviewUserID, geolocationID, rating, comment);
                                    reviews.add(review);
                                }
                            }
                            JSONArray geolocationsJSON = userJSON.optJSONArray("geolocations");
                            LinkedList<GeolocationModel> geolocations = null;
                            if(geolocationsJSON != null){
                                geolocations = new LinkedList<GeolocationModel>();
                                for(int i = 0; i < geolocationsJSON.length(); i++){
                                    JSONObject geolocationJSON = (JSONObject)geolocationsJSON.get(i);
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
                            }
                            user = new UserModel(userID, firstName, lastName, email, reviews, geolocations);
                            valid = true;
                        }
                        catch (Exception e){
                            Log.w("Error Reports", e.getMessage());
                        }
                    }
                    else{
                        valid = false;
                    }
                }
                else{
                    valid = false;
                }
                setComplete.handle(null);
            }
        };
        if(token != null){
            requester.authorize(token);
            requester.run(setUser);
        }
        else{
            setComplete.handle(null);
        }
    }

    public void refreshAndGetUser(){
        //PRE: a token must be present for this request
        //POST: sends a refresh request and subsequently sends a getUser request
        completed = false;
        requester = new RequestMaker(context, "POST", "refreshToken", null);
        CompletionHandler setTokenThenGetUser = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                if(JSON != null){
                    token = JSON.optString("token", null);
                    if(token != null){
                        valid = true;
                    }
                    else{
                        valid = false;
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", token);
                    editor.apply();
                }
                getUser();
            }
        };
        if(token != null){
            requester.authorize(token);
            requester.run(setTokenThenGetUser);
        }
        else{
            setComplete.handle(null);
        }
    }

    public void destroyToken(){
        //PRE: if a token is present, destroys it
        //POST: destroys the token and, if successful, any internal data related to authentication
        completed = false;
        requester = new RequestMaker(context, "POST", "destroyToken", null);
        CompletionHandler destroyInternalData = new CompletionHandler() {
            @Override
            public void handle(JSONObject JSON) {
                valid = false;
                user = null;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", null);
                editor.apply();
                setComplete.handle(JSON);
            }
        };
        requester.authorize(token);
        requester.run(destroyInternalData);
    }
}
