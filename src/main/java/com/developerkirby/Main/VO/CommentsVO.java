package com.developerkirby.Main.VO;

import java.sql.Date;

public class CommentsVO {
	private int commentNum;
	private int writeNum;
	private String nickname;
	private String commentContent;
	private Date writeDate;
	private String writeDateStr;
	private String pfImg;
	
	public int getWriteNum() {
		return writeNum;
	}
	public void setWriteNum(int writeNum) {
		this.writeNum = writeNum;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public Date getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}
	public String getWriteDateStr() {
		return writeDateStr;
	}
	public void setWriteDateStr(String writeDateStr) {
		this.writeDateStr = writeDateStr;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public String getPfImg() {
		return pfImg;
	}
	public void setPfImg(String pfImg) {
		this.pfImg = pfImg;
	}
}
