package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.guardian.ebutler.timehelper.DateTimeHelper;

public class ChooseTime extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        bindNavigationLocation();
    }

    private void bindNavigationLocation() {
        final Context context = this;

        ImageButton button = (ImageButton)findViewById(R.id.choose_time_buttonConfirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();

                TimePicker timePicker = (TimePicker)findViewById(R.id.choose_time_timePicker);
                timePicker.clearFocus();
                String result = DateTimeHelper.getTimeFromTimePicker(timePicker);
                returnIntent.putExtra("time", result);

                DatePicker datePicker = (DatePicker)findViewById(R.id.choose_time_datePicker);
                datePicker.clearFocus();
                result = DateTimeHelper.getDateFromDatePicker(datePicker);
                returnIntent.putExtra("date", result);

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        button = (ImageButton)findViewById(R.id.choose_time_buttonCancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }
}
