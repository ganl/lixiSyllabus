package com.example.syllabus.db;

import java.util.List;

import com.example.syllabus.bean.Course;

public interface CourseDao
{
    public List<List<Course>> getCourseByTeacherID(int currentWeek, int teacherid);
    
    public long addCourse(Course course, boolean isTeacher);
    
    public void deleteAllCourse();
    
    public void updateCourse(Course course);
    
    public void updateCourseid(long id, int courseid);
    
    public List<Course> getAllCourse(boolean isTeacher);
    
    public void deleteCourseById(long id);
    
    public List<Course> getDayCourse(int currentWeek, int currentDay, boolean isTeacher);
    
    public Course getCourseById(long id);
    
    public List<List<Course>> getWeekCourse(int currentWeek, boolean isTeacher);
    
}
