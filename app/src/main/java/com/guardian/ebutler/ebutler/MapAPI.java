package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guardian.ebutler.maphelper.MapHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapAPI extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, MapAPICallbackActivityInterface {
    private GoogleMap priMap;
    private MapAPI priThis;
    private ImageButton priButtonCancel;
    private TextView priLocationAddressTextView;
    private HashMap<Marker, com.guardian.ebutler.ebutler.dataclasses.Location> priMarkerLocationMap;
    private com.guardian.ebutler.ebutler.dataclasses.Location priCurrentLocation = null;
    private boolean priIsMarkerClicked = false;
    final private float MIN_ZOOM = (float) 13.75;
    final public static int PERMISSION_REQUEST_CODE = 1;
    final static public int NULL_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_api);
        priThis = this;
        findViewByIds();
        bindEvents();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.api_map);
        mapFragment.getMapAsync(this);
    }

    private void navigateBack() {
        Intent lIntent = new Intent();
        setResult(Activity.RESULT_OK, lIntent);
        finish();
    }

    private void bindEvents() {
        priButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBack();
            }
        });
    }

    private void findViewByIds() {
        priButtonCancel = (ImageButton)findViewById(R.id.map_api_buttonCancel);
        priLocationAddressTextView = (TextView) findViewById(R.id.map_api_locationAddress);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        priMap = googleMap;
        setupMapParameters();
        setupViewLocation();
        setupMarkers();
    }

    private void setupViewLocation() {
        final LocationManager lLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        final String lLocationProvider = LocationManager.NETWORK_PROVIDER;
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            new Thread(){
                @Override
                public void run() throws SecurityException {
                    lLocationManager.requestSingleUpdate(lLocationProvider, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            LatLng lCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                            priMap.moveCamera(CameraUpdateFactory.newLatLng(lCoordinates));
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            Log.w("tab", Integer.toString(status));
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Log.w("tab", provider);
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Toast.makeText(priThis, "GPS đã bị vô hiệu hóa",Toast.LENGTH_LONG).show();
                        }
                    }, null);
                }
            }.run();
        }
    }

    private void setupMapParameters() {
        priMap.animateCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM + 2));
        priMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                //Because onCameraChange is called latter than onMarkerClicked;
                if (priIsMarkerClicked) {
                    priIsMarkerClicked = false;
                } else {
                    priCurrentLocation = null;
                }

                setupAddressCallback(cameraPosition.target.latitude, cameraPosition.target.longitude);

                if (cameraPosition.zoom < MIN_ZOOM)
                    priMap.animateCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM));
            }
        });
        priMap.setOnMarkerClickListener(this);
    }

    private void setupAddressCallback(final Double iLatitude, final Double iLongitude) {
        final Context priThis = this;
        new Thread(){
            @Override
            public void run() {
                Geocoder lGeocoder = new Geocoder(priThis, Locale.ENGLISH);
                String lReturnValue = "";
                List<Address> lAddresses = null;
                try {
                    lAddresses = lGeocoder.getFromLocation(iLatitude, iLongitude, 1);
                    if (lAddresses.size() > 0) {
                        Address lReturnedAddress = lAddresses.get(0);
                        for (int i = 0; i < lReturnedAddress.getMaxAddressLineIndex(); i++) {
                            lReturnValue += lReturnedAddress.getAddressLine(i);
                            if (i + 1 < lReturnedAddress.getMaxAddressLineIndex()) {
                                lReturnValue += ", ";
                            }
                        }
                    }
                    if (lReturnValue.length() > 0) {
                        priLocationAddressTextView.setText(lReturnValue);
                        priLocationAddressTextView.setVisibility(View.VISIBLE);
                    } else {
                        priLocationAddressTextView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Log.e("tab", "Error getting address");
                    e.printStackTrace();
                }
            }
        }.run();
    }

    private void setupMarkers() {
        MapHelper.getInstance(priThis).getNearbyATM(priThis);
    }

    public synchronized void onMapAPILoaded() {
        List<com.guardian.ebutler.ebutler.dataclasses.Location> lLocations = MapHelper.getInstance(priThis).nearbyATMs;
        priMarkerLocationMap = new HashMap<>();
        for (com.guardian.ebutler.ebutler.dataclasses.Location lLocation : lLocations) {
            LatLng lLatLng = new LatLng(lLocation.pubCoorX, lLocation.pubCoorY);
            priMarkerLocationMap.put(priMap.addMarker(new MarkerOptions().position(lLatLng).title(lLocation.pubName)), lLocation);
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        priCurrentLocation = priMarkerLocationMap.get(marker);
        priIsMarkerClicked = true;
        return false;
    }
}
