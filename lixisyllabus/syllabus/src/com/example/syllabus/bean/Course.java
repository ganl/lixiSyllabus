package com.example.syllabus.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Course implements Serializable
{
    private long id; // self-increasing
    
    private int courseid;
    
    private String cName; // course name
    
    private String tName; // name of teacher
    
    private int tNo; // teacherid
    
    private String cAddress; // address
    
    private int cStartWeek;
    
    private int cEndWeek;
    
    private int cWeekday; // day of week
    
    private int courseIndex; // the index of day
    
    public Course()
    {
        
    }
    
    public long getId()
    {
        return id;
    }
    
    public void setId(long id)
    {
        this.id = id;
    }
    
    public String getcName()
    {
        return cName;
    }
    
    public void setcName(String cName)
    {
        this.cName = cName;
    }
    
    public String gettName()
    {
        return tName;
    }
    
    public void settName(String tName)
    {
        this.tName = tName;
    }
    
    public String getcAddress()
    {
        return cAddress;
    }
    
    public void setcAddress(String cAddress)
    {
        this.cAddress = cAddress;
    }
    
    public int getcStartWeek()
    {
        return cStartWeek;
    }
    
    public void setcStartWeek(int cStartWeek)
    {
        this.cStartWeek = cStartWeek;
    }
    
    public int getcEndWeek()
    {
        return cEndWeek;
    }
    
    public void setcEndWeek(int cEndWeek)
    {
        this.cEndWeek = cEndWeek;
    }
    
    public int getcWeekday()
    {
        return cWeekday;
    }
    
    public void setcWeekday(int cWeekday)
    {
        this.cWeekday = cWeekday;
    }
    
    public int getCourseIndex()
    {
        return courseIndex;
    }
    
    public void setCourseIndex(int courseIndex)
    {
        this.courseIndex = courseIndex;
    }
    
    public Course(long id, String cNo, String cName, String tNo, String tName, String cAddress, int cStartWeek,
        int cEndWeek, int cWeekday, int courseIndex)
    {
        super();
        this.id = id;
        this.cName = cName;
        this.tName = tName;
        this.cAddress = cAddress;
        this.cStartWeek = cStartWeek;
        this.cEndWeek = cEndWeek;
        this.cWeekday = cWeekday;
        this.courseIndex = courseIndex;
    }
    
    public Course(JSONObject inforOfCourse)
    {
        this.courseid = inforOfCourse.optInt("courseid");
        this.cName = inforOfCourse.optString("cName");
        this.tName = inforOfCourse.optString("tName");
        this.cAddress = inforOfCourse.optString("cAddress");
        this.cStartWeek = inforOfCourse.optInt("cStartWeek");
        this.cEndWeek = inforOfCourse.optInt("cEndWeek");
        this.cWeekday = inforOfCourse.optInt("cWeekday");
        this.courseIndex = inforOfCourse.optInt("courseIndex");
    }
    
    public Map<String, String> beanToMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("courseIndex", this.courseIndex + "");
        map.put("courseName", this.cName);
        map.put("courseTeacher", this.tName);
        map.put("courseRoom", this.cAddress);
        return map;
    }
    
    public int getCourseid()
    {
        return courseid;
    }
    
    public void setCourseid(int courseid)
    {
        this.courseid = courseid;
    }
    
    public int gettNo()
    {
        return tNo;
    }
    
    public void settNo(int tNo)
    {
        this.tNo = tNo;
    }
    
}
