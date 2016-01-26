package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.guardian.ebutler.screenhelper.FullscreenHelper;
import com.guardian.ebutler.world.Global;

import java.util.HashMap;

public class ChooseLocation extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullscreenHelper.setFullScreen(this);
        setContentView(R.layout.activity_choose_location);
        bindNavigateLocation();
        bindNavigateDetail();
    }

    private void bindNavigateDetail() {
        Button button = (Button)findViewById(R.id.detail1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateDetail(v);
            }
        });

        button = (Button)findViewById(R.id.detail2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateDetail(v);
            }
        });

        button = (Button)findViewById(R.id.detail3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateDetail(v);
            }
        });
    }

    static final int EDIT_LOCATION = 2;
    public void navigateDetail(View v) {
        Button button = (Button)findViewById(this.getResources().getIdentifier((v.getTag().toString()), "id", this.getPackageName()));
        Intent intent = new Intent(this, LocationDetail.class);
        intent.putExtra("location", button.getText());
        startActivityForResult(intent, EDIT_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_LOCATION) {
            if (resultCode == RESULT_OK) {
                //TODO: update location info
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("location", ((Button) v).getText());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void bindNavigateLocation() {
        Button button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(this);

        button = (Button)findViewById(R.id.button2);
        button.setOnClickListener(this);

        button = (Button)findViewById(R.id.button3);
        button.setOnClickListener(this);
    }
}
