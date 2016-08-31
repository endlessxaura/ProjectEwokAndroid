package com.example.asap.projectewok.Helpers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asap on 8/18/16.
 */
public class ExtendedRequest extends JsonObjectRequest {

    public Map<String, String> headers = new HashMap();

    public ExtendedRequest(int method, String url, JSONObject jsonRequest, Response.Listener listener, Response.ErrorListener errorListener){
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders(){
        return headers;
    }
}