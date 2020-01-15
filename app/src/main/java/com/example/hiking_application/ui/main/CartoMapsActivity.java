package com.example.hiking_application.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.carto.datasources.HTTPTileDataSource;
import com.carto.datasources.TileDataSource;
import com.carto.layers.CartoBaseMapStyle;
import com.carto.layers.CartoOnlineVectorTileLayer;
import com.carto.layers.RasterTileLayer;
import com.carto.ui.MapClickInfo;
import com.carto.ui.MapEventListener;
import com.carto.ui.MapView;
import com.example.hiking_application.R;

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
        MapView.registerLicense(getString(R.string.carto_licence_key), this);

        /*// Add basemap layer to mapView
        CartoOnlineVectorTileLayer baseLayer = new CartoOnlineVectorTileLayer(CartoBaseMapStyle.CARTO_BASEMAP_STYLE_VOYAGER);
        mapView.getLayers().add(baseLayer);
        */

        // Add basemap layer to mapView
        TileDataSource tileDataSource = new HTTPTileDataSource(1,19,getString(R.string.open_street_map_tile_url));
        RasterTileLayer baseLayer = new RasterTileLayer(tileDataSource);
        mapView.getLayers().add(baseLayer);
        mapView.setMapEventListener(new HikingMapEvenListener());
    }

    private class HikingMapEvenListener extends MapEventListener
    {
        @Override
        public void onMapClicked(MapClickInfo mapClickInfo) {
            super.onMapClicked(mapClickInfo);
            Log.i(TAG, "onMapClicked: "+mapClickInfo.getClickPos().getZ());
        }
    }
}
