package com.guardian.ebutler.ebutler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        String username = intent.getStringExtra(Login.EXTRA_USERNAME);
        TextView textview = (TextView)findViewById(R.id.user_name);
        textview.setText(username);
    }
}
