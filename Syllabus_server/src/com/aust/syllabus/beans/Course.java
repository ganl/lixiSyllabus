package com.aust.syllabus.beans;

public class Course {
	private int classid; // self-increasing

	private int id; // courseid

	public Course(int classid, String cName, String tName, String cAddress,
			int cStartWeek, int cEndWeek, int cWeekday, int courseIndex) {
		super();
		this.classid = classid;
		this.cName = cName;
		this.tName = tName;
		this.cAddress = cAddress;
		this.cStartWeek = cStartWeek;
		this.cEndWeek = cEndWeek;
		this.cWeekday = cWeekday;
		this.courseIndex = courseIndex;
	}

	public Course() {

	}

	public int getClassid() {
		return classid;
	}

	public void setClassid(int classid) {
		this.classid = classid;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public String getcAddress() {
		return cAddress;
	}

	public void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}

	public int getcStartWeek() {
		return cStartWeek;
	}

	public void setcStartWeek(int cStartWeek) {
		this.cStartWeek = cStartWeek;
	}

	public int getcEndWeek() {
		return cEndWeek;
	}

	public void setcEndWeek(int cEndWeek) {
		this.cEndWeek = cEndWeek;
	}

	public int getcWeekday() {
		return cWeekday;
	}

	public void setcWeekday(int cWeekday) {
		this.cWeekday = cWeekday;
	}

	public int getCourseIndex() {
		return courseIndex;
	}

	public void setCourseIndex(int courseIndex) {
		this.courseIndex = courseIndex;
	}

	private String cName; // course name

	private String tName; // name of teacher

	private String cAddress; // address

	private int cStartWeek;

	private int cEndWeek;

	private int cWeekday; // day of week

	private int courseIndex; // the index of day

	public String toJSONString() {
		StringBuilder sb = new StringBuilder("{classid:");
		sb.append("");
		sb.append(classid);
		sb.append(",");
		sb.append("courseid:" + id + ",");
		sb.append("cName:" + "'" + cName + "',");
		sb.append("tName:" + "'" + tName + "',");
		sb.append("cAddress:" + "'" + cAddress + "',");
		sb.append("cStartWeek:" + cStartWeek + ",");
		sb.append("cEndWeek:" + cEndWeek + ",");
		sb.append("cWeekday:" + cWeekday + ",");
		sb.append("courseIndex:" + courseIndex + "}");
		return sb.toString();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
