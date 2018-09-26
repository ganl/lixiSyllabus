package com.austgl.syllabus.db;

import java.util.List;

import com.austgl.syllabus.bean.Course;

public interface CourseDao {
	public List<List<Course>> getCourseByTeacherID(int currentWeek,
			int teacherid);

	/**
	 * ��ӿγ̵����ݿ��У�������ݿ������и����ݣ���ֻ���¸�����courseid�����ݿ���
	 * ����ǽ�ʦ�������ݣ����Զ������ݵ�TNO��TNAME���룬���û�е�¼������д��-1��""
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
