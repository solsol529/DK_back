package com.developerkirby.Admin.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.developerkirby.Admin.Common;
import com.developerkirby.Admin.VO.AdminBoardVO;
import com.developerkirby.Admin.VO.AdminWriteVO;

public class AdminWriteDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private PreparedStatement pstmt = null;
	
	public List<AdminWriteVO> writeSelect() {
		List<AdminWriteVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "SELECT WRITE_NUM \"글번호\", WRITE_NAME \"글제목\", WRITE_DATE \"작성일\", NICKNAME \"작성자\" \r\n"
					+ "FROM WRITE W, MEMBER M WHERE W.MEMBER_NUM = M.MEMBER_NUM ORDER BY WRITE_NUM";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int writeNum = rs.getInt("글번호");
				String writeName = rs.getString("글제목");
				Date writeDate = rs.getDate("작성일");
				String nickname = rs.getString("작성자");

				AdminWriteVO vo = new AdminWriteVO();
				vo.setWriteNum(writeNum);
				vo.setWriteName(writeName);
				vo.setWriteDate(writeDate);
				vo.setNickname(nickname);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);
			
		} catch (Exception e) { e.printStackTrace();}
		return list;
	}
	
	public List<AdminBoardVO> writeSearchSelect(String target) {
		List<AdminBoardVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "SELECT BOARD_NAME \"게시판\", COUNT(*) \"게시글수\" FROM WRITE \r\n"
					+ "WHERE BOARD_NAME LIKE ? GROUP BY BOARD_NAME \r\n"
					+ "UNION SELECT BOARD_NAME \"게시판\", 0 \"게시글수\" \r\n"
					+ "FROM BOARD WHERE BOARD_NAME NOT IN (SELECT BOARD_NAME FROM WRITE GROUP BY BOARD_NAME) \r\n"
					+ "AND BOARD_NAME LIKE ? ORDER BY \"게시글수\" DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, '%'+target+'%');
			pstmt.setString(2, '%'+target+'%');
			rs = pstmt.executeQuery();
			int cnt = 1;
			while(rs.next()) {
				int boardNum = cnt++;
				String boardName = rs.getString("게시판");
				int CountWrite = rs.getInt("게시글수");
				
				AdminBoardVO vo = new AdminBoardVO();
				vo.setBoardNum(boardNum);
				vo.setBoardName(boardName);
				vo.setCountWrite(CountWrite);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);
			
		} catch (Exception e) { e.printStackTrace();}
		return list;
	}
	
	public void writeDelete(String target) {
		String[] targetArr = target.split(",");
		for(String targetStr : targetArr) {
			try {
				conn = Common.getConnection();
				PreparedStatement pstmt1 = null;
				String sql1 = "DELETE FROM GOOD WHERE WRITE_NUM \r\n"
						+ "IN (SELECT WRITE_NUM FROM WRITE WHERE BOARD_NAME IN (?))";
				pstmt1 = conn.prepareStatement(sql1);
				pstmt1.setString(1, targetStr);
				pstmt1.executeUpdate();
				
				PreparedStatement pstmt2 = null;
				String sql2 = "DELETE FROM COMMENTS WHERE WRITE_NUM IN "
						+ "(SELECT WRITE_NUM FROM WRITE WHERE BOARD_NAME IN (?))";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, targetStr);
				pstmt2.executeUpdate();
				
				PreparedStatement pstmt3 = null;
				String sql3 = "DELETE FROM WRITE WHERE BOARD_NAME IN (?)";
				pstmt3 = conn.prepareStatement(sql3);
				pstmt3.setString(1, targetStr);
				pstmt3.executeUpdate();
				
				String sql = "DELETE FROM BOARD WHERE BOARD_NAME IN (?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, targetStr);
				pstmt.executeUpdate();
				
				Common.close(pstmt1);
				Common.close(pstmt2);
				Common.close(pstmt3);
				Common.close(pstmt);
				Common.close(conn);
				
			} catch (Exception e) { e.printStackTrace();}
		}
	}
	
	public List<AdminWriteVO> writeDetail(int writeNum) {
		List<AdminWriteVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, writeNum);
			rs = pstmt.executeQuery();
			int cnt = 1;
			while(rs.next()) {
				int boardNum = cnt++;
				String boardName = rs.getString("게시판");
				int CountWrite = rs.getInt("게시글수");
				
				AdminWriteVO vo = new AdminWriteVO();
				vo.setWriteDate(null);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);
			
		} catch (Exception e) { e.printStackTrace();}
		return list;
	}
}
