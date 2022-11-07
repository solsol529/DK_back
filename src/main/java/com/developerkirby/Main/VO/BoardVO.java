package com.developerkirby.Main.VO;

import java.util.List;

public class BoardVO {
	private int boardNum;
	private String boardName;
	private int CountWrite;
	private List<WriteVO> writes;
	
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
	public List<WriteVO> getWrites() {
		return writes;
	}
	public void setWrites(List<WriteVO> writes) {
		this.writes = writes;
	}
	
}