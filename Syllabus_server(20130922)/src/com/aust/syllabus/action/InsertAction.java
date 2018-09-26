package com.aust.syllabus.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.aust.syllabus.beans.Course;
import com.aust.syllabus.dao.ClassDaoImpl;

public class InsertAction {
	public String insert() throws IOException {

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();

		String cName = request.getParameter("cName");
		String tName = request.getParameter("tName");
		String cAddress = request.getParameter("cAddress");
		String cStartWeek = request.getParameter("cStartWeek");
		String cEndWeek = request.getParameter("cEndWeek");
		String cWeekday = request.getParameter("cWeekday");
		String courseIndex = request.getParameter("courseIndex");
		String classid = request.getParameter("classid");
		// 如果没有classid字段，那么表示为老师插入课程

		ClassDaoImpl dao = new ClassDaoImpl();
		Course course = null;
		Course result = null;
		if (null == classid) {
			course = new Course(0, cName, tName, cAddress, Integer
					.parseInt(cStartWeek), Integer.parseInt(cEndWeek), Integer
					.parseInt(cWeekday), Integer.parseInt(courseIndex));
			result = dao.selectTeacherCourse(course);
		} else {
			course = new Course(Integer.parseInt(classid), cName, tName,
					cAddress, Integer.parseInt(cStartWeek), Integer
							.parseInt(cEndWeek), Integer.parseInt(cWeekday),
					Integer.parseInt(courseIndex));
			result = dao.selectOneClass(course);
		}

		PrintWriter out = response.getWriter();
		// 如果该课程还不存在，则插入该课程，返回课程插入后的courseid作为同步过的标志
		// 如果该课程已经存在了，则不做操作，同样返回courseid作为同步过的标志
		int resultID = 0;
		if (null == result) {
			resultID = dao.insertOneCourse(course);
		} else {
			resultID = result.getId();
		}
		out.print("{result:1,courseid:" + resultID + "}");

		out.flush();
		out.close();
		return null;
	}
}
