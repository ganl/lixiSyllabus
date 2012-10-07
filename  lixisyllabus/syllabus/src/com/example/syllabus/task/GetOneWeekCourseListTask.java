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
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.syllabus.activity.OneWeekCourseListActivity;
import com.example.syllabus.bean.Course;
import com.example.syllabus.db.CourseDao;
import com.example.syllabus.db.CourseDaoImpl;
import com.example.syllabus.utils.CommonConstants;
import com.example.syllabus.utils.HttpConnect;
import com.example.syllabus.utils.Urls;

public class GetOneWeekCourseListTask extends AsyncTask<String, String, List<Course>>
{
    
    private Context context;
    
    private Handler handler;
    
    private int classid;
    
    public GetOneWeekCourseListTask(Context context, Handler handler)
    {
        this.context = context;
        this.handler = handler;
    }
    
    @Override
    protected List<Course> doInBackground(String... params)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (int i = 0; i < params.length; i++)
        {
            NameValuePair pair =
                new BasicNameValuePair(CommonConstants.STUDENT_INFORMATION[i], URLEncoder.encode(params[i]));// params[i]);
            list.add(pair);
        }
        List<Course> courses = new ArrayList<Course>();
        try
        {
            System.out.println(Urls.getLoginUrl());
            String s = HttpConnect.postHttpString(Urls.getLoginUrl(), list);
            System.out.println(s);
            
            // String s =
            // "{'result':'OK','courseInformation':[{'cAddress':'381','tName':'�˵���','cName':'���ݽṹ','cStartWeek':2,'id':2,'courseIndex':1,'cWeekday':1,'cEndWeek':8},{'cAddress':'','tName':'','cName':'�ǺǷ���','cStartWeek':2,'id':4,'courseIndex':1,'cWeekday':1,'cEndWeek':8},{'cAddress':'�úø�','tName':'��vv��','cName':'�ͺúø�','cStartWeek':2,'id':9,'courseIndex':1,'cWeekday':1,'cEndWeek':8},{'cAddress':'108','tName':'�˵���','cName':'����ϵͳ','cStartWeek':2,'id':11,'courseIndex':5,'cWeekday':1,'cEndWeek':8},{'cAddress':'301','tName':'¶¶','cName':'��ɢ��ѧ','cStartWeek':2,'id':13,'courseIndex':1,'cWeekday':2,'cEndWeek':8},{'cAddress':'355','tName':'����','cName':'ʵ�亯��','cStartWeek':2,'id':15,'courseIndex':3,'cWeekday':2,'cEndWeek':8},{'cAddress':'355','tName':'����','cName':'ʵ�亯��','cStartWeek':2,'id':14,'courseIndex':1,'cWeekday':3,'cEndWeek':8},{'cAddress':'301','tName':'¶¶','cName':'��ɢ��ѧ','cStartWeek':2,'id':12,'courseIndex':1,'cWeekday':4,'cEndWeek':8},{'cAddress':'381','tName':'�˵���','cName':'���ݽṹ','cStartWeek':2,'id':1,'courseIndex':1,'cWeekday':5,'cEndWeek':8},{'cAddress':'�üһ�','tName':'����','cName':'�ǺǷ���','cStartWeek':2,'id':3,'courseIndex':1,'cWeekday':5,'cEndWeek':8},{'cAddress':'���úø�','tName':'�ǻҺ�','cName':'���Ѳ�','cStartWeek':2,'id':5,'courseIndex':1,'cWeekday':5,'cEndWeek':8},{'cAddress':'�ոո�','tName':'����','cName':'�úø�','cStartWeek':1,'id':6,'courseIndex':1,'cWeekday':5,'cEndWeek':8},{'cAddress':'�úø�','tName':'��vv��','cName':'�ͺúø�','cStartWeek':2,'id':8,'courseIndex':1,'cWeekday':5,'cEndWeek':8},{'cAddress':'�����','tName':'����','cName':'Ӻ�͹�','cStartWeek':2,'id':7,'courseIndex':2,'cWeekday':5,'cEndWeek':8},{'cAddress':'108','tName':'�˵���','cName':'����ϵͳ','cStartWeek':2,'id':10,'courseIndex':2,'cWeekday':5,'cEndWeek':8},{'cAddress':'503','tName':'����ѧ','cName':'ͼ����','cStartWeek':2,'id':16,'courseIndex':1,'cWeekday':6,'cEndWeek':8},{'cAddress':'503','tName':'����ѧ','cName':'ͼ����','cStartWeek':2,'id':17,'courseIndex':1,'cWeekday':7,'cEndWeek':8}]}";
            
            JSONObject obj = new JSONObject(s);
            String result = obj.optString("result");
            
            classid = obj.optInt(CommonConstants.CLASSID);
            SharedPreferences preferences = CommonConstants.getMyPreferences(context);
            Editor editor = preferences.edit();
            editor.putInt(CommonConstants.CLASSID, classid);
            editor.commit();
            if ("2".equals(result) || "0".equals(result))
            {
                ((Activity)context).runOnUiThread(new Runnable()
                {
                    
                    public void run()
                    {
                        Toast.makeText(context, "�����������༶�γ̣������ӡ�", Toast.LENGTH_SHORT).show();
                    }
                });
                System.out.println("*********************************************************");
            }
            
            if ("1".equals(result))
            {
                JSONArray array = obj.optJSONArray("courses");
                // classid = obj.optInt(CommonConstants.CLASSID);
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
                    dao.addCourse(course);
                }
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ((Activity)context).runOnUiThread(new Runnable()
            {
                
                public void run()
                {
                    Toast.makeText(context, "����������⣬���Ժ�����", Toast.LENGTH_SHORT).show();
                }
            });
        }
        
        return courses;
    }
    
    @Override
    protected void onPostExecute(List<Course> result)
    {
        List<ArrayList<Map<String, String>>> weekList = new ArrayList<ArrayList<Map<String, String>>>();
        
        if (result.isEmpty())
        {
            ((OneWeekCourseListActivity)context).showDialog("�������������༶���ݣ������ӿγ̣�Ϊͬѧ����");
        }
        
        for (int i = 1; i < 8; i++)
        {
            ArrayList<Map<String, String>> dayList = new ArrayList<Map<String, String>>();
            for (int j = 0; j < result.size(); j++)
            {
                if (i == result.get(j).getcWeekday())
                {
                    dayList.add(result.get(j).beanToMap());
                    // result.remove(j);
                }
            }
            weekList.add(dayList);
        }
        Editor editor = CommonConstants.getMyPreferences(context).edit();
        editor.putInt("classid", classid);
        editor.commit();
        Message message = Message.obtain();
        message.what = 1;
        message.obj = weekList;
        handler.sendMessage(message);
        super.onPostExecute(result);
    }
    
}