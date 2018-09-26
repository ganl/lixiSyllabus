package com.example.syllabus.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.example.syllabus.R;
import com.example.syllabus.SyllabusApplication;
import com.example.syllabus.adapter.ViewPagerAdapter;
import com.example.syllabus.utils.CommonConstants;

/**
 * 滑动介绍界面
 * 
 * @author Administrator
 * 
 */
public class WelcomeActivity extends Activity implements OnClickListener, OnPageChangeListener
{
    
    private ViewPager viewPager;
    
    private ViewPagerAdapter viewPagerAdapter;
    
    private List<View> views;
    
    private static final int[] pics = {R.drawable.top_one, R.drawable.top_two, R.drawable.top_three,
        R.drawable.top_four, R.drawable.top_five, R.drawable.top_six};
    
    private static final int[] bottoms = {R.drawable.bottom_one, R.drawable.bottom_two, R.drawable.bottom_three,
        R.drawable.bottom_four, R.drawable.bottom_five, R.drawable.bottom_six};
    
    private ImageView ivBottom;
    
    private ImageView ivStudent;
    
    private ImageView ivTeacher;
    
    private ImageView ivInputPerson;
    
    private ImageView ivDownload;
    
    // private boolean isStudent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        SyllabusApplication.getInstance().addActivity(this);
        
        views = new ArrayList<View>();
        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        for (int i = 0; i < pics.length; i++)
        {
            RelativeLayout rl = null;
            
            if (i == pics.length - 1)
            {
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                rl = (RelativeLayout)inflater.inflate(R.layout.lastwelcome, null);
                
                ivStudent = (ImageView)rl.findViewById(R.id.studentimage);
                ivTeacher = (ImageView)rl.findViewById(R.id.teacherimage);
                ivInputPerson = (ImageView)rl.findViewById(R.id.selfinput);
                ivDownload = (ImageView)rl.findViewById(R.id.download);
                
                ivStudent.setOnClickListener(this);
                ivTeacher.setOnClickListener(this);
                ivInputPerson.setOnClickListener(this);
                ivDownload.setOnClickListener(this);
                
            }
            else
            {
                rl = new RelativeLayout(this);
                rl.setLayoutParams(params);
                rl.setBackgroundResource(pics[i]);
            }
            
            views.add(rl);
        }
        
        viewPager = (ViewPager)findViewById(R.id.page);
        
        viewPagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(viewPagerAdapter);
        
        viewPager.setOnPageChangeListener(this);
        
        ivBottom = (ImageView)findViewById(R.id.bottom);
    }
    
    public void onPageScrollStateChanged(int arg0)
    {
        Log.i("WelcomeActivity", "arg0 in onPageScrollStateChanged:" + arg0);
    }
    
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {
    }
    
    public void onPageSelected(int arg0)
    {
        Log.i("WelcomeActivity", "arg0 in onPageSelected:" + arg0);
        ivBottom.setImageResource(bottoms[arg0]);
    }
    
    public void onClick(View arg0)
    {
        Intent intent;
        
        SharedPreferences preferences = CommonConstants.getMyPreferences(this);
        Editor editor = preferences.edit();
        switch (arg0.getId())
        {
            case R.id.studentimage:
                ivStudent.setBackgroundResource(R.drawable.student_selected);
                // isStudent = true;
                editor.putBoolean(CommonConstants.IS_TEACHER, false);
                editor.commit();
                ((SyllabusApplication)getApplication()).setTeacher(false);
                ivTeacher.setBackgroundResource(R.drawable.teacher);
                
                break;
            case R.id.teacherimage:
                ivTeacher.setBackgroundResource(R.drawable.teacher_selected);
                // isStudent = false;
                editor.putBoolean(CommonConstants.IS_TEACHER, true);
                editor.commit();
                ((SyllabusApplication)getApplication()).setTeacher(true);
                ivStudent.setBackgroundResource(R.drawable.student);
                break;
            case R.id.selfinput:
                if (!preferences.getBoolean(CommonConstants.SHOW_WELCOME, false))
                {
                    // 如果是第一次显示欢迎界面，那么让用户进入SetUpActivity进行设置，如果不是第一次了，那么就不再显示SetUpActivity
                    intent = new Intent(this, SetUpActivity.class);
                    editor.putBoolean(CommonConstants.SHOW_WELCOME, true);
                    editor.commit();
                    startActivity(intent);
                }
                else
                {
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                
                this.finish();
                break;
            case R.id.download:
                intent = new Intent(this, LoginActivity.class);
                // intent.putExtra(CommonConstants.IS_TEACHER, true);
                editor.putBoolean(CommonConstants.SHOW_WELCOME, true);
                editor.commit();
                startActivity(intent);
                this.finish();
                break;
            
            default:
                break;
        }
    }
    
}
