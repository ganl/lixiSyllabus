package com.example.syllabus.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syllabus.R;
import com.example.syllabus.SyllabusApplication;
import com.example.syllabus.bean.Course;
import com.example.syllabus.db.CourseDao;
import com.example.syllabus.db.CourseDaoImpl;
import com.example.syllabus.db.UnUploadedCourseDao;
import com.example.syllabus.db.UnUploadedCourseDaoImpl;
import com.example.syllabus.service.AddCourseToServer;
import com.example.syllabus.utils.CommonConstants;
import com.example.syllabus.utils.HttpConnect;

/**
 * A class for adding, modifying course. You can add two course at once and you can change course whatever you want.
 * 
 * @author Administrator
 * 
 */
public class AddCourseActivity extends Activity implements OnClickListener, OnLongClickListener
{
    private static final int UPDATE_COURSE = 1;
    
    private static final int INSERT_COURSE_TO_SERVER = 0;
    
    private int dayOfWeek;
    
    private int courseIndex;
    
    private int secondCourseIndex = 1;
    
    private int secondDayOfWeek = 1;
    
    private TextView tvLeft;
    
    private TextView tvTitle;
    
    private TextView tvRightT;
    
    private TextView tvStartWeek;
    
    private TextView tvEndWeek;
    
    private TextView tvSecondStartWeek;
    
    private TextView tvSecondEndWeek;
    
    private TextView tvSecondWeekAndIndex;
    
    private TextView tvSecondDayOfWeek;
    
    private TextView tvSecondCourseIndex;
    
    private EditText etCourseName;
    
    private EditText etCourseAddress;
    
    private EditText etCourseTeacher;
    
    private TextView tvIsTwo; // whether to add another same course
    
    private EditText etSecondCourseAddress;
    
    private EditText etSecondCourseTeacher;
    
    private LinearLayout llSecondCourse;
    
    private String[] items = {"1", "2", "3", "4", "5", "6", "7", "8", "8", "9", "10", "11", "12", "13", "14", "15",
        "16", "17", "18", "19", "20"}; // items to be selected as week of semister
    
    int intStartWeek;
    
    int intEndWeek;
    
    int intSecondStartWeek;
    
    int intSecondEndWeek;
    
    private Course course;
    
    private Course secondCourse;
    
    private Long idOfLocalCourse;
    
    private SharedPreferences preferences;
    
    private boolean isTeacher;
    
    private boolean isLogined;
    
    private boolean isNetworkOn;
    
