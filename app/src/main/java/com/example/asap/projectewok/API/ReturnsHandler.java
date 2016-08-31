package com.example.asap.projectewok.API;

import org.json.JSONObject;

/**
 * Created by asap on 8/18/16.
 */
public interface ReturnsHandler {
    /**
     * Basic usage:
     * Create this and pass it into the request maker to run onComplete once the request finishes
     * NOTE: Handle should always check to see if the JSON is not null
     */

    public void handle(Object returns);
    //PRE: A JSON
    //POST: Performs some task for the user upon request completion

}
