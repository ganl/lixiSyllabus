package com.example.syllabus.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DBService<T> extends SQLiteOpenHelper
{
    private final static String DATABASE_NAME = "course_db";
    
    private final static int DATABASE_VERSION = 1;
    
    public final static String COURSE_NAME = "course";
    
    public final static String TEACHER_COURSE_NAME = "teacher_course_";
    
    public final static String ID = "_id";
    
    public final static String TEACHERID = "teacherId";
    
    public final static String CNO = "cNo";
    
    public final static String CNAME = "cName";
    
    public final static String TNO = "tNo";
    
    public final static String TNAME = "tName";
    
    public final static String CADDRESS = "cAddress";
    
    public final static String CSTARTWEEK = "cStartWeek";
    
    public final static String CENDWEEK = "cEndWeek";
    
    public final static String CWEEKDAY = "cWeekday";
    
    public final static String COURSEINDEX = "courseIndex";
    
    public static final String COURSEID = "courseId";// the id column in the server db
    
    protected SQLiteDatabase db;
    
    public DBService(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // TODO Auto-generated method stub
        String sql = " (" + ID + " INTEGER primary key autoincrement, ";
        
        String course_sql =
            "CREATE TABLE " + COURSE_NAME + sql + COURSEID + " INTEGER," + CNO + " text, " + CNAME + " text, " + TNO
                + " INTEGER, " + TNAME + " text, " + CADDRESS + " text, " + CSTARTWEEK + " INTEGER, " + CENDWEEK
                + " INTEGER, " + CWEEKDAY + " INTEGER, " + COURSEINDEX + " INTEGER)";
        
        // String teacher_course_sql =
        // "CREATE TABLE " + TEACHER_COURSE_NAME + sql + TEACHERID + " INTEGER, " + CNAME + " text, " + CADDRESS
        // + " text, " + CSTARTWEEK + " INTEGER, " + CENDWEEK + " INTEGER, " + CWEEKDAY + " INTEGER, "
        // + COURSEINDEX + " INTEGER)";
        
        db.execSQL(course_sql);
        // db.execSQL(teacher_course_sql);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO Auto-generated method stub
        String course_sql = "DROP TABLE IF EXISTS " + COURSE_NAME;
        // String teacher_course_sql = "DROP TABLE IF EXISTS " + TEACHER_COURSE_NAME;
        db.execSQL(course_sql);
        // db.execSQL(teacher_course_sql);
        
        onCreate(db);
    }
    
    /**
     * 将cursor解析成制定的对象
     * 
     * @param c
     * @return
     */
    public abstract T build(Cursor c);
    
    /**
     * 将制定的对象解析成ContentValues对象
     * 
     * @param t
     * @return
     */
    public abstract ContentValues deconstruct(T t);
    
    /**
     * 解析成单个对象
     * 
     * @param c
     * @return
     */
    public T buildOne(Cursor c)
    {
        if (c.moveToNext())
        {
            return build(c);
        }
        return null;
    }
    
    /**
     * 解析成多个多项的集合
     * 
     * @param c
     * @return
     */
    
    public List<T> buildList(Cursor c)
    {
        List<T> list = new ArrayList<T>();
        while (c.moveToNext())
        {
            list.add(build(c));
            
        }
        
        return list;
    }
    
}
