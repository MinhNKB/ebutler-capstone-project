package com.guardian.ebutler.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.guardian.ebutler.ebutler.R;

public class ProgressBarFragment extends Fragment {
    private View priView;
    private OnFragmentInteractionListener mListener;

    public ProgressBarFragment() {
        // Required empty public constructor
    }

    public static ProgressBarFragment newInstance() {
        ProgressBarFragment fragment = new ProgressBarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        priView = inflater.inflate(R.layout.fragment_progress_bar, container, false);
        setupView();
        return priView;
    }

    protected void setupView() {
        priView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                priView.post(new Runnable() {
                    public void run() {
                        float lWidth = priView.getWidth();
                        float lBubbleSize = getResources().getDimension(R.dimen.progress_bar_bubbleSize);
                        float lInBetween = (lWidth - (lBubbleSize * 5)) / 4;
                        float lFirstLocation = 0;
                        float lSecondLocation = lBubbleSize + lInBetween;
                        float lThirdLocation = lSecondLocation + lSecondLocation;
                        float lForthLocation = lThirdLocation + lSecondLocation;
                        float lFifthLocation = lForthLocation + lSecondLocation;

                        setLeftMargin(priView.findViewById(R.id.progress_bar_firstBubble), (int) lFirstLocation);
                        setLeftMargin(priView.findViewById(R.id.progress_bar_secondBubble), (int) lSecondLocation);
                        setLeftMargin(priView.findViewById(R.id.progress_bar_thirdBubble), (int) lThirdLocation);
                        setLeftMargin(priView.findViewById(R.id.progress_bar_forthBubble), (int) lForthLocation);
                        setLeftMargin(priView.findViewById(R.id.progress_bar_fifthBubble), (int) lFifthLocation);
                    }
                });
            }
        });
    }

    protected void setLeftMargin(View rView, int iLeftMargin) {
        ViewGroup.MarginLayoutParams lMarginParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lMarginParams.setMargins(iLeftMargin, 0, 0, 0);
        RelativeLayout.LayoutParams lLayoutParams = new RelativeLayout.LayoutParams(lMarginParams);
        rView.setLayoutParams(lLayoutParams);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
