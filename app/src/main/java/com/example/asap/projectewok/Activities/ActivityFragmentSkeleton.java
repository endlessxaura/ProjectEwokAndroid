package com.example.asap.projectewok.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//Notice that it extends the fragment class
//To use this fragment, call the fragment in XML using the following format
/*
    <fragment android:name="location in directory"
               android:id="myID"
               android:layout_width="whatever"
               android:layout_height="whatever" />
 */
/*
    or, use code to insert and remove it dynamically
    using a FragmentManager and FragmentTransactions
    To call a FragmentManager, use getSupportFragmentManager()
    Example:
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            HeadlinesFragment firstFragment = new HeadlinesFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

        Replacing:
        // Create fragment and give it an argument specifying the article it should show
        ArticleFragment newFragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt(ArticleFragment.ARG_POSITION, position);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
 */
public class ActivityFragmentSkeleton extends Fragment {

    //Notice that it uses lifecycle callbacks, just like an activity, but they a public
//    @Override
        //Note that this must contain an inflater, a container, and a bundle
        //and that the container MUST be defined in the activity layout
        /*
            <FrameLayout xmlns:android="fileName"
                ... />
         */
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment (the layout is how it appears)
//        //return inflater.inflate(R.layout.MYACTIVITY, container, false);
//    }

    /*
        The following indicates how to send a message
        Basically, the fragment can use the callback to call the method as such
        callback.myFunction(); inside an event like onListItemClick
        if the activity has an implementation of the callback, the activity will follow it
        for example:
        public static class MainActivity extends Activity
            implements ActivityFragmentSkeleton.Listener{
                ...
            }

            public void onEvent(int position) {
                // The user did something on the fragment which is myFunction
                // Do something here to respond to the event
            }
        }

        As for the activity, it can simply capture the fragment with findFragmentById and
        call its methods directly
     */
    public interface Listener{
        //myFunction
    }

    Listener callback;

    //This function is called when it is attached to an activity
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        //You should try your methods here to ensure it is connected correctly
        try{
            callback = (Listener) context;
        }
        catch (ClassCastException e){
            throw e;
        }
    }

}
