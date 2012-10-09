package com.example.syllabus.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.example.syllabus.bean.Course;
import com.example.syllabus.utils.CommonConstants;

public class CourseDaoImpl extends DBService<Course> implements CourseDao
{
    SharedPreferences preferences;
    
    public CourseDaoImpl(Context context)
    {
        super(context);
        preferences = CommonConstants.getMyPreferences(context);
    }
    
    public long addCourse(Course course, boolean isTeacher)
    {
        db = this.getWritableDatabase();
        if (isTeacher)
        {
            course.settNo(preferences.getInt(CommonConstants.TEACHER_ID, 0));
        }
        if (isCourseExisted(course, isTeacher))
        {
            return 0;
        }
        long id = db.insert(COURSE_NAME, null, deconstruct(course));
        db.close();
        return id;
    }
    
    public boolean isCourseExisted(Course course, boolean isTeacher)
    {
        db = this.getReadableDatabase();
        StringBuilder sb = new StringBuilder();
        String where = COURSEID + " = ?";
        String[] whereArgs = null;
        sb.append(where);
        if (isTeacher)
        {
            sb.append(" and " + TNO + " = ?");
            whereArgs = new String[] {course.getCourseid() + "", course.gettNo() + ""};
            
        }
        else
        {
            whereArgs = new String[] {course.getCourseid() + ""};
        }
        Cursor c = db.query(COURSE_NAME, null, sb.toString(), whereArgs, null, null, null);
        if (null != c)
        {
            if (false == c.moveToFirst())
            {
                return false;
            }
            return true;
        }
        
        return false;
    }
    
    public void deleteAllCourse()
    {
        db = this.getWritableDatabase();
        db.delete(COURSE_NAME, null, null);
        db.close();
    }
    
    public void updateCourse(Course course)
    {
        db = this.getWritableDatabase();
        String where = ID + " = ?";
        String[] whereValue = {Long.toString(course.getId())};
        db.update(COURSE_NAME, deconstruct(course), where, whereValue);
        db.close();
    }
    
    public void updateCourseid(long id, int courseid)
    {
        db = this.getWritableDatabase();
        String where = ID + " = ?";
        String[] whereValue = {Long.toString(id)};
        ContentValues cv = new ContentValues();
        cv.put(COURSEID, courseid);
        db.update(COURSE_NAME, cv, where, whereValue);
        db.close();
    }
    
    public List<Course> getAllCourse(boolean isTeacher)
    {
        db = this.getReadableDatabase();
        StringBuilder where = null;
        String[] whereArgs = null;
        Cursor c = null;
        if (isTeacher)
        {
            where = new StringBuilder("tNo" + " = ?");
            whereArgs = new String[] {Integer.toString(preferences.getInt(CommonConstants.TEACHER_ID, 0))};
            c = db.query(COURSE_NAME, null, where.toString(), whereArgs, null, null, CWEEKDAY + " , " + COURSEINDEX);
        }
        else
        {
            c = db.query(COURSE_NAME, null, null, null, null, null, CWEEKDAY + " , " + COURSEINDEX);
        }
        List<Course> courseList = buildList(c);
        c.close();
        db.close();
        return courseList;
    }
    
    /**
     * 获取本周本天的课表
     */
    
    @Override
    public ContentValues deconstruct(Course t)
    {
        ContentValues cv = new ContentValues();
        if (0 != t.getCourseid())
        {
            cv.put(COURSEID, t.getCourseid());
        }
        if (null != t.getcName())
        {
            cv.put(CNAME, t.getcName());
        }
        if (null != t.getcAddress())
        {
            cv.put(CADDRESS, t.getcAddress());
        }
        if (0 != t.gettNo())
        {
            cv.put(TNO, t.gettNo());
        }
        if (null != t.gettName())
        {
            cv.put(TNAME, t.gettName());
        }
        if (0 != t.getcStartWeek())
        {
            cv.put(CSTARTWEEK, t.getcStartWeek());
        }
        if (0 != t.getcWeekday())
        {
            cv.put(CWEEKDAY, t.getcWeekday());
        }
        if (0 != t.getcEndWeek())
        {
            cv.put(CENDWEEK, t.getcEndWeek());
        }
        if (0 != t.getCourseIndex())
        {
            cv.put(COURSEINDEX, t.getCourseIndex());
        }
        return cv;
    }
    
