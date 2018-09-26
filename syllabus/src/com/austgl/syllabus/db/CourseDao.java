package com.austgl.syllabus.db;

import java.util.List;

import com.austgl.syllabus.bean.Course;

public interface CourseDao {
	public List<List<Course>> getCourseByTeacherID(int currentWeek,
			int teacherid);

	/**
	 * 添加课程到数据库中，如果数据库中已有该数据，则只更新该数据courseid到数据库中
	 * 如果是教师插入数据，则自动将数据的TNO与TNAME带入，如果没有登录过，则写入-1与""
	 */
	public long addCourse(Course course, boolean isTeacher);

	public void deleteAllCourse();

	public void updateCourse(Course course);

	public void updateCourseid(long id, int courseid);

	public List<Course> getAllCourse(boolean isTeacher);

	public void deleteCourseById(long id);

	public List<Course> getDayCourse(int currentWeek, int currentDay,
			boolean isTeacher);

	public Course getCourseById(long id);

	public List<List<Course>> getWeekCourse(int currentWeek, boolean isTeacher);

}
