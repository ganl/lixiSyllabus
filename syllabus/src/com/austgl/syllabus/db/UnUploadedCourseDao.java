package com.austgl.syllabus.db;

import java.util.List;

import com.austgl.syllabus.bean.Course;

public interface UnUploadedCourseDao {
	public void addCourseToUn(long courseID, int action);

	public void deleteCourse(long courseID, int action);

	public boolean isCourseInDB(long courseID);

	public void updateCourseUn(long courseId, int action);

	public List<Course> getAllActionAddCourses();

	public List<Course> getAllActionUpdateCourses();
}
