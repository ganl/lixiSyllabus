package com.example.syllabus.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.syllabus.task.GetOneWeekCourseListTask;
import com.example.syllabus.utils.CommonConstants;

public class GetCourseFromServer extends Service
{
    
    private String universityName;
    
    private String departmentName;
    
    private String majorName;
    
    private String gradeNum;
    
    private String className;
    
    private String teacherName;
    
    private SharedPreferences preferences;
    
    private boolean isTeacher;
    
    private Handler handler;
    
    private boolean missionCompleted;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        preferences = CommonConstants.getMyPreferences(this);
        isTeacher = preferences.getBoolean(CommonConstants.IS_TEACHER, false);
        handler = new Handler()
        {
            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
    
    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        universityName = preferences.getString(CommonConstants.UNIVERSITY_NAME, null);
        if (isTeacher)
        {
            teacherName = preferences.getString(CommonConstants.TEACHER_NAME, null);
            GetOneWeekCourseListTask task = new GetOneWeekCourseListTask(this, handler);
            task.execute(universityName, teacherName);
        }
        else
        {
            departmentName = preferences.getString(CommonConstants.DEPARTMENT_NAME, null);
            majorName = preferences.getString(CommonConstants.MAJOR_NAME, null);
            gradeNum = preferences.getString(CommonConstants.GRADE_NUM, null);
            className = preferences.getString(CommonConstants.CLASS_NAME, null);
            
            GetOneWeekCourseListTask task = new GetOneWeekCourseListTask(this, handler);
            task.execute(universityName, departmentName, majorName, gradeNum, className);
        }
        return super.onStartCommand(intent, flags, startId);
    }
    
}
