package com.aust.syllabus.dao;

import com.aust.syllabus.beans.Teacher;

public interface TeacherMapper {

	public Teacher selectOne(Teacher teacher);

	public int insertOne(Teacher teacher);

}
