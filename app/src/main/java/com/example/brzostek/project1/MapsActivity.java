package com.example.brzostek.project1;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.brzostek.common.model.Shop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String SHOPS_KEY = "SHOPS_KEY";
    private static final String SELECTED_INDEX_KEY = "SELECTED_INDEX_KEY";

    private List<Shop> shops;
    private int selectedIndex;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        shops = (List<Shop>) getIntent().getSerializableExtra(SHOPS_KEY);
        selectedIndex = getIntent().getIntExtra(SELECTED_INDEX_KEY, 0);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < shops.size(); i++) {
            Shop shop = shops.get(i);
            LatLng shopLatLng = new LatLng(shop.getLatitude(), shop.getLongitude());
            mMap.addMarker(new MarkerOptions().position(shopLatLng).title(shop.getName()));
            if (i == selectedIndex) {
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(shopLatLng, mMap.getMaxZoomLevel() - 2)));
            }
        }
    }

    public static Intent buildIntent(Context context, List<Shop> shops, int selectedIndex) {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(SHOPS_KEY, (Serializable) shops);
        intent.putExtra(SELECTED_INDEX_KEY, selectedIndex);
        return intent;
    }
}
