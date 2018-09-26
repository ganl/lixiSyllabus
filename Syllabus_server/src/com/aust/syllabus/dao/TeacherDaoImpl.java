package com.aust.syllabus.dao;

import org.apache.ibatis.session.SqlSession;

import com.aust.syllabus.beans.Teacher;
import com.aust.syllabus.util.Util;

public class TeacherDaoImpl implements TeacherMapper {

	public int insertOne(Teacher teacher) {
		SqlSession session = Util.getSqlSessionFactory().openSession();
		int id = 0;
		try {
			TeacherMapper dao = session.getMapper(TeacherMapper.class);
			dao.insertOne(teacher);
			session.commit();
			id = teacher.getId();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return id;
	}

	public Teacher selectOne(Teacher teacher) {
		SqlSession session = Util.getSqlSessionFactory().openSession();
		Teacher teacherOfResult = null;
		try {
			TeacherMapper dao = session.getMapper(TeacherMapper.class);
			teacherOfResult = dao.selectOne(teacher);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return teacherOfResult;
	}

}
