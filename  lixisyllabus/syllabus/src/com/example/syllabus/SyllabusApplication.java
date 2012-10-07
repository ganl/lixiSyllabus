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
    
    public int notificationID;
    
    private List<Activity> activityList = new LinkedList<Activity>();
    
    private static SyllabusApplication instance;
    
    private SyllabusApplication()
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
        System.exit(0);
    }
    
    static
    {
        AdshelfManager.setAid(CommonConstants.AID);
    }
    
    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        
        preferences = getSharedPreferences(CommonConstants.SHARED_NAME, 0);
        
        int oldWeekOfYear = preferences.getInt(CommonConstants.WEEK_IN_YEAR, 0);
        int currentWeekOfYear = CommonConstants.getCurrentWeekInYear();
        Editor editor = preferences.edit();
        if (0 != oldWeekOfYear)
        {
            editor.putInt(CommonConstants.WEEKOFSEMISTER,
                preferences.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER)
                    + (currentWeekOfYear - oldWeekOfYear));
        }
        else
        {
            editor.putInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER);
        }
        editor.putInt(CommonConstants.WEEK_IN_YEAR, currentWeekOfYear);
        // if (preferences.getBoolean(CommonConstants.IS_FIRST_RUN, true))
        // {
        // editor.putBoolean(CommonConstants.IS_FIRST_RUN, true);
        // }
        
        editor.commit();
        notificationID = 2;
    }
}
