package com.example.asap.projectewok.Helpers;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * Created by asap on 8/18/16.
 */
public class ListenWithCompletion implements Response.Listener<JSONObject> {

    /**
     * Basic usage:
     * Use this when you need a listener who has a completion handler.
     * However, it does not require a completion handler
     * If you override onResponse, be sure to include
     * if(completion != null){
     *     completion.onComplete(JSON);
     * }
     * somewhere in the code, preferably at the end
     */

    public CompletionHandler completion;        //A completion handler for variable completion handling

    public void setCompletion(CompletionHandler completion){
        //PRE: A completion handler
        //POST: sets the completion handler to the completion
        this.completion = completion;
    }

    public void onResponse(JSONObject arg){
        //PRE: A request that responds with a JSONObject argument
        //POST: Runs the completion handler, if there is one
        if(completion != null){
            completion.handle(arg);
        }
    }
}
