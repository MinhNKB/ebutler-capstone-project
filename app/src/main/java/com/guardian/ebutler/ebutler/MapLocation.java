package com.guardian.ebutler.ebutler;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapLocation extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap priMap;
    final private float MIN_ZOOM = 12;
    final public int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        priMap = googleMap;
        setupMapParameters();
        setupViewLocation();
        setupMarkers();
        navigateToCurrentLocation();
        LatLng sydney = new LatLng(-34, 151);
        priMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        priMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void setupViewLocation() {
        LocationManager lLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String lLocationProvider = LocationManager.NETWORK_PROVIDER;
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            android.location.Location lLastKnownLocation = lLocationManager.getLastKnownLocation(lLocationProvider);
            LatLng lLastKnownCoordinates = new LatLng(lLastKnownLocation.getLatitude(), lLastKnownLocation.getLongitude());
            priMap.moveCamera(CameraUpdateFactory.newLatLng(lLastKnownCoordinates));
        }
    }

    private void setupMapParameters() {
        priMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.i("tab", "centerLat: " + Double.toString(cameraPosition.target.latitude));
                Log.i("tab", "centerLong: " + Double.toString(cameraPosition.target.longitude));
                if (cameraPosition.zoom < MIN_ZOOM)
                    priMap.animateCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM));
            }
        });
    }

    private void navigateToCurrentLocation() {
        //TODO: get GPS info and move there;
    }

    private void setupMarkers() {
        //TODO: get markers of locations from db and load to map;

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

//        if (marker.equals())
//        {
//            //handle click here
//        }
        return true;
    }
}
