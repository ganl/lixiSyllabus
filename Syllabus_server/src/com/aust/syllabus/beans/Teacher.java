package com.aust.syllabus.beans;

public class Teacher {

	private int id;

	private String universityName;

	private String teacherName;

	public int getId() {
		return id;
	}

	public Teacher() {
	}

	public Teacher(int id, String universityName, String teacherName) {
		super();
		this.id = id;
		this.universityName = universityName;
		this.teacherName = teacherName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

}
