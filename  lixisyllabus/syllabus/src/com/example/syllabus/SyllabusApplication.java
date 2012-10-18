package com.example.syllabus;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import cn.appmedia.adshelf.AdshelfManager;

import com.example.syllabus.utils.CommonConstants;

public class SyllabusApplication extends Application
{
    
    SharedPreferences preferences;
    
    public boolean isWeekHasBeenChanged = false;
    
    public boolean isDataHasBeenMotifyed = false;
    
    public int notificationID; // use to identify the notification, which is self-increasing
    
    private List<Activity> activityList = new LinkedList<Activity>();
    
    private static SyllabusApplication instance;
    
    private boolean isTeacher;
    
    public SyllabusApplication()
    {
    }
    
    public static SyllabusApplication getInstance()
    {
        if (null == instance)
        {
            instance = new SyllabusApplication();
        }
        return instance;
    }
    
    public void addActivity(Activity activity)
    {
        activityList.add(activity);
    }
    
    public void exitApplication()
    {
        for (Activity activity : activityList)
        {
            if (null != activity)
            {
                activity.finish();
            }
        }
        System.runFinalizersOnExit(true);
        System.exit(0);
    }
    
    // static
    // {
    // AdshelfManager.setAid(CommonConstants.AID);
    // }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        
        preferences = getSharedPreferences(CommonConstants.SHARED_NAME, 0);
        
        int oldWeekOfYear = preferences.getInt(CommonConstants.WEEK_IN_YEAR, 0);
        int currentWeekOfYear = CommonConstants.getCurrentWeekInYear();
        Editor editor = preferences.edit();
        if (0 != oldWeekOfYear)
        {
            int weekOfSemister =
                preferences.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER);
            weekOfSemister = weekOfSemister + (currentWeekOfYear - oldWeekOfYear);
            editor.putInt(CommonConstants.WEEKOFSEMISTER, weekOfSemister);
        }
        else
        {
            editor.putInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER);
        }
        editor.putInt(CommonConstants.WEEK_IN_YEAR, currentWeekOfYear);
        
        editor.commit();
        notificationID = 2;
    }
    
    public boolean isTeacher()
    {
        return isTeacher;
    }
    
    public void setTeacher(boolean isTeacher)
    {
        this.isTeacher = isTeacher;
    }
}
