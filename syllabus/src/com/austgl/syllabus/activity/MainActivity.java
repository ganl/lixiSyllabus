package com.austgl.syllabus.activity;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.austgl.Zxing.CaptureActivity;
import com.austgl.syllabus.R;
import com.austgl.syllabus.SyllabusApplication;
import com.austgl.syllabus.adapter.SimpleCourseAdapter;
import com.austgl.syllabus.bean.Course;
import com.austgl.syllabus.db.CourseDao;
import com.austgl.syllabus.db.CourseDaoImpl;
import com.austgl.syllabus.utils.CommonConstants;
import com.austgl.syllabus.utils.HttpConnect;
import com.austgl.syllabus.utils.LogUtil;

public class MainActivity extends Activity implements OnClickListener,
		OnTouchListener, OnItemLongClickListener, SensorEventListener,
		OnLongClickListener {
	private static final String LOGTAG = LogUtil.makeLogTag(MainActivity.class);
	public static final String ACTION_ADD_COURSE = "add";

	private TextView tvLeft; // 左键

	private TextView tvTitle; // 标题

	private TextView tvRightT; // 右键

	private int dayOfWeek; // 星期几

	private int weekOfSemister; // 第几周

	// private ImageView ivAddCourse; // 添加课程

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

	private boolean isFromOnCreate = true; // use to identify whether it is the
											// first creation of Acitivity

	public SensorManager sensorManager;

	private long lastUpdate = -1;

	private float x, y, z;

	private float last_x, last_y, last_z;

	private static final int SHAKE_THRESHOLD = 850;

	private boolean isTurningToAnotherActivity = false;

	private boolean isTeacher = false;


    private Button buttonQcode, buttonDelete, buttonWith, buttonPlace, buttonMusic, buttonThought, buttonLock;
    private Animation animationTranslate, animationRotate, animationScale;
    private static int width, height;
    private RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
    private static Boolean isClick = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);

		SyllabusApplication.getInstance().addActivity(this);

		preferences = CommonConstants.getMyPreferences(this);
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		/**
		 * 如果没有显示过欢迎界面，先显示他
		 */
		if (!preferences.getBoolean(CommonConstants.SHOW_WELCOME, false)) {
			Intent intent = new Intent();
			intent.setClass(this, WelcomeActivity.class);
			startActivity(intent);
			isTurningToAnotherActivity = true;
			this.finish();
		}

		/**
		 * 如果没有显示过设置界面，先显示他
		 */
		if (!isTurningToAnotherActivity
				&& !preferences.getBoolean(CommonConstants.IS_SETUP_ALREADY,
						false)) {
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

        initialButton();
	}

    private void initialButton()
    {
        // TODO Auto-generated method stub
        Display display = getWindowManager().getDefaultDisplay();
        height = display.getHeight();
        width = display.getWidth();
        Log.v("width  & height is:", String.valueOf(width) + ", " + String.valueOf(height));

        params.height = 50;
        params.width = 50;
        //设置边距  (int left, int top, int right, int bottom)
        params.setMargins(10, height - 98, 0, 10);

        buttonLock = (Button) findViewById(R.id.button_composer_lock);
        buttonLock.setLayoutParams(params);

        buttonThought = (Button) findViewById(R.id.button_composer_thought);
        buttonThought.setLayoutParams(params);

        buttonMusic = (Button) findViewById(R.id.button_composer_music);
        buttonMusic.setLayoutParams(params);

        buttonPlace = (Button) findViewById(R.id.button_composer_place);
        buttonPlace.setLayoutParams(params);

        buttonWith = (Button) findViewById(R.id.button_composer_with);
        buttonWith.setLayoutParams(params);

        buttonQcode = (Button) findViewById(R.id.button_composer_qcode);
        buttonQcode.setLayoutParams(params);

        buttonDelete = (Button) findViewById(R.id.button_friends_delete);
        buttonDelete.setLayoutParams(params);

        buttonDelete.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if(isClick == false)
                {
                    isClick = true;
                    buttonDelete.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));
                    buttonQcode.startAnimation(animTranslate(0.0f, -180.0f, 10, height - 240, buttonQcode, 80));
                    buttonWith.startAnimation(animTranslate(30.0f, -150.0f, 60, height - 230, buttonWith, 100));
                    buttonPlace.startAnimation(animTranslate(70.0f, -120.0f, 110, height - 210, buttonPlace, 120));
                    buttonMusic.startAnimation(animTranslate(80.0f, -110.0f, 150, height - 180, buttonMusic, 140));
                    buttonThought.startAnimation(animTranslate(90.0f, -60.0f, 175, height - 135, buttonThought, 160));
                    buttonLock.startAnimation(animTranslate(170.0f, -30.0f, 190, height - 90, buttonLock, 180));

                }
                else
                {
                    isClick = false;
                    buttonDelete.startAnimation(animRotate(90.0f, 0.5f, 0.45f));
                    buttonQcode.startAnimation(animTranslate(0.0f, 140.0f, 10, height - 98, buttonQcode, 180));
                    buttonWith.startAnimation(animTranslate(-50.0f, 130.0f, 10, height - 98, buttonWith, 160));
                    buttonPlace.startAnimation(animTranslate(-100.0f, 110.0f, 10, height - 98, buttonPlace, 140));
                    buttonMusic.startAnimation(animTranslate(-140.0f, 80.0f, 10, height - 98, buttonMusic, 120));
                    buttonThought.startAnimation(animTranslate(-160.0f, 40.0f, 10, height - 98, buttonThought, 80));
                    buttonLock.startAnimation(animTranslate(-170.0f, 0.0f, 10, height - 98, buttonLock, 50));

                }

            }
        });
        buttonQcode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                buttonQcode.startAnimation(setAnimScale(2.5f, 2.5f));
                buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonLock.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));
                Intent it = new Intent();
                it.setClass(MainActivity.this, CaptureActivity.class);
                startActivity(it);
            }
        });
        buttonWith.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                buttonWith.startAnimation(setAnimScale(2.5f, 2.5f));
                buttonQcode.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonLock.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));
            }
        });
        buttonPlace.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                buttonPlace.startAnimation(setAnimScale(2.5f, 2.5f));
                buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonQcode.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonLock.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));
            }
        });
        buttonMusic.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                buttonMusic.startAnimation(setAnimScale(2.5f, 2.5f));
                buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonQcode.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonLock.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));
            }
        });
        buttonThought.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                buttonThought.startAnimation(setAnimScale(2.5f, 2.5f));
                buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonQcode.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonLock.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
                //Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:100"));
                //MainActivity.this.startActivity(intent);
                buttonDelete.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));
                Uri uri = Uri.parse("smsto:"); 
                Intent it = new Intent(Intent.ACTION_SENDTO, uri); 
                it.putExtra("sms_body", ""); 
                startActivity(it); 
            }
        });
        buttonLock.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                buttonLock.startAnimation(setAnimScale(2.5f, 2.5f));
                buttonPlace.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonWith.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonQcode.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));
            }
        });

    }

    protected Animation setAnimScale(float toX, float toY)
    {
        // TODO Auto-generated method stub
        animationScale = new ScaleAnimation(1f, toX, 1f, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.45f);
        animationScale.setInterpolator(MainActivity.this, android.R.anim.accelerate_decelerate_interpolator);
        animationScale.setDuration(500);
        animationScale.setFillAfter(false);
        return animationScale;

    }

    protected Animation animRotate(float toDegrees, float pivotXValue, float pivotYValue)
    {
        // TODO Auto-generated method stub
        animationRotate = new RotateAnimation(0, toDegrees, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
        animationRotate.setAnimationListener(new Animation.AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                // TODO Auto-generated method stub
                animationRotate.setFillAfter(true);
            }
        });
        return animationRotate;
    }
    //移动的动画效果
	/*
	 * TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
	 *
	 * float fromXDelta:这个参数表示动画开始的点离当前View X坐标上的差值；
     *
　　       * float toXDelta, 这个参数表示动画结束的点离当前View X坐标上的差值；
     *
　　       * float fromYDelta, 这个参数表示动画开始的点离当前View Y坐标上的差值；
     *
　　       * float toYDelta)这个参数表示动画开始的点离当前View Y坐标上的差值；
	 */
    protected Animation animTranslate(float toX, float toY, final int lastX, final int lastY,
                                      final Button button, long durationMillis)
    {
        // TODO Auto-generated method stub
        animationTranslate = new TranslateAnimation(0, toX, 0, toY);
        animationTranslate.setAnimationListener(new Animation.AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                // TODO Auto-generated method stub
                params = new RelativeLayout.LayoutParams(0, 0);
                params.height = 50;
                params.width = 50;
                params.setMargins(lastX, lastY, 0, 0);
                button.setLayoutParams(params);
                button.clearAnimation();

            }
        });
        animationTranslate.setDuration(durationMillis);
        return animationTranslate;
    }



    private void initViews() {
		tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvLeft.setText("设置");
		tvLeft.setOnClickListener(this);

		tvRightT = (TextView) findViewById(R.id.tvRightT);
		tvRightT.setText("一周");
		tvRightT.setOnClickListener(this);

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setOnClickListener(this);
		tvTitle.setOnLongClickListener(this);

		llShowList = (LinearLayout) findViewById(R.id.showlist);
		llShowList.setOnTouchListener(this);

		ivTurnLeft = (ImageView) findViewById(R.id.turnleft);
		ivTurnRight = (ImageView) findViewById(R.id.turnright);
		ivTurnLeft.setOnClickListener(this);
		ivTurnRight.setOnClickListener(this);

		btnAddCourse = (Button) findViewById(R.id.addcourse);
		btnAddCourse.setOnClickListener(this);

		Log.i(LOGTAG, "in onCreate()");
	}

	private void initData() {

		String str = CommonConstants.getCurrentDayOfWeek();

		Intent intent = getIntent();

		Bundle extras = intent.getExtras();
		int appWidgetId = 0;
		if (null != extras) {
			appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
			Log.i(LOGTAG, appWidgetId + "appWidgetId");
		}
		// if it is not from AppWidget and is not first created
		if (0 == appWidgetId && -1 != intent.getIntExtra("dayOfWeek", -1)) {
			Log.i(LOGTAG, "before intent, dayOfWeek is " + dayOfWeek);
			dayOfWeek = intent.getIntExtra("dayOfWeek", -1);
			Log.i(LOGTAG, "from widget intent, dayOfWeek is " + dayOfWeek);
			weekOfSemister = preferences.getInt(CommonConstants.WEEKOFSEMISTER,
					CommonConstants.DEFAULT_WEEKOFSEMISTER);
			str = CommonConstants.getStrFromWeekNum(dayOfWeek);
		} else {
			// if it is from AppWidget or first created
			if (str.matches("^[a-zA-Z]*")) {
				dayOfWeek = CommonConstants.getDayOfWeekFromEng(str);
				str = CommonConstants.getStrFromWeekNum(dayOfWeek);
			} else {
				dayOfWeek = CommonConstants.getWeekNumFromStr(str);
			}
			weekOfSemister = preferences.getInt(CommonConstants.WEEKOFSEMISTER,
					CommonConstants.DEFAULT_WEEKOFSEMISTER);
		}

		if (!isTurningToAnotherActivity
				&& !preferences.getBoolean("isSetUpAlready", false)) {// if the
																		// user
																		// has
																		// not
																		// set
																		// up
																		// the
																		// profile,
																		// we
																		// need
																		// to
																		// encourage
																		// them
																		// to
																		// do;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			AlertDialog dialog = null;
			builder.setTitle("添加课程之前，请先设置本周次数及相关重要参数先");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(MainActivity.this,
									SetUpActivity.class);
							startActivity(intent);
							dialog.dismiss();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = builder.create();
			dialog.show();
		}
		tvTitle.setText(str);

		viewFlipper = (ViewFlipper) findViewById(R.id.viewfilpper);

		loadDataFromDB();

		viewFlipper.setDisplayedChild(dayOfWeek - 1);

	}

	@Override
	protected void onResume() {
		if (isFromOnCreate) {
			isFromOnCreate = false;
		} else {// if come back from set or add activity, we should update the
				// view
			if (((SyllabusApplication) getApplication()).isWeekHasBeenChanged) {
				weekOfSemister = preferences.getInt(
						CommonConstants.WEEKOFSEMISTER,
						CommonConstants.DEFAULT_WEEKOFSEMISTER);
				((SyllabusApplication) getApplication()).isWeekHasBeenChanged = false;
				updateViewFlipper();
			}
			if (((SyllabusApplication) getApplication()).isDataHasBeenMotifyed) {
				((SyllabusApplication) getApplication()).isDataHasBeenMotifyed = false;
				updateViewFlipper();
			}

		}

		if (preferences.getBoolean(CommonConstants.IS_ACCELEREMETER_SUPPORTED,
				true)) {
			Editor editor = preferences.edit();
			if (!sensorManager.registerListener(this,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL)) {
				if (preferences.getBoolean(CommonConstants.IS_FIRST_RUN, true)) {
					Toast.makeText(this, "您的手机不支持摇一摇回到当天课程表功能",
							Toast.LENGTH_SHORT).show();
					editor.putBoolean(
							CommonConstants.IS_ACCELEREMETER_SUPPORTED, false);
				}
			} else {
				if (preferences.getBoolean(CommonConstants.IS_FIRST_RUN, true)) {
					Toast.makeText(this, "摇一摇回到当天课程哦！", Toast.LENGTH_SHORT)
							.show();
					editor.putBoolean(
							CommonConstants.IS_ACCELEREMETER_SUPPORTED, true);
				}
			}

			editor.putBoolean(CommonConstants.IS_FIRST_RUN, false);
			editor.commit();
		}

		super.onResume();
	}

	@Override
	protected void onStop() {
		sensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	protected void onPause() {
		sensorManager.unregisterListener(this);
		super.onPause();
	}

	private void setAdapterOfListView(List<Course> tempCourses,
			SimpleCourseAdapter adapter, ListView listView) {
		adapter = new SimpleCourseAdapter(this, tempCourses);

		listView.setAdapter(adapter);

		listView.setOnTouchListener(this);
		listView.setOnItemLongClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {

		case R.id.menu_settings:
			/**
			 * for test
			 */
			// intent = new Intent(this, UpLoadedActivity.class);
			// // intent.putExtra("fromMainActivity", true);
			// startActivity(intent);
			// ***************************************************************
			if (!preferences.getBoolean(CommonConstants.LOGINED, false)
					&& HttpConnect.isNetworkHolding(this)) {
				intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			} else if (HttpConnect.isNetworkHolding(this)) {
				intent = new Intent(this, UpLoadedActivity.class);
				// intent.putExtra("fromMainActivity", true);
				startActivity(intent);
			} else {
				Toast.makeText(this, "您的网络没有打开，同步前请先打开网络", Toast.LENGTH_SHORT)
						.show();
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

	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.tvLeft:
			intent = new Intent(MainActivity.this, SetUpActivity.class);
			intent.putExtra("fromMainActivity", true);
			this.startActivity(intent);
			break;
		case R.id.addcourse:
			intent = new Intent(this, AddCourseActivity.class);
			int index = 0;
			if (0 == oneWeekCourses.get(dayOfWeek - 1).size()) {
				index = 1;
			} else {
				List<Course> dayCourses = oneWeekCourses.get(dayOfWeek - 1);
				index = dayCourses.get(dayCourses.size() - 1).getCourseIndex() + 1;
			}
			if (index > 6) {
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
			builder.setTitle("请选择");
			builder.setItems(CommonConstants.DAYOFWEEKS_INCHN,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dayOfWeek = which + 1;
							viewFlipper.setDisplayedChild(which);
							tvTitle.setText(CommonConstants
									.getStrFromWeekNum(dayOfWeek));
						}
					});
			builder.create().show();
			break;

		case R.id.turnleft:
			MainActivity.this.viewFlipper.setInAnimation(AnimationUtils
					.loadAnimation(MainActivity.this, R.anim.push_right_in));
			MainActivity.this.viewFlipper.setOutAnimation(AnimationUtils
					.loadAnimation(MainActivity.this, R.anim.push_right_out));

			if (dayOfWeek > yestoday(dayOfWeek)) // previous day
			{
				dayOfWeek = yestoday(dayOfWeek);
				MainActivity.this.viewFlipper.showPrevious();
				handler.sendEmptyMessage(2);
			} else {
				// previous week
				dayOfWeek = yestoday(dayOfWeek);
				weekOfSemister = weekOfSemister - 1;
				if (weekOfSemister < 1) {
					weekOfSemister = 1;
				}
				handler.sendEmptyMessage(1);
			}
			break;
		case R.id.turnright:
			MainActivity.this.viewFlipper.setInAnimation(AnimationUtils
					.loadAnimation(MainActivity.this, R.anim.push_left_in));
			MainActivity.this.viewFlipper.setOutAnimation(AnimationUtils
					.loadAnimation(MainActivity.this, R.anim.push_left_out));
			if (dayOfWeek < tomorrow(dayOfWeek)) // previous day
			{
				dayOfWeek = tomorrow(dayOfWeek);
				Log.i(LOGTAG, "dayOfWeek :" + dayOfWeek);
				MainActivity.this.viewFlipper.showNext();
				handler.sendEmptyMessage(2);
			} else {
				// previous week
				dayOfWeek = tomorrow(dayOfWeek);
				Log.i(LOGTAG, "dayOfWeek :" + dayOfWeek);
				weekOfSemister = weekOfSemister + 1;
				if (weekOfSemister > 20) {
					weekOfSemister = 20;
				}
				handler.sendEmptyMessage(1);
			}
			break;
		default:
			break;
		}
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (v == llShowList) {
				return true;
			}
		}
		return gestureDetector.onTouchEvent(event);
	}

	private void loadDataFromDB() {
		CourseDao dao = new CourseDaoImpl(this);
		if (isTeacher) {
			oneWeekCourses = dao.getCourseByTeacherID(weekOfSemister,
					preferences.getInt(CommonConstants.TEACHER_ID,
							CommonConstants.DEFAULT_TEACHER_ID));
		} else {
			oneWeekCourses = dao.getWeekCourse(weekOfSemister, isTeacher);
		}
		for (int i = 0; i < 7; i++) {
			List<Course> oneDayCourses = oneWeekCourses.get(i);
			FrameLayout frameLayout = (FrameLayout) inflater.inflate(
					R.layout.courselist, null);
			frameLayout.setOnTouchListener(this);
			ListView listView = (ListView) frameLayout.findViewById(R.id.list);
			listView.setSelector(R.drawable.hide_listview_yellow_selector);
			SimpleCourseAdapter simpleCourseAdapter = new SimpleCourseAdapter(
					this, oneDayCourses);

			setAdapterOfListView(oneDayCourses, simpleCourseAdapter, listView);

			viewFlipper.addView(frameLayout);
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		pos = position;
		Log.i(LOGTAG, "onItemLongClick triggered");
		return false;
	}

	private int yestoday(int dayOfWeek) {
		int yes = dayOfWeek - 1;
		int result = yes < 1 ? 7 : yes;
		return result;
	}

	private int tomorrow(int dayOfWeek) {
		int result = dayOfWeek + 1 > 7 ? 1 : (dayOfWeek + 1);
		return result;
	}

	/**
	 * update the listview if you change the data of DB
	 */
	private void updateViewFlipper() {
		viewFlipper.removeAllViews();
		loadDataFromDB();
		viewFlipper.setDisplayedChild(dayOfWeek - 1);
	}

	public class MyGestureListener implements OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// 参数解释：
			// e1：第1个ACTION_DOWN MotionEvent
			// e2：最后一个ACTION_MOVE MotionEvent
			// velocityX：X轴上的移动速度，像素/秒
			// velocityY：Y轴上的移动速度，像素/秒
			if (null != e1 && null != e2) {// sometimes, there will be some Null
											// Pointer Exception about e1 or e2,
											// so we need to ensure the validity
											// of e1 and e2
				if (e1.getX() - e2.getX() > distance) {
					// Fling left
					Log.v(LOGTAG, "onFling Invoked + ");
					MainActivity.this.viewFlipper.setInAnimation(AnimationUtils
							.loadAnimation(MainActivity.this,
									R.anim.push_left_in));
					MainActivity.this.viewFlipper
							.setOutAnimation(AnimationUtils.loadAnimation(
									MainActivity.this, R.anim.push_left_out));
					if (dayOfWeek < tomorrow(dayOfWeek)) // previous day
					{
						dayOfWeek = tomorrow(dayOfWeek);
						Log.i(LOGTAG, "dayOfWeek :" + dayOfWeek);
						MainActivity.this.viewFlipper.showNext();
						handler.sendEmptyMessage(2);
					} else {
						// previous week
						dayOfWeek = tomorrow(dayOfWeek);
						Log.i(LOGTAG, "dayOfWeek :" + dayOfWeek);
						weekOfSemister = weekOfSemister + 1;
						if (weekOfSemister > 20) {
							weekOfSemister = 20;
						}
						handler.sendEmptyMessage(1);
					}
					return true;
				} else if (e2.getX() - e1.getX() > distance) {
					// Fling right
					Log.v(LOGTAG, "onFling Invoked - ");
					MainActivity.this.viewFlipper.setInAnimation(AnimationUtils
							.loadAnimation(MainActivity.this,
									R.anim.push_right_in));
					MainActivity.this.viewFlipper
							.setOutAnimation(AnimationUtils.loadAnimation(
									MainActivity.this, R.anim.push_right_out));

					if (dayOfWeek > yestoday(dayOfWeek)) // previous day
					{
						dayOfWeek = yestoday(dayOfWeek);
						MainActivity.this.viewFlipper.showPrevious();
						handler.sendEmptyMessage(2);
					} else {
						// previous week
						dayOfWeek = yestoday(dayOfWeek);
						weekOfSemister = weekOfSemister - 1;
						if (weekOfSemister < 1) {
							weekOfSemister = 1;
						}
						handler.sendEmptyMessage(1);
					}
					return true;
				}
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			Log.v(LOGTAG, "onLongPress triggered");
			Runnable r = new Runnable() {
				@Override
				public void run() {
					if (pos == -1) {
						handler.postDelayed(this, 100);
					} else {
						CommonConstants.vibratePhone(MainActivity.this, 100);
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MainActivity.this);
						AlertDialog dialog = null;

						builder.setPositiveButton("修改",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent();
										intent.setClass(MainActivity.this,
												AddCourseActivity.class);
										List<Course> list = oneWeekCourses
												.get(dayOfWeek - 1);
										intent.putExtra("id", list.get(pos)
												.getId());
										intent.putExtra(ACTION_ADD_COURSE,
												false);
										startActivity(intent);
										dialog.dismiss();

										pos = -1;
									}
								});

						builder.setNeutralButton("删除",
								new DialogInterface.OnClickListener() {
									AlertDialog deleteDialog = null;

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										AlertDialog.Builder builder = new AlertDialog.Builder(
												MainActivity.this);
										StringBuilder str = new StringBuilder(
												"你确定要删除 ");
										str.append(oneWeekCourses
												.get(dayOfWeek - 1).get(pos)
												.getcName());
										str.append(" 这门课么?");
										builder.setTitle(str.toString());
										builder.setPositiveButton(
												"确定",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														CourseDao dao = new CourseDaoImpl(
																MainActivity.this);
														dao.deleteCourseById(oneWeekCourses
																.get(dayOfWeek - 1)
																.get(pos)
																.getId());
														deleteDialog.dismiss();
														pos = -1;
														updateViewFlipper();
													}
												});
										builder.setNegativeButton(
												"取消",
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														deleteDialog.dismiss();
														pos = -1;
													}
												});
										deleteDialog = builder.create();
										dialog.dismiss();
										deleteDialog.show();
									}
								});

						builder.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										pos = -1;
									}
								});

						builder.setTitle("你要对该节课做");
						dialog = builder.create();

						dialog.show();
						Log.v(LOGTAG, "onItemLongTouch ---");
					}
				}
			};
			handler.post(r);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// does nothing
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {

			long curTime = System.currentTimeMillis();
			// only allow one update every 120ms.
			if ((curTime - lastUpdate) > 120) {
				long diffTime = (curTime - lastUpdate);
				lastUpdate = curTime;

				x = values[SensorManager.DATA_X];
				y = values[SensorManager.DATA_Y];
				z = values[SensorManager.DATA_Z];

				float speed = Math.abs(x + y + z - last_x - last_y - last_z)
						/ diffTime * 10000;
				if (speed > SHAKE_THRESHOLD) {
					// yes, this is a shake action! Do something about it!
					dayOfWeek = CommonConstants
							.getWeekNumFromStr(CommonConstants
									.getCurrentDayOfWeek());
					Log.i(LOGTAG, "dayOfWeek == " + dayOfWeek);
					if (weekOfSemister == (preferences.getInt(
							CommonConstants.WEEKOFSEMISTER,
							CommonConstants.DEFAULT_WEEKOFSEMISTER))) {
						viewFlipper.setDisplayedChild(dayOfWeek - 1);

						tvTitle.setText(CommonConstants
								.getStrFromWeekNum(dayOfWeek));
					} else {
						weekOfSemister = preferences.getInt(
								CommonConstants.WEEKOFSEMISTER,
								CommonConstants.DEFAULT_WEEKOFSEMISTER);
						updateViewFlipper();
						tvTitle.setText(CommonConstants
								.getStrFromWeekNum(dayOfWeek));
						Toast.makeText(this, "第" + weekOfSemister + "周课表",
								Toast.LENGTH_SHORT).show();
					}
					Log.i(LOGTAG, "weekOfSemister = " + weekOfSemister);
				}
				last_x = x;
				last_y = y;
				last_z = z;
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.tvTitle:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);// )
			builder.setTitle("选择周次");
			builder.setItems(CommonConstants.WEEKOFSEMISTER_INNUMBER,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int which) {
							if (which != weekOfSemister) {
								weekOfSemister = which;
								updateViewFlipper();
							}
							Toast.makeText(MainActivity.this,
									"这是第" + which + "周", Toast.LENGTH_SHORT)
									.show();
						}
					});
			builder.create().show();
			break;
		default:
			break;
		}
		return true;
	}

	public class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				updateViewFlipper();
				tvTitle.setText(CommonConstants.getStrFromWeekNum(dayOfWeek));
				Toast.makeText(MainActivity.this, "第" + weekOfSemister + "周课表",
						Toast.LENGTH_SHORT).show();
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
