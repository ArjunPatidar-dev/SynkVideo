package com.example.synkvideo.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.synkvideo.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton ibHome;
    private ImageButton ibStartRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = new Toolbar(this);
        toolbar.removeAllViews();

        initViews();
    }

    private void initViews() {
        ibHome = findViewById(R.id.ib_home);
        ibStartRecord = findViewById(R.id.ib_start_record);

        ibHome.setOnClickListener(this);
        ibStartRecord.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_home:
                Intent intent2 = new Intent(this, VideoStreamingActivity.class);
                startActivity(intent2);
                break;
            case R.id.ib_start_record:
                requestCameraPermission();
                break;
        }
    }

    private void openVideoRecord(){
        Intent intent = new Intent(this, VideoRecordingActivity.class);
        startActivity(intent);
    }
    public void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Permission");
//                    builder.setPositiveButton(android.R.string.ok, null);
//                    builder.setMessage("Please enable access to camera and microphone.");
//                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @TargetApi(Build.VERSION_CODES.M)
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            requestPermissions(
//                                    new String[]
//                                            {Manifest.permission.CAMERA}
//                                    , 1);
//                        }
//                    });
//                    builder.show();
//                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                            1);
//                }
            } else {
                openVideoRecord();            }
        } else {
            openVideoRecord();        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i("HomeActivity", "onRequestPermissionsResult: grantResults length ::" + grantResults.length);
        Log.i("HomeActivity", "onRequestPermissionsResult: grantResults 0 ::" + grantResults[0]);
        Log.i("HomeActivity", "onRequestPermissionsResult: grantResults 1 ::" + grantResults[1]);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openVideoRecord();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}