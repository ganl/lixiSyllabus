package com.aust.syllabus.dao;

import org.apache.ibatis.session.SqlSession;

import com.aust.syllabus.beans.User;
import com.aust.syllabus.util.Util;

public class UserDaoImpl implements UserMapper {
	public User selectOne(User user) {
		SqlSession session = Util.getSqlSessionFactory().openSession();
		User userOfResult = null;
		try {
			UserMapper dao = session.getMapper(UserMapper.class);
			userOfResult = dao.selectOne(user);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return userOfResult;
	}

	public int insertOne(User user) {
		SqlSession session = Util.getSqlSessionFactory().openSession();
		int id = 0;
		try {
			UserMapper dao = session.getMapper(UserMapper.class);
			dao.insertOne(user);
			session.commit();
			id = user.getID();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return id;
	}

	// public User selectOne(String UniversityName) {
	// // TODO Auto-generated method stub
	// return null;
	// }
}