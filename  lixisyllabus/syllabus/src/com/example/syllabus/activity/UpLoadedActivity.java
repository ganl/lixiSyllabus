package com.example.syllabus.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syllabus.R;
import com.example.syllabus.adapter.UpLoadCourseAdapter;
import com.example.syllabus.bean.Course;
import com.example.syllabus.db.CourseDao;
import com.example.syllabus.db.CourseDaoImpl;
import com.example.syllabus.db.UnUploadedCourseDao;
import com.example.syllabus.db.UnUploadedCourseDaoImpl;
import com.example.syllabus.utils.CommonConstants;
import com.example.syllabus.utils.HttpConnect;
import com.example.syllabus.utils.LogUtil;
import com.example.syllabus.utils.Urls;

/**
 * ͬ������δͬ���б��пγ̵���������ֻ���ڵ�¼���������ʱ�Ž����Activity
 * 
 * @author Administrator
 * 
 */
public class UpLoadedActivity extends Activity implements OnClickListener
{
    private static final String LOGTAG = LogUtil.makeLogTag(UpLoadedActivity.class);
    
    private TextView tvTitle;
    
    private TextView tvLeft;
    
    private TextView tvRightT;
    
    private ListView lvListAdd;
    
    private ListView lvListUpdate;
    
    private ImageView ivCheckAll_Add;
    
    private ImageView ivCheckAll_Update;
    
    private List<Course> courses_Add;
    
    private List<Boolean> course_add_selected;
    
    private List<Course> courses_Update;
    
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
    
    private ExecutorService executorService; // executorService can accept tasks
    
    private TaskTracker taskTracker; // track the task num in the executor
    
    private TaskSubmitter taskSubmitter;
    
    private List<Runnable> taskList;
    
    private Future<?> futureTask;
    
    private boolean running = false;
    
    public TaskTracker getTaskTracker()
    {
        return taskTracker;
    }
    
