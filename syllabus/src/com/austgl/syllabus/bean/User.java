package com.austgl.syllabus.bean;

public class User {
	// private String sNo;

	private String universityName;

	private String departmentName;

	private String majorName;

	private String className;

	private String gradeNum;

	public User() {

	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getGradeNum() {
		return gradeNum;
	}

	public void setGradeNum(String gradeNum) {
		this.gradeNum = gradeNum;
	}

	public User(String universityName, String departmentName, String majorName,
			String className, String gradeNum) {
		super();
		this.universityName = universityName;
		this.departmentName = departmentName;
		this.setMajorName(majorName);
		this.className = className;
		this.gradeNum = gradeNum;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

}
