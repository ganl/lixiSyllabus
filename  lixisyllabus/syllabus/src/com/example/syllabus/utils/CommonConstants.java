package com.example.syllabus.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;

import com.example.syllabus.R;
import com.example.syllabus.bean.Course;
import com.example.syllabus.bean.Teacher;
import com.example.syllabus.db.CourseDao;
import com.example.syllabus.db.CourseDaoImpl;

public class CommonConstants
{
    public static final String AID = "27b4c4fd30fc5aac";
    
    public static final String IS_SETUP_ALREADY = "isSetUpAlready";
    
    public static final String IS_TEACHER = "isTeacher";
    
    public static final String SKIPPED = "skipped";
    
    public static final String SHOW_WELCOME = "showWelcome";
    
    public static final String CLASSID = "classid";
    
    public static final String IS_FIRST_RUN = "isFirstRun";
    
    public static final String SERVER_URL = "http://192.168.0.1/";
    
    public static final String MAJOR_NAME = "majorName";
    
    public static final String UNIVERSITY_NAME = "universityName";
    
    public static final String CLASS_NAME = "className";
    
    public static final String GRADE_NUM = "gradeNum";
    
    public static final String DEPARTMENT_NAME = "departmentName";
    
    public static final String[] STUDENT_INFORMATION = {UNIVERSITY_NAME, DEPARTMENT_NAME, MAJOR_NAME, GRADE_NUM,
        CLASS_NAME};
    
    public static final String TEACHER_NAME = "teacherName";
    
    public static final String[] TEACHER_INFORMATION = {UNIVERSITY_NAME, TEACHER_NAME};
    
    public static final String LOGINED = "Logined";
    
    public static final String WEEK_IN_YEAR = "WeekInYear";
    
    public static final int DEFAULT_WEEKOFSEMISTER = 1;
    
    public static final String WEEKOFSEMISTER = "weekOfSemister";
    
    public static final String SHIELDED = "shielded";
    
    public static final String FIRST_CLASS_START = "firstClassStart"; // used for preferences
    
    public static final String FIRST_CLASS_START_TIME = "07:20"; // default time of uppper one
    
    public static final String FIRST_CLASS_END = "firstClassEnd";
    
    public static final String FIRST_CLASS_END_TIME = "09:00";
    
    public static final String SECOND_CLASS_START = "secondClassStart";
    
    public static final String SECOND_CLASS_START_TIME = "09:20";
    
    public static final String SECOND_CLASS_END = "secondClassEnd";
    
    public static final String SECOND_CLASS_END_TIME = "11:00";
    
    public static final String THIRD_CLASS_START = "thirdClassStart";
    
    public static final String THIRD_CLASS_START_TIME = "13:30";
    
    public static final String THIRD_CLASS_END = "thirdClassEnd";
    
    public static final String THIRD_CLASS_END_TIME = "15:15";
    
    public static final String FOURTH_CLASS_START = "fourthClassStart";
    
    public static final String FOURTH_CLASS_START_TIME = "15:30";
    
    public static final String FOURTH_CLASS_END = "fourthClassEnd";
    
    public static final String FOURTH_CLASS_END_TIME = "17:00";
    
    public static final String FIFTH_CLASS_START = "fifthClassStart";
    
    public static final String FIFTH_CLASS_START_TIME = "18:20";
    
    public static final String FIFTH_CLASS_END = "fifthClassEnd";
    
    public static final String FIFTH_CLASS_END_TIME = "20:00";
    
    public static final String SIXTH_CLASS_START = "sixthClassStart";
    
    public static final String SIXTH_CLASS_START_TIME = "20:20";
    
    public static final String SIXTH_CLASS_END = "sixthClassEnd";
    
    public static final String SIXTH_CLASS_END_TIME = "22:00";
    
    public final static String SHARED_NAME = "Course";
    
    public final static int[] DAYOFWEEKS_INNUMBER = {1, 2, 3, 4, 5, 6, 7};
    
    public final static String[] DAYOFWEEKS_INCHN = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    
    public final static String[] DAYOFWEEKS_INENG = {"Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"};
    
    public final static Integer[] COURSEINDEX = {1, 2, 3, 4, 5, 6};
    
