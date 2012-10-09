package com.example.syllabus.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.syllabus.R;
import com.example.syllabus.activity.AddCourseActivity;
import com.example.syllabus.activity.MainActivity;
import com.example.syllabus.bean.Course;
import com.example.syllabus.utils.CommonConstants;

public class SimpleCourseAdapter extends BaseAdapter
{
    
    private Context context;
    
    private List<Course> courses;
    
    private LayoutInflater inflater;
    
    private SharedPreferences preferences;
    
    private boolean isTeacher = false;
    
    public List<Course> getCourses()
    {
        return courses;
    }
    
    public void setCourses(List<Course> courses)
    {
        this.courses = courses;
    }
    
    public SimpleCourseAdapter(Context context, List<Course> courses)
    {
        this.context = context;
        
        this.courses = courses;
        
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        this.preferences = CommonConstants.getMyPreferences(context);
        
        isTeacher = preferences.getBoolean(CommonConstants.IS_TEACHER, false);
    }
    
    public int getCount()
    {
        if (null == courses)
        {
            return 0;
        }
        return courses.size();
    }
    
    public Object getItem(int position)
    {
        return courses.get(position);
    }
    
    public long getItemId(int arg0)
    {
        return arg0;
    }
    
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout)inflater.inflate(R.layout.course, null);
            viewHolder.coursename = (TextView)convertView.findViewById(R.id.coursename);
            viewHolder.time = (TextView)convertView.findViewById(R.id.coursetime);
            viewHolder.roomumber = (TextView)convertView.findViewById(R.id.roomnumber);
            viewHolder.teacher = (TextView)convertView.findViewById(R.id.teachername);
            // viewHolder.addimage = (ImageView)convertView.findViewById(R.id.addimage);
            viewHolder.courseindex = (ImageView)convertView.findViewById(R.id.courseindex);
            
            convertView.setTag(viewHolder);
        }
        else
        {
            // 这里的viewHolder主要用来防止重复创建那几个view
            viewHolder = (ViewHolder)convertView.getTag();
        }
        
        final Course course = (Course)getItem(position);
        viewHolder.coursename.setText(course.getcName());
        
        int courseIndex = course.getCourseIndex();
        viewHolder.time.setText(CommonConstants.COURSEINDEXTIME[courseIndex - 1]);
        
        viewHolder.courseindex.setImageResource(CommonConstants.COURSEINDEX_IMAGE[courseIndex - 1]);
        
        viewHolder.roomumber.setText(course.getcAddress());
        if (!isTeacher)
        {
            viewHolder.teacher.setText(course.gettName());
        }
        else
        {
            viewHolder.teacher.setVisibility(View.GONE);
        }
        
        // viewHolder.addimage.setVisibility(View.VISIBLE);
        // viewHolder.addimage.setOnClickListener(new OnClickListener()
        // {
        //
        // public void onClick(View v)
        // {
        // Intent intent = new Intent();
        // intent.setClass(context, AddCourseActivity.class);
        // int courseIndex = course.getCourseIndex();
        // int dayOfWeek = ((MainActivity)context).getDayOfWeek();
        // intent.putExtra("courseIndex", courseIndex);
        // intent.putExtra("dayOfWeek", dayOfWeek);
        // context.startActivity(intent);
        // }
        // });
        
        return convertView;
    }
    
    private class ViewHolder
    {
        TextView coursename;
        
        TextView time;
        
        TextView roomumber;
        
        TextView teacher;
        
        ImageView courseindex;
        
        // ImageView addimage;
    }
}
