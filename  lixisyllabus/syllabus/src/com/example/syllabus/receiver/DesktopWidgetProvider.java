package com.example.syllabus.receiver;

import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.syllabus.R;
import com.example.syllabus.activity.MainActivity;
import com.example.syllabus.bean.Course;
import com.example.syllabus.db.CourseDao;
import com.example.syllabus.db.CourseDaoImpl;
import com.example.syllabus.utils.CommonConstants;

public class DesktopWidgetProvider extends AppWidgetProvider
{
    public static final String ACTION_UPDATE_COURSE = "com.example.syllabus.updatecourse";
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // TODO Auto-generated method stub
        final int N = appWidgetIds.length;
        
        for (int i = 0; i < N; i++)
        {
            int appWidgetId = appWidgetIds[i];
            
            CourseDao dao = new CourseDaoImpl(context);
            int weekOfSemister = CommonConstants.getCurrentWeekOfSemister(context);
            int dayOfWeek = CommonConstants.getWeekNumFromStr(CommonConstants.getCurrentDayOfWeek());
            Log.i(context.toString(), weekOfSemister + "," + dayOfWeek);
            List<Course> list =
                dao.getDayCourse(weekOfSemister,
                    dayOfWeek,
                    CommonConstants.getMyPreferences(context).getBoolean(CommonConstants.IS_TEACHER, false));
            
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            /**
             * ≥ı ºªØ
             */
            remoteViews.setImageViewResource(R.id.dayimage, CommonConstants.DAYOFWEEK_IMAGE[dayOfWeek - 1]);
            remoteViews.setTextViewText(R.id.firstcoursename, "");
            remoteViews.setTextViewText(R.id.secondcoursename, "");
            remoteViews.setTextViewText(R.id.thirdcoursename, "");
            remoteViews.setTextViewText(R.id.fourthcoursename, "");
            remoteViews.setTextViewText(R.id.fifthcoursename, "");
            remoteViews.setTextViewText(R.id.sixthcoursename, "");
            remoteViews.setTextViewText(R.id.firstcourseroom, "");
            remoteViews.setTextViewText(R.id.secondcourseroom, "");
            remoteViews.setTextViewText(R.id.thirdcourseroom, "");
            remoteViews.setTextViewText(R.id.fourthcourseroom, "");
            remoteViews.setTextViewText(R.id.fifthcourseroom, "");
            remoteViews.setTextViewText(R.id.sixthcourseroom, "");
            for (Course course : list)
            {
                switch (course.getCourseIndex())
                {
                    case 1:
                        remoteViews.setTextViewText(R.id.firstcoursename, course.getcName());
                        remoteViews.setTextViewText(R.id.firstcourseroom, course.getcAddress());
                        break;
                    case 2:
                        remoteViews.setTextViewText(R.id.secondcoursename, course.getcName());
                        remoteViews.setTextViewText(R.id.secondcourseroom, course.getcAddress());
                        break;
                    case 3:
                        remoteViews.setTextViewText(R.id.thirdcoursename, course.getcName());
                        remoteViews.setTextViewText(R.id.thirdcourseroom, course.getcAddress());
                        break;
                    case 4:
                        remoteViews.setTextViewText(R.id.fourthcoursename, course.getcName());
                        remoteViews.setTextViewText(R.id.fourthcourseroom, course.getcAddress());
                        break;
                    case 5:
                        remoteViews.setTextViewText(R.id.fifthcoursename, course.getcName());
                        remoteViews.setTextViewText(R.id.fifthcourseroom, course.getcAddress());
                        break;
                    case 6:
                        remoteViews.setTextViewText(R.id.sixthcoursename, course.getcName());
                        remoteViews.setTextViewText(R.id.sixthcourseroom, course.getcAddress());
                        break;
                    default:
                        break;
                }
            }
            
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle value = new Bundle();
            
            value.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            Log.i(context.toString(), "appWidgetId is " + appWidgetId);
            
            intent.putExtras(value);
            intent.setData(ContentUris.withAppendedId(Uri.EMPTY, appWidgetId));
            // intent.putExtra("weekOfSemister", weekOfSemister);
            // intent.putExtra("dayOfWeek", dayOfWeek);
            Log.i(context.toString(), "dayOfWeek is " + dayOfWeek);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            
            remoteViews.setOnClickPendingIntent(R.id.dayofweek, pendingIntent);
            
            // intent = new Intent();
            // intent.setAction(ACTION_UPDATE_COURSE);
            // if (dayOfWeek - 1 < 1)
            // {
            // dayOfWeek = 1;
            // }
            // dayOfWeek = dayOfWeek - 1 < 1 ? 1 : (dayOfWeek - 1);
            // intent.putExtra("dayOfWeek", dayOfWeek);
            //
            // intent.putExtra("weekOfSemister", weekOfSemister);
            // pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            // remoteViews.setOnClickPendingIntent(R.id.turnleft, pendingIntent);
            // AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            // ComponentName provider = new ComponentName(context, DesktopWidgetProvider.class);
            // appWidgetManager.updateAppWidget(provider, remoteViews);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    
    @Override
    public void onEnabled(Context context)
    {
        super.onEnabled(context);
    }
    
    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
    }
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
    }
    
    @Override
    public void onDisabled(Context context)
    {
        super.onDisabled(context);
    }
}
