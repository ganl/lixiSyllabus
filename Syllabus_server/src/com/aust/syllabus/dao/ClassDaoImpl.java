package com.aust.syllabus.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.aust.syllabus.beans.Course;
import com.aust.syllabus.beans.Teacher;
import com.aust.syllabus.util.Util;

public class ClassDaoImpl implements ClassSyllabusMapper {

	public int insertOneCourse(Course course) {
		SqlSession session = Util.getSqlSessionFactory().openSession();
		int id = 0;
		try {
			ClassSyllabusMapper mapper = session
					.getMapper(ClassSyllabusMapper.class);
			mapper.insertOneCourse(course);

			// System.out.println("id" + id);
			session.commit();
			id = course.getId();
			// System.out.println("id of course:" + course.getId());
			// id = course.getId();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			session.commit();
			session.close();
		}

		return id;
	}

	public List<Course> selectClass(int classid) {
		SqlSession session = Util.getSqlSessionFactory().openSession();
		List<Course> courses = null;
		try {
			ClassSyllabusMapper mapper = session
					.getMapper(ClassSyllabusMapper.class);
			courses = mapper.selectClass(classid);
		} catch (Exception e) {
			// TODO: handle exceptione
			e.printStackTrace();
		} finally {
			session.close();
		}
		return courses;
	}

	public Course selectOneClass(Course course) {
		SqlSession session = Util.getSqlSessionFactory().openSession();
		Course result = null;
		try {
			ClassSyllabusMapper mapper = session
					.getMapper(ClassSyllabusMapper.class);
			result = mapper.selectOneClass(course);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			session.close();
		}
		return result;
	}

	public Course selectTeacherCourse(Course course) {
		SqlSession session = Util.getSqlSessionFactory().openSession();
		Course result = null;
		try {
			ClassSyllabusMapper mapper = session
					.getMapper(ClassSyllabusMapper.class);
			result = mapper.selectTeacherCourse(course);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			session.close();
		}
		return result;
	}

	public void updateOneCourse(Course course) {
		SqlSession session = Util.getSqlSessionFactory().openSession();
		try {
			ClassSyllabusMapper mapper = session
					.getMapper(ClassSyllabusMapper.class);
			mapper.updateOneCourse(course);// (course);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			session.close();
		}
	}

	public List<Course> selectTeacherCourses(Teacher teacher) {
		SqlSession session = Util.getSqlSessionFactory().openSession();
		List<Course> courses = null;
		try {
			ClassSyllabusMapper mapper = session
					.getMapper(ClassSyllabusMapper.class);
			courses = mapper.selectTeacherCourses(teacher);
		} catch (Exception e) {
			// TODO: handle exceptione
			e.printStackTrace();
		} finally {
			session.close();
		}
		return courses;
	}

}
