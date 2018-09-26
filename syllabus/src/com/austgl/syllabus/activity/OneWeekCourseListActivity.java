package com.austgl.syllabus.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.austgl.syllabus.R;
import com.austgl.syllabus.SyllabusApplication;
import com.austgl.syllabus.adapter.OneDayCourseAdapter;
import com.austgl.syllabus.bean.Course;
import com.austgl.syllabus.db.CourseDao;
import com.austgl.syllabus.db.CourseDaoImpl;
import com.austgl.syllabus.task.GetOneWeekCourseListTask;
import com.austgl.syllabus.utils.CommonConstants;
import com.austgl.syllabus.utils.LogUtil;

/**
 * 一周课表全面展示，用ExpandableListView二级展示
 * 
 * @author Administrator
 * 
 */
public class OneWeekCourseListActivity extends ExpandableListActivity implements
		OnClickListener {
	private static final String LOGTAG = LogUtil
			.makeLogTag(OneWeekCourseListActivity.class);
	private TextView tvLeft;

	private TextView tvTitle;

	private TextView tvRightT;

	private ExpandableListView lvOneWeekListView;

	private OneDayCourseAdapter adapter;

	private Handler handler;

	private int weekOfSemister;

	private ProgressDialog progressDialog;

	private List<Map<String, String>> groupList;

	private List<List<Map<String, String>>> childList;

	private AlertDialog alertDialog;

	private boolean isTeacher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oneweekcourselist);
		SyllabusApplication.getInstance().addActivity(this);

		isTeacher = CommonConstants.getMyPreferences(this).getBoolean(
				CommonConstants.IS_TEACHER, false);

		groupList = new ArrayList<Map<String, String>>();

		for (int i = 0; i < 7; i++) {
			Map<String, String> map = new HashMap<String, String>();
			groupList.add(map);
		}

		initViews();

		progressDialog = new ProgressDialog(this);

		handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					childList = (List<List<Map<String, String>>>) msg.obj;
					adapter = new OneDayCourseAdapter(groupList, childList,
							OneWeekCourseListActivity.this);
					progressDialog.dismiss();
					lvOneWeekListView.setAdapter(adapter);
					int groupCount = lvOneWeekListView.getCount();
					for (int i = 0; i < groupCount; i++) {
						lvOneWeekListView.expandGroup(i);
					}
					break;
				case 2:
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

		initData();

	}

	private void initViews() {
		tvLeft = (TextView) findViewById(R.id.tvLeft);
		tvLeft.setText("返回");
		tvLeft.setOnClickListener(this);

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("学期总表");
		tvTitle.setOnClickListener(this);

		tvRightT = (TextView) findViewById(R.id.tvRightT);
		tvRightT.setText("当周");
		tvRightT.setTag("当周"); // identify the current week tag
		tvRightT.setOnClickListener(this);

		lvOneWeekListView = getExpandableListView();

	}

	private void initData() {
		Intent intent = getIntent();

		weekOfSemister = intent.getIntExtra(CommonConstants.WEEKOFSEMISTER, -1); // intent
																					// will
																					// contain
																					// this
																					// value
																					// if
																					// this
																					// intent
																					// is
																					// launched
																					// from
																					// MainActivity
		if (-1 != weekOfSemister) {
			tvLeft.setText("返回");
			tvRightT.setText("学期");
			tvRightT.setTag("学期");
			tvTitle.setText("当周课表");
			getCurrentWeekData(weekOfSemister);
		} else {
			SharedPreferences preferences = CommonConstants
					.getMyPreferences(this);
			String universityName = preferences.getString(
					CommonConstants.UNIVERSITY_NAME, null);// intent.getStringExtra(CommonConstants.UNIVERSITY_NAME);
			String departmentName, gradeNum, majorName, className, teacherName;
			if (!isTeacher) {
				departmentName = preferences.getString(
						CommonConstants.DEPARTMENT_NAME, null);// intent.getStringExtra(CommonConstants.DEPARTMENT_NAME);

				gradeNum = preferences.getString(CommonConstants.GRADE_NUM,
						null);// intent.getStringExtra(CommonConstants.GRADE_NUM);
				majorName = preferences.getString(CommonConstants.MAJOR_NAME,
						null);// intent.getStringExtra(CommonConstants.MAJOR_NAME);
				className = preferences.getString(CommonConstants.CLASS_NAME,
						null);// intent.getStringExtra(CommonConstants.CLASS_NAME);

				if (null != universityName) {
					GetOneWeekCourseListTask task = new GetOneWeekCourseListTask(
							this, handler);
					task.execute(universityName, departmentName, majorName,
							gradeNum, className);
					progressDialog.setTitle("请稍候");

					progressDialog.show();
				}
			} else {
				teacherName = preferences.getString(
						CommonConstants.TEACHER_NAME, null);
				if (null != teacherName) {
					GetOneWeekCourseListTask task = new GetOneWeekCourseListTask(
							this, handler);
					task.execute(universityName, teacherName);
					progressDialog.setTitle("请稍候");

					progressDialog.show();
				}
			}

			tvLeft.setText("当天");
		}

	}

	private void getCurrentWeekData(int weekOfSemister) {
		CourseDao dao = new CourseDaoImpl(this);

		List<List<Course>> oneWeekCourses = dao.getWeekCourse(weekOfSemister,
				isTeacher);

		childList = new ArrayList<List<Map<String, String>>>();
		for (int i = 0; i < oneWeekCourses.size(); i++) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (int j = 0; j < oneWeekCourses.get(i).size(); j++) {
				Map<String, String> map = oneWeekCourses.get(i).get(j)
						.beanToMap();
				list.add(map);
			}
			childList.add(list);
		}

		Message message = Message.obtain();
		message.what = 1;
		message.obj = childList;
		handler.sendMessage(message);
	}

	public void setDataToAdapter() {

	}

	public void showDialog(String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(content);
		builder.setPositiveButton("去添加", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(OneWeekCourseListActivity.this,
						MainActivity.class);
				intent.putExtra("weekNum", 1);
				intent.putExtra("weekOfSemister", 1);
				OneWeekCourseListActivity.this.startActivity(intent);
				OneWeekCourseListActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog = builder.create();
		alertDialog.show();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tvLeft:
			if (-1 != weekOfSemister) {
				this.finish();
			} else {
				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
				this.finish();
			}
			break;
		case R.id.tvRightT:
			if ("当周".equals(tvRightT.getTag()))// tvRightT.getTag()))
			{
				tvRightT.setText("学期");
				tvTitle.setText("当周课表");
				tvRightT.setTag("学期");
				getCurrentWeekData(CommonConstants
						.getCurrentWeekOfSemister(this));
			} else if ("学期".equals(tvRightT.getTag())) {
				tvRightT.setText("当周");
				tvTitle.setText("学期总表");
				tvRightT.setTag("当周");

				// 得倒学期总表的DAO还没写
				getSemisterData();
			}
			break;
		case R.id.tvTitle:
			// 广告设置
			break;
		default:
			break;
		}

	}

	private void getSemisterData() {
		CourseDao dao = new CourseDaoImpl(this);

		List<Course> allCourses = dao.getAllCourse(isTeacher);
		List<List<Map<String, String>>> oneWeekCourses = new ArrayList<List<Map<String, String>>>();

		for (int i = 1; i < 8; i++) {
			ArrayList<Map<String, String>> dayList = new ArrayList<Map<String, String>>();
			for (int j = 0; j < allCourses.size(); j++) {
				if (i == allCourses.get(j).getcWeekday()) {
					dayList.add(allCourses.get(j).beanToMap());
					// result.remove(j);
				}
			}
			oneWeekCourses.add(dayList);
		}

		Message message = Message.obtain();
		message.what = 1;
		message.obj = oneWeekCourses;
		handler.sendMessage(message);

	}
}
