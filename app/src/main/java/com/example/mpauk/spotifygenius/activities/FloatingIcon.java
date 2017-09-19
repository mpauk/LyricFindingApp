package com.example.mpauk.spotifygenius.activities;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mpauk.spotifygenius.R;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.support.design.R.styleable.FloatingActionButton;

/**
 * Created by mpauk on 5/10/2017.
 */

public class FloatingIcon extends Service {
    protected static String artist;
    protected static String track;
    protected static String previousArtist;
    protected static String previousTrack;
    FloatingActionButton floatingIcon;
    WindowManager windowManager;
    WindowManager.LayoutParams params;
    TextView tv;
    SpotifyReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(R.style.AppTheme);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View fabLayout = inflater.inflate(R.layout.floating_button, null);
        View tvLayout = inflater.inflate(R.layout.text_view, null);
        tv = (TextView) tvLayout.findViewById(R.id.t_view);
        floatingIcon = (FloatingActionButton) fabLayout.findViewById(R.id.FloatingButton);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingIcon, params);
        receiver = new SpotifyReceiver();
        floatingIcon.setOnTouchListener(new View.OnTouchListener() {
            private Lyrics mTask;
            private int viewX;
            private int viewY;
            private float rawX;
            private float rawY;
            long startTime;
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewX = params.x;
                        viewY = params.y;
                        rawX = event.getRawX();
                        rawY = event.getRawY();
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (System.currentTimeMillis() - startTime > 900 && System.currentTimeMillis()- startTime<2500&&Math.abs(rawX-event.getRawX())<100 && Math.abs(rawY-event.getRawY())<100) {
                            windowManager.removeView(v);
                            if(artist!=null&&track!=null) {
                                try {
                                    mTask = new Lyrics();
                                    tv.setText(mTask.execute(artist,track).get());
                                    tv.setMovementMethod(new ScrollingMovementMethod());
                                }  catch (InterruptedException e) {
                                    e.printStackTrace();
                                    tv.setText("An error occured while finding lyrics for"+track +"and"+artist);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                    tv.setText("An error occured while finding lyrics"+track +"and"+artist);
                                }
                                tv.setOnTouchListener(new View.OnTouchListener(){
                                    private long startTime;
                                    private long endTime;
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        switch(event.getAction()){
                                            case MotionEvent.ACTION_DOWN:
                                                startTime = System.currentTimeMillis();
                                                break;
                                            case MotionEvent.ACTION_UP:
                                                endTime = System.currentTimeMillis();
                                                if(Math.abs(endTime-startTime)>1300) {
                                                    windowManager.removeView(tv);
                                                    windowManager.addView(floatingIcon, params);
                                                }
                                                break;
                                        }
                                        return false;
                                    }
                                });
                            }
                            else {
                                tv.setText("An error occured while finding lyrics"+track +"and"+artist);
                            }
                            windowManager.addView(tv, params);
                        }
                        else if(System.currentTimeMillis()-startTime>2500&& Math.abs(rawX-event.getRawX())<100 && Math.abs(rawY-event.getRawY())<100){
                            mTask.cancel(true);
                            Log.d("cancel","service should stop");
                            windowManager.removeView(v);
                            stopSelf();
                        }
                    case MotionEvent.ACTION_MOVE:

                        params.x = viewX + (int) (event.getRawX() - rawX);
                        params.y = viewY + (int) (event.getRawY() - rawY);
                        windowManager.updateViewLayout(v, params);
                        break;
                }
                return true;
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

