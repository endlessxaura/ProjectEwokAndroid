package com.example.asap.projectewok.Helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by asap on 8/17/16.
 */
public class RequestMaker {
    /**
     * Basic usage:
     * Use the constuctors to construct a request based on your needs.
     * Note that the context MUST be an application context
     * use the run function to run the request
     * You may, optionally, provide a completion handler with the run request
     * to have it use the handle function of the completion handler.
     */
    //Properties
    public String requestURL;               //The request URL
    public ExtendedRequest request;         //The request itself
    public String rawData;                  //The string for the JSON
    public JSONObject decodedJSON;          //The JSON object from the request
    public boolean ready;                   //A boolean for whether or not the request is ready for external computation
    public int status;                      //The status code for the request
    public String error;                    //Any errors reported by the API
    private ListenWithCompletion decodeJSON = new ListenWithCompletion() {
        //A reponse listener that performs all needed functions and runs the completion handler
        @Override
        public void onResponse(JSONObject JSON) {
            //PRE: A JSON passed to it from a request
            //POST: sets rawData to the string JSON, decodedJSON to the JSON,
            //      status to 200, error to null, and ready to true.
            //      Passes the JSON to the completion handler.
            rawData = JSON.toString();
            decodedJSON = JSON;
            status = 200;
            error = null;
            ready = true;
            if (completion != null) {
                completion.handle(JSON);
            }
        }
    };
    private Response.ErrorListener errorReporter = new Response.ErrorListener() {
        //An error listener that reports errors and passed any data to the completion handler
        @Override
        public void onErrorResponse(VolleyError errorResponse){
            String rawJSON = null;

            NetworkResponse response = errorResponse.networkResponse;
            if(response != null && response.data != null) {
                status = response.statusCode;
                Log.w("Error Reports", "Response status: " + status);
                rawJSON = new String(response.data);
                decodedJSON = getJSONfromString(rawJSON);
                if (decodedJSON != null) {
                    try {
                        error = decodedJSON.getString("error");
                    } catch (Exception e) {
                        Log.w("Error Reports", e.getMessage());
                    }
                }
            }
            if(decodeJSON.completion != null){
                decodeJSON.completion.handle(decodedJSON);
            }
        }
    };
    private Session session;                //The session for which requests are passed through
    private String baseURL = "http://chitna.asap.um.maine.edu/projectcrowdsource/public/api/";
                                            //The base URL to contact the API

    //Constructors
    public RequestMaker(Context application, String method, String url, String data){
        //PRE: the context MUST be the application context. A method, url, and data string must be provided.
        //      The data string must follow URL encoding formats, even for post requests
        //POST: creates the request with all the requisite data
        requestURL = baseURL + url;
        session = Session.getSession(application);
        if(method == "GET"){
            if(data != null){
                requestURL = requestURL + "?" + data;
            }
            request = new ExtendedRequest(Request.Method.GET, requestURL, null, decodeJSON, errorReporter) {
            };
        }
        else if(method == "POST"){
            JSONObject postData = null;
            if(data != null){
                postData = encodeDataString(data);
            }
            request = new ExtendedRequest(Request.Method.POST, requestURL, postData, decodeJSON, errorReporter);
        }
        else if(method == "PUT"){
            JSONObject postData = null;
            if(data != null){
                postData = encodeDataString(data);
            }
            request = new ExtendedRequest(Request.Method.PUT, requestURL, postData, decodeJSON, errorReporter);
        }
        else if(method == "DELETE"){
            JSONObject postData = null;
            if(data != null){
                postData = encodeDataString(data);
            }
            request = new ExtendedRequest(Request.Method.DELETE, requestURL, postData, decodeJSON, errorReporter);
        }
    }

    public RequestMaker(Context application, String url, String data){
        //PRE: the context MUST be the application context. A url and data string must be provided.
        //      The data string must follow URL encoding formats, even for post requests
        //POST: creates the request with all the requisite data to a GET route
        requestURL = baseURL + url;
        session = Session.getSession(application);
        requestURL = requestURL + "?" + data;
        request = new ExtendedRequest(Request.Method.GET, requestURL, null, decodeJSON, errorReporter);
    }

    public RequestMaker(Context application, String url){
        //PRE: the context MUST be the application context. A url must be provided.
        //POST: creates the request to a GET route with no data
        requestURL = baseURL + url;
        session = Session.getSession(application);
        request = new ExtendedRequest(Request.Method.GET, requestURL, null, decodeJSON, errorReporter);
    }

    //Functions
    private JSONObject encodeDataString(String data){
        //PRE: A data string in URL encoding ("email=ogeaingao&password=geoaingaeon", etc.)
        //POST: encodes the data string in a JSON object to send to a POST or PUT route and returns it
        JSONObject JSON = new JSONObject();
        String[] parsedStrings = data.split("&");
        for(int i = 0; i<parsedStrings.length; i++){
            String[] keyValuePair = parsedStrings[i].split("=");
            try{
                JSON.put(keyValuePair[0], keyValuePair[1]);
            }
            catch (Exception e){
                Log.w("Error reports", e.getMessage());
            }
        }
        return JSON;
    }

    public JSONObject getJSONfromString(String json){
        //PRE: A string that represents a JSON
        //POST: returns a JSON object for the string
        try{
            JSONObject obj = new JSONObject(json);
            return obj;
        } catch(JSONException e){
            e.printStackTrace();
            Log.w("Error Reports", e.getMessage());
            return null;
        }
    }

    public void authorize(String token){
        request.headers.put("Authorization", "Bearer : \"" + token + "\"");
    }

    /////RUNNING REQUEST/////
    public void run(CompletionHandler completion){
        //PRE: a completion handler must be provided
        //POST: runs the request and runs the completion handler upon completion
        ready = false;
        error = null;
        rawData = null;
        decodedJSON = null;
        decodeJSON.setCompletion(completion);
        session.addToRequestQueue(request);
    }

    public void run(){
        //POST: runs the request
        ready = false;
        error = null;
        rawData = null;
        decodedJSON = null;
        session.addToRequestQueue(request);
    }
}