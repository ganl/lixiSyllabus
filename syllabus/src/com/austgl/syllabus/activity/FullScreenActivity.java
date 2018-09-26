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
        timer.schedule(new MyTask(), 1000, 2000);//��1���ִ�д�����,ÿ�μ��2��,�������һ��Data����,�Ϳ�����ĳ���̶���ʱ��ִ���������.
       //���������ֹͣ�������,�����һֱѭ��ִ�д�������
            try {
                    timer.cancel();//ʹ����������˳�����

            } catch (Exception e) {
                e.printStackTrace();
            }
*/

    }


