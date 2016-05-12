package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.guardian.ebutler.screenhelper.FullscreenHelper;

public class LocationDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullscreenHelper.setFullScreen(this);
        setContentView(R.layout.activity_location_detail);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        TextView locationName = (TextView)findViewById(R.id.location_detail_locationName);
        locationName.setText(location);
    }
}