    public void setTaskTracker(TaskTracker taskTracker)
    {
        this.taskTracker = taskTracker;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
            }
        };
        executorService = Executors.newSingleThreadExecutor(); // single thread exector
        // setExecutorService(Executors.newSingleThreadExecutor()); // single thread exector
        taskTracker = new TaskTracker(this);
        taskSubmitter = new TaskSubmitter(this);
        taskList = new ArrayList<Runnable>();
        pd = new ProgressDialog(this);
    }
    
    private void initViews()
    {
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText("ͬ��");
        
        tvLeft = (TextView)findViewById(R.id.tvLeft);
        tvLeft.setText("����");
        
        tvLeft.setOnClickListener(this);
        
        tvRightT = (TextView)findViewById(R.id.tvRightT);
        tvRightT.setText("�ϴ�");
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
        courses_Update = unDao.getAllActionUpdateCourses();
        
        course_add_selected = new ArrayList<Boolean>();
        if (!courses_Add.isEmpty())
        {
            for (int i = 0; i < courses_Add.size(); i++)
            {
                course_add_selected.add(false);
            }
        }
        
        course_update_selected = new ArrayList<Boolean>();
        if (!courses_Update.isEmpty())
        {
            for (int i = 0; i < courses_Update.size(); i++)
            {
                course_update_selected.add(false);
            }
        }
        
        adapterForAdd = new UpLoadCourseAdapter(courses_Add, course_add_selected, this);
        adapterForUpdate = new UpLoadCourseAdapter(courses_Update, course_update_selected, this);
        
        lvListAdd.setAdapter(adapterForAdd);
        lvListUpdate.setAdapter(adapterForUpdate);
        
        adapterForAdd.notifyDataSetChanged();
        adapterForUpdate.notifyDataSetChanged();
        
        ivCheckAll_Add.setImageResource(R.drawable.checkbox);
        ivCheckAll_Update.setImageResource(R.drawable.checkbox);
        
    }
    
    public void onClick(View arg0)
    {
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
                pd.setTitle("�γ�ͬ����,���Ժ�...");
                pd.show();
                
                // �ϴ����ݷ������ԣ����ܿ�����ô���߳�
                // �������޸ģ�ʹ�õ��̳߳ء�
                for (int i = 0; i < course_add_selected.size(); i++)
                {
                    if (course_add_selected.get(i))
                    {
                        Course course = courses_Add.get(i);
                        List<NameValuePair> values = configureValues(course);
                        
                        // handler.sendEmptyMessage(1);
                        addTask(new SendCourseToServerTask(values, course));
                        // new SendCourseToServerTask(values, course).execute("");
                        runTask();
                    }
                }
                
                for (int i = 0; i < course_update_selected.size(); i++)
                {
                    if (course_update_selected.get(i))
                    {
                        Course course = courses_Update.get(i);
                        List<NameValuePair> values = configureValues(course);
                        
                        // handler.sendEmptyMessage(2);
                        addTask(new UpdateCourseTask(values, course));
                        // new UpdateCourseTask(values, course).execute("");
                        runTask();
                    }
                }
                
                // ����ֹͣ�̣߳��������������δ��ɣ���ִ�йرգ�һֱ��ִ����Źرգ��´˲�����������������Ҫ��ѯ
                executorService.shutdown();
                handler.post(new Runnable()
                {
                    public void run()
                    {
                        if (executorService.isTerminated())
                        {
                            pd.dismiss();
                            initData();
                            Toast.makeText(UpLoadedActivity.this, "ͬ����ɣ����Ŀα���������", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            handler.postDelayed(this, 1000);
                        }
                    }
                });
                if (executorService.isTerminated())
                {
                    pd.dismiss();
                }
                else
                {
                    Log.i(LOGTAG, "task is not finishing");
                }
                // ��ʧ��ʱ������
                // pd.setTitle("�γ�ͬ����,���Ժ�...");
                // pd.show();
                Log.i(LOGTAG, "taskList size :" + taskList.size());
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
        // �����ѧ����ӿγ̣���Ҫclassid�ֶ�
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
    
    public ExecutorService getExecutorService()
    {
        return executorService;
    }
    
    public void setExecutorService(ExecutorService executorService)
    {
        this.executorService = executorService;
    }
    
    public class SendCourseToServerTask implements Runnable
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
        
        public void run()
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
                        
                        // ���γ̴�δͬ���α���ɾ��
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
        }
        
    }
    
    public class UpdateCourseTask implements Runnable
    {
        private List<NameValuePair> pairs;
        
        private Course course;
        
        public UpdateCourseTask(List<NameValuePair> values, Course course)
        {
            this.pairs = values;
            this.course = course;
        }
        
        public void run()
        {
            try
            {
                System.out.println(Urls.getUpdateCourse());
                String s = HttpConnect.postHttpString(Urls.getUpdateCourse(), pairs);
                System.out.println(s);
                if (null != s && !"".equals(s))
                {
                    
                    JSONObject obj = new JSONObject(s);
                    int resultCode = obj.optInt("result");
                    if (UPDATE_COURSE == resultCode)
                    {
                        System.out.println("updated");
                        
                        // ���γ̴�Ϊͬ���б���ɾ��
                        UnUploadedCourseDao unDao = new UnUploadedCourseDaoImpl(UpLoadedActivity.this);
                        unDao.deleteCourse(course.getId(), CommonConstants.UNDO_ACTION_UPDATE);
                    }
                    else if (INSERT_COURSE_TO_SERVER == resultCode)
                    {
                        System.out.println("added");
                        int courseid = obj.optInt("courseid");
                        // ���γ̴�δͬ���б���ɾ���������¿α���Ϣ
                        CourseDaoImpl dao = new CourseDaoImpl(UpLoadedActivity.this);
                        dao.updateCourseid(course.getId(), courseid);
                        UnUploadedCourseDaoImpl unDao = new UnUploadedCourseDaoImpl(UpLoadedActivity.this);
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
        }
        
    }
    
    /**
     * Class for summiting a new runnable task.
     */
    public class TaskSubmitter
    {
        
        final UpLoadedActivity activity;
        
        public TaskSubmitter(UpLoadedActivity activity)
        {
            this.activity = activity;
        }
        
        @SuppressWarnings("unchecked")
        public Future submit(Runnable task)
        {
            Future result = null;
            if (!activity.getExecutorService().isTerminated() && !activity.getExecutorService().isShutdown()
                && task != null)
            {
                result = activity.getExecutorService().submit(task);
            }
            return result; // if result.get() returns null, it means that the computation has completed.
        }
        
    }
    
    /**
     * Class for monitoring the running task count.
     */
    public class TaskTracker
    {
        
        final UpLoadedActivity activity;
        
        public int count; // task number
        
        public TaskTracker(UpLoadedActivity activity)
        {
            this.activity = activity;
            this.count = 0;
        }
        
        // increase the number of task
        public void increase()
        {
            synchronized (activity.getTaskTracker())
            {
                activity.getTaskTracker().count++;
                Log.d(LOGTAG, "Incremented task count to " + count);
            }
        }
        
        // decrease the number of task
        public void decrease()
        {
            synchronized (activity.getTaskTracker())
            {
                activity.getTaskTracker().count--;
                Log.d(LOGTAG, "Decremented task count to " + count);
            }
        }
        
    }
    
    public void runTask()
    {
        Log.d(LOGTAG, "runTask()...");
        synchronized (taskList)
        {
            running = false;
            futureTask = null;
            if (!taskList.isEmpty())
            {
                Runnable runnable = (Runnable)taskList.get(0);
                Log.v(LOGTAG, "run task-" + runnable.toString());
                taskList.remove(0);
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if (futureTask == null)
                {
                    Log.v(LOGTAG, "from here decrease");
                    taskTracker.decrease();
                }
            }
        }
        taskTracker.decrease();
        Log.d(LOGTAG, "runTask()...done");
    }
    
    private void addTask(Runnable runnable)
    {
        Log.d(LOGTAG, "addTask(runnable)..." + runnable.toString());
        taskTracker.increase();
        synchronized (taskList)
        {
            // ����ǵ�һ�����룬��ôֱ���ύ���У�������taskList,����û������������ʱ������taskList
            if (taskList.isEmpty() && !running) // running is false when first register
            {
                running = true;
                futureTask = taskSubmitter.submit(runnable); // submit the runnable object to the ExecutorService.
                if (futureTask == null) // if futureTask is null, it means that the computation has completed.
                {
                    taskTracker.decrease();
                }
            }
            else
            {
                Log.v(LOGTAG, "task is not empty or is running" + runnable.toString());
                taskList.add(runnable);
                Log.v("num of list", "" + taskList.size());
            }
        }
        Log.d(LOGTAG, "addTask(runnable)... done");
    }
}
