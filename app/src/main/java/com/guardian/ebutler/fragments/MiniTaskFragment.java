package com.guardian.ebutler.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.Dashboard;
import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.world.Global;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MiniTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MiniTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MiniTaskFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private View priView;

    public MiniTaskFragment() {
        // Required empty public constructor
    }

    private Task priTask;

    public static MiniTaskFragment newInstance(Task iTask) {
        MiniTaskFragment fragment = new MiniTaskFragment();
        fragment.setTask(iTask);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void setTask(Task iTask) {
        this.priTask = iTask;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.priView = inflater.inflate(R.layout.fragment_mini_task, container, false);
        setValuesToView(priView);
        return this.priView;
    }

    private void setValuesToView(View priView) {
        ImageButton lImageButton = (ImageButton) priView.findViewById(R.id.fragment_mini_task_imageButton);
        lImageButton.setImageResource(Global.getInstance().getTaskTypeDrawable(priTask));
        TextView lTextView = (TextView) priView.findViewById(R.id.fragment_mini_task_textView);
        lTextView.setText(Dashboard.getFirstLine(priTask));
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
