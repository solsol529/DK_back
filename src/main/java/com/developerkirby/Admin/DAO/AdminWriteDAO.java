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
	
	public List<AdminWriteVO> writeSearchSelect(String target) {
		List<AdminWriteVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "SELECT WRITE_NUM \"글번호\", WRITE_NAME \"글제목\", WRITE_DATE \"작성일\", NICKNAME \"작성자\"\r\n"
					+ "FROM WRITE W, MEMBER M WHERE W.MEMBER_NUM = M.MEMBER_NUM AND\r\n"
					+ "(NICKNAME LIKE ? OR WRITE_NAME LIKE ?) ORDER BY WRITE_NUM";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, '%'+target+'%');
			pstmt.setString(2, '%'+target+'%');
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
	
	public List<AdminWriteVO> writeDetail(String target) {
		List<AdminWriteVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "SELECT COUNT(*) \"좋아요수\", W.WRITE_NUM \"게시글번호\", BOARD_NAME \"게시판명\", \r\n"
					+ "WRITE_NAME \"게시글명\", NICKNAME \"글작성자\", \r\n"
					+ "WRITE_DATE \"작성일\", WRITE_CONTENTS \"글내용\",\r\n"
					+ "(SELECT COUNT(*) FROM COMMENTS C WHERE C.WRITE_NUM = W.WRITE_NUM) \"댓글수\"\r\n"
					+ "FROM MEMBER M, WRITE W, (SELECT DISTINCT * FROM GOOD) G\r\n"
					+ "WHERE M.MEMBER_NUM = W.MEMBER_NUM AND G.WRITE_NUM = W.WRITE_NUM AND W.WRITE_NUM = ?\r\n"
					+ "GROUP BY W.WRITE_NUM, BOARD_NAME, WRITE_NAME, NICKNAME, WRITE_DATE, WRITE_CONTENTS";
			pstmt = conn.prepareStatement(sql);
			if(target.matches("[+-]?\\d*(\\.\\d+)?")) pstmt.setInt(1, Integer.parseInt(target));
			// 문자열이 정규식을 만족하면(숫자로만 이루어진 문자열이면)
			else pstmt.setInt(1, 0);
			rs = pstmt.executeQuery();
			
			PreparedStatement pstmt1 = null;
			ResultSet rs1 = null;
			String sql1 = "";
			pstmt1 = conn.prepareStatement(sql1);
			if(target.matches("[+-]?\\d*(\\.\\d+)?")) pstmt1.setInt(1, Integer.parseInt(target));
			else pstmt1.setInt(1, 0);
			rs1 = pstmt.executeQuery();
			rs1.next();
			
			rs.next();
			AdminWriteVO vo = new AdminWriteVO();
			int writeNum = rs.getInt("게시글번호");
			String writeName = rs.getString("게시글명");
			String nickname = rs.getString("글작성자");
			Date writeDate = rs.getDate("작성일");
			int countComment = rs.getInt("댓글수");
			int countGood = rs.getInt("좋아요수");
			String boardName = rs.getString("게시판명");
			String writeContents = rs.getString("글내용");
			
			vo.setWriteNum(writeNum);
			vo.setWriteName(writeName);
			vo.setNickname(nickname);
			vo.setWriteDate(writeDate);
			vo.setCountComment(countComment);
			vo.setCountGood(countGood);
			vo.setBoardName(boardName);
			vo.setWriteContents(writeContents);
			list.add(vo);
			
			Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);
			
		} catch (Exception e) { e.printStackTrace();}
		return list;
	}
}
