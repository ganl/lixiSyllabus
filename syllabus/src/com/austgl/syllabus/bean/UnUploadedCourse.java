package com.austgl.syllabus.bean;

public class UnUploadedCourse {
	private long id; // self-increasing

	private long courseId;

	private int action;

	public UnUploadedCourse() {
	}

	public UnUploadedCourse(long id, int courseId, int action) {
		super();
		this.id = id;
		this.courseId = courseId;
		this.action = action;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
}
