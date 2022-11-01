package com.developerkirby.Admin.VO;

import java.sql.Date;

public class AdminWriteVO {
	private int writeNum;
	private String writeName;
	private String nickname;
	private Date writeDate;
	private int countComment;
	private int countGood;
	
	public int getWriteNum() {
		return writeNum;
	}
	public void setWriteNum(int writeNum) {
		this.writeNum = writeNum;
	}
	public String getWriteName() {
		return writeName;
	}
	public void setWriteName(String writeName) {
		this.writeName = writeName;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Date getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}
	public int getCountComment() {
		return countComment;
	}
	public void setCountComment(int countComment) {
		this.countComment = countComment;
	}
	public int getCountGood() {
		return countGood;
	}
	public void setCountGood(int countGood) {
		this.countGood = countGood;
	}
}