    public final static String[] WEEKOFSEMISTER_INNUMBER = {"0", Integer.toString(DEFAULT_WEEKOFSEMISTER), "2", "3",
        "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    
    public final static String[] COURSEINDEXTIME = {FIRST_CLASS_START_TIME + " - " + FIRST_CLASS_END_TIME,
        SECOND_CLASS_START_TIME + " - " + SECOND_CLASS_END_TIME, THIRD_CLASS_START_TIME + " - " + THIRD_CLASS_END_TIME,
        FOURTH_CLASS_START_TIME + " - " + FOURTH_CLASS_END_TIME, FIFTH_CLASS_START_TIME + " - " + FIFTH_CLASS_END_TIME,
        SIXTH_CLASS_START_TIME + " - " + SIXTH_CLASS_END_TIME};
    
    public final static String[] STR_START_TIME = {FIRST_CLASS_START, SECOND_CLASS_START, THIRD_CLASS_START,
        FOURTH_CLASS_START, FIFTH_CLASS_START, SIXTH_CLASS_START};
    
    public final static String[] STARTTIME = {FIRST_CLASS_START_TIME, SECOND_CLASS_START_TIME, THIRD_CLASS_START_TIME,
        FOURTH_CLASS_START_TIME, FIFTH_CLASS_START_TIME, SIXTH_CLASS_START_TIME};
    
    public final static String[] ENDTIME = {FIRST_CLASS_END_TIME, SECOND_CLASS_END_TIME, THIRD_CLASS_END_TIME,
        FOURTH_CLASS_END_TIME, FIFTH_CLASS_END_TIME, SIXTH_CLASS_END_TIME};
    
    public final static String[] STR_END_TIME = {FIRST_CLASS_END, SECOND_CLASS_END, THIRD_CLASS_END, FOURTH_CLASS_END,
        FIFTH_CLASS_END, SIXTH_CLASS_END};
    
    public final static int[] COURSEINDEX_IMAGE = {R.drawable.courseone, R.drawable.coursetwo, R.drawable.coursethree,
        R.drawable.coursefour, R.drawable.coursefive, R.drawable.coursesix};
    
    public final static int[] WEEKINDEXIMAGE = {R.drawable.icon_monday, R.drawable.icon_tuesday,
        R.drawable.icon_wednesday, R.drawable.icon_thurday, R.drawable.icon_friday, R.drawable.icon_saturday,
        R.drawable.icon_sunday};
    
    public final static int[] DAYOFWEEK_IMAGE = {R.drawable.icon_monday, R.drawable.icon_thurday,
        R.drawable.icon_wednesday, R.drawable.icon_tuesday, R.drawable.icon_friday, R.drawable.icon_saturday,
        R.drawable.icon_sunday};
    
    public static final String IS_ACCELEREMETER_SUPPORTED = "isAccelermeterSupported";
    
    public static int defaultRingerMode;
    
    /**
     * 从中文星期得到数字，如周六得6
     * 
     * @param str
     * @return
     */
    public static int getWeekNumFromStr(String str)
    {
        
        for (int i = 0; i < DAYOFWEEKS_INCHN.length; i++)
        {
            if (DAYOFWEEKS_INCHN[i].equals(str))
            {
                return DAYOFWEEKS_INNUMBER[i];
            }
        }
        return 0;
    }
    
    public static int getDayOfWeekFromEng(String str)
    {
        for (int i = 0; i < DAYOFWEEKS_INENG.length; i++)
        {
            if (DAYOFWEEKS_INENG[i].contains(str))
            {
                return DAYOFWEEKS_INNUMBER[i];
            }
        }
        return 0;
    }
    
    /**
     * 从数字星期得中文，如6得周六
     * 
     * @param weekNum
     * @return
     */
    public static String getStrFromWeekNum(int weekNum)
    {
        for (int i = 0; i < DAYOFWEEKS_INNUMBER.length; i++)
        {
            if (weekNum == DAYOFWEEKS_INNUMBER[i])
            {
                return DAYOFWEEKS_INCHN[i];
            }
        }
        return null;
        
    }
    
    /**
     * 获得当前时间
     * 
     * @return
     */
    public static String getCurrentHourOfDay()
    {
        String hourString;
        Date curHour = new Date(System.currentTimeMillis());
        SimpleDateFormat formatForHour = new SimpleDateFormat("kk");
        hourString = formatForHour.format(curHour);
        return hourString;
    }
    
    /**
     * 获得当前分钟
     * 
     * @return
     */
    public static String getCurrentMinuteOfHour()
    {
        String minute;
        Date curminute = new Date(System.currentTimeMillis());
        SimpleDateFormat formatForMinute = new SimpleDateFormat("mm");
        minute = formatForMinute.format(curminute);
        return minute;
    }
    
    /**
     * 确定某个时间是否是在上课时间中
     * 
     * @param currentTime
     * @param context
     * @return
     */
    public static boolean isDuringCourseTime(Date currentTime, Context context)
    {
        
        SharedPreferences share = CommonConstants.getMyPreferences(context);
        int weekOfSemister = share.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER);
        
        SimpleDateFormat formatter = new SimpleDateFormat("EE:kk:mm");
        String current = formatter.format(currentTime);
        Log.i(context.toString(), "current time is " + current);
        String[] items = current.split(":");
        String dayOfWeek = items[0];// 星期几
        int day = CommonConstants.getWeekNumFromStr(dayOfWeek);
        
        int hour = Integer.parseInt(items[1]);
        int minute = Integer.parseInt(items[2]);
        
        CourseDao dao = new CourseDaoImpl(context);
        List<Course> list = dao.getDayCourse(weekOfSemister, day);
        
        if (!list.isEmpty())
        {
            Log.i(context.toString(), "list of today is " + list.size());
            for (int i = 0; i < list.size(); i++)
            {
                int courseIndex = list.get(i).getCourseIndex();
                String startTime =
                    share.getString(CommonConstants.STR_START_TIME[courseIndex - 1],
                        CommonConstants.STARTTIME[courseIndex - 1]);
                String[] time = startTime.split(":");
                int hs = Integer.parseInt(time[0]);
                int ms = Integer.parseInt(time[1]);
                
                String endTime =
                    share.getString(CommonConstants.STR_END_TIME[courseIndex - 1],
                        CommonConstants.ENDTIME[courseIndex - 1]);
                String[] timee = endTime.split(":");
                int he = Integer.parseInt(timee[0]);
                int me = Integer.parseInt(timee[1]);
                
                if (hour >= hs && hour <= he)
                {
                    if (hour > hs && hour < he)
                    {
                        return true;
                    }
                    if (hour == hs)
                    {
                        if (minute > ms)
                        {
                            return true;
                        }
                    }
                    if (hour == he)
                    {
                        if (minute < me)
                        {
                            return true;
                        }
                    }
                }
            }
            
            return false;
        }
        
        return false;
        
    }
    
    /**
     * 获取今日是周几
     * 
     * @return
     */
    public static String getCurrentDayOfWeek()
    {
        SimpleDateFormat formatForWeek = new SimpleDateFormat("EEE");
        
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatForWeek.format(curDate);
        return str;
    }
    
    /**
     * 震动手机
     * 
     * @param context
     * @param milliseconds
     */
    public static void vibratePhone(Context context, int milliseconds)
    {
        Vibrator vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }
    
    public static SharedPreferences getMyPreferences(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_NAME, 0);
        return preferences;
    }
    
    /**
     * 获取当前周是一年中的第几周
     * 
     * @return
     */
    public static int getCurrentWeekInYear()
    {
        // TODO Auto-generated method stub
        SimpleDateFormat formatForWeekInYear = new SimpleDateFormat("ww");
        Date date = new Date(System.currentTimeMillis());
        String weekInYear = formatForWeekInYear.format(date);
        return Integer.parseInt(weekInYear);
    }
    
    /**
     * 获取当前周是一学期中的第几周
     * 
     * @param context
     * @return
     */
    public static int getCurrentWeekOfSemister(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_NAME, 0);
        
        return preferences.getInt(WEEKOFSEMISTER, DEFAULT_WEEKOFSEMISTER);
    }
}
