package com.example.syllabus.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.syllabus.R;
import com.example.syllabus.activity.MainActivity;
import com.example.syllabus.utils.CommonConstants;

public class OneDayCourseAdapter extends BaseExpandableListAdapter
{
    private List<? extends Map<String, String>> groupList;
    
    private List<? extends List<? extends Map<String, String>>> childList;
    
    private Context context;
    
    private LayoutInflater inflater;
    
    public OneDayCourseAdapter(List<? extends Map<String, String>> groupList,
        List<? extends List<? extends Map<String, String>>> childList, Context context)
    {
        super();
        this.groupList = groupList;
        this.childList = childList;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public Object getChild(int groupPosition, int childPosition)
    {
        // TODO Auto-generated method stub
        return childList.get(groupPosition).get(childPosition);
    }
    
    public long getChildId(int groupPosition, int childPosition)
    {
        // TODO Auto-generated method stub
        return childPosition;
    }
    
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
        ViewGroup parent)
    {
        ViewHolderForChild viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolderForChild();
            convertView = inflater.inflate(R.layout.onecourseitem, null);
            viewHolder.tvCourseIndex = (TextView)convertView.findViewById(R.id.courseindex);
            viewHolder.tvCourseName = (TextView)convertView.findViewById(R.id.coursename);
            viewHolder.tvCourseTeacher = (TextView)convertView.findViewById(R.id.courseteacher);
            viewHolder.tvCourseRoom = (TextView)convertView.findViewById(R.id.courseroom);
            
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolderForChild)convertView.getTag();
        }
        
        Map<String, String> map = childList.get(groupPosition).get(childPosition);
        viewHolder.tvCourseIndex.setText(map.get("courseIndex"));
        viewHolder.tvCourseName.setText(map.get("courseName"));
        viewHolder.tvCourseTeacher.setText(map.get("courseTeacher"));
        viewHolder.tvCourseRoom.setText(map.get("courseRoom"));
        
        return convertView;
    }
    
    public int getChildrenCount(int groupPosition)
    {
        // TODO Auto-generated method stub
        if (null == childList.get(groupPosition) || childList.get(groupPosition).isEmpty())
        {
            return 0;
        }
        return childList.get(groupPosition).size();
    }
    
    public Object getGroup(int groupPosition)
    {
        // TODO Auto-generated method stub
        return groupList.get(groupPosition);
    }
    
    public int getGroupCount()
    {
        // TODO Auto-generated method stub
        if (null == groupList || groupList.isEmpty())
        {
            return 0;
        }
        return groupList.size();
    }
    
    public long getGroupId(int groupPosition)
    {
        // TODO Auto-generated method stub
        return groupPosition;
    }
    
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolderForGroup viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolderForGroup();
            convertView = (LinearLayout)inflater.inflate(R.layout.onedaycourseitem, null);
            viewHolder.tvWeekNum = (TextView)convertView.findViewById(R.id.weeknum);
            viewHolder.tvCourseNum = (TextView)convertView.findViewById(R.id.coursenum);
            viewHolder.ivExpandGroup = (ImageView)convertView.findViewById(R.id.expandgroup);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolderForGroup)convertView.getTag();
        }
        viewHolder.tvWeekNum.setText(CommonConstants.getStrFromWeekNum(groupPosition + 1));
        final int gp = groupPosition + 1;
        viewHolder.tvWeekNum.setOnClickListener(new OnClickListener()
        {
            
            public void onClick(View v)
            {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("dayOfWeek", gp);
                context.startActivity(intent);
            }
        });
        viewHolder.tvCourseNum.setText(getChildrenCount(groupPosition) + "");
        if (isExpanded)
        {
            viewHolder.ivExpandGroup.setImageResource(R.drawable.jiantouexpanded);
        }
        else
        {
            viewHolder.ivExpandGroup.setImageResource(R.drawable.jiantou);
        }
        
        return convertView;
    }
    
    public boolean hasStableIds()
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        // TODO Auto-generated method stub
        return true;
    }
    
    class ViewHolderForGroup
    {
        TextView tvWeekNum;
        
        TextView tvCourseNum;
        
        ImageView ivExpandGroup;
    }
    
    class ViewHolderForChild
    {
        TextView tvCourseIndex;
        
        TextView tvCourseName;
        
        TextView tvCourseTeacher;
        
        TextView tvCourseRoom;
    }
}
