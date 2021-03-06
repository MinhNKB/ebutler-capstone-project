package com.guardian.ebutler.fragments.tasks;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guardian.ebutler.ebutler.dataclasses.Condition;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.fragments.tasks.TaskFragmentInterface;
import com.guardian.ebutler.world.Global;

import java.util.ArrayList;

/**
 * Created by Tabuzaki IA on 3/16/2016.
 */
public class AbstractTaskFragment extends Fragment implements TaskFragmentInterface {
    protected int proFragmentId = -1;
    protected OnFragmentInteractionListener mListener;

    public void setValuesToView(View view) {

    }

    public void getValues(Task rTask) {

    }

    public void setValues(Task iTask) {

    }

    protected View proView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.proView = inflater.inflate(proFragmentId, container, false);
        setValuesToView(this.proView);
        if(Global.getInstance().pubSelectedTask!=null) {
            setValues(Global.getInstance().pubSelectedTask);
        }
        return this.proView;
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
