package com.guardian.ebutler.fragments.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.R;

public class CheckListItemFragment extends Fragment {
    private static final String ARG_PARAM1 = "Text";
    private static final String ARG_PARAM2 = "IsChecked";

    public String pubText;
    public Boolean pubIsChecked;

    protected View proView;

    private OnFragmentInteractionListener mListener;

    public CheckListItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param iText Parameter 1.
     * @param iIsChecked Parameter 2.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckListItemFragment newInstance(String iText, Boolean iIsChecked) {
        CheckListItemFragment fragment = new CheckListItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, iText);
        args.putBoolean(ARG_PARAM2, iIsChecked);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pubText = getArguments().getString(ARG_PARAM1);
            pubIsChecked = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    protected void setValuesToView() {
        TextView iTextView = (TextView)proView.findViewById(R.id.fragment_checklist_item_TextView);
        iTextView.setText(pubText);
        ImageButton lImageButton = (ImageButton) proView.findViewById(R.id.fragment_checklist_item_Delete);
        final Fragment lThis = this;
        lImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().beginTransaction().remove(lThis).commit();
            }
        });
        CheckBox lCheckBox = (CheckBox) proView.findViewById(R.id.fragment_checklist_item_Checkbox);
        lCheckBox.setChecked(pubIsChecked);
        lCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pubIsChecked = isChecked;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        proView = inflater.inflate(R.layout.fragment_checklist_item, container, false);
        setValuesToView();
        return proView;
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
