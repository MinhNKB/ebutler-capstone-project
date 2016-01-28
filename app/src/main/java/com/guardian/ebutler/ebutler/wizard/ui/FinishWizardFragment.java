package com.guardian.ebutler.ebutler.wizard.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.wizardpager.wizard.model.ModelCallbacks;
import com.example.android.wizardpager.wizard.model.Page;
import com.guardian.ebutler.ebutler.R;

/**
 * Created by mypc on 1/25/16.
 */
public class FinishWizardFragment extends Fragment implements ModelCallbacks {

    public FinishWizardFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_simple_info, container, false);
        ((TextView) rootView.findViewById(R.id.butler_info_text)).setText(getString(R.string.info_finish_butlerConfirm));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onPageTreeChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPageDataChanged(Page page) {

    }

    @Override
    public void onPageTreeChanged() {

    }
}
