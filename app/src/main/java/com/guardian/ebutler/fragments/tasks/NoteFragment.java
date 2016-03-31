package com.guardian.ebutler.fragments.tasks;

import android.os.Bundle;
import android.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.Task;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends AbstractTaskFragment {
    public NoteFragment() {
        proFragmentId = R.layout.fragment_note;
    }

    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setValuesToView(View view){
        ((EditText) proView.findViewById(R.id.fragment_note_description)).requestFocusFromTouch();
    }

    public void getValues(Task rNewTask) {
        rNewTask.pubDescription = ((EditText) proView.findViewById(R.id.fragment_note_description)).getText().toString();
    }
}
