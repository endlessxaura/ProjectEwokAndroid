package com.example.asap.projectewok.Helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by asap on 8/18/16.
 */
public class Requester {
    /*
     This class is used by anything that directly makes requests.
     It has a requester and a completed boolean to make requests and tell when they are completed.
     It has a built in completion handler called setComplete which should be used as part of
     every request made. probably within a custom completion handler. This will set completed to
     true and call the onComplete method of onComplete.
     */
    //Properties
    public RequestMaker requester;              //A request maker that runs requests
    public boolean completed;                   //True if done, false if not done
    public CompletionHandler onComplete;        //A variable completion handler for the user
    public Context context;                     //The application's context
    public CompletionHandler setComplete = new CompletionHandler() {
        //Run this after each request
        @Override
        public void handle(JSONObject JSON) {
            //PRE: A JSON from a request
            //POST: sets completed to true and passes the JSON to onComplete, if it exists
            if(onComplete != null){
                onComplete.handle(JSON);
            }
            completed = true;
        }
    };
}
