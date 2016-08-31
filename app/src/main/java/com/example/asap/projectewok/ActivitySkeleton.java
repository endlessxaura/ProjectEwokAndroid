package com.example.asap.projectewok;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

//This is not an actual activity; rather, it is meant to be a description of an activity with examples
//DO NOT ACTUALLY USE THIS; IT WILL CRASH!
public class ActivitySkeleton extends AppCompatActivity {
    //Lifecycle guide
    //Opening the app: onCreate->onStart->onResume
    //Navigating out of the app: onPause->onStop
    //Navigating back into the app: onRestart->onStart->onResume

    //Counter-part methods that should roughly mirror each other
    //onStart/onRestart and onStop
    //onPause and onResume
    //onCreate and onDestroy

    /**These are transient states, in which the activity merely passes through**/
    @Override //YOU NEED TO OVERRIDE BECAUSE IT INHERETS
    //This function creates that activity when it is first opened
    //The system calls this on EVERY ACTIVITY when the application starts
    //If this is the main activity, this is the main point of entry for the application
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     //You should always call the super method

        //This declares the user interface (should always be done)
//        setContentView(R.layout.MYACTIVITY);

        //Other things you can do; Check api level, check resolution, instantiate variables, etc.

        //You may also restore data using a bundle, created in onSavedInstanceState
        //or you could save that for onRestoreInstanceState
        //If you do, check if the bundle is null
    }

    //The startup of the activity
    //This is similar to onCreate, but waits to do other setup that relies on the onCreate method
    //You want to check for preferences here because of restarts and preference changes
    protected void onStart(){
        super.onStart();
    }

    //This is when the app is completed close out of
    //NOTE: many apps don't even use this because a lot of cleanup should be done in onPause and onStop
    //Be aware that if you are using this for ALL of your cleanup, you're doing it wrong
    //The only exception is when you call the finish() method (which you really shouldn't often)
    //Also note: changing orientation does recreate the activity
    protected void onDestroy(){
        super.onDestroy();
    }

    //This is when the app is rebooted from the stopped state
    //This restarts resources closed by onStop
    //Because you may not need onStop, you may not need this
    protected void onRestart(){
        super.onRestart();
    }

    /**These are static states, that activity can remain in for long periods of time**/
    //This function is when the app is running and visible
    //You should resume stuff that was paused with onPause, but that's about it
    //In fact, much of that work can be done in onStart (because you'll need to do it
    //  when you create the app as well), so you may not even need this
    protected void onResume(){
        super.onResume();
    }

    //This is when the activity is partially obscured, but not entirely
    //Note that this can happen within your own application
    //Example usage is pausing a video, releasing resources, or stopping sound
    //You can also use this to save things permanently when your sure the user would want that
    //But, keep this as least CPU-intensive as possible (save it for onStop)
    //NOTE: This keeps instantiated variables and components
    protected void onPause(){
        super.onPause();
    }

    //This is when the activity is entirely hidden
    //You want to clear any resources and variables being used here
    //This can be as CPU-intensive as need be
    //NOTE: As with onPause, this keeps the activity in memory, so you may not need this
    //Also note: sometimes, the system might destroy your activity, even without calling onDestroy
    //in other words, stop all system resources that could cause a memory leak
    //Stop network connections here, btw
    protected void onStop() {
        super.onStop();
    }

    /**These methods are not state methods, but relate to them**/
    //This is when the application is destroyed and resumed, saving relevant data
    //The saved data is stored in a bundle, which is a bunch of key-value pairs
    //NOTE: you normally don't need this because a lot of this is taken care of by the system
    protected void onSaveInstanceState(Bundle savedInstanceData){
        super.onSaveInstanceState(savedInstanceData);   //Super must be called with the same bundle
    }

    //This method is called after onStart when a bundle is present
    //Unlike onCreate, you do not need to check if the bundle is null
    protected void onRestoreInstanceState(Bundle savedInstanceData){
        super.onRestoreInstanceState(savedInstanceData);
    }
}
