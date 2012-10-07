package com.example.syllabus.utils;

public class Urls
{
    final static String base_url = "http://192.168.1.100:8088/Syllabus_server/";
    
    public static String getLoginUrl()
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
}
