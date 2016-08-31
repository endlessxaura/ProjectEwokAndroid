package com.example.asap.projectewok.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.asap.projectewok.API.ApiInterface;
import com.example.asap.projectewok.API.ReturnsHandler;
import com.example.asap.projectewok.Helpers.Session;
import com.example.asap.projectewok.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPointStyle;

import org.json.JSONObject;

/**
 * Created by asap on 8/8/16.
 */
public class MainMap extends AppCompatActivity implements OnMapReadyCallback {
    
    //Properties
    MapFragment mapFragment;
    GoogleMapOptions initialOptions;
    GeoJsonLayer geoJsonLayer;
    ApiInterface api;
    Toolbar toolbar;

    //Auxiliary Functions
    @Override
    public void onMapReady(final GoogleMap map) {
        //PRE: An asynchronous map request must be made
        //POST: runs this when the map is ready, getting the raw geolocations and setting them to the map
        api.onCompleteWithReturns = new ReturnsHandler() {
            @Override
            public void handle(Object returns) {
                if(returns != null){
                    try{
                        geoJsonLayer = new GeoJsonLayer(map, (JSONObject)returns);
                        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
                            public boolean onMarkerClick(Marker marker){
                                GeoJsonPointStyle style = geoJsonLayer.getDefaultPointStyle();
                                GeoJsonFeature feature = geoJsonLayer.getFeature(marker);
                                marker.setTitle(feature.getProperty("name"));
                                marker.setSnippet(feature.getProperty("description"));
                                marker.showInfoWindow();
                                return false;
                            }
                        });
                        geoJsonLayer.addLayerToMap();
                    }
                    catch (Exception e){
                        Log.w("Error Reports", e.getMessage());
                    }
                }
            }
        };
        api.getRawGeolocations();
    }

    //State functions
    @Override
    protected void onCreate(Bundle instanceData){
        //Preliminary initialization
        super.onCreate(instanceData);
        api = new ApiInterface(this.getApplicationContext());

        //Instantiating view
        setContentView(R.layout.mainmap_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Starting the map
        initialOptions = new GoogleMapOptions();
        CameraPosition position = new CameraPosition(new LatLng(44.8962, -68.6726), 15, 0, 0);
        initialOptions.mapType(GoogleMap.MAP_TYPE_HYBRID)
                .compassEnabled(true)
                .camera(position);
        mapFragment = com.google.android.gms.maps.MapFragment.newInstance(initialOptions);
        getFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);;
        api.getPicture(3, false);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Session.getSession(getApplicationContext()).killSession();
    }

    //Other functions

}
