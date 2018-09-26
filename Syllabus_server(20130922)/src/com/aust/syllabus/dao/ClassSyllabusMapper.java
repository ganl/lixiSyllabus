package com.aust.syllabus.dao;

import java.util.List;

import com.aust.syllabus.beans.Course;
import com.aust.syllabus.beans.Teacher;

public interface ClassSyllabusMapper {
	public List<Course> selectClass(int classid);

	public int insertOneCourse(Course course);

	public List<Course> selectTeacherCourses(Teacher teacher);

	public Course selectOneClass(Course course);

	public void updateOneCourse(Course course);

	public Course selectTeacherCourse(Course course);
}
