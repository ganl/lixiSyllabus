package com.austgl.syllabus.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.austgl.syllabus.bean.Course;
import com.austgl.syllabus.bean.UnUploadedCourse;
import com.austgl.syllabus.utils.CommonConstants;

public class UnUploadedCourseDaoImpl extends DBService<UnUploadedCourse>
		implements UnUploadedCourseDao {
	Context context;

	public UnUploadedCourseDaoImpl(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public boolean isCourseInDB(long courseID) {
		db = this.getReadableDatabase();
		String whereClause = UNUPLOADED_COURSE_ID + " = ?";
		String[] whereArgs = { courseID + "" };
		Cursor c = db.query(UNUPLOADED_NAME, null, whereClause, whereArgs,
				null, null, null);

		if (c.moveToFirst()) {
			return true;
		}
		return false;

	}

	@Override
	public UnUploadedCourse build(Cursor c) {
		UnUploadedCourse unUploadedCourse = new UnUploadedCourse();
		try {
			unUploadedCourse.setId(c.getLong(c.getColumnIndex(ID)));
			unUploadedCourse.setCourseId(c.getLong(c
					.getColumnIndex(UNUPLOADED_COURSE_ID)));
			unUploadedCourse.setAction(c.getInt(c.getColumnIndex(ACTION_UNDO)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unUploadedCourse;
	}

	@Override
	public ContentValues deconstruct(UnUploadedCourse t) {
		ContentValues cv = new ContentValues();

		if (0 != t.getId()) {
			cv.put(ID, t.getId());
		}
		if (0 != t.getCourseId()) {
			cv.put(UNUPLOADED_COURSE_ID, t.getCourseId());
		}
		if (0 != t.getAction()) {
			cv.put(ACTION_UNDO, t.getAction());
		}
		return cv;
	}

	@Override
	public void addCourseToUn(long courseID, int action) {
		// TODO Auto-generated method stub
		db = this.getWritableDatabase();
		UnUploadedCourse unCourse = new UnUploadedCourse();
		unCourse.setCourseId(courseID);
		unCourse.setAction(action);

		if (isCourseInDB(courseID)) {
			updateCourseUn(courseID, action);
		} else {
			db.insert(UNUPLOADED_NAME, null, deconstruct(unCourse));
		}
		db.close();
	}

	@Override
	public void deleteCourse(long courseID, int action) {
		db = this.getWritableDatabase();
		UnUploadedCourse unCourse = new UnUploadedCourse();
		unCourse.setCourseId(courseID);
		unCourse.setAction(action);
		String whereClause = UNUPLOADED_COURSE_ID + " = ? and " + ACTION_UNDO
				+ " = ?";
		String[] whereArgs = { courseID + "", action + "" };
		db.delete(UNUPLOADED_NAME, whereClause, whereArgs);
		db.close();
	}

	@Override
	public void updateCourseUn(long courseId, int action) {
		db = this.getWritableDatabase();
		String whereClause = UNUPLOADED_COURSE_ID + " = ? and " + ACTION_UNDO
				+ " = ?";
		String[] whereArgs = { courseId + "", action + "" };
		UnUploadedCourse t = new UnUploadedCourse();
		t.setCourseId(courseId);
		t.setAction(action);
		db.update(UNUPLOADED_NAME, deconstruct(t), whereClause, whereArgs);
		db.close();
	}

	@Override
	public List<Course> getAllActionAddCourses() {
		// TODO Auto-generated method stub
		List<UnUploadedCourse> uncourses;// = new ArrayList<Course>();
		db = this.getReadableDatabase();
		String where = ACTION_UNDO + " = " + CommonConstants.UNDO_ACTION_ADD;
		Cursor c = db.query(UNUPLOADED_NAME, null, where, null, null, null, ID);
		uncourses = buildList(c);
		List<Course> courses = new ArrayList<Course>();
		if (!uncourses.isEmpty()) {

			CourseDaoImpl dao = new CourseDaoImpl(context);
			for (UnUploadedCourse unUploadedCourse : uncourses) {
				String where2 = ID + " = ?";
				String[] whereValue = { Long.toString(unUploadedCourse
						.getCourseId()) };
				Cursor c2 = db.query(COURSE_NAME, null, where2, whereValue,
						null, null, null);
				Course course = dao.buildOne(c2);
				c.close();

				courses.add(course);
			}
		}
		db.close();
		return courses;
	}

	@Override
	public List<Course> getAllActionUpdateCourses() {
		List<UnUploadedCourse> uncourses;// = new ArrayList<Course>();
		db = this.getReadableDatabase();
		String where = ACTION_UNDO + " = " + CommonConstants.UNDO_ACTION_UPDATE;
		Cursor c = db.query(UNUPLOADED_NAME, null, where, null, null, null, ID);
		uncourses = buildList(c);
		List<Course> courses = new ArrayList<Course>();
		if (!uncourses.isEmpty()) {

			CourseDaoImpl dao = new CourseDaoImpl(context);
			for (UnUploadedCourse unUploadedCourse : uncourses) {
				String where2 = ID + " = ?";
				String[] whereValue = { Long.toString(unUploadedCourse
						.getCourseId()) };
				Cursor c2 = db.query(COURSE_NAME, null, where2, whereValue,
						null, null, null);
				Course course = dao.buildOne(c2);
				c.close();

				courses.add(course);
			}
		}
		db.close();
		return courses;
	}

}
