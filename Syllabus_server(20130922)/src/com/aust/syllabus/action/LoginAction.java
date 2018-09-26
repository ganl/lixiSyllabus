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

		//无此用户，插入该用户，返回该用户插入后的id作为classid
		if (null == result) {
			int id = userDao.insertOne(user);
			int classid = id;
			System.out.println("id:" + classid);
			out.print("{result:2,classid:" + classid + "}");
		} else {
			//用户存在，同样返回classid
			int classid = result.getID();
			System.out.println("classid:" + classid);
			ClassDaoImpl classDao = new ClassDaoImpl();
			List<Course> courses = classDao.selectClass(classid);
			StringBuilder sb;
			if (courses.isEmpty()) {
				//如果查询课程结果为空，返回result 为0
				sb = new StringBuilder("{result:0,classid:" + classid + "}");
			} else {
				//如果查询结果不空，返回reuslt为1
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
