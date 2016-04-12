package com.guardian.ebutler.fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.screenhelper.DimensionHelper;

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
        ProgressBubbleFragment lBubble1 = ((ProgressBubbleFragment) getFragmentManager().findFragmentById(R.id.progress_bar_firstBubble));
        lBubble1.pubIsChecked = true;
        lBubble1.setupView();
        priView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                priView.post(new Runnable() {
                    public void run() {
                        float lWidth = priView.getWidth();
                        float lBubbleSize = getResources().getDimension(R.dimen.progress_bar_bubbleSize);
                        float lInBetween = (lWidth - (lBubbleSize * 5)) / 4;
                        float lFirstBubbleLocation = 0;
                        float lSecondBubbleLocation = lBubbleSize + lInBetween;
                        float lThirdBubbleLocation = lSecondBubbleLocation + lSecondBubbleLocation;
                        float lForthBubbleLocation = lThirdBubbleLocation + lSecondBubbleLocation;
                        float lFifthBubbleLocation = lForthBubbleLocation + lSecondBubbleLocation;
                        float lFirstDashLocation = lFirstBubbleLocation + lBubbleSize;
                        float lSecondDashLocation = lSecondBubbleLocation + lBubbleSize;
                        float lThirdDashLocation = lThirdBubbleLocation + lBubbleSize;
                        float lForthDashLocation = lForthBubbleLocation + lBubbleSize;

                        setLeftMargin(priView.findViewById(R.id.progress_bar_firstBubble), (int) lFirstBubbleLocation);
                        setLeftMargin(priView.findViewById(R.id.progress_bar_secondBubble), (int) lSecondBubbleLocation);
                        setLeftMargin(priView.findViewById(R.id.progress_bar_thirdBubble), (int) lThirdBubbleLocation);
                        setLeftMargin(priView.findViewById(R.id.progress_bar_forthBubble), (int) lForthBubbleLocation);
                        setLeftMargin(priView.findViewById(R.id.progress_bar_fifthBubble), (int) lFifthBubbleLocation);
                        setLeftMargin(priView.findViewById(R.id.progress_bar_firstDash), (int) (lFirstDashLocation - DimensionHelper.convertDpToPixel(1)));
                        setLeftMargin(priView.findViewById(R.id.progress_bar_secondDash), (int) (lSecondDashLocation - DimensionHelper.convertDpToPixel(1)));
                        setLeftMargin(priView.findViewById(R.id.progress_bar_thirdDash), (int) (lThirdDashLocation - DimensionHelper.convertDpToPixel(1)));
                        setLeftMargin(priView.findViewById(R.id.progress_bar_forthDash), (int) (lForthDashLocation - DimensionHelper.convertDpToPixel(1)));
                        setDashSize(priView.findViewById(R.id.progress_bar_firstDash), (int) (lInBetween + DimensionHelper.convertDpToPixel(2)), (int) (DimensionHelper.convertDpToPixel(3)));
                        setDashSize(priView.findViewById(R.id.progress_bar_secondDash), (int) (lInBetween + DimensionHelper.convertDpToPixel(2)), (int) (DimensionHelper.convertDpToPixel(3)));
                        setDashSize(priView.findViewById(R.id.progress_bar_thirdDash), (int) (lInBetween + DimensionHelper.convertDpToPixel(2)), (int) (DimensionHelper.convertDpToPixel(3)));
                        setDashSize(priView.findViewById(R.id.progress_bar_forthDash), (int) (lInBetween + DimensionHelper.convertDpToPixel(2)), (int) (DimensionHelper.convertDpToPixel(3)));
                    }
                });
            }
        });
    }

    protected void setDashSize(View rView, int iWidth, int iHeight) {
        rView.getLayoutParams().width = iWidth;
        rView.getLayoutParams().height = iHeight;
        ((RelativeLayout.LayoutParams)rView.getLayoutParams()).addRule(RelativeLayout.CENTER_VERTICAL);
        rView.requestLayout();
    }

    protected void setLeftMargin(View rView, int iLeftMargin) {
        if (rView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) rView.getLayoutParams();
            p.setMargins(iLeftMargin, 0, 0, 0);
            rView.requestLayout();
        }
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
