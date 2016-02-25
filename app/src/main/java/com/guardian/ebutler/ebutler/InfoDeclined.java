package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class InfoDeclined extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_declined);
        bindNavigationLocation();
    }

    private void bindNavigationLocation() {
        final Context context = this;

        ImageButton button = (ImageButton)findViewById(R.id.info_declined_buttonDashboard);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button = (ImageButton)findViewById(R.id.info_declined_buttonAddTask);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryList.class);
                startActivity(intent);
            }
        });
    }
}