    @Override
    public Course build(Cursor c)
    {
        Course course = new Course();
        try
        {
            course.setCourseid(c.getInt(c.getColumnIndex(COURSEID)));
            course.setId(c.getLong(c.getColumnIndex(ID)));
            course.setcName(c.getString(c.getColumnIndex(CNAME)));
            course.settNo(c.getInt(c.getColumnIndex(TNO)));
            course.settName(c.getString(c.getColumnIndex(TNAME)));
            course.setcAddress(c.getString(c.getColumnIndex(CADDRESS)));
            course.setcStartWeek(c.getInt(c.getColumnIndex(CSTARTWEEK)));
            course.setcEndWeek(c.getInt(c.getColumnIndex(CENDWEEK)));
            course.setcWeekday(c.getInt(c.getColumnIndex(CWEEKDAY)));
            course.setCourseIndex(c.getInt(c.getColumnIndex(COURSEINDEX)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return course;
    }
    
    public void deleteCourseById(long id)
    {
        db = this.getWritableDatabase();
        String where = ID + " = ?";
        String[] whereValue = {Long.toString(id)};
        db.delete(COURSE_NAME, where, whereValue);
        db.close();
    }
    
    public List<Course> getDayCourse(int currentWeek, int currentDay, boolean isTeacher)
    {
        db = this.getReadableDatabase();
        StringBuilder where = new StringBuilder();
        String where2 = CSTARTWEEK + " <= ?" + " and " + CENDWEEK + " >= ?" + " and " + CWEEKDAY + " = ?";
        String[] whereArgs = null;
        where.append(where2);
        if (isTeacher)
        {
            int teacherid = preferences.getInt(CommonConstants.TEACHER_ID, 0);
            where.append(" and " + TNO + " = ?");
            whereArgs = new String[] {currentWeek + "", currentWeek + "", currentDay + "", teacherid + ""};
        }
        else
        {
            whereArgs = new String[] {currentWeek + "", currentWeek + "", currentDay + ""};
        }
        
        Cursor c = db.query(COURSE_NAME, null, where.toString(), whereArgs, null, null, COURSEINDEX);
        List<Course> courseList = buildList(c);
        c.close();
        db.close();
        return courseList;
    }
    
    public Course getCourseById(long id)
    {
        db = this.getReadableDatabase();
        String where = ID + " = ?";
        String[] whereValue = {Long.toString(id)};
        Cursor c = db.query(COURSE_NAME, null, where, whereValue, null, null, null);
        Course course = buildOne(c);
        c.close();
        db.close();
        return course;
    }
    
    public List<List<Course>> getWeekCourse(int currentWeek, boolean isTeacher)
    {
        db = this.getReadableDatabase();
        List<List<Course>> listOfWeek = new ArrayList<List<Course>>();
        for (int i = 0; i < 7; i++)
        {
            listOfWeek.add(new ArrayList<Course>());
        }
        StringBuilder where = new StringBuilder();
        String[] whereArgs = null;
        String where2 = CSTARTWEEK + " <= ?" + " and " + CENDWEEK + " >= ?";
        where.append(where2);
        if (isTeacher)
        {
            int teacherid = preferences.getInt(CommonConstants.TEACHER_ID, 0);
            where.append(" and " + TNO + " = ?");
            whereArgs = new String[] {currentWeek + "", currentWeek + "", teacherid + ""};
        }
        else
        {
            whereArgs = new String[] {currentWeek + "", currentWeek + ""};
        }
        Cursor c = db.query(COURSE_NAME, null, where.toString(), whereArgs, null, null, CWEEKDAY + " , " + COURSEINDEX);
        List<Course> courseList = buildList(c);
        List<Course> oneDay = new ArrayList<Course>();
        for (int i = 0; i < courseList.size(); i++)
        {
            oneDay.add(courseList.get(i));
            if ((i + 1) == courseList.size() || courseList.get(i).getcWeekday() != courseList.get(i + 1).getcWeekday())
            {
                listOfWeek.set(courseList.get(i).getcWeekday() - 1, oneDay);// add(oneDay);
                oneDay = new ArrayList<Course>();
            }
        }
        c.close();
        db.close();
        return listOfWeek;
    }
    
    public List<List<Course>> getCourseByTeacherID(int currentWeek, int teacherid)
    {
        db = this.getReadableDatabase();
        List<List<Course>> listOfWeek = new ArrayList<List<Course>>();
        for (int i = 0; i < 7; i++)
        {
            listOfWeek.add(new ArrayList<Course>());
        }
        String where = "TNO" + " = ?" + " and " + CSTARTWEEK + " <= ?" + " and " + CENDWEEK + " >= ?";
        String[] whereArgs = {Integer.toString(teacherid), currentWeek + "", currentWeek + ""};
        Cursor c = db.query(COURSE_NAME, null, where, whereArgs, null, null, CWEEKDAY + " , " + COURSEINDEX);
        
        List<Course> courseList = buildList(c);
        List<Course> oneDay = new ArrayList<Course>();
        for (int i = 0; i < courseList.size(); i++)
        {
            oneDay.add(courseList.get(i));
            if ((i + 1) == courseList.size() || courseList.get(i).getcWeekday() != courseList.get(i + 1).getcWeekday())
            {
                listOfWeek.set(courseList.get(i).getcWeekday() - 1, oneDay);// add(oneDay);
                oneDay = new ArrayList<Course>();
            }
        }
        c.close();
        db.close();
        return listOfWeek;
    }
    
    // public List<List<Course>> getCourseByTeacherID(int currentWeek, int teacherid)
    // {
    // db = this.getReadableDatabase();
    // List<List<Course>> listOfWeek = new ArrayList<List<Course>>();
    // for (int i = 0; i < 7; i++)
    // {
    // listOfWeek.add(new ArrayList<Course>());
    // }
    // String where = "TNO" + " = ?" + " and " + CSTARTWEEK + " <= ?" + " and " + CENDWEEK + " >= ?";
    // String[] whereArgs = {Integer.toString(teacherid), currentWeek + "", currentWeek + ""};
    // Cursor c = db.query(COURSE_NAME, null, where, whereArgs, null, null, CWEEKDAY + " , " + COURSEINDEX);
    //
    // List<Course> courseList = buildList(c);
    // List<Course> oneDay = new ArrayList<Course>();
    // for (int i = 0; i < courseList.size(); i++)
    // {
    // oneDay.add(courseList.get(i));
    // if ((i + 1) == courseList.size() || courseList.get(i).getcWeekday() != courseList.get(i + 1).getcWeekday())
    // {
    // listOfWeek.set(courseList.get(i).getcWeekday() - 1, oneDay);// add(oneDay);
    // oneDay = new ArrayList<Course>();
    // }
    // }
    // c.close();
    // db.close();
    // return listOfWeek;
    // }
}
