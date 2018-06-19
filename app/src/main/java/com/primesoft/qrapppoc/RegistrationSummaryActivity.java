package com.primesoft.qrapppoc;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationSummaryActivity extends BaseActivity {

    TextView reg_no, reg_name, mob_no, gender, shirt_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_summary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String qrValue = getIntent().getStringExtra("QR_VALUE");
        String values[] = qrValue.split("\\|");

        reg_no = findViewById(R.id.reg_no);
        reg_name = findViewById(R.id.reg_name);
        mob_no = findViewById(R.id.mob_no);
        gender = findViewById(R.id.gender);
        shirt_size = findViewById(R.id.shirt_size);

        if (values.length <= 1){
            Toast.makeText(this, "Incorrect QR format", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            reg_no.setText(values[0]);
            reg_name.setText(values[1]);
            mob_no.setText(values[2]);
            gender.setText(values[3]);
            shirt_size.setText(values[4]);
        }

        final ProgressBar progressBar = findViewById(R.id.progressBar_reg);

        final Thread successThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Registration Successed",Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                });
            }
        });

        final Thread tempErrorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Registration Failed",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        Button done = findViewById(R.id.summary_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.animate();
                successThread.start();
            }
        });

        Button temp_error = findViewById(R.id.temp_error);
        temp_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.animate();
                tempErrorThread.start();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
