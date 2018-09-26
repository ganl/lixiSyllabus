package com.example.syllabus.utils;

public class Urls
{
    final static String base_url = "http://10.0.2.2:8008/Syllabus_server/";
    
    public static String getStudentLoginUrl()
    {
        return base_url + "login/login.action";
    }
    
    public static String getInsertCourse()
    {
        // TODO Auto-generated method stub
        return base_url + "insert/insert.action";
    }
    
    public static String getUpdateCourse()
    {
        return base_url + "update/update.action";
    }
    
    public static String getTeacherLoginUrl()
    {
        return base_url + "login/teacherlogin.action";
    }
}
