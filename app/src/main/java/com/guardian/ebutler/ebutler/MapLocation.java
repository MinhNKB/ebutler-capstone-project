package com.guardian.ebutler.ebutler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.world.Global;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapLocation extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final String LOCATION_EXTRA = "Location Extra";
    private GoogleMap priMap;
    private Context priThis;
    private ImageButton priButtonCancel;
    private ImageButton priButtonDelete;
    private ImageButton priButtonDone;
    private TextView priLocationAddressTextView;
    private HashMap<Marker, com.guardian.ebutler.ebutler.dataclasses.Location> priMarkerLocationMap;
    private com.guardian.ebutler.ebutler.dataclasses.Location priCurrentLocation = null;
    private boolean priIsMarkerClicked = false;
    final private float MIN_ZOOM = 12;
    final public int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        priThis = this;
        findViewByIds();
        bindEvents();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void returnResult(int iResultCode, com.guardian.ebutler.ebutler.dataclasses.Location rReturnLocation) {
        Intent resultIntent = new Intent();
        if (rReturnLocation != null) {
            Global.getInstance().pubTaskLocation = rReturnLocation;
        }
        setResult(iResultCode, resultIntent);
        finish();
    }

    private void bindEvents() {
        priButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult(RESULT_CANCELED, null);
            }
        });

        priButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: delete Location here
            }
        });

        priButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("cool", priCurrentLocation.toString());
                if (priCurrentLocation != null) {
                    returnResult(RESULT_OK, priCurrentLocation);
                } else {
                    final com.guardian.ebutler.ebutler.dataclasses.Location lNewLocation = new com.guardian.ebutler.ebutler.dataclasses.Location();
                    LatLng lCenterOfMap = priMap.getCameraPosition().target;
                    lNewLocation.pubCoorX = (float) lCenterOfMap.latitude;
                    lNewLocation.pubCoorY = (float) lCenterOfMap.longitude;
                    lNewLocation.pubAndress = priLocationAddressTextView.getText().toString();
                    AlertDialog.Builder lBuilder = new AlertDialog.Builder(priThis);
                    lBuilder.setTitle("Ghi nhớ địa điểm");

                    final EditText lInput = new EditText(priThis);
                    lInput.setInputType(InputType.TYPE_CLASS_TEXT);
                    ColorDrawable lBackgroundColor = null;
                    if (Build.VERSION.SDK_INT > 23) {
                        lBackgroundColor = new ColorDrawable(getResources().getColor(R.color.transparent, getTheme()));
                    } else {
                        lBackgroundColor = new ColorDrawable(getResources().getColor(R.color.transparent));
                    }
                    lInput.setBackground(lBackgroundColor);
                    lInput.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    lInput.setHint("Tên địa điểm (ví dụ: nhà, công ti, v.v.)");
                    LinearLayout lLinearLayout = new LinearLayout(priThis);
                    int pixels = getResources().getDimensionPixelSize(R.dimen.layout_padding_double);
                    lLinearLayout.addView(lInput);
                    lInput.setPadding(pixels, pixels, pixels, pixels);
                    lBuilder.setView(lLinearLayout);

                    lBuilder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            lNewLocation.pubName = lInput.getText().toString().trim();
                            if (lNewLocation.pubName.length() < 1) {
                                lNewLocation.pubName = "Chưa đặt tên";
                            }
                            returnResult(RESULT_OK, lNewLocation);
                        }
                    });
//                    lBuilder.setNeutralButton("Bỏ qua", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            returnResult(RESULT_CANCELED, lNewLocation);
//                        }
//                    });
                    lBuilder.setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    lBuilder.show();
                }
            }
        });
    }

    private void findViewByIds() {
        priButtonCancel = (ImageButton)findViewById(R.id.map_location_buttonCancel);
        priButtonDelete = (ImageButton)findViewById(R.id.map_location_buttonDelete);
        priButtonDone = (ImageButton)findViewById(R.id.map_location_buttonDone);
        priLocationAddressTextView = (TextView) findViewById(R.id.map_location_locationAddress);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        priMap = googleMap;
        setupMapParameters();
        setupViewLocation();
        setupMarkers();
    }

    private void setupViewLocation() {
        LocationManager lLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String lLocationProvider = LocationManager.NETWORK_PROVIDER;
        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
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
                    Toast.makeText(priThis, "GPS đã bị vô hiệu hóa",Toast.LENGTH_LONG);
                }
            }, null);
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
                String lNearbyAddress = getAddress(cameraPosition.target.latitude, cameraPosition.target.longitude);
                if (lNearbyAddress.length() > 0) {
                    priLocationAddressTextView.setText(lNearbyAddress);
                    priLocationAddressTextView.setVisibility(View.VISIBLE);
                } else {
                    priLocationAddressTextView.setVisibility(View.GONE);
                }
                if (cameraPosition.zoom < MIN_ZOOM)
                    priMap.animateCamera(CameraUpdateFactory.zoomTo(MIN_ZOOM));
            }
        });
        priMap.setOnMarkerClickListener(this);
    }

    private String getAddress(Double iLatitude, Double iLongitude) {
        Geocoder lGeocoder = new Geocoder(this, Locale.ENGLISH);
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
        } catch (Exception e) {
            Log.e("tab", "Error getting address");
            e.printStackTrace();
        }
        return lReturnValue;
    }

    private void setupMarkers() {
        List<com.guardian.ebutler.ebutler.dataclasses.Location> lLocations = null;
        try
        {
            DatabaseHelper iHelper = new DatabaseHelper(this);
            lLocations = iHelper.GetAllLocations();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
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
