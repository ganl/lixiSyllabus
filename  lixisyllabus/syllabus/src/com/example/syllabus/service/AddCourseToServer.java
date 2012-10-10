package com.example.syllabus.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.example.syllabus.bean.Course;
import com.example.syllabus.db.CourseDao;
import com.example.syllabus.db.CourseDaoImpl;
import com.example.syllabus.utils.CommonConstants;
import com.example.syllabus.utils.HttpConnect;
import com.example.syllabus.utils.Urls;

public class AddCourseToServer extends Service
{
    
    private static final int UPDATE_COURSE = 1;
    
    private static final int INSERT_COURSE_TO_SERVER = 0;
    
    SharedPreferences preferences;
    
    private Course course;
    
    private boolean isTeacher = false;
    
    public static final String[] FIELDS = {"cName", "tName", "cAddress", "cStartWeek", "cEndWeek", "cWeekday",
        "courseIndex"};
    
    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
        preferences = CommonConstants.getMyPreferences(this);
        isTeacher = preferences.getBoolean(CommonConstants.IS_TEACHER, false);
    }
    
    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // TODO Auto-generated method stub
        course = (Course)intent.getSerializableExtra("course");
        int action = intent.getIntExtra("action", -1);
        
        System.out.println("---------------------------------service started");
        System.out.println("course is " + course.getcAddress());
        // this.course = course;
        String[] courseValues =
            {course.getcName(), course.gettName(), course.getcAddress(), Integer.toString(course.getcStartWeek()),
                Integer.toString(course.getcEndWeek()), Integer.toString(course.getcWeekday()),
                Integer.toString(course.getCourseIndex())};
        List<NameValuePair> values = new ArrayList<NameValuePair>();
        NameValuePair classidValue =
            new BasicNameValuePair(CommonConstants.CLASSID, preferences.getInt(CommonConstants.CLASSID, -1) + "");
        values.add(classidValue);
        for (int i = 0; i < courseValues.length; i++)
        {
            NameValuePair value = new BasicNameValuePair(FIELDS[i], URLEncoder.encode(courseValues[i]));
            values.add(value);
        }
        
        if (INSERT_COURSE_TO_SERVER == action)
        {
            new SendCourseToServerTask(values, this).execute("");
        }
        else
        {
            NameValuePair value = new BasicNameValuePair("courseid", course.getCourseid() + "");
            Log.i("AddCourseToServer", "courseid:" + course.getCourseid());
            values.add(value);
            new UpdateCourseTask(values, this).execute("");
        }
        
        return super.onStartCommand(intent, flags, startId);
    }
    
    public class SendCourseToServerTask extends AsyncTask<String, String, String>
    {
        private List<NameValuePair> pairs;
        
        private Service service;
        
        public SendCourseToServerTask(List<NameValuePair> values, Service service)
        {
            this.pairs = values;
            this.service = service;
        }
        
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String s = HttpConnect.postHttpString(Urls.getInsertCourse(), pairs);
                System.out.println(s);
                JSONObject obj = new JSONObject(s);
                int resultCode = obj.optInt("result");
                if (1 == resultCode)
                {
                    System.out.println("inserted");
                    course.setCourseid(obj.optInt("courseid"));
                    Log.i("AddCourseToServer", "courseid:" + obj.optInt("courseid"));
                    CourseDao dao = new CourseDaoImpl(AddCourseToServer.this);
                    Log.i("AddcourseTOServer", "course id:" + course.getId() + ",courseid:" + course.getCourseid());
                    dao.updateCourseid(course.getId(), course.getCourseid());
                }
                else if (2 == resultCode)
                {
                    System.out.println("course has been inserted already!");
                }
                else
                {
                    System.out.println("server is updating, please waiting...");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            return null;
        }
        
        @Override
        protected void onPostExecute(String result)
        {
            // TODO Auto-generated method stub
            service.stopSelf();
            super.onPostExecute(result);
        }
    }
    
    public class UpdateCourseTask extends AsyncTask<String, String, String>
    {
        private List<NameValuePair> pairs;
        
        private Service service;
        
        public UpdateCourseTask(List<NameValuePair> values, Service service)
        {
            this.pairs = values;
            this.service = service;
        }
        
        @Override
        protected String doInBackground(String... arg0)
        {
            // TODO Auto-generated method stub
            try
            {
                String s = HttpConnect.postHttpString(Urls.getUpdateCourse(), pairs);
                System.out.println(s);
                JSONObject obj = new JSONObject(s);
                int resultCode = obj.optInt("result");
                if (1 == resultCode)
                {
                    System.out.println("updated");
                    // course.setCourseid(obj.optInt("courseid"));
                    // CourseDao dao = new CourseDaoImpl(AddCourseToServer.this);
                    // dao.updateCourse(course);
                    
                }
                // else if (2 == resultCode)
                // {
                // System.out.println("course has been inserted already!");
                // }
                else
                {
                    System.out.println("server is updating, please waiting...");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            return null;
        }
        
        @Override
        protected void onPostExecute(String result)
        {
            // TODO Auto-generated method stub
            service.stopSelf();
            super.onPostExecute(result);
        }
        
    }
    
}
