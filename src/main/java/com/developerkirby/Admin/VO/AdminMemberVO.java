package com.developerkirby.Admin.VO;

import java.sql.Date;

public class AdminMemberVO {
	private int memberNum;
	private String nickname;
	private String grade;
	private int countWrite;
	private int countComment;
	private String phone;
	private String email;
	private Date regDate;
	private String pfImg;
	private String isAdOk;

	public int getMemberNum() {
		return memberNum;
	}
	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public int getCountWrite() {
		return countWrite;
	}
	public void setCountWrite(int countWrite) {
		this.countWrite = countWrite;
	}
	public int getCountComment() {
		return countComment;
	}
	public void setCountComment(int countComment) {
		this.countComment = countComment;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getPfImg() {
		return pfImg;
	}
	public void setPfImg(String pfImg) {
		this.pfImg = pfImg;
	}
	public String getIsAdOk() {
		return isAdOk;
	}
	public void setIsAdOk(String isAdOk) {
		this.isAdOk = isAdOk;
	}
}
