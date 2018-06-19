package com.primesoft.qrapppoc;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView scannerView;
    private Vibrator vibrator;
    private boolean isRegistration;
    private int regCount = 0;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isRegistration = getIntent().getBooleanExtra("IS_REGISTRATION",false);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        builder = new AlertDialog.Builder(ScannerActivity.this);
        builder.setMessage("Do you want to sync data?")
                .setTitle("Sync Data");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(),"Syncing data....",Toast.LENGTH_LONG).show();
                scannerView.resumeCameraPreview(ScannerActivity.this);
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                scannerView.resumeCameraPreview(ScannerActivity.this);
            }
        });
        builder.create();


//        Button button = new Button(this);
//        button.setText("Click");
//
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.gravity = Gravity.TOP|Gravity.RIGHT;
//        scannerView.addView(button,layoutParams);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ScannerActivity.this, RegistrationSummaryActivity.class);
//                startActivity(intent);
//            }
//        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},101);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(500);
        }

        if (isRegistration) {
            Intent intent = new Intent(ScannerActivity.this, RegistrationSummaryActivity.class);
            intent.putExtra("QR_VALUE", result.getText());
            startActivity(intent);
        } else {
            regCount++;

            if (regCount>5){
                regCount = 0;
                builder.show();
            } else {
                scannerView.resumeCameraPreview(this);
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scanner_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.summary:
                startActivity(new Intent(ScannerActivity.this,TrackSummaryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