    private boolean isAdd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcourse);
        
        SyllabusApplication.getInstance().addActivity(this);
        
        preferences = CommonConstants.getMyPreferences(this);
        
        isTeacher = preferences.getBoolean(CommonConstants.IS_TEACHER, false);
        
        isLogined = preferences.getBoolean(CommonConstants.LOGINED, false);
        
        isNetworkOn = HttpConnect.isNetworkHolding(this);
        
        Intent intent = getIntent();
        
        // detect whether to add or modify the course
        isAdd = intent.getBooleanExtra(MainActivity.ACTION_ADD_COURSE, true);
        if (!isAdd)
        {
            idOfLocalCourse = intent.getLongExtra("id", -1);
            CourseDao dao = new CourseDaoImpl(this);
            course = dao.getCourseById(idOfLocalCourse);
            
            dayOfWeek = course.getcWeekday();
            courseIndex = course.getCourseIndex();
        }
        else
        {
            // else, the intent is to add a course
            dayOfWeek = intent.getIntExtra("dayOfWeek", 0);
            courseIndex = intent.getIntExtra("courseIndex", 0);
            course = new Course();
            if (isTeacher)
            {
                course.settName(CommonConstants.getMyPreferences(this).getString(CommonConstants.TEACHER_NAME, null));
            }
        }
        
        initViews();
    }
    
    private void initViews()
    {
        tvLeft = (TextView)findViewById(R.id.tvLeft);
        tvLeft.setText("返回");
        tvLeft.setOnClickListener(this);
        
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(CommonConstants.getStrFromWeekNum(dayOfWeek) + "第" + courseIndex + "节课");
        tvTitle.setOnClickListener(this);
        tvTitle.setOnLongClickListener(this);
        
        tvRightT = (TextView)findViewById(R.id.tvRightT);// , direction)
        tvRightT.setText("完成");
        tvRightT.setOnClickListener(this);
        
        tvStartWeek = (TextView)findViewById(R.id.startweek);
        tvStartWeek.setOnClickListener(this);
        tvEndWeek = (TextView)findViewById(R.id.endweek);
        tvEndWeek.setOnClickListener(this);
        
        tvSecondWeekAndIndex = (TextView)findViewById(R.id.secondweekandindex);
        tvSecondWeekAndIndex.setOnClickListener(this);
        
        tvSecondDayOfWeek = (TextView)findViewById(R.id.secondcoursedayofweek);
        tvSecondCourseIndex = (TextView)findViewById(R.id.secondcourseindex);
        
        tvSecondDayOfWeek.setOnClickListener(this);
        tvSecondCourseIndex.setOnClickListener(this);
        
        StringBuilder sb;
        if (isAdd)
        {
            sb = new StringBuilder(tvStartWeek.getText().toString());
            sb.append("2");
            tvStartWeek.setText(sb.toString());
            
            sb = new StringBuilder(tvEndWeek.getText().toString());
            sb.append("18");
            tvEndWeek.setText(sb.toString());
        }
        else
        {
            sb = new StringBuilder(tvStartWeek.getText());
            sb.append(course.getcStartWeek());
            tvStartWeek.setText(sb);
            
            sb = new StringBuilder(tvEndWeek.getText());
            sb.append(course.getcEndWeek());
            tvEndWeek.setText(sb);
        }
        
        tvSecondStartWeek = (TextView)findViewById(R.id.secondcoursestartweek);
        tvSecondStartWeek.setOnClickListener(this);
        tvSecondEndWeek = (TextView)findViewById(R.id.secondcourseendweek);
        tvSecondEndWeek.setOnClickListener(this);
        
        etCourseName = (EditText)findViewById(R.id.coursename);
        if (null != course.getcName())
        {
            etCourseName.setText(course.getcName());
        }
        
        etCourseAddress = (EditText)findViewById(R.id.courseaddress);
        if (null != course.getcAddress())
        {
            etCourseAddress.setText(course.getcAddress());
        }
        
        tvIsTwo = (TextView)findViewById(R.id.istwo);
        if (isAdd)
        {
            tvIsTwo.setVisibility(View.VISIBLE);
        }
        else
        {
            tvIsTwo.setVisibility(View.GONE);
        }
        
        tvIsTwo.setOnClickListener(this);
        
        llSecondCourse = (LinearLayout)findViewById(R.id.secondCourse);
        
        ArrayAdapter<String> weekNumAdapter =
            new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CommonConstants.DAYOFWEEKS_INCHN);
        weekNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        ArrayAdapter<Integer> courseIndexAdapter =
            new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, CommonConstants.COURSEINDEX);
        courseIndexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        etSecondCourseAddress = (EditText)findViewById(R.id.secondcourseaddress);
        if (!isTeacher)
        {
            
            etCourseTeacher = (EditText)findViewById(R.id.courseteacher);
            if (null != course.gettName())
            {
                etCourseTeacher.setText(course.gettName());
            }
            etSecondCourseTeacher = (EditText)findViewById(R.id.secondcourseteacher);
        }
        else
        {
            LinearLayout ll = (LinearLayout)findViewById(R.id.addteacher);
            LinearLayout ll2 = (LinearLayout)findViewById(R.id.addsecondteacher);
            
            ll.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            
        }
        
    }
    
    private void showDialogToPick(TextView tv, boolean startOrEnd)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        if (startOrEnd)
        {
            builder.setTitle("请选择课程起始周");
        }
        else
        {
            builder.setTitle("请选择课程结束周");
        }
        builder.setItems(items, new MyDialogListener(tv));
        builder.create().show();
    }
    
    public void onClick(View arg0)
    {
        switch (arg0.getId())
        {
            case R.id.tvLeft:
                this.finish();
                break;
            case R.id.tvRightT:
                if (null == etCourseName.getText() || "".equals((etCourseName).getText().toString()))
                {
                    Toast.makeText(this, "课程名未填写", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    course.setcName(etCourseName.getText().toString());
                    
                    if (null != etCourseAddress.getText() && !"".equals((etCourseAddress).getText()))
                    {
                        course.setcAddress(etCourseAddress.getText().toString());
                    }
                    
                    if (!isTeacher && null != etCourseTeacher.getText() && !"".equals((etCourseTeacher).getText()))
                    {
                        course.settName(etCourseTeacher.getText().toString());
                    }
                    
                    course.setcStartWeek(getNumOfTV(tvStartWeek));
                    course.setcEndWeek(getNumOfTV(tvEndWeek));
                    course.setcWeekday(dayOfWeek);
                    course.setCourseIndex(courseIndex);
                    if (llSecondCourse.getVisibility() != View.GONE)
                    {
                        if (null == etCourseName.getText() || "".equals((etCourseName).getText().toString()))
                        {
                            Toast.makeText(this, "课程名未填写", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            secondCourse.setcName(etCourseName.getText().toString());
                        }
                        secondCourse.setcStartWeek(getNumOfTV(tvSecondStartWeek));
                        secondCourse.setcEndWeek(getNumOfTV(tvSecondEndWeek));
                        if (!isTeacher && null != etSecondCourseTeacher.getText()
                            && !"".equals((etSecondCourseTeacher).getText()))
                        {
                            secondCourse.settName(etSecondCourseTeacher.getText().toString());
                        }
                        if (null != etSecondCourseAddress.getText() && !"".equals((etSecondCourseAddress).getText()))
                        {
                            secondCourse.setcAddress(etSecondCourseAddress.getText().toString());
                        }
                        secondCourse.setcWeekday(secondDayOfWeek);
                        secondCourse.setCourseIndex(secondCourseIndex);
                    }
                    CourseDao dao = new CourseDaoImpl(this);
                    UnUploadedCourseDao unDao = new UnUploadedCourseDaoImpl(this);
                    if (isAdd)
                    {
                        
                        // 将课程添加入未同步列表，如果同步成功，则从列表中删除
                        
                        long id = dao.addCourse(course, isTeacher);
                        
                        unDao.addCourseToUn(id, CommonConstants.UNDO_ACTION_ADD);
                        
                        addCourseToServer(course, id, INSERT_COURSE_TO_SERVER);
                        
                        // Intent intent = new Intent();
                        // intent.putExtra("course", course);
                        // intent.putExtra("action", INSERT_COURSE_TO_SERVER);
                        // if (0 != id)
                        // {
                        // course.setId(id);
                        // intent.setClass(this, AddCourseToServer.class);
                        // // intent.putStringArrayListExtra(name, value)Extra("course", course);
                        
                        // }
                        if (null != secondCourse)
                        {
                            long secondid = dao.addCourse(secondCourse, isTeacher);
                            unDao.addCourseToUn(secondid, CommonConstants.UNDO_ACTION_ADD);
                            addCourseToServer(secondCourse, secondid, INSERT_COURSE_TO_SERVER);
                        }
                    }
                    else
                    {
                        dao.updateCourse(course);
                        unDao.addCourseToUn(idOfLocalCourse, CommonConstants.UNDO_ACTION_UPDATE);
                        // 将修改课程添加入为同步列表，如果同步陈宫，从列表中删除
                        
                        addCourseToServer(course, idOfLocalCourse, UPDATE_COURSE);
                    }
                    ((SyllabusApplication)getApplication()).isDataHasBeenMotifyed = true;
                    this.finish();
                    
                }
                break;
            case R.id.tvTitle:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog dialog = null;
                builder.setItems(R.array.CourseIndex, new DialogInterface.OnClickListener()
                {
                    
                    public void onClick(DialogInterface dialog, int which)
                    {
                        StringBuilder text = new StringBuilder(tvTitle.getText().subSequence(0, 2));
                        text.append(getResources().getStringArray(R.array.CourseIndex)[which]);
                        tvTitle.setText(text);
                        courseIndex = which + 1;
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.startweek:
                showDialogToPick(tvStartWeek, true);
                break;
            case R.id.endweek:
                showDialogToPick(tvEndWeek, false);
                break;
            case R.id.secondcoursestartweek:
                showDialogToPick(tvSecondStartWeek, true);
                break;
            case R.id.secondcourseendweek:
                showDialogToPick(tvSecondEndWeek, false);
                break;
            case R.id.secondweekandindex:
            case R.id.secondcoursedayofweek:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setItems(CommonConstants.DAYOFWEEKS_INCHN, new DialogInterface.OnClickListener()
                {
                    
                    public void onClick(DialogInterface dialog, int which)
                    {
                        tvSecondDayOfWeek.setText(CommonConstants.DAYOFWEEKS_INCHN[which]);
                        secondDayOfWeek = which + 1;
                    }
                });
                builder2.create().show();
                break;
            case R.id.secondcourseindex:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setItems(R.array.CourseIndex, new DialogInterface.OnClickListener()
                {
                    
                    public void onClick(DialogInterface dialog, int which)
                    {
                        tvSecondCourseIndex.setText(getResources().getStringArray(R.array.CourseIndex)[which]);
                        secondCourseIndex = which + 1;
                    }
                });
                builder3.create().show();
                break;
            case R.id.istwo:
                if (View.GONE == llSecondCourse.getVisibility())
                {
                    secondCourse = new Course();
                    secondCourse.settName(CommonConstants.getMyPreferences(this)
                        .getString(CommonConstants.TEACHER_NAME, null));
                    tvIsTwo.setBackgroundResource(R.drawable.btn_down_bcg);
                    llSecondCourse.setVisibility(View.VISIBLE);
                    
                    etSecondCourseAddress.setText(etCourseAddress.getText());
                    if (!isTeacher)
                    {
                        etSecondCourseTeacher.setText(etCourseTeacher.getText());
                    }
                    
                    tvSecondStartWeek.setText(tvStartWeek.getText());
                    tvSecondEndWeek.setText(tvEndWeek.getText());
                }
                else
                {
                    tvIsTwo.setBackgroundResource(R.drawable.btn_bcg);// )
                    llSecondCourse.setVisibility(View.GONE);
                    secondCourse = null;
                }
            default:
                break;
        }
    }
    
    /**
     * @param id
     */
    private void addCourseToServer(Course course, long id, int ACTION)
    {
        if (isLogined && isNetworkOn && 0 != id)
        {
            Intent intent = new Intent();
            course.setId(id);
            intent.putExtra("course", course);
            intent.putExtra("action", ACTION);
            intent.setClass(this, AddCourseToServer.class);
            startService(intent);
        }
        else if (id == 0)
        {
            Toast.makeText(this, "此课程已存在，请勿重复添加同一课程", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 
     */
    private int getNumOfTV(TextView tv)
    {
        // int size = tv.getText().toString().length();
        String[] items = tv.getText().toString().split("：");
        int num = Integer.parseInt(items[1]);
        return num;
    }
    
    class MyDialogListener implements DialogInterface.OnClickListener
    {
        private TextView tv;
        
        public MyDialogListener(TextView tvStartWeek)
        {
            tv = tvStartWeek;
        }
        
        public void onClick(DialogInterface dialog, int which)
        {
            int index = tv.getText().toString().indexOf("：");
            Log.i("AddCourseActivity", "index of :" + index);
            Log.i("AddCourseActivity", "text of tv is " + tv.getText().toString());
            String subString = tv.getText().toString().substring(0, index + 1);
            Log.i("AddCourseActivity", "subString of textview is " + subString);
            tv.setText(subString + items[which]);
        }
    }
    
    public boolean onLongClick(View arg0)
    {
        // TODO Auto-generated method stub
        switch (arg0.getId())
        {
            case R.id.tvTitle:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择周几");
                builder.setItems(CommonConstants.DAYOFWEEKS_INCHN, new DialogInterface.OnClickListener()
                {
                    
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Auto-generated method stub
                        dayOfWeek = which + 1;
                        tvTitle.setText(CommonConstants.getStrFromWeekNum(dayOfWeek) + "第" + courseIndex + "节课");
                    }
                });
                builder.create().show();
                break;
            
            default:
                break;
        }
        return true;
    }
    
}
