package com.example.mpauk.spotifygenius.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mpauk.spotifygenius.R;

public class MainActivity extends AppCompatActivity {
    protected static boolean serviceRunning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this, FloatingIcon.class);
                if(Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 1234);
                    }
                    if(!serviceRunning) {
                        startService(intent1);
                        serviceRunning = true;
                    }
                    else{

                        stopService(intent1);

                    }
                }
                else
                {
                    if(!serviceRunning) {
                        startService(intent1);
                        serviceRunning = true;
                    }
                    else{
                        stopService(intent1);

                    }
                }

             }
        });
    }

}
