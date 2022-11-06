package com.developerkirby.Admin.VO;

import java.util.List;

public class AdminBoardVO {
	private int boardNum;
	private String boardName;
	private int CountWrite;
	private List<AdminWriteVO> writes;

	public int getBoardNum() {
		return boardNum;
	}
	public void setBoardNum(int boardNum) {
		this.boardNum = boardNum;
	}
	public String getBoardName() {
		return boardName;
	}
	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}
	public int getCountWrite() {
		return CountWrite;
	}
	public void setCountWrite(int countWrite) {
		CountWrite = countWrite;
	}
	public List<AdminWriteVO> getWrites() {
		return writes;
	}
	public void setWrites(List<AdminWriteVO> writes) {
		this.writes = writes;
	}

}
