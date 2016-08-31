package com.example.asap.projectewok.Helpers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by asap on 8/17/16.
 */
public class Session {
    /**
     * Basic usage:
     * This allows you to have a shared session and request queue.
     * To use the shared session, simply call getSession with the application's context
     * Add a request using addToRequestQueue (which will immediately run it if there is
     * nothing queued). Upon stopping the application, call killSession to prevent memory leaks.
     */
    private static Session sharedInstance;          //The singleton for session sharing
    private static Context context;                 //The context for which the session is made (APP wide)
    private RequestQueue requestQueue;              //The queue for requests

    private Session(Context context){
        //PRE: Session MUST have an appwide context
        //POST: creates a session singleton
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        //POST: returns the shared request queue
        if(this.requestQueue == null){
            this.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized Session getSession(Context context){
        //PRE: the context passed into this MUST BE AN APPLICATION CONTEXT! OR IT WILL BREAK!!!
        //POST: Returns a session if there is one, or creates one if by calling the constructor on the context
        if(sharedInstance == null){
            sharedInstance = new Session(context);
        }
        return sharedInstance;
    }

    public <T> void addToRequestQueue(Request<T> request){
        //PRE: a request must be provided
        //POST: adds the request to the queue to be ran ASAP
        getRequestQueue().add(request);
    }

    public void killSession(){
        //POST: kills the session to prevent memory leaks
        if(requestQueue != null){
            requestQueue.stop();
            requestQueue = null;
        }
    }
}
