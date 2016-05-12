package com.guardian.ebutler.fragments.answers;

import android.os.Bundle;
import android.app.Fragment;
import android.view.View;
import android.widget.DatePicker;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Condition;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateFragment extends AbstractAnswerFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ConditionName";
    private static final String ARG_PARAM2 = "DefaultValue";

    // TODO: Rename and change types of parameters
    private String priConditionName = "condition name";
    private ArrayList<String> priDefaultValue = new ArrayList<>();

    public DateFragment() {
        proFragmentId = R.layout.fragment_date;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param iInformationPropertiesNames Parameter 1.
     * @return A new instance of fragment DateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DateFragment newInstance(String iInformationPropertiesNames, ArrayList<String> rDefaultValue) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, iInformationPropertiesNames);
        args.putSerializable(ARG_PARAM2, rDefaultValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setValuesToView(View view) {
        DatePicker lDatePicker = (DatePicker)(view.findViewById(R.id.fragment_date_DatePicker));
        int lDay, lMonth, lYear;
        Calendar lCalendar = Calendar.getInstance();
        if (priDefaultValue == null || priDefaultValue.size() < 3) {
            lYear = lCalendar.get(Calendar.YEAR);
        } else {
            lYear = Integer.parseInt(priDefaultValue.get(2));
        }
        if (priDefaultValue == null || priDefaultValue.size() < 2) {
            lMonth = lCalendar.get(Calendar.MONTH);
        } else {
            lMonth = Integer.parseInt(priDefaultValue.get(1)) - 1;
        }
        if (priDefaultValue == null || priDefaultValue.size() < 1) {
            lDay = lCalendar.get(Calendar.DAY_OF_MONTH);
        } else {
            lDay = Integer.parseInt(priDefaultValue.get(0));
        }
        lDatePicker.init(lYear, lMonth, lDay, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String lString = getArguments().getString(ARG_PARAM1);
            if (lString.length() > 0) {
                priConditionName = lString;
            }
            priDefaultValue = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public String getChatStatement() {
        String lReturnValue = "";
        DatePicker lDatePicker = (DatePicker)(getView().findViewById(R.id.fragment_date_DatePicker));
        lDatePicker.clearFocus();
        lReturnValue += String.format("%02d", lDatePicker.getDayOfMonth());
        lReturnValue += "/";
        lReturnValue += String.format("%02d", lDatePicker.getMonth() + 1);
        lReturnValue += "/";
        lReturnValue += String.format("%04d", lDatePicker.getYear());
        return lReturnValue;
    }

    public ArrayList<Condition> getValues() {
        ArrayList<Condition> lReturnValues = new ArrayList<>();
        Condition lReturnValue = new Condition();
        lReturnValue.pubConditionName = priConditionName;
        lReturnValue.pubType = "Date";
        DatePicker lDatePicker = (DatePicker)(getView().findViewById(R.id.fragment_date_DatePicker));
        lDatePicker.clearFocus();
        String result = "";
        result += String.format("%02d", lDatePicker.getDayOfMonth());
        result += "/";
        result += String.format("%02d", lDatePicker.getMonth() + 1);
        result += "/";
        result += String.format("%04d", lDatePicker.getYear());
        lReturnValue.pubValue = result;
        lReturnValues.add(lReturnValue);
        return lReturnValues;
    }
}
