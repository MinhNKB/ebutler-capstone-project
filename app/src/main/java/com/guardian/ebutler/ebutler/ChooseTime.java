package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class ChooseTime extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        bindNavigationLocation();
    }

    private void bindNavigationLocation() {
        final Context context = this;

        Button button = (Button)findViewById(R.id.choose_time_buttonConfirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();

                TimePicker timePicker = (TimePicker)findViewById(R.id.choose_time_timePicker);
                timePicker.clearFocus();
                String result = "";
                if (Build.VERSION.SDK_INT >= 23 )
                    result += String.format("%02d", timePicker.getHour());
                else
                    result += String.format("%02d", timePicker.getCurrentHour());
                result += ":";
                if (Build.VERSION.SDK_INT >= 23 )
                    result += String.format("%02d", timePicker.getMinute());
                else
                    result += String.format("%02d", timePicker.getCurrentMinute());
                returnIntent.putExtra("time", result);

                DatePicker datePicker = (DatePicker)findViewById(R.id.choose_time_datePicker);
                datePicker.clearFocus();
                result = "";
                result += String.format("%02d", datePicker.getDayOfMonth());
                result += "-";
                result += String.format("%02d", datePicker.getMonth() + 1);
                result += "-";
                result += String.format("%04d", datePicker.getYear());
                returnIntent.putExtra("date", result);

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        button = (Button)findViewById(R.id.choose_time_buttonCancel);
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
