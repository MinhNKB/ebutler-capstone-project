package com.guardian.ebutler.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Condition;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MultipleChoiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MultipleChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultipleChoiceFragment extends Fragment implements AnswerFragmentInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ConditionName";
    private static final String ARG_PARAM2 = "OptionNameList";
    private static final String ARG_PARAM3 = "EnumList";

    // TODO: Rename and change types of parameters
    private String priConditionName = "condition name";
    private ArrayList<String> priOptionNameList = new ArrayList<String>();
    private ArrayList<String> priEnumList = new ArrayList<String>();

    private OnFragmentInteractionListener mListener;

    public MultipleChoiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param iConditionName Parameter 1.
     * @param rOptionNameList Parameter 2.
     * @param rEnumList Parameter 2.
     * @return A new instance of fragment MultipleChoiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MultipleChoiceFragment newInstance(String iConditionName, ArrayList<String> rOptionNameList, ArrayList<String> rEnumList) {
        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, iConditionName);
        args.putSerializable(ARG_PARAM2, rOptionNameList);
        args.putSerializable(ARG_PARAM3, rEnumList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String lString = getArguments().getString(ARG_PARAM1);
            if (lString.length() > 0) {
                priConditionName = lString;
                priOptionNameList = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM2);
                priEnumList = (ArrayList<String>) getArguments().getSerializable(ARG_PARAM3);
            }
        }
    }

    public void setValuesToView(View view) {
        LinearLayout lLinearLayout = (LinearLayout) view.findViewById(R.id.fragment_multiple_choice_RadioBoxContainer);

        for (String lOptionName :
                priOptionNameList) {
            RadioButton lRadioButton = new RadioButton(getActivity());
            lRadioButton.setText(lOptionName);
            lRadioButton.setTag(lOptionName);
            lLinearLayout.addView(lRadioButton);
        }
    }

    public ArrayList<Condition> getValues() {
        ArrayList<Condition> lReturnValues = new ArrayList<>();
        Condition lReturnValue = new Condition();
        lReturnValue.pubConditionName = priConditionName;
        lReturnValue.pubType = "enum";
        int index = 0;
        for (String lOptionName :
                priOptionNameList) {
            String lEnum = priEnumList.get(index);
            ++index;
            if (((RadioButton) getView().findViewWithTag(lOptionName)).isChecked()) {
                lReturnValue.pubValue = lEnum;
                break;
            }
        }
        lReturnValues.add(lReturnValue);
        return lReturnValues;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multiple_choice, container, false);
        setValuesToView(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
