package com.austgl.syllabus.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import java.util.TimerTask;
import com.austgl.syllabus.R;


public class FullScreenActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGHT = 2000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.fullscreen);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent mainIntent = new Intent(FullScreenActivity.this,MainActivity.class);
                FullScreenActivity.this.startActivity(mainIntent);
                FullScreenActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);
    }
     /*
        Intent intent = new Intent();
        intent.setClass(FullScreenActivity.this,MainActivity.class);
        startActivity(intent);
        FullScreenActivity.this.finish();
       Timer timer = new Timer();
        timer.schedule(new MyTask(), 1000, 2000);//在1秒后执行此任务,每次间隔2秒,如果传递一个Data参数,就可以在某个固定的时间执行这个任务.
       //这个是用来停止此任务的,否则就一直循环执行此任务了
            try {
                    timer.cancel();//使用这个方法退出任务

            } catch (Exception e) {
                e.printStackTrace();
            }
*/

    }


