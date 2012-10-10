package com.example.syllabus.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.syllabus.R;
import com.example.syllabus.adapter.UpLoadCourseAdapter;
import com.example.syllabus.bean.Course;
import com.example.syllabus.db.CourseDao;
import com.example.syllabus.db.CourseDaoImpl;
import com.example.syllabus.db.UnUploadedCourseDao;
import com.example.syllabus.db.UnUploadedCourseDaoImpl;
import com.example.syllabus.utils.CommonConstants;
import com.example.syllabus.utils.HttpConnect;
import com.example.syllabus.utils.Urls;

/**
 * 同步本地未同步列表中课程到服务器，只有在登录且网络存在时才进入此Activity
 * 
 * @author Administrator
 * 
 */
public class UpLoadedActivity extends Activity implements OnClickListener
{
    private TextView tvTitle;
    
    private TextView tvLeft;
    
    private TextView tvRightT;
    
    private ListView lvListAdd;
    
    private ListView lvListUpdate;
    
    private ImageView ivCheckAll_Add;
    
    private ImageView ivCheckAll_Update;
    
    private List<Course> courses_Add;
    
    private List<Boolean> course_add_selected;
    
    private List<Course> courses_update;
    
    private List<Boolean> course_update_selected;
    
    private RelativeLayout rlAdd;
    
    private RelativeLayout rlUpdate;
    
    UpLoadCourseAdapter adapterForAdd;
    
    UpLoadCourseAdapter adapterForUpdate;
    
    public static final String[] FIELDS = {"cName", "tName", "cAddress", "cStartWeek", "cEndWeek", "cWeekday",
        "courseIndex"};
    
    SharedPreferences preferences;
    
    private boolean isTeacher;
    
    private static final int UPDATE_COURSE = 1;
    
    private static final int INSERT_COURSE_TO_SERVER = 0;
    
    private Handler handler;
    
