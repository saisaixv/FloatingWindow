package com.saisai.floatingwindow;

import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements  View.OnTouchListener {

    private WindowManager windowManager;
    private Button button;
    private WindowManager.LayoutParams params;
    private float rawY;
    private float rawX;
    private int y;
    private int x;
    private int[] local;
    private VideoView videoView;
    private VideoView videoView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                windowManager.addView(videoView,params);
//                videoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aaa.mp4");
//                videoView.start();
            }
        });

//        button = new Button(this);
        videoView2 = (VideoView) findViewById(R.id.btn_add);
//        button.setText("我是新加的");
        videoView=new VideoView(this);
        videoView.setOnTouchListener(this);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0, 0,
                PixelFormat.TRANSPARENT);

        params.type= WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 100;
        params.y = 300;
        windowManager = getWindowManager();

        MediaController controller=new MediaController(this);
        videoView2.setMediaController(controller);
        videoView2.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aaa.mp4");
        videoView2.start();

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG","x="+params.x+"   y="+params.y);
                Log.e("TAG","down");

                local = new int[2];
                v.getLocationOnScreen(local);

                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e("TAG","move");

                if(Math.abs(event.getRawX()-local[0]-v.getWidth()/2)<10||Math.abs(event.getRawY()-local[1]-v.getHeight()/2)<10){
                    break;
                }

                params.x= (int) event.getRawX()-v.getWidth()/2;//-local[0]-v.getWidth()/2;
                params.y= (int) event.getRawY()-v.getHeight()/2;//-local[1]-v.getHeight()/2;
                Log.e("TAG","x="+params.x+"   y="+params.y);
                getWindowManager().updateViewLayout(v,params);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG","up");
                break;
        }
        return false;
    }

//    @Override
//    public void onClick(View v) {
//        Toast.makeText(this,"hhhhh",Toast.LENGTH_SHORT).show();
//    }


    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        videoView2.pause();
        int currentPosition = videoView2.getCurrentPosition();
        windowManager.addView(videoView,params);
        videoView.setVideoPath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aaa.mp4");
        videoView.start();
        videoView.seekTo(currentPosition);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        if(videoView.isAttachedToWindow()){
            Log.e("TAG","Attached");
            int currentPosition = videoView.getCurrentPosition();
            windowManager.removeView(videoView);
            videoView2.seekTo(currentPosition);
            videoView2.start();
        }else {
            Log.e("TAG"," not Attached");
        }
    }
}
