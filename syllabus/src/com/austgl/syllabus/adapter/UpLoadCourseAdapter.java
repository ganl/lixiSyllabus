package com.austgl.syllabus.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.austgl.syllabus.R;
import com.austgl.syllabus.bean.Course;

public class UpLoadCourseAdapter extends BaseAdapter {
	private Context context;

	private LayoutInflater inflater;

	private List<Course> list;

	private List<Boolean> course_selected;

	public UpLoadCourseAdapter(List<Course> list,
			List<Boolean> course_selected, Context context) {
		this.list = list;
		this.course_selected = course_selected;
		this.context = context;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null == list || list.isEmpty()) {
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolderForChild viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolderForChild();
			convertView = inflater.inflate(R.layout.onecourseitem, null);
			viewHolder.tvCourseIndex = (TextView) convertView
					.findViewById(R.id.courseindex);
			viewHolder.tvCourseName = (TextView) convertView
					.findViewById(R.id.coursename);
			viewHolder.tvCourseTeacher = (TextView) convertView
					.findViewById(R.id.courseteacher);
			viewHolder.tvCourseRoom = (TextView) convertView
					.findViewById(R.id.courseroom);

			viewHolder.llOneCourse = (LinearLayout) convertView
					.findViewById(R.id.onecourse);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderForChild) convertView.getTag();
		}
		Course course = list.get(position);
		// Map<String, String> map =
		// childList.get(groupPosition).get(childPosition);
		viewHolder.tvCourseIndex.setText(course.getCourseIndex() + "");
		viewHolder.tvCourseName.setText(course.getcName());
		viewHolder.tvCourseTeacher.setText(course.gettName());
		viewHolder.tvCourseRoom.setText(course.getcAddress());

		viewHolder.llOneCourse.setOnClickListener(new MyClickListener(
				viewHolder.llOneCourse, position));
		if (course_selected.get(position)) {
			viewHolder.llOneCourse
					.setBackgroundResource(R.drawable.daycourseitem_selected);
		} else {
			viewHolder.llOneCourse
					.setBackgroundResource(R.drawable.daycourseitem);
		}

		return convertView;
	}

	class ViewHolderForChild {
		TextView tvCourseIndex;

		TextView tvCourseName;

		TextView tvCourseTeacher;

		TextView tvCourseRoom;

		LinearLayout llOneCourse;
	}

	private class MyClickListener implements OnClickListener {
		private LinearLayout ll;

		private int position;

		public MyClickListener(LinearLayout layout, int position) {
			this.ll = layout;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (!course_selected.get(position)) {
				course_selected.set(position, true);
				ll.setBackgroundResource(R.drawable.daycourseitem_selected);
			} else {
				course_selected.set(position, false);
				ll.setBackgroundResource(R.drawable.daycourseitem);
			}
		}
	}

}
