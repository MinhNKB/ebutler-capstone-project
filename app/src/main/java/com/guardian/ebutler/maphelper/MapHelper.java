package com.guardian.ebutler.maphelper;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tabuzaki IA on 4/6/2016.
 */
public class MapHelper {
    static public class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... uri) {
            String strFileContents = "";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(uri[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                byte[] contents = new byte[1024];

                int bytesRead = 0;
                while( (bytesRead = in.read(contents)) != -1){
                    strFileContents = strFileContents + new String(contents, 0, bytesRead);
                }
                Log.w("cool", strFileContents);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return strFileContents;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
        }
    }
}
