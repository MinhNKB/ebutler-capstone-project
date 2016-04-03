package com.guardian.ebutler.fragments.tasks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.guardian.ebutler.ebutler.MapLocation;
import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.TaskDetail;
import com.guardian.ebutler.ebutler.dataclasses.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmFragment extends AbstractTaskFragment {
    private EditText priEditTextDate;
    private EditText priEditTextTime;
    private EditText priEditTextLocation;

    public AlarmFragment() {
        proFragmentId = R.layout.fragment_alarm;
    }

    public static AlarmFragment newInstance() {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public void getValues(Task rNewTask) {
        rNewTask.pubTime = getTimeFromDateTextbox();
    }

    public void setValuesToView(View view) {
        findViewsById();
        bindEvents();
    }

    private void findViewsById() {
        priEditTextDate = (EditText)proView.findViewById(R.id.fragment_alarm_editTextDate);
        priEditTextTime = (EditText)proView.findViewById(R.id.fragment_alarm_editTextTime);
        priEditTextLocation = (EditText)proView.findViewById(R.id.fragment_alarm_editTextLocation);
    }

    private void bindEvents() {
        final Calendar lCalendar = Calendar.getInstance();

        final TimePickerDialog.OnTimeSetListener lTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                priEditTextTime.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                lCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                lCalendar.set(Calendar.MINUTE, minute);
            }
        };
        final DatePickerDialog.OnDateSetListener lDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                priEditTextDate.setText(Integer.toString(dayOfMonth) + "/" + Integer.toString(monthOfYear + 1) + "/" + Integer.toString(year));
                lCalendar.set(Calendar.YEAR, year);
                lCalendar.set(Calendar.MONTH, monthOfYear);
                lCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };
        priEditTextTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.w("mytab", "timer");
                    new TimePickerDialog(proView.getContext(), lTimePickerListener, lCalendar.get(Calendar.HOUR_OF_DAY), lCalendar.get(Calendar.MINUTE), false).show();
                }
            }
        });

        priEditTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.w("mytab", "dater");
                    new DatePickerDialog(proView.getContext(), lDatePickerListener, lCalendar.get(Calendar.YEAR), lCalendar.get(Calendar.MONTH), lCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        priEditTextLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new Intent(proView.getContext(), MapLocation.class);
                    startActivityForResult(intent, TaskDetail.GET_LOCATION);
                }
            }
        });
    }

    private Date getTimeFromDateTextbox() {
        SimpleDateFormat lDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date lDate = null;
        try {
            lDate = lDateFormat.parse(priEditTextDate.getText() + " " + priEditTextTime.getText());
        } catch (ParseException e) {
            lDate = null;
        } finally {
            if (lDate == null) {
                lDate = new Date();
            }
        }
        return lDate;
    }

    public void setLocation(String iLocation) {
    }
}
