package com.austgl.syllabus.service;

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

import com.austgl.syllabus.bean.Course;
import com.austgl.syllabus.db.CourseDao;
import com.austgl.syllabus.db.CourseDaoImpl;
import com.austgl.syllabus.db.UnUploadedCourseDao;
import com.austgl.syllabus.db.UnUploadedCourseDaoImpl;
import com.austgl.syllabus.utils.CommonConstants;
import com.austgl.syllabus.utils.HttpConnect;
import com.austgl.syllabus.utils.Urls;

/**
 * 添加课程至服务器，只要在有网，且登录过的情况下使用该类
 * 
 * @author Administrator
 * 
 */
public class AddCourseToServer extends Service {

	private static final int UPDATE_COURSE = 1;

	private static final int INSERT_COURSE_TO_SERVER = 0;

	SharedPreferences preferences;

	private Course course;

	public static final String[] FIELDS = { "cName", "tName", "cAddress",
			"cStartWeek", "cEndWeek", "cWeekday", "courseIndex" };

	private boolean isTeacher;

	@Override
	public void onCreate() {
		super.onCreate();
		preferences = CommonConstants.getMyPreferences(this);
		isTeacher = preferences.getBoolean(CommonConstants.IS_TEACHER, false);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		course = (Course) intent.getSerializableExtra("course");
		int action = intent.getIntExtra("action", -1);

		Log.i("AddCourseToServer",
				"---------------------------------add service started");

		String[] courseValues = { course.getcName(), course.gettName(),
				course.getcAddress(), Integer.toString(course.getcStartWeek()),
				Integer.toString(course.getcEndWeek()),
				Integer.toString(course.getcWeekday()),
				Integer.toString(course.getCourseIndex()) };
		List<NameValuePair> values = new ArrayList<NameValuePair>();
		// 如果是学生添加课程，需要classid字段
		if (!isTeacher) {
			NameValuePair classidValue = new BasicNameValuePair(
					CommonConstants.CLASSID, preferences.getInt(
							CommonConstants.CLASSID, -1) + "");
			values.add(classidValue);
		}
		for (int i = 0; i < courseValues.length; i++) {
			NameValuePair value = new BasicNameValuePair(FIELDS[i],
					URLEncoder.encode(courseValues[i]));
			values.add(value);
		}

		if (INSERT_COURSE_TO_SERVER == action) {
			new SendCourseToServerTask(values, this).execute("");
		} else {
			// 本地没有被同步过的课程，
			if (0 == course.getCourseid()) {
				new SendCourseToServerTask(values, this).execute("");
			} else {
				// 课程已同步过，同步修改过程至服务器
				NameValuePair value = new BasicNameValuePair(
						CommonConstants.COURSE_ID, course.getCourseid() + "");
				Log.i("AddCourseToServer", "courseid:" + course.getCourseid());
				values.add(value);
				new UpdateCourseTask(values, this).execute("");
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	public class SendCourseToServerTask extends
			AsyncTask<String, String, String> {
		private List<NameValuePair> pairs;

		private Service service;

		public SendCourseToServerTask(List<NameValuePair> values,
				Service service) {
			this.pairs = values;
			this.service = service;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				String s = HttpConnect.postHttpString(Urls.getInsertCourse(),
						pairs);
				System.out.println(s);
				if (!"".equals(s) && null != s) {
					JSONObject obj = new JSONObject(s);
					int resultCode = obj.optInt("result");
					if (1 == resultCode) {
						System.out.println("inserted");
						course.setCourseid(obj.optInt("courseid"));
						Log.i("AddCourseToServer",
								"courseid:" + obj.optInt("courseid"));
						CourseDao dao = new CourseDaoImpl(
								AddCourseToServer.this);
						Log.i("AddcourseTOServer",
								"course id:" + course.getId() + ",courseid:"
										+ course.getCourseid());
						dao.updateCourseid(course.getId(), course.getCourseid());

						// 将课程从未同步课表中删除
						UnUploadedCourseDao unDao = new UnUploadedCourseDaoImpl(
								AddCourseToServer.this);
						unDao.deleteCourse(course.getId(),
								CommonConstants.UNDO_ACTION_ADD);
					}
				} else {

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			service.stopSelf();
			super.onPostExecute(result);
		}
	}

	public class UpdateCourseTask extends AsyncTask<String, String, String> {
		private List<NameValuePair> pairs;

		private Service service;

		public UpdateCourseTask(List<NameValuePair> values, Service service) {
			this.pairs = values;
			this.service = service;
		}

		@Override
		protected String doInBackground(String... arg0) {
			try {
				String s = HttpConnect.postHttpString(Urls.getUpdateCourse(),
						pairs);
				System.out.println(s);
				if (null != s && !"".equals(s)) {

					JSONObject obj = new JSONObject(s);
					int resultCode = obj.optInt("result");
					if (UPDATE_COURSE == resultCode) {
						System.out.println("updated");

						// 将课程从为同步列表中删除
						UnUploadedCourseDao unDao = new UnUploadedCourseDaoImpl(
								AddCourseToServer.this);
						unDao.deleteCourse(course.getId(),
								CommonConstants.UNDO_ACTION_UPDATE);

						// course.setCourseid(obj.optInt("courseid"));
						// CourseDao dao = new
						// CourseDaoImpl(AddCourseToServer.this);
						// dao.updateCourse(course);
					} else {
						System.out
								.println("server is updating, please waiting...");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			service.stopSelf();
			super.onPostExecute(result);
		}

	}

}
