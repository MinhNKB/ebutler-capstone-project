package com.guardian.ebutler.fragments.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.Dashboard;
import com.guardian.ebutler.ebutler.R;

public class CheckListItemFragment extends Fragment {
    private static final String ARG_PARAM1 = "Text";
    private static final String ARG_PARAM2 = "IsChecked";

    public String pubText;
    public Boolean pubIsChecked;
    public CheckListFragment pubCheckListFragment;

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
        final ImageButton lDeleteButton = (ImageButton) proView.findViewById(R.id.fragment_checklist_item_Delete);
        final Fragment lThis = this;
        lDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().beginTransaction().remove(lThis).commit();
            }
        });

        final EditText iTextView = (EditText)proView.findViewById(R.id.fragment_checklist_item_EditText);
        iTextView.setText(pubText);
        iTextView.requestFocusFromTouch();
        Dashboard.showSoftKeyboard(getActivity(), InputMethodManager.SHOW_IMPLICIT);

        iTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                pubText = iTextView.getText().toString();
                return false;
            }
        });
        iTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                pubText = iTextView.getText().toString();
                pubCheckListFragment.addNewItem();
                return false;
            }
        });
        iTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                lDeleteButton.setVisibility(hasFocus == true ? View.VISIBLE : View.GONE);
            }
        });


        final ImageButton lCheckBox = (ImageButton) proView.findViewById(R.id.fragment_chekclist_item_CheckBox);
        lCheckBox.setImageResource(pubIsChecked == true ? R.mipmap.ic_check_box_grey : R.mipmap.ic_check_box_outline_blank_grey);

        lCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pubIsChecked = !pubIsChecked;
                lCheckBox.setImageResource(pubIsChecked == true ? R.mipmap.ic_check_box_grey : R.mipmap.ic_check_box_outline_blank_grey);
                pubCheckListFragment.reallocateFragment(CheckListItemFragment.this);
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
