package com.aust.syllabus.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.aust.syllabus.beans.Course;
import com.aust.syllabus.beans.User;
import com.aust.syllabus.dao.ClassDaoImpl;
import com.aust.syllabus.dao.UserDaoImpl;

public class LoginAction {
	public String login() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();

		String universityName = request.getParameter("universityName");
		String departmentName = request.getParameter("departmentName");
		String gradeNum = request.getParameter("gradeNum");
		String className = request.getParameter("className");
		String majorName = request.getParameter("majorName");

		System.out.println("******************************");
		User user = new User(universityName, departmentName, majorName,
				className, gradeNum);
		UserDaoImpl userDao = new UserDaoImpl();
		User result = userDao.selectOne(user);// ("USTB");

		PrintWriter out = response.getWriter();

		//�޴��û���������û������ظ��û�������id��Ϊclassid
		if (null == result) {
			int id = userDao.insertOne(user);
			int classid = id;
			System.out.println("id:" + classid);
			out.print("{result:2,classid:" + classid + "}");
		} else {
			//�û����ڣ�ͬ������classid
			int classid = result.getID();
			System.out.println("classid:" + classid);
			ClassDaoImpl classDao = new ClassDaoImpl();
			List<Course> courses = classDao.selectClass(classid);
			StringBuilder sb;
			if (courses.isEmpty()) {
				//�����ѯ�γ̽��Ϊ�գ�����result Ϊ0
				sb = new StringBuilder("{result:0,classid:" + classid + "}");
			} else {
				//�����ѯ������գ�����reusltΪ1
				sb = new StringBuilder("{result:1,classid:" + classid
						+ ",courses:");
				for (int i = 0; i < courses.size(); i++) {
					if (i == 0) {
						sb.append("[");
					}
					if (i != courses.size() - 1) {
						sb.append(courses.get(i).toJSONString() + ",");
					} else {
						sb.append(courses.get(i).toJSONString() + "]");
					}
				}
			}
			sb.append("}");

			out.print(sb.toString());
		}

		out.flush();
		out.close();

		return null;
	}
}
