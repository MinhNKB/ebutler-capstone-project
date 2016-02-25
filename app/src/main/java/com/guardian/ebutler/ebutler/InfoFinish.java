package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoFinish extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_finish);
        bindNavigationLocation();
    }

    private void bindNavigationLocation() {
        final Context context = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("EXTRA_TYPE");
            if(value.compareTo("Declined")==0) {
                TextView textView = (TextView)findViewById(R.id.info_finish_chatText);
                textView.setText(R.string.info_declined_butlerSpeech);
            }
            else
            {
                TextView textView = (TextView)findViewById(R.id.info_finish_chatText);
                textView.setText(R.string.info_finish_butlerSpeech);
            }
        }


        ImageButton button = (ImageButton)findViewById(R.id.info_finish_buttonDashboard);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        button = (ImageButton)findViewById(R.id.info_finish_buttonAddTask);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryList.class);
                startActivity(intent);
            }
        });
    }
}
