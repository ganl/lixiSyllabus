package com.example.syllabus.task;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.syllabus.bean.Course;
import com.example.syllabus.db.CourseDao;
import com.example.syllabus.db.CourseDaoImpl;
import com.example.syllabus.utils.CommonConstants;
import com.example.syllabus.utils.HttpConnect;
import com.example.syllabus.utils.Urls;

public class GetOneWeekCourseListTask extends AsyncTask<String, String, List<Course>>
{
    
    private static final String RESULT_OK_NO_COURSES = "2";
    
    private static final String RESULT_OK = "1";
    
    private Context context;
    
    private Handler handler;
    
    private int classid;
    
    private boolean isTeacher;
    
    public GetOneWeekCourseListTask(Context context, Handler handler)
    {
        this.context = context;
        this.handler = handler;
    }
    
    @Override
    protected List<Course> doInBackground(String... params)
    {
        List<Course> courses = null;
        if (5 == params.length)
        {
            isTeacher = false;
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (int i = 0; i < params.length; i++)
            {
                NameValuePair pair =
                    new BasicNameValuePair(CommonConstants.STUDENT_INFORMATION[i], URLEncoder.encode(params[i]));// params[i]);
                list.add(pair);
            }
            courses = new ArrayList<Course>();
            try
            {
                System.out.println(Urls.getStudentLoginUrl());
                String s = HttpConnect.postHttpString(Urls.getStudentLoginUrl(), list);
                System.out.println(s);
                
                // String s =
                // "{'result':'OK','courseInformation':[{'cAddress':'381','tName':'潘地林','cName':'数据结构','cStartWeek':2,'id':2,'courseIndex':1,'cWeekday':1,'cEndWeek':8},{'cAddress':'','tName':'','cName':'呵呵发发','cStartWeek':2,'id':4,'courseIndex':1,'cWeekday':1,'cEndWeek':8},{'cAddress':'好好干','tName':'不vv他','cName':'就好好干','cStartWeek':2,'id':9,'courseIndex':1,'cWeekday':1,'cEndWeek':8},{'cAddress':'108','tName':'潘地林','cName':'操作系统','cStartWeek':2,'id':11,'courseIndex':5,'cWeekday':1,'cEndWeek':8},{'cAddress':'301','tName':'露露','cName':'离散数学','cStartWeek':2,'id':13,'courseIndex':1,'cWeekday':2,'cEndWeek':8},{'cAddress':'355','tName':'刘级','cName':'实变函数','cStartWeek':2,'id':15,'courseIndex':3,'cWeekday':2,'cEndWeek':8},{'cAddress':'355','tName':'刘级','cName':'实变函数','cStartWeek':2,'id':14,'courseIndex':1,'cWeekday':3,'cEndWeek':8},{'cAddress':'301','tName':'露露','cName':'离散数学','cStartWeek':2,'id':12,'courseIndex':1,'cWeekday':4,'cEndWeek':8},{'cAddress':'381','tName':'潘地林','cName':'数据结构','cStartWeek':2,'id':1,'courseIndex':1,'cWeekday':5,'cEndWeek':8},{'cAddress':'好家伙','tName':'换个','cName':'呵呵发发','cStartWeek':2,'id':3,'courseIndex':1,'cWeekday':5,'cEndWeek':8},{'cAddress':'个好好干','tName':'骨灰盒','cName':'广告费不','cStartWeek':2,'id':5,'courseIndex':1,'cWeekday':5,'cEndWeek':8},{'cAddress':'刚刚给','tName':'广告费','cName':'好好干','cStartWeek':1,'id':6,'courseIndex':1,'cWeekday':5,'cEndWeek':8},{'cAddress':'好好干','tName':'不vv他','cName':'就好好干','cStartWeek':2,'id':8,'courseIndex':1,'cWeekday':5,'cEndWeek':8},{'cAddress':'过过瘾','tName':'更好','cName':'雍和宫','cStartWeek':2,'id':7,'courseIndex':2,'cWeekday':5,'cEndWeek':8},{'cAddress':'108','tName':'潘地林','cName':'操作系统','cStartWeek':2,'id':10,'courseIndex':2,'cWeekday':5,'cEndWeek':8},{'cAddress':'503','tName':'刘科学','cName':'图像处理','cStartWeek':2,'id':16,'courseIndex':1,'cWeekday':6,'cEndWeek':8},{'cAddress':'503','tName':'刘科学','cName':'图像处理','cStartWeek':2,'id':17,'courseIndex':1,'cWeekday':7,'cEndWeek':8}]}";
                
                JSONObject obj = new JSONObject(s);
                String result = obj.optString("result");
                
                classid = obj.optInt(CommonConstants.CLASSID);
                SharedPreferences preferences = CommonConstants.getMyPreferences(context);
                Editor editor = preferences.edit();
                editor.putInt(CommonConstants.CLASSID, classid);
                editor.commit();
                if (RESULT_OK_NO_COURSES.equals(result) || "0".equals(result))
                {
                    ((Activity)context).runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            handler.sendEmptyMessage(1);
                            Toast.makeText(context, "服务器无您班级课程，请添加。", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                
                if (RESULT_OK.equals(result))
                {
                    JSONArray array = obj.optJSONArray("courses");
                    Log.i("GetOneWeekCoursesActivity", "classid " + classid);
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject inforOfCourse = array.getJSONObject(i);
                        Course course = new Course(inforOfCourse);
                        course.setcName(URLDecoder.decode(course.getcName()));
                        course.settName(URLDecoder.decode(course.gettName()));
                        course.setcAddress(URLDecoder.decode(course.getcAddress()));
                        
                        courses.add(course);
                        CourseDao dao = new CourseDaoImpl(context);
                        dao.addCourse(course, false);
                    }
                    
                    handler.sendEmptyMessage(2);
                }
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ((Activity)context).runOnUiThread(new Runnable()
                {
                    
                    public void run()
                    {
                        handler.sendEmptyMessage(1);
                        Toast.makeText(context, "网络出现问题，请稍候重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else
        {
            isTeacher = true;
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (int i = 0; i < params.length; i++)
            {
                NameValuePair pair =
                    new BasicNameValuePair(CommonConstants.TEACHER_INFORMATION[i], URLEncoder.encode(params[i]));// params[i]);
                list.add(pair);
            }
            courses = new ArrayList<Course>();
            try
            {
                System.out.println(Urls.getTeacherLoginUrl());
                String s = HttpConnect.postHttpString(Urls.getTeacherLoginUrl(), list);
                System.out.println(s);
                
                JSONObject obj = new JSONObject(s);
                String result = obj.optString("result");
                int teacherID = obj.optInt("teacherID");
                SharedPreferences preferences = CommonConstants.getMyPreferences(context);
                Editor editor = preferences.edit();
                editor.putInt(CommonConstants.TEACHER_ID, teacherID);
                editor.commit();
                if (RESULT_OK.equals(result))
                {
                    JSONArray array = obj.optJSONArray("courses");
                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject json = array.getJSONObject(i);
                        Course course = new Course(json);
                        course.setcName(URLDecoder.decode(course.getcName()));
                        course.settName(URLDecoder.decode(course.gettName()));
                        course.setcAddress(URLDecoder.decode(course.getcAddress()));
                        course.settNo(teacherID);
                        
                        courses.add(course);
                        CourseDao dao = new CourseDaoImpl(context);
                        dao.addCourse(course, true);
                    }
                    
                    handler.sendEmptyMessage(2);
                }
                else
                {
                    ((Activity)context).runOnUiThread(new Runnable()
                    {
                        
                        public void run()
                        {
                            handler.sendEmptyMessage(1);
                            Toast.makeText(context, "服务器无您的课程，请添加。", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
                ((Activity)context).runOnUiThread(new Runnable()
                {
                    
                    public void run()
                    {
                        handler.sendEmptyMessage(1);
                        Toast.makeText(context, "网络出现问题，请稍候重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        
        return courses;
    }
    
    @Override
    protected void onPostExecute(List<Course> result)
    {
        
        super.onPostExecute(result);
    }
    
}
