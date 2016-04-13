package com.guardian.ebutler.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.UserInfoInput;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgressBubbleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgressBubbleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressBubbleFragment extends Fragment {
    private View priView = null;
    private Context priContext = null;
    private OnFragmentInteractionListener mListener;

    private static final String ARG_PARAM1 = "IsChecked";
    private static final String ARG_PARAM2 = "CategoryId";

    private Boolean priIsChecked = false;
    private int priCategoryId = -1;

    public ProgressBubbleFragment() {
        // Required empty public constructor
    }

    public static ProgressBubbleFragment newInstance(Boolean iIsChecked, Integer iCategoryId) {
        ProgressBubbleFragment fragment = new ProgressBubbleFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, iIsChecked);
        args.putInt(ARG_PARAM2, iCategoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            priIsChecked = getArguments().getBoolean(ARG_PARAM1);
            priCategoryId = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        priView = inflater.inflate(R.layout.fragment_progress_bubble, container, false);
        setupView();
        setupEvent();
        return priView;
    }

    public void setCategory(int iCategoryId) {
        priCategoryId = iCategoryId;
    }

    public void setupEvent() {
        if (getActivity() instanceof UserInfoInput) {

            priView.findViewById(R.id.progress_bubble_bubble).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((UserInfoInput) getActivity()).progressBubbleClicked(priCategoryId);
                }
            });

        } else {
        }
    }

    public void setupView() {
        View progressBubble = priView.findViewById(R.id.progress_bubble_bubble);
        if (priIsChecked) {
            if (Build.VERSION.SDK_INT >= 22) {
                progressBubble.setBackground(priView.getResources().getDrawable(R.drawable.blue_bubble, null));
            } else {
                progressBubble.setBackground(priView.getResources().getDrawable(R.drawable.blue_bubble));
            }
            priView.findViewById(R.id.progress_bubble_checkmate).setVisibility(View.VISIBLE);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void turnOn() {
        priIsChecked = true;
        setupView();
    }

    public void turnOff() {
        priIsChecked = false;
        setupView();
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
        priContext = context;
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
