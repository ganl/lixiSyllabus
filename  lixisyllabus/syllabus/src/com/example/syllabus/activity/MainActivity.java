package com.example.syllabus.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.syllabus.R;
import com.example.syllabus.SyllabusApplication;
import com.example.syllabus.adapter.SimpleCourseAdapter;
import com.example.syllabus.bean.Course;
import com.example.syllabus.db.CourseDao;
import com.example.syllabus.db.CourseDaoImpl;
import com.example.syllabus.utils.CommonConstants;
import com.example.syllabus.utils.HttpConnect;

public class MainActivity extends Activity implements OnClickListener, OnTouchListener, OnItemLongClickListener,
    SensorEventListener, OnLongClickListener
{
    public static final String ACTION_ADD_COURSE = "add";
    
    private TextView tvLeft; // ���
    
    private TextView tvTitle; // ����
    
    private TextView tvRightT; // �Ҽ�
    
    private int dayOfWeek; // ���ڼ�
    
    private int weekOfSemister; // �ڼ���
    
    // private ImageView ivAddCourse; // ��ӿγ�
    
    private Button btnAddCourse;
    
    private ImageView ivTurnLeft;
    
    private ImageView ivTurnRight;
    
    private LinearLayout llShowList;
    
    private SharedPreferences preferences;
    
    private List<List<Course>> oneWeekCourses;
    
    int distance = 180;
    
    private ViewFlipper viewFlipper;
    
    private LayoutInflater inflater;
    
    private Handler handler;
    
    private GestureDetector gestureDetector;
    
    private MyGestureListener myGestureListener;
    
    private int pos = -1; // use to detect the position of long time click
    
    private boolean isFromOnCreate = true; // use to identify whether it is the first creation of Acitivity
    
    public SensorManager sensorManager;
    
    private long lastUpdate = -1;
    
    private float x, y, z;
    
    private float last_x, last_y, last_z;
    
    private static final int SHAKE_THRESHOLD = 850;
    
    private boolean isTurningToAnotherActivity = false;
    
    private boolean isTeacher = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        
        SyllabusApplication.getInstance().addActivity(this);
        
        preferences = CommonConstants.getMyPreferences(this);
        inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        
        if (!preferences.getBoolean(CommonConstants.SHOW_WELCOME, false))
        {
            Intent intent = new Intent();
            intent.setClass(this, WelcomeActivity.class);
            startActivity(intent);
            isTurningToAnotherActivity = true;
            this.finish();
        }
        
        // if not logined already, turn around to the login activity
        // if (!isTurningToAnotherActivity && !preferences.getBoolean(CommonConstants.LOGINED, false)
        // && !preferences.getBoolean(CommonConstants.SKIPPED, false))
        // {
        // Intent intent = new Intent();
        // intent.setClass(this, LoginActivity.class);
        // startActivity(intent);
        // isTurningToAnotherActivity = true;
        // this.finish();
        // }
        
        if (!isTurningToAnotherActivity && !preferences.getBoolean(CommonConstants.IS_SETUP_ALREADY, false))
        {
            Intent intent = new Intent();
            intent.setClass(this, SetUpActivity.class);
            startActivity(intent);
            isTurningToAnotherActivity = true;
            this.finish();
        }
        
        myGestureListener = new MyGestureListener();
        gestureDetector = new GestureDetector(myGestureListener);
        
        isTeacher = preferences.getBoolean(CommonConstants.IS_TEACHER, false);
        
        initViews();
        
        initData();
        
        handler = new MyHandler();
        
    }
    
    private void initViews()
    {
        tvLeft = (TextView)findViewById(R.id.tvLeft);
        tvLeft.setText("����");
        tvLeft.setOnClickListener(this);
        
        tvRightT = (TextView)findViewById(R.id.tvRightT);
        tvRightT.setText("һ��");
        tvRightT.setOnClickListener(this);
        
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setOnClickListener(this);
        tvTitle.setOnLongClickListener(this);
        
        llShowList = (LinearLayout)findViewById(R.id.showlist);
        llShowList.setOnTouchListener(this);
        
        ivTurnLeft = (ImageView)findViewById(R.id.turnleft);
        ivTurnRight = (ImageView)findViewById(R.id.turnright);
        ivTurnLeft.setOnClickListener(this);
        ivTurnRight.setOnClickListener(this);
        
        btnAddCourse = (Button)findViewById(R.id.addcourse);
        btnAddCourse.setOnClickListener(this);
        
        Log.i("MainActivity", "in onCreate()");
    }
    
    private void initData()
    {
        
        String str = CommonConstants.getCurrentDayOfWeek();
        
        Intent intent = getIntent();
        
        Bundle extras = intent.getExtras();
        int appWidgetId = 0;
        if (null != extras)
        {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            Log.i("MainActivity", appWidgetId + "appWidgetId");
        }
        // if it is not from AppWidget and is not first created
        if (0 == appWidgetId && -1 != intent.getIntExtra("dayOfWeek", -1))
        {
            Log.i("MainActivity", "before intent, dayOfWeek is " + dayOfWeek);
            dayOfWeek = intent.getIntExtra("dayOfWeek", -1);
            Log.i("MainActivity", "from widget intent, dayOfWeek is " + dayOfWeek);
            weekOfSemister = preferences.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER);
            str = CommonConstants.getStrFromWeekNum(dayOfWeek);
        }
        else
        {
            // if it is from AppWidget or first created
            if (str.matches("^[a-zA-Z]*"))
            {
                dayOfWeek = CommonConstants.getDayOfWeekFromEng(str);
                str = CommonConstants.getStrFromWeekNum(dayOfWeek);
            }
            else
            {
                dayOfWeek = CommonConstants.getWeekNumFromStr(str);
            }
            weekOfSemister = preferences.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER);
        }
        
        if (!isTurningToAnotherActivity && !preferences.getBoolean("isSetUpAlready", false))
        {// if the user has not set up the profile, we need to encourage them to do;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog = null;
            builder.setTitle("��ӿγ�֮ǰ���������ñ��ܴ����������Ҫ������");
            builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    Intent intent = new Intent(MainActivity.this, SetUpActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
        tvTitle.setText(str);
        
        viewFlipper = (ViewFlipper)findViewById(R.id.viewfilpper);
        
        loadDataFromDB();
        
        viewFlipper.setDisplayedChild(dayOfWeek - 1);
        
    }
    
    @Override
    protected void onResume()
    {
        if (isFromOnCreate)
        {
            isFromOnCreate = false;
        }
        else
        {// if come back from set or add activity, we should update the view
            if (((SyllabusApplication)getApplication()).isWeekHasBeenChanged)
            {
                weekOfSemister =
                    preferences.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER);
                ((SyllabusApplication)getApplication()).isWeekHasBeenChanged = false;
                updateViewFlipper();
            }
            if (((SyllabusApplication)getApplication()).isDataHasBeenMotifyed)
            {
                ((SyllabusApplication)getApplication()).isDataHasBeenMotifyed = false;
                updateViewFlipper();
            }
            
        }
        
        if (preferences.getBoolean(CommonConstants.IS_ACCELEREMETER_SUPPORTED, true))
        {
            Editor editor = preferences.edit();
            if (!sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL))
            {
                if (preferences.getBoolean(CommonConstants.IS_FIRST_RUN, true))
                {
                    Toast.makeText(this, "�����ֻ���֧��ҡһҡ�ص�����γ̱���", Toast.LENGTH_SHORT).show();
                    editor.putBoolean(CommonConstants.IS_ACCELEREMETER_SUPPORTED, false);
                }
            }
            else
            {
                if (preferences.getBoolean(CommonConstants.IS_FIRST_RUN, true))
                {
                    Toast.makeText(this, "ҡһҡ�ص�����γ�Ŷ��", Toast.LENGTH_SHORT).show();
                    editor.putBoolean(CommonConstants.IS_ACCELEREMETER_SUPPORTED, true);
                }
            }
            
            editor.putBoolean(CommonConstants.IS_FIRST_RUN, false);
            editor.commit();
        }
        
        super.onResume();
    }
    
    @Override
    protected void onStop()
    {
        sensorManager.unregisterListener(this);
        super.onStop();
    }
    
    @Override
    protected void onPause()
    {
        sensorManager.unregisterListener(this);
        super.onPause();
    }
    
    private void setAdapterOfListView(List<Course> tempCourses, SimpleCourseAdapter adapter, ListView listView)
    {
        adapter = new SimpleCourseAdapter(this, tempCourses);
        
        listView.setAdapter(adapter);
        
        listView.setOnTouchListener(this);
        listView.setOnItemLongClickListener(this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent = null;
        switch (item.getItemId())
        {
        
            case R.id.menu_settings:
                /**
                 * for test
                 */
                // intent = new Intent(this, UpLoadedActivity.class);
                // // intent.putExtra("fromMainActivity", true);
                // startActivity(intent);
                // ***************************************************************
                if (!preferences.getBoolean(CommonConstants.LOGINED, false) && HttpConnect.isNetworkHolding(this))
                {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                else if (HttpConnect.isNetworkHolding(this))
                {
                    intent = new Intent(this, UpLoadedActivity.class);
                    // intent.putExtra("fromMainActivity", true);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(this, "��������û�д򿪣�ͬ��ǰ���ȴ�����", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                // this.finish();
                break;
            case R.id.memu_exiting:
                SyllabusApplication.getInstance().exitApplication();
                break;
            case R.id.menu_help:
                /**
                 * need to be added in the future
                 */
                intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onClick(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.tvLeft:
                intent = new Intent(MainActivity.this, SetUpActivity.class);
                intent.putExtra("fromMainActivity", true);
                this.startActivity(intent);
                break;
            case R.id.addcourse:
                intent = new Intent(this, AddCourseActivity.class);
                int index = 0;
                if (0 == oneWeekCourses.get(dayOfWeek - 1).size())
                {
                    index = 1;
                }
                else
                {
                    List<Course> dayCourses = oneWeekCourses.get(dayOfWeek - 1);
                    index = dayCourses.get(dayCourses.size() - 1).getCourseIndex() + 1;
                }
                if (index > 6)
                {
                    index = 6;
                }
                intent.putExtra("dayOfWeek", dayOfWeek);
                intent.putExtra("courseIndex", index);
                startActivity(intent);
                break;
            case R.id.tvRightT:
                intent = new Intent(this, OneWeekCourseListActivity.class);
                intent.putExtra(CommonConstants.WEEKOFSEMISTER, weekOfSemister);
                startActivity(intent);
                break;
            case R.id.tvTitle:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("��ѡ��");
                builder.setItems(CommonConstants.DAYOFWEEKS_INCHN, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dayOfWeek = which + 1;
                        viewFlipper.setDisplayedChild(which);
                        tvTitle.setText(CommonConstants.getStrFromWeekNum(dayOfWeek));
                    }
                });
                builder.create().show();
                break;
            
            case R.id.turnleft:
                MainActivity.this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.push_right_in));
                MainActivity.this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.push_right_out));
                
                if (dayOfWeek > yestoday(dayOfWeek)) // previous day
                {
                    dayOfWeek = yestoday(dayOfWeek);
                    MainActivity.this.viewFlipper.showPrevious();
                    handler.sendEmptyMessage(2);
                }
                else
                {
                    // previous week
                    dayOfWeek = yestoday(dayOfWeek);
                    weekOfSemister = weekOfSemister - 1;
                    if (weekOfSemister < 1)
                    {
                        weekOfSemister = 1;
                    }
                    handler.sendEmptyMessage(1);
                }
                break;
            case R.id.turnright:
                MainActivity.this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.push_left_in));
                MainActivity.this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.push_left_out));
                if (dayOfWeek < tomorrow(dayOfWeek)) // previous day
                {
                    dayOfWeek = tomorrow(dayOfWeek);
                    Log.i("MainActivity", "dayOfWeek :" + dayOfWeek);
                    MainActivity.this.viewFlipper.showNext();
                    handler.sendEmptyMessage(2);
                }
                else
                {
                    // previous week
                    dayOfWeek = tomorrow(dayOfWeek);
                    Log.i("MainActivity", "dayOfWeek :" + dayOfWeek);
                    weekOfSemister = weekOfSemister + 1;
                    if (weekOfSemister > 20)
                    {
                        weekOfSemister = 20;
                    }
                    handler.sendEmptyMessage(1);
                }
                break;
            default:
                break;
        }
    }
    
    public int getDayOfWeek()
    {
        return dayOfWeek;
    }
    
    public boolean onTouch(View v, MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (v == llShowList)
            {
                return true;
            }
        }
        return gestureDetector.onTouchEvent(event);
    }
    
    private void loadDataFromDB()
    {
        CourseDao dao = new CourseDaoImpl(this);
        if (isTeacher)
        {
            oneWeekCourses =
                dao.getCourseByTeacherID(weekOfSemister,
                    preferences.getInt(CommonConstants.TEACHER_ID, CommonConstants.DEFAULT_TEACHER_ID));
        }
        else
        {
            oneWeekCourses = dao.getWeekCourse(weekOfSemister, isTeacher);
        }
        for (int i = 0; i < 7; i++)
        {
            List<Course> oneDayCourses = oneWeekCourses.get(i);
            FrameLayout frameLayout = (FrameLayout)inflater.inflate(R.layout.courselist, null);
            frameLayout.setOnTouchListener(this);
            ListView listView = (ListView)frameLayout.findViewById(R.id.list);
            listView.setSelector(R.drawable.hide_listview_yellow_selector);
            SimpleCourseAdapter simpleCourseAdapter = new SimpleCourseAdapter(this, oneDayCourses);
            
            setAdapterOfListView(oneDayCourses, simpleCourseAdapter, listView);
            
            viewFlipper.addView(frameLayout);
        }
        
    }
    
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        pos = position;
        Log.i("MainActivity", "onItemLongClick triggered");
        return false;
    }
    
    private int yestoday(int dayOfWeek)
    {
        int yes = dayOfWeek - 1;
        int result = yes < 1 ? 7 : yes;
        return result;
    }
    
    private int tomorrow(int dayOfWeek)
    {
        int result = dayOfWeek + 1 > 7 ? 1 : (dayOfWeek + 1);
        return result;
    }
    
    /**
     * update the listview if you change the data of DB
     */
    private void updateViewFlipper()
    {
        viewFlipper.removeAllViews();
        loadDataFromDB();
        viewFlipper.setDisplayedChild(dayOfWeek - 1);
    }
    
    public class MyGestureListener implements OnGestureListener
    {
        
        public boolean onDown(MotionEvent e)
        {
            return false;
        }
        
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            // �������ͣ�
            // e1����1��ACTION_DOWN MotionEvent
            // e2�����һ��ACTION_MOVE MotionEvent
            // velocityX��X���ϵ��ƶ��ٶȣ�����/��
            // velocityY��Y���ϵ��ƶ��ٶȣ�����/��
            if (null != e1 && null != e2)
            {// sometimes, there will be some Null Pointer Exception about e1 or e2, so we need to ensure the validity
             // of e1 and e2
                if (e1.getX() - e2.getX() > distance)
                {
                    // Fling left
                    Log.v("MainActivity", "onFling Invoked + ");
                    MainActivity.this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                        R.anim.push_left_in));
                    MainActivity.this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                        R.anim.push_left_out));
                    if (dayOfWeek < tomorrow(dayOfWeek)) // previous day
                    {
                        dayOfWeek = tomorrow(dayOfWeek);
                        Log.i("MainActivity", "dayOfWeek :" + dayOfWeek);
                        MainActivity.this.viewFlipper.showNext();
                        handler.sendEmptyMessage(2);
                    }
                    else
                    {
                        // previous week
                        dayOfWeek = tomorrow(dayOfWeek);
                        Log.i("MainActivity", "dayOfWeek :" + dayOfWeek);
                        weekOfSemister = weekOfSemister + 1;
                        if (weekOfSemister > 20)
                        {
                            weekOfSemister = 20;
                        }
                        handler.sendEmptyMessage(1);
                    }
                    return true;
                }
                else if (e2.getX() - e1.getX() > distance)
                {
                    // Fling right
                    Log.v("MainActivity", "onFling Invoked - ");
                    MainActivity.this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                        R.anim.push_right_in));
                    MainActivity.this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                        R.anim.push_right_out));
                    
                    if (dayOfWeek > yestoday(dayOfWeek)) // previous day
                    {
                        dayOfWeek = yestoday(dayOfWeek);
                        MainActivity.this.viewFlipper.showPrevious();
                        handler.sendEmptyMessage(2);
                    }
                    else
                    {
                        // previous week
                        dayOfWeek = yestoday(dayOfWeek);
                        weekOfSemister = weekOfSemister - 1;
                        if (weekOfSemister < 1)
                        {
                            weekOfSemister = 1;
                        }
                        handler.sendEmptyMessage(1);
                    }
                    return true;
                }
            }
            return false;
        }
        
        public void onLongPress(MotionEvent e)
        {
            Log.v("MainActivity", "onLongPress triggered");
            Runnable r = new Runnable()
            {
                public void run()
                {
                    if (pos == -1)
                    {
                        handler.postDelayed(this, 100);
                    }
                    else
                    {
                        CommonConstants.vibratePhone(MainActivity.this, 100);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        AlertDialog dialog = null;
                        
                        builder.setPositiveButton("�޸�", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, AddCourseActivity.class);
                                List<Course> list = oneWeekCourses.get(dayOfWeek - 1);
                                intent.putExtra("id", list.get(pos).getId());
                                intent.putExtra(ACTION_ADD_COURSE, false);
                                startActivity(intent);
                                dialog.dismiss();
                                
                                pos = -1;
                            }
                        });
                        
                        builder.setNeutralButton("ɾ��", new DialogInterface.OnClickListener()
                        {
                            AlertDialog deleteDialog = null;
                            
                            public void onClick(DialogInterface dialog, int which)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                StringBuilder str = new StringBuilder("��ȷ��Ҫɾ�� ");
                                str.append(oneWeekCourses.get(dayOfWeek - 1).get(pos).getcName());
                                str.append(" ���ſ�ô?");
                                builder.setTitle(str.toString());
                                builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
                                {
                                    
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        CourseDao dao = new CourseDaoImpl(MainActivity.this);
                                        dao.deleteCourseById(oneWeekCourses.get(dayOfWeek - 1).get(pos).getId());
                                        deleteDialog.dismiss();
                                        pos = -1;
                                        updateViewFlipper();
                                    }
                                });
                                builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        deleteDialog.dismiss();
                                        pos = -1;
                                    }
                                });
                                deleteDialog = builder.create();
                                dialog.dismiss();
                                deleteDialog.show();
                            }
                        });
                        
                        builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                pos = -1;
                            }
                        });
                        
                        builder.setTitle("��Ҫ�Ըýڿ���");
                        dialog = builder.create();
                        
                        dialog.show();
                        Log.v("MainActivity", "onItemLongTouch ---");
                    }
                }
            };
            handler.post(r);
        }
        
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            return false;
        }
        
        public void onShowPress(MotionEvent e)
        {
        }
        
        public boolean onSingleTapUp(MotionEvent e)
        {
            return false;
        }
        
    }
    
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // does nothing
    }
    
    public void onSensorChanged(SensorEvent event)
    {
        int sensorType = event.sensor.getType();
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER)
        {
            
            long curTime = System.currentTimeMillis();
            // only allow one update every 120ms.
            if ((curTime - lastUpdate) > 120)
            {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                
                x = values[SensorManager.DATA_X];
                y = values[SensorManager.DATA_Y];
                z = values[SensorManager.DATA_Z];
                
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD)
                {
                    // yes, this is a shake action! Do something about it!
                    dayOfWeek = CommonConstants.getWeekNumFromStr(CommonConstants.getCurrentDayOfWeek());
                    Log.i("MainActivity", "dayOfWeek == " + dayOfWeek);
                    if (weekOfSemister == (preferences.getInt(CommonConstants.WEEKOFSEMISTER,
                        CommonConstants.DEFAULT_WEEKOFSEMISTER)))
                    {
                        viewFlipper.setDisplayedChild(dayOfWeek - 1);
                        
                        tvTitle.setText(CommonConstants.getStrFromWeekNum(dayOfWeek));
                    }
                    else
                    {
                        weekOfSemister =
                            preferences.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER);
                        updateViewFlipper();
                        tvTitle.setText(CommonConstants.getStrFromWeekNum(dayOfWeek));
                        Toast.makeText(this, "��" + weekOfSemister + "�ܿα�", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("MainActivity", "weekOfSemister = " + weekOfSemister);
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }
    
    public boolean onLongClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tvTitle:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);// )
                builder.setTitle("ѡ���ܴ�");
                builder.setItems(CommonConstants.WEEKOFSEMISTER_INNUMBER, new DialogInterface.OnClickListener()
                {
                    
                    public void onClick(DialogInterface arg0, int which)
                    {
                        if (which != weekOfSemister)
                        {
                            weekOfSemister = which;
                            updateViewFlipper();
                        }
                        Toast.makeText(MainActivity.this, "���ǵ�" + which + "��", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
                break;
            default:
                break;
        }
        return true;
    }
    
    public class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    updateViewFlipper();
                    tvTitle.setText(CommonConstants.getStrFromWeekNum(dayOfWeek));
                    Toast.makeText(MainActivity.this, "��" + weekOfSemister + "�ܿα�", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    tvTitle.setText(CommonConstants.getStrFromWeekNum(dayOfWeek));
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
