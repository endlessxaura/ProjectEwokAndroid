package com.example.asap.projectewok.Helpers;

import org.json.JSONObject;

/**
 * Created by asap on 8/18/16.
 */
public interface CompletionHandler {
    /**
     * Basic usage:
     * Create this and pass it into the request maker to run onComplete once the request finishes
     * NOTE: Handle should always check to see if the JSON is not null
     */

    public void handle(JSONObject JSON);
    //PRE: A JSON
    //POST: Performs some task for the user upon request completion

}
