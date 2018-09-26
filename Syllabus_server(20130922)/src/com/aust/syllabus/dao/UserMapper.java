package com.aust.syllabus.dao;

import com.aust.syllabus.beans.User;

public interface UserMapper {
	public int insertOne(User user);

	public User selectOne(User user);
}
