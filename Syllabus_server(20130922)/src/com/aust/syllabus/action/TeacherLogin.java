package com.aust.syllabus.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.aust.syllabus.beans.Course;
import com.aust.syllabus.beans.Teacher;
import com.aust.syllabus.dao.ClassDaoImpl;
import com.aust.syllabus.dao.ClassSyllabusMapper;
import com.aust.syllabus.dao.TeacherDaoImpl;
import com.aust.syllabus.dao.TeacherMapper;

public class TeacherLogin {
	public String login() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();

		String universityName = request.getParameter("universityName");
		String teacherName = request.getParameter("teacherName");
		System.out.println("TeacherLogin: universityName" + universityName);
		System.out.println("TeacherLogin: teacherName" + teacherName);

		Teacher teacher = new Teacher();
		teacher.setTeacherName(teacherName);
		teacher.setUniversityName(universityName);

		PrintWriter out = response.getWriter();
		int teacherID = 0;

		TeacherMapper mapper = new TeacherDaoImpl();
		if (null == mapper.selectOne(teacher)) {
			teacherID = mapper.insertOne(teacher);
			System.out.println("TeacherLogin: ------------------------");
			// out.print("{result:0,teacherID:" + teacherID + "}");
		}
		teacherID = mapper.selectOne(teacher).getId();
		ClassSyllabusMapper classMapper = new ClassDaoImpl();

		List<Course> list = classMapper.selectTeacherCourses(teacher);
		StringBuilder sb = null;
		System.out.println("TeacherLogin: ****************************");
		if (!list.isEmpty()) {
			sb = new StringBuilder("{result:1,teacherID:" + teacherID
					+ ",courses:");
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					sb.append("[");
				}
				if (i != list.size() - 1) {
					sb.append(list.get(i).toJSONString() + ",");
				} else {
					sb.append(list.get(i).toJSONString() + "]");
				}
			}
			sb.append("}");
		} else {

			sb = new StringBuilder("{result:2, teacherID:" + teacherID + "}");
			System.out.println("TeacherLogin: &&&&&&&&&&&&&&&&&&&&&&&&");
		}
		out.print(sb.toString());

		out.flush();
		out.close();
		return null;
	}
}