    private ProgressDialog pd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_activity);
        preferences = CommonConstants.getMyPreferences(this);
        isTeacher = preferences.getBoolean(CommonConstants.IS_TEACHER, false);
        initViews();
        initData();
        
        handler = new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {
                switch (msg.what)
                {
                    case 1:
                        
                        break;
                    
                    default:
                        break;
                }
            }
        };
        
        pd = new ProgressDialog(this);
    }
    
    private void initViews()
    {
        // TODO Auto-generated method stub
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText("同步");
        
        tvLeft = (TextView)findViewById(R.id.tvLeft);
        tvLeft.setText("返回");
        
        tvLeft.setOnClickListener(this);
        
        tvRightT = (TextView)findViewById(R.id.tvRightT);
        tvRightT.setText("上传");
        tvRightT.setOnClickListener(this);
        
        lvListAdd = (ListView)findViewById(R.id.action_add);
        lvListUpdate = (ListView)findViewById(R.id.action_update);
        
        ivCheckAll_Add = (ImageView)findViewById(R.id.checkall_add);
        // ivCheckAll_Add.setOnClickListener(this);
        ivCheckAll_Add.setTag(null);
        
        ivCheckAll_Update = (ImageView)findViewById(R.id.checkall_update);
        // ivCheckAll_Update.setOnClickListener(this);
        ivCheckAll_Add.setTag(null);
        
        rlAdd = (RelativeLayout)findViewById(R.id.rladd);
        rlUpdate = (RelativeLayout)findViewById(R.id.rlupdate);
        rlAdd.setOnClickListener(this);
        rlUpdate.setOnClickListener(this);
    }
    
    private void initData()
    {
        UnUploadedCourseDao unDao = new UnUploadedCourseDaoImpl(this);
        courses_Add = unDao.getAllActionAddCourses();
        courses_update = unDao.getAllActionUpdateCourses();
        
        course_add_selected = new ArrayList<Boolean>();
        if (!courses_Add.isEmpty())
        {
            for (int i = 0; i < courses_Add.size(); i++)
            {
                course_add_selected.add(false);
            }
        }
        
        course_update_selected = new ArrayList<Boolean>();
        if (!courses_update.isEmpty())
        {
            for (int i = 0; i < courses_update.size(); i++)
            {
                course_update_selected.add(false);
            }
        }
        
        adapterForAdd = new UpLoadCourseAdapter(courses_Add, course_add_selected, this);
        adapterForUpdate = new UpLoadCourseAdapter(courses_update, course_update_selected, this);
        
        lvListAdd.setAdapter(adapterForAdd);
        lvListUpdate.setAdapter(adapterForUpdate);
        
    }
    
    public void onClick(View arg0)
    {
        // TODO Auto-generated method stub
        switch (arg0.getId())
        {
            case R.id.rladd:
                if (null == ivCheckAll_Add.getTag())
                {
                    for (int i = 0; i < course_add_selected.size(); i++)
                    {
                        course_add_selected.set(i, true);
                    }
                    ivCheckAll_Add.setTag("selected");
                    adapterForAdd.notifyDataSetChanged();
                    ivCheckAll_Add.setImageResource((R.drawable.selected));
                }
                else
                {
                    for (int i = 0; i < course_add_selected.size(); i++)
                    {
                        course_add_selected.set(i, false);
                    }
                    ivCheckAll_Add.setTag(null);
                    adapterForAdd.notifyDataSetChanged();
                    ivCheckAll_Add.setImageResource((R.drawable.checkbox));
                }
                break;
            case R.id.rlupdate:
                if (null == ivCheckAll_Update.getTag())
                {
                    for (int i = 0; i < course_update_selected.size(); i++)
                    {
                        course_update_selected.set(i, true);
                    }
                    adapterForUpdate.notifyDataSetChanged();
                    ivCheckAll_Update.setTag("selected");
                    ivCheckAll_Update.setImageResource(R.drawable.selected);
                }
                else
                {
                    for (int i = 0; i < course_update_selected.size(); i++)
                    {
                        course_update_selected.set(i, false);
                    }
                    ivCheckAll_Update.setTag(null);
                    adapterForUpdate.notifyDataSetChanged();
                    ivCheckAll_Update.setImageResource(R.drawable.checkbox);
                }
                
                break;
            case R.id.tvRightT:
                for (int i = 0; i < course_add_selected.size(); i++)
                {
                    System.out.println("UpLoadedActivity---course_add_selected[" + i + "]:"
                        + course_add_selected.get(i));
                }
                pd.setTitle("课程同步中,请稍候...");
                pd.show();
                
                // 上传数据方法不对，不能开启这么多线程
                for (int i = 0; i < course_add_selected.size(); i++)
                {
                    if (course_add_selected.get(i))
                    {
                        Course course = courses_Add.get(i);
                        List<NameValuePair> values = configureValues(course);
                        
                        // handler.sendEmptyMessage(1);
                        new SendCourseToServerTask(values, course).execute("");
                    }
                }
                
                for (int i = 0; i < course_update_selected.size(); i++)
                {
                    if (course_update_selected.get(i))
                    {
                        Course course = courses_Add.get(i);
                        List<NameValuePair> values = configureValues(course);
                        
                        // handler.sendEmptyMessage(2);
                        new UpdateCourseTask(values, course).execute("");
                    }
                }
                
                // 消失的时机不对
                pd.dismiss();
                break;
            case R.id.tvLeft:
                this.finish();
                break;
            default:
                break;
        }
    }
    
    /**
     * @param course
     * @return
     */
    private List<NameValuePair> configureValues(Course course)
    {
        String[] courseValues =
            {course.getcName(), course.gettName(), course.getcAddress(), Integer.toString(course.getcStartWeek()),
                Integer.toString(course.getcEndWeek()), Integer.toString(course.getcWeekday()),
                Integer.toString(course.getCourseIndex())};
        List<NameValuePair> values = new ArrayList<NameValuePair>();
        // 如果是学生添加课程，需要classid字段
        if (!isTeacher)
        {
            NameValuePair classidValue =
                new BasicNameValuePair(CommonConstants.CLASSID, preferences.getInt(CommonConstants.CLASSID, -1) + "");
            values.add(classidValue);
        }
        for (int j = 0; j < courseValues.length; j++)
        {
            NameValuePair value = new BasicNameValuePair(FIELDS[j], URLEncoder.encode(courseValues[j]));
            values.add(value);
        }
        return values;
    }
    
    public class SendCourseToServerTask extends AsyncTask<String, String, String>
    {
        private List<NameValuePair> pairs;
        
        // private Service service;
        private Course course;
        
        public SendCourseToServerTask(List<NameValuePair> values, Course course)
        {
            this.pairs = values;
            this.course = course;
            // this.service = service;
        }
        
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String s = HttpConnect.postHttpString(Urls.getInsertCourse(), pairs);
                System.out.println(s);
                if (!"".equals(s) && null != s)
                {
                    JSONObject obj = new JSONObject(s);
                    int resultCode = obj.optInt("result");
                    if (1 == resultCode)
                    {
                        System.out.println("inserted");
                        course.setCourseid(obj.optInt("courseid"));
                        Log.i("AddCourseToServer", "courseid:" + obj.optInt("courseid"));
                        CourseDao dao = new CourseDaoImpl(UpLoadedActivity.this);
                        Log.i("AddcourseTOServer", "course id:" + course.getId() + ",courseid:" + course.getCourseid());
                        dao.updateCourseid(course.getId(), course.getCourseid());
                        
                        // 将课程从未同步课表中删除
                        UnUploadedCourseDao unDao = new UnUploadedCourseDaoImpl(UpLoadedActivity.this);
                        unDao.deleteCourse(course.getId(), CommonConstants.UNDO_ACTION_ADD);
                    }
                }
                else
                {
                    
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            return null;
        }
        
    }
    
    public class UpdateCourseTask extends AsyncTask<String, String, String>
    {
        private List<NameValuePair> pairs;
        
        private Course course;
        
        public UpdateCourseTask(List<NameValuePair> values, Course course)
        {
            this.pairs = values;
            this.course = course;
        }
        
        @Override
        protected String doInBackground(String... arg0)
        {
            try
            {
                String s = HttpConnect.postHttpString(Urls.getUpdateCourse(), pairs);
                System.out.println(s);
                if (null != s && !"".equals(s))
                {
                    
                    JSONObject obj = new JSONObject(s);
                    int resultCode = obj.optInt("result");
                    if (UPDATE_COURSE == resultCode)
                    {
                        System.out.println("updated");
                        
                        // 将课程从为同步列表中删除
                        UnUploadedCourseDao unDao = new UnUploadedCourseDaoImpl(UpLoadedActivity.this);
                        unDao.deleteCourse(course.getId(), CommonConstants.UNDO_ACTION_UPDATE);
                        
                    }
                    else
                    {
                        System.out.println("server is updating, please waiting...");
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            return null;
        }
        
    }
}
