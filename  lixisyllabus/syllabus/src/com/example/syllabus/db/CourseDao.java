package com.example.syllabus.db;

import java.util.List;

import com.example.syllabus.bean.Course;

public interface CourseDao
{
    public long addCourse(Course course);
    
    public Course getCourseById(long id);
    
    public void deleteCourseById(long id);
    
    public void deleteAllCourse();
    
    public void updateCourse(Course course);
    
    public List<Course> getAllCourse();
    
    public List<Course> getDayCourse(int currentWeek, int weekNum);
    
    public List<List<Course>> getWeekCourse(int currentWeek);
    
    public void updateCourseid(long id, int courseid);
    
    public List<List<Course>> getCourseByTeacherID(int currentWeek, int teacherid);
    
}
