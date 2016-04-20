package com.guardian.ebutler.maphelper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.guardian.ebutler.ebutler.MapAPICallbackActivityInterface;
import com.guardian.ebutler.ebutler.MapLocation;
import com.guardian.ebutler.ebutler.dataclasses.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Tabuzaki IA on 4/6/2016.
 */
public class MapHelper {

    private static MapHelper priInstance;

    public static synchronized MapHelper getInstance(Context context) {
        if (priInstance == null) {
            priInstance = new MapHelper(context.getApplicationContext());
        }
        return priInstance;
    }

    private Context priContext;
    public MapHelper(Context iContext) {
        priContext = iContext;
    }

    public List<Location> nearbyATMs = null;
    public void getNearbyATM(MapAPICallbackActivityInterface rMapAPIActivity) {
        getCurrentLocation(priContext, rMapAPIActivity);
    }

    private void getCurrentLocation(Context rContext, final MapAPICallbackActivityInterface rMapAPIActivity) {
        final Context lContext = rContext;
        LocationManager lLocationManager = (LocationManager) lContext.getSystemService(Context.LOCATION_SERVICE);
        String lLocationProvider = LocationManager.NETWORK_PROVIDER;
        if ( ContextCompat.checkSelfPermission(lContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions((Activity) lContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MapLocation.PERMISSION_REQUEST_CODE);
        }
        if(ContextCompat.checkSelfPermission(lContext, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            lLocationManager.requestSingleUpdate(lLocationProvider, new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    LatLng lCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    nearbyATMs = getATMNear(lCoordinates);
                    rMapAPIActivity.onMapAPILoaded();
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
                    Toast.makeText((Activity) lContext, "GPS đã bị vô hiệu hóa", Toast.LENGTH_LONG).show();
                }
            }, null);
        }
    }

    public List<Location> getATMNear(LatLng iLatLng) {
        List<Location> lReturnValue = null;
        try {
            String lResult = (new MapHelper.RequestTask().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBQ00RE-Ujwm0Rw8F3bKuJ33yUF9IHT1LI&location=" + iLatLng.latitude + "," + iLatLng.longitude + "&radius=500&keyword=atm")).get();
            JSONObject lJsonObject = new JSONObject(lResult);
            lReturnValue = parseGoogleLocation(lJsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            lReturnValue = new ArrayList<Location>();
        }
        return lReturnValue;
    }

    public List<Location> parseGoogleLocation(JSONObject rJsonObject) throws JSONException{
        List<Location> lReturnValue = new ArrayList<>();
        JSONArray lResults = rJsonObject.getJSONArray("results");
        for (int i = 0; i < lResults.length(); ++i) {
            JSONObject lLocationObject = lResults.getJSONObject(i);
            Location lLocation = new Location();
            lLocation.pubName = lLocationObject.getString("name");
            lLocation.pubAndress = lLocationObject.getString("vicinity");
            lLocation.pubCoorX = (float) lLocationObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            lLocation.pubCoorY = (float) lLocationObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            lReturnValue.add(lLocation);
        }
        return lReturnValue;
    }

    protected class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... uri) {
            StringBuilder total = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(uri[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return total.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }
    }
}
