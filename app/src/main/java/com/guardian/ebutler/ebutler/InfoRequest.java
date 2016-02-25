package com.guardian.ebutler.ebutler;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.guardian.ebutler.ebutler.wizard.UserWizard;

public class InfoRequest extends Activity {

    private ImageButton mOkButton;
    private ImageButton mDeclButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_request);

        mOkButton = (ImageButton) findViewById(R.id.ok_button);
        mDeclButton = (ImageButton) findViewById(R.id.decline_button);

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
        Intent i = new Intent(this, InfoFinish.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("EXTRA_TYPE", "Declined");
        startActivity(i);
    }

    private void onClickOkButton() {
        Intent i = new Intent(this, UserWizard.class);
        startActivity(i);
    }

}
