package com.guardian.ebutler.fragments.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.guardian.ebutler.ebutler.MapLocation;
import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.custom.CustomListAdapter;
import com.guardian.ebutler.ebutler.custom.CustomListItem;
import com.guardian.ebutler.ebutler.TaskDetail;
import com.guardian.ebutler.ebutler.dataclasses.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmFragment extends AbstractTaskFragment {
    private CustomListAdapter priCustomListAdapter;
    private ListView priCustomListView;
    private List<CustomListItem> priCustomListItems;
    private Calendar priCalendar;
    private TimePickerDialog.OnTimeSetListener priTimePickerListener;
    private DatePickerDialog.OnDateSetListener priDatePickerListener;

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
        bindEvents();
        findViewsByIds();
        this.initCustomListView();
    }

    private void initCustomListView() {
        this.priCustomListItems = this.getCustomItems();
        this.addTasksListView();
        this.initCustomListViewListeners();
    }

    private void initCustomListViewListeners() {
        this.priCustomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (priCustomListItems.get(position).pubSecondLine) {
                    case "Thời gian":
                        new TimePickerDialog(proView.getContext(), priTimePickerListener, priCalendar.get(Calendar.HOUR_OF_DAY), priCalendar.get(Calendar.MINUTE), false).show();
                        break;
                    case "Ngày tháng":
                        new DatePickerDialog(proView.getContext(), priDatePickerListener, priCalendar.get(Calendar.YEAR), priCalendar.get(Calendar.MONTH), priCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        break;
                    case "Lặp lại":
                        createRepeatDialog();
                        break;
                    case "Địa điểm":
                        navigateToLocationPage();
                        break;
                }
            }
        });
    }

    private void createRepeatDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle("Lặp lại");
        final String[] types = {"Mỗi ngày", "Mỗi tuần", "Mỗi tháng", "Mỗi năm", "Không lặp lại"};
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                priCustomListItems.get(2).pubThirdLine = types[which];
                addTasksListView();
            }

        });

        b.show();
    }

    public void addTasksListView(){
        this.priCustomListAdapter = new CustomListAdapter(getActivity(), this.priCustomListItems);
        this.priCustomListView.setAdapter(this.priCustomListAdapter);
    }

    private List<CustomListItem> getCustomItems() {
        List<CustomListItem> lResult = new ArrayList<CustomListItem>();
        Date lDate = new Date();
        lResult.add(new CustomListItem(null, null, "Thời gian", "08:00", -1));
        lResult.add(new CustomListItem(null, null, "Ngày tháng", (lDate.getDate() + 1) + "/" + (lDate.getMonth() + 1) + "/" + (lDate.getYear() + 1900), -1));
        lResult.add(new CustomListItem(null, null, "Lặp lại", "Không lặp lại", -1));
        lResult.add(new CustomListItem(null, null, "Địa điểm", "Không có địa điểm", -1));
        return lResult;
    }

    private void findViewsByIds() {
        this.priCustomListView = (ListView)  proView.findViewById(R.id.fragment_alarm_listView);
    }


    private void bindEvents() {
        priCalendar = Calendar.getInstance();
        priTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                priCustomListItems.get(0).pubThirdLine = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
                addTasksListView();
                priCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                priCalendar.set(Calendar.MINUTE, minute);
            }
        };
        priDatePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                priCustomListItems.get(1).pubThirdLine = Integer.toString(dayOfMonth) + "/" + Integer.toString(monthOfYear + 1) + "/" + Integer.toString(year);
                addTasksListView();
                priCalendar.set(Calendar.YEAR, year);
                priCalendar.set(Calendar.MONTH, monthOfYear);
                priCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };
    }

    private Date getTimeFromDateTextbox() {
        SimpleDateFormat lDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        Date lDate = null;
        try {
            lDate = lDateFormat.parse(priCustomListItems.get(1).pubThirdLine + " " + priCustomListItems.get(0).pubThirdLine);
        } catch (ParseException e) {
            lDate = null;
        } finally {
            if (lDate == null) {
                lDate = new Date();
            }
        }
        return lDate;
    }

    private void navigateToLocationPage() {
        Intent intent = new Intent(proView.getContext(), MapLocation.class);
        startActivityForResult(intent, TaskDetail.GET_LOCATION);
    }

    public void setLocation(String iLocation) {
    }
}
