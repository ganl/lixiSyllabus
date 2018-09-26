package com.aust.syllabus.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.aust.syllabus.beans.Course;
import com.aust.syllabus.dao.ClassDaoImpl;

public class UpdateAction {
	public String update() throws IOException {
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
		String id = request.getParameter("courseid");

		ClassDaoImpl dao = new ClassDaoImpl();
		Course course = null;
		Course result = null;
		PrintWriter out = response.getWriter();
		// id Ϊ�գ���ʾΪͬ�����Ŀγ̣���ôִ�еľ��ǲ������
		if (id == null) {
			// ���classidΪ�գ���ʾ����ʦ
			if (null == classid) {
				course = new Course(0, cName, tName, cAddress, Integer
						.parseInt(cStartWeek), Integer.parseInt(cEndWeek),
						Integer.parseInt(cWeekday), Integer
								.parseInt(courseIndex));
				// course.setId(Integer.parseInt(id));
				result = dao.selectTeacherCourse(course);
				System.out.println("UpdateAction: " + result);
			} else {
				// ����ǿգ���ʾ��ѧ��
				course = new Course(Integer.parseInt(classid), cName, tName,
						cAddress, Integer.parseInt(cStartWeek), Integer
								.parseInt(cEndWeek),
						Integer.parseInt(cWeekday), Integer
								.parseInt(courseIndex));
				// course.setId(Integer.parseInt(id));
				result = dao.selectOneClass(course);
			}

			int resultID = 0;
			if (null == result) {
				resultID = dao.insertOneCourse(course);
			} else {
				resultID = result.getId();
			}
			out.print("{result:0,courseid:" + resultID + "}");

		} else {
			// ���id���գ���ʾ�Ǹ����Ѵ��ڿγ�
			if (null == classid) {
				course = new Course(0, cName, tName, cAddress, Integer
						.parseInt(cStartWeek), Integer.parseInt(cEndWeek),
						Integer.parseInt(cWeekday), Integer
								.parseInt(courseIndex));
				course.setId(Integer.parseInt(id));
				result = dao.selectTeacherCourse(course);
			} else {
				course = new Course(Integer.parseInt(classid), cName, tName,
						cAddress, Integer.parseInt(cStartWeek), Integer
								.parseInt(cEndWeek),
						Integer.parseInt(cWeekday), Integer
								.parseInt(courseIndex));
				course.setId(Integer.parseInt(id));
				result = dao.selectOneClass(course);
			}
			course.setId(Integer.parseInt(id));
			System.out.println("id:" + id);
			// ClassDaoImpl dao = new ClassDaoImpl();
			dao.updateOneCourse(course);
			out.print("{result:1}");
		}

		out.flush();
		out.close();

		return null;
	}
}
