package com.austgl.syllabus.task;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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

import com.austgl.syllabus.bean.Course;
import com.austgl.syllabus.db.CourseDao;
import com.austgl.syllabus.db.CourseDaoImpl;
import com.austgl.syllabus.utils.CommonConstants;
import com.austgl.syllabus.utils.HttpConnect;
import com.austgl.syllabus.utils.Urls;

public class GetOneWeekCourseListTask extends
		AsyncTask<String, String, List<Course>> {

	private static final String RESULT_USER_NO_COURSES = "0";

	private static final String RESULT_NO_USER = "2";

	private static final String RESULT_OK = "1";

	private Context context;

	private Handler handler;

	private int classid;

	private boolean isTeacher;

	private SharedPreferences preferences;

	public GetOneWeekCourseListTask(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		preferences = CommonConstants.getMyPreferences(context);
	}

	@Override
	protected List<Course> doInBackground(String... params) {
		// List<Course> courses = null;
		if (5 == params.length) {
			isTeacher = false;
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (int i = 0; i < params.length; i++) {
				NameValuePair pair = new BasicNameValuePair(
						CommonConstants.STUDENT_INFORMATION[i],
						URLEncoder.encode(params[i]));// params[i]);
				list.add(pair);
			}
			// courses = new ArrayList<Course>();
			try {
				System.out.println(Urls.getStudentLoginUrl());
				String s = HttpConnect.postHttpString(
						Urls.getStudentLoginUrl(), list);
				System.out.println(s);
				if (!"".equals(s) && null != s) {
					JSONObject obj = new JSONObject(s);
					String result = obj.optString("result");

					classid = obj.optInt(CommonConstants.CLASSID);

					Editor editor = preferences.edit();
					editor.putInt(CommonConstants.CLASSID, classid);
					editor.putBoolean(CommonConstants.LOGINED, true);
					editor.commit();

					if (RESULT_USER_NO_COURSES.equals(result)) {
						showToastOnUiThread("服务器无您班级课程，请为班级造福，添加课程之服务器吧。");
						handler.sendEmptyMessageDelayed(2, 2000);
					} else if (RESULT_NO_USER.equals(result)) {
						showToastOnUiThread("你是你们班第一个登录的同学，请为班级造福，添加课程之服务器吧。");
						handler.sendEmptyMessageDelayed(2, 2000);
					}

					if (RESULT_OK.equals(result)) {
						JSONArray array = obj.optJSONArray("courses");
						Log.i("GetOneWeekCoursesActivity", "classid " + classid);
						for (int i = 0; i < array.length(); i++) {
							JSONObject inforOfCourse = array.getJSONObject(i);
							Course course = new Course(inforOfCourse);
							course.setcName(URLDecoder.decode(course.getcName()));
							course.settName(URLDecoder.decode(course.gettName()));
							course.setcAddress(URLDecoder.decode(course
									.getcAddress()));

							// courses.add(course);
							CourseDao dao = new CourseDaoImpl(context);
							dao.addCourse(course, isTeacher);
						}

						handler.sendEmptyMessage(2);
					}
				} else {
					// editor.putBoolean(CommonConstants.LOGINED, false);
					showToastOnUiThread("服务器正在维护，请稍候再试");
				}
			} catch (Exception e) {
				e.printStackTrace();
				showToastOnUiThread("网络出现问题，请稍候重试");
			}
		} else {
			isTeacher = true;
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (int i = 0; i < params.length; i++) {
				NameValuePair pair = new BasicNameValuePair(
						CommonConstants.TEACHER_INFORMATION[i],
						URLEncoder.encode(params[i]));// params[i]);
				list.add(pair);
			}
			// courses = new ArrayList<Course>();
			try {
				System.out.println(Urls.getTeacherLoginUrl());
				String s = HttpConnect.postHttpString(
						Urls.getTeacherLoginUrl(), list);
				System.out.println(s);
				if (!"".equals(s) && null != s) {
					JSONObject obj = new JSONObject(s);
					String result = obj.optString("result");
					int teacherID = obj.optInt("teacherID");

					Editor editor = preferences.edit();
					editor.putInt(CommonConstants.TEACHER_ID, teacherID);
					editor.putBoolean(CommonConstants.LOGINED, true);
					editor.commit();

					if (RESULT_OK.equals(result)) {
						JSONArray array = obj.optJSONArray("courses");
						for (int i = 0; i < array.length(); i++) {
							JSONObject json = array.getJSONObject(i);
							Course course = new Course(json);
							course.setcName(URLDecoder.decode(course.getcName()));
							course.settName(URLDecoder.decode(course.gettName()));
							course.setcAddress(URLDecoder.decode(course
									.getcAddress()));
							course.settNo(teacherID);

							// courses.add(course);
							CourseDao dao = new CourseDaoImpl(context);
							dao.addCourse(course, isTeacher);
						}

						handler.sendEmptyMessage(2);
					} else if (RESULT_NO_USER.equals(result)) {
						showToastOnUiThread("未查询到您的课程，请添加。");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				showToastOnUiThread("网络出现问题，请稍候重试");
			}
		}

		return null;
	}

	/**
     * 
     */
	private void showToastOnUiThread(final String message) {
		((Activity) context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				handler.sendEmptyMessage(1);
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
