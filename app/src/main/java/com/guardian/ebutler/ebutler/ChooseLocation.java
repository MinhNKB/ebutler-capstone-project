package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.guardian.ebutler.screenhelper.FullscreenHelper;

import java.util.Calendar;
import java.util.HashMap;

public class ChooseLocation extends Activity implements View.OnTouchListener, CompoundButton.OnCheckedChangeListener {
    private HashMap<Integer, String> priSelectedLocationIds = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullscreenHelper.setFullScreen(this);
        setContentView(R.layout.activity_choose_location);
        bindEvents();
        bindNavigateLocation();
    }

    private void bindEvents() {
        ImageButton button = (ImageButton) findViewById(R.id.choose_location_DoneButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                //TODO: something to put here
                String result = TextUtils.join(", ", priSelectedLocationIds.values());
                returnIntent.putExtra("location", result);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    static final int EDIT_LOCATION = 2;
    public void navigateDetail(View v) {
        // TODO: don't remove the commented code here, this is to find a View by a String.
        //  Button button = (Button)findViewById(this.getResources().getIdentifier((v.getTag().toString()), "id", this.getPackageName()));
        ToggleButton button = (ToggleButton) v;
        Intent intent = new Intent(this, LocationDetail.class);
        intent.putExtra("location", button.getTextOn());
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            priSelectedLocationIds.put(Integer.parseInt((String) buttonView.getTag()), ((ToggleButton) buttonView).getTextOn().toString());
        } else {
            priSelectedLocationIds.remove(Integer.parseInt((String) buttonView.getTag()));
        }
    }

    private void bindNavigateLocation() {
        ToggleButton button = (ToggleButton)findViewById(R.id.button1);
        button.setOnTouchListener(this);
        button.setOnCheckedChangeListener(this);

        button = (ToggleButton)findViewById(R.id.button2);
        button.setOnTouchListener(this);
        button.setOnCheckedChangeListener(this);

        button = (ToggleButton)findViewById(R.id.button3);
        button.setOnTouchListener(this);
        button.setOnCheckedChangeListener(this);
    }

    private static final int MIN_CLICK_DURATION = 2000;
    private Boolean priLongClickActive = false;
    private long priStartClickTime = Calendar.getInstance().getTimeInMillis();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final View lTouched = v;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                priLongClickActive = true;
                priStartClickTime = Calendar.getInstance().getTimeInMillis();
                break;
            case MotionEvent.ACTION_UP:
                long clickDuration = Calendar.getInstance().getTimeInMillis() - priStartClickTime;
                if (priLongClickActive && clickDuration >= MIN_CLICK_DURATION) {
                    priLongClickActive = false;
                    navigateDetail(lTouched);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }
}
