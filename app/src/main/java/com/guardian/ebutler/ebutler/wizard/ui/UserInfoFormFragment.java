package com.guardian.ebutler.ebutler.wizard.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.wizardpager.wizard.ui.PageFragmentCallbacks;
import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.wizard.model.UserInfoFormPage;

/**
 * Created by mypc on 1/25/16.
 */
public class UserInfoFormFragment extends Fragment {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private UserInfoFormPage mPage;
    private TextView mNameField;
    private Spinner mGenderField;
    private DatePicker mDOBField;
    private TextView mAddrField;
    private Spinner mMarriedStatsField;

    public static UserInfoFormFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UserInfoFormFragment fragment = new UserInfoFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UserInfoFormFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (UserInfoFormPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_userinfo_form, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        ((TextView) rootView.findViewById(R.id.butler_info_text)).setText(getString(R.string.user_info_butlerQuestion));

        mNameField = ((TextView) rootView.findViewById(R.id.wizard_field_name));
        mGenderField = ((Spinner) rootView.findViewById(R.id.wizard_field_gender));
        mDOBField = ((DatePicker) rootView.findViewById(R.id.wizard_field_dob));
        mAddrField = ((TextView) rootView.findViewById(R.id.wizard_field_address));
        mMarriedStatsField = ((Spinner) rootView.findViewById(R.id.wizard_field_marriedstats));


        mNameField.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(UserInfoFormPage.NAME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });
        mGenderField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPage.getData().putString(UserInfoFormPage.GENDER_DATA_KEY, (String) parent.getItemAtPosition(position));
                mPage.notifyDataChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Init DatePicker DOBField >_<
        //TODO:(nthoang/task1) Please specify initial UserInfo Wizard DOB datepicker value here
        mDOBField.init(2015, 1, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //TODO:(nthoang/task1) Format Date Data for UserInfo Wizard is dd-mm-yyyy
                //Fixed:(nthoang/task1) monthofYear should + 1
                mPage.getData().putString(UserInfoFormPage.DOB_DATA_KEY, String.format("%d-%d-%d", dayOfMonth, monthOfYear + 1, year));
            }
        });
        mAddrField.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(UserInfoFormPage.ADDRESS_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });
        mMarriedStatsField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPage.getData().putString(UserInfoFormPage.MARRIEDSTATS_DATA_KEY, (String) parent.getItemAtPosition(position));
                mPage.notifyDataChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

    }
}
