package com.aust.syllabus.beans;

public class User {

	private int ID;

	private String UNIVERSITY_NAME;

	private String DEPARTMENT_NAME;

	private String MAJOR_NAME;

	private String CLASS_NAME;

	private String GRADE_NUM;

	public User() {

	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getUNIVERSITY_NAME() {
		return UNIVERSITY_NAME;
	}

	public void setUNIVERSITY_NAME(String uNIVERSITYNAME) {
		UNIVERSITY_NAME = uNIVERSITYNAME;
	}

	public String getDEPARTMENT_NAME() {
		return DEPARTMENT_NAME;
	}

	public void setDEPARTMENT_NAME(String dEPARTMENTNAME) {
		DEPARTMENT_NAME = dEPARTMENTNAME;
	}

	public String getMAJOR_NAME() {
		return MAJOR_NAME;
	}

	public void setMAJOR_NAME(String mAJORNAME) {
		MAJOR_NAME = mAJORNAME;
	}

	public String getCLASS_NAME() {
		return CLASS_NAME;
	}

	public void setCLASS_NAME(String cLASSNAME) {
		CLASS_NAME = cLASSNAME;
	}

	public String getGRADE_NUM() {
		return GRADE_NUM;
	}

	public void setGRADE_NUM(String gRADENUM) {
		GRADE_NUM = gRADENUM;
	}

	public User(String uNIVERSITYNAME, String dEPARTMENTNAME, String mAJORNAME,
			String cLASSNAME, String gRADENUM) {
		super();
		UNIVERSITY_NAME = uNIVERSITYNAME;
		DEPARTMENT_NAME = dEPARTMENTNAME;
		MAJOR_NAME = mAJORNAME;
		CLASS_NAME = cLASSNAME;
		GRADE_NUM = gRADENUM;
	}

	public String toJSONString() {
		StringBuilder sb = new StringBuilder("{classid:");
		sb.append("'");
		sb.append(CLASS_NAME + ID);
		sb.append("'}");
		return sb.toString();
	}

}
