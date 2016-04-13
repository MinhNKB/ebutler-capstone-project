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
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.screenhelper.DimensionHelper;

import java.util.List;

public class ProgressBarFragment extends Fragment {
    private View priView;
    private OnFragmentInteractionListener mListener;
    private View priBubble1;
    private View priBubble2;
    private View priBubble3;
    private View priBubble4;
    private View priBubble5;
    private View priDash1;
    private View priDash2;
    private View priDash3;
    private View priDash4;


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
        priBubble1 = priView.findViewById(R.id.progress_bar_firstBubble);
        priBubble2 = priView.findViewById(R.id.progress_bar_secondBubble);
        priBubble3 = priView.findViewById(R.id.progress_bar_thirdBubble);
        priBubble4 = priView.findViewById(R.id.progress_bar_forthBubble);
        priBubble5 = priView.findViewById(R.id.progress_bar_fifthBubble);
        priDash1 = priView.findViewById(R.id.progress_bar_firstDash);
        priDash2 = priView.findViewById(R.id.progress_bar_secondDash);
        priDash3 = priView.findViewById(R.id.progress_bar_thirdDash);
        priDash4 = priView.findViewById(R.id.progress_bar_forthDash);

        setCategories();

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

                        setLeftMargin(priBubble1, (int) lFirstBubbleLocation);
                        setLeftMargin(priBubble2, (int) lSecondBubbleLocation);
                        setLeftMargin(priBubble3, (int) lThirdBubbleLocation);
                        setLeftMargin(priBubble4, (int) lForthBubbleLocation);
                        setLeftMargin(priBubble5, (int) lFifthBubbleLocation);
                        setLeftMargin(priDash1, (int) (lFirstDashLocation - DimensionHelper.convertDpToPixel(1)));
                        setLeftMargin(priDash2, (int) (lSecondDashLocation - DimensionHelper.convertDpToPixel(1)));
                        setLeftMargin(priDash3, (int) (lThirdDashLocation - DimensionHelper.convertDpToPixel(1)));
                        setLeftMargin(priDash4, (int) (lForthDashLocation - DimensionHelper.convertDpToPixel(1)));
                        setDashSize(priDash1, (int) (lInBetween + DimensionHelper.convertDpToPixel(2)), (int) (DimensionHelper.convertDpToPixel(3)));
                        setDashSize(priDash2, (int) (lInBetween + DimensionHelper.convertDpToPixel(2)), (int) (DimensionHelper.convertDpToPixel(3)));
                        setDashSize(priDash3, (int) (lInBetween + DimensionHelper.convertDpToPixel(2)), (int) (DimensionHelper.convertDpToPixel(3)));
                        setDashSize(priDash4, (int) (lInBetween + DimensionHelper.convertDpToPixel(2)), (int) (DimensionHelper.convertDpToPixel(3)));
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

    public void setProgress(List<Boolean> rProgresses) {
        toggleSwitch(rProgresses.get(0), R.id.progress_bar_firstBubble);
        toggleSwitch(rProgresses.get(1), R.id.progress_bar_secondBubble);
        toggleSwitch(rProgresses.get(2), R.id.progress_bar_thirdBubble);
        toggleSwitch(rProgresses.get(3), R.id.progress_bar_forthBubble);
        toggleSwitch(rProgresses.get(4), R.id.progress_bar_fifthBubble);
    }

    private void toggleSwitch(Boolean iIsChecked, int iFragmentId) {
        ProgressBubbleFragment lBubble = ((ProgressBubbleFragment) getFragmentManager().findFragmentById(iFragmentId));
        if (iIsChecked) {
            lBubble.turnOn();
        } else {
            lBubble.turnOff();
        }
    }

    private void setCategories() {
        setCategory(1, R.id.progress_bar_firstBubble);
        setCategory(2, R.id.progress_bar_secondBubble);
        setCategory(3, R.id.progress_bar_thirdBubble);
        setCategory(4, R.id.progress_bar_forthBubble);
        setCategory(5, R.id.progress_bar_fifthBubble);
    }

    private void setCategory(int iCategory, int iFragmentId) {
        ProgressBubbleFragment lBubble = ((ProgressBubbleFragment) getFragmentManager().findFragmentById(iFragmentId));
        lBubble.setCategory(iCategory);
    }

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
