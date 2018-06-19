package com.primesoft.qrapppoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SessionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        Button register = findViewById(R.id.registerButton);
        Button track1 = findViewById(R.id.track1_button);

        final Intent intent = new Intent(SessionActivity.this, ScannerActivity.class);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("IS_REGISTRATION", true);
                startActivity(intent);
            }
        });

        track1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("IS_REGISTRATION", false);
                startActivity(intent);
            }
        });


    }
}
