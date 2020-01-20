package com.example.hiking_application.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.carto.core.MapPos;
import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.RasterTileLayer;
import com.carto.projections.Projection;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;
import com.carto.ui.MapView;
import com.example.hiking_application.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartoMapsActivity extends Activity
{
    private final String TAG = CartoMapsActivity.class.getSimpleName();

    @BindView(R.id.mapView)
    MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carto_maps);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());
        MapView.registerLicense(getString(R.string.carto_licence_key), this);

        /*// Add basemap layer to mapView
        CartoOnlineVectorTileLayer baseLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER);
        mapView.getLayers().add(baseLayer);
        */

        // Add basemap layer to mapView
        /*CartoOnlineVectorTileLayer baseLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER);
        mapView.getLayers().add(baseLayer);*/

        // Add basemap layer to mapView
        TileDataSource tileDataSource = new HTTPTileDataSource(1,19,getString(R.string.open_street_map_tile_url));
        RasterTileLayer baseLayer = new RasterTileLayer(tileDataSource);
        Projection projection = mapView.getOptions().getBaseProjection();
        mapView.getLayers().add(baseLayer);
        mapView.setMapEventListener(new HikingMapEvenListener());
        mapView.zoom(10f, projection.fromLatLong(53.513853, -9.676749), 1);
    }

    private class HikingMapEvenListener extends MapEventListener
    {
        @Override
        public void onMapClicked(MapClickInfo mapClickInfo) {
            super.onMapClicked(mapClickInfo);
            Projection projection = mapView.getOptions().getBaseProjection();
            MapPos pos = projection.toLatLong(mapClickInfo.getClickPos().getX(), mapClickInfo.getClickPos().getY());
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            /*AndroidNetworking.get(getString(R.string.open_elevation_API))
                    .addPathParameter( "locations", pos.getX()+","+pos.getY())
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                Log.i(TAG, "FUCK"+response.get(0));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError)
                        {
                            Log.i(TAG, "onError: "+ anError.getErrorBody());
                        }
                    });*/


            Map<String, Double> location = new HashMap<>();
            location.put("latitude", pos.getX());
            location.put("longitude", pos.getY());
            JSONObject locationObject = new JSONObject(location);
            JSONArray locations = new JSONArray();
            locations.put(locationObject);
            locations.put(locationObject);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("locations", locations);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest fireRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.open_elevation_API), jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i(TAG, "onResponse: ");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "onErrorResponse: "+error.networkResponse+ " "+ error.getMessage());
                }
            });

            /*try {
                Log.i(TAG, "onMapClicked: "+jsonObject.toString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            Log.i(TAG, "onMapClicked: "+fireRequest.toString());

            requestQueue.add(fireRequest);
        }
    }
}
