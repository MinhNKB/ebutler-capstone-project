package com.guardian.ebutler.ebutler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.guardian.ebutler.ebutler.wizard.UserWizardActivity;

public class InfoRequestActivity extends AppCompatActivity {

    private Button mOkButton;
    private Button mDeclButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_request);

        mOkButton = (Button) findViewById(R.id.ok_button);
        mDeclButton = (Button) findViewById(R.id.decline_button);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOkButton();
            }
        });

        mDeclButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeclButton();
            }
        });
    }

    private void onClickDeclButton() {
        Intent i = new Intent(this, InfoDeclinedActivity.class);
        startActivity(i);
    }

    private void onClickOkButton() {
        Intent i = new Intent(this, UserWizardActivity.class);
        startActivity(i);
    }

}
