package com.example.syllabus.utils;

public class Urls
{
    final static String base_url = "http://do.jhost.cn/leoking870/";
    
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
