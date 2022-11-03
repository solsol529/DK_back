package com.developerkirby.Admin.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.developerkirby.Admin.Common;
import com.developerkirby.Admin.VO.AdminCommentVO;


public class AdminCommentDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private PreparedStatement pstmt = null;
	
	public List<AdminCommentVO> commentSelect() {
		List<AdminCommentVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "SELECT COMMENT_NUM \"댓글번호\", NICKNAME \"작성자\", COMMENT_CONTENT \"댓글내용\", WRITE_DATE \"작성일\", WRITE_NUM \"원글번호\"\r\n"
					+ "FROM COMMENTS C, MEMBER M WHERE M.MEMBER_NUM = C.MEMBER_NUM";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int commentNum = rs.getInt("댓글번호");
				int writeNum = rs.getInt("원글번호");
				String nickname = rs.getString("작성자");
				String commentContent = rs.getString("댓글내용");
				Date writeDate = rs.getDate("작성일");
				
				AdminCommentVO vo = new AdminCommentVO();
				vo.setCommentNum(commentNum);
				vo.setWriteNum(writeNum);
				vo.setNickname(nickname);
				vo.setCommentContent(commentContent);
				vo.setWriteDate(writeDate);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);
			
		} catch (Exception e) { e.printStackTrace();}
		return list;
	}
	public List<AdminCommentVO> commentSearchSelect(String target) {
		List<AdminCommentVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "SELECT COMMENT_NUM \"댓글번호\", NICKNAME \"작성자\", COMMENT_CONTENT \"댓글내용\", WRITE_DATE \"작성일\", WRITE_NUM \"원글번호\"\r\n"
					+ "FROM COMMENTS C, MEMBER M WHERE M.MEMBER_NUM = C.MEMBER_NUM AND (NICKNAME LIKE ? OR COMMENT_CONTENT LIKE ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, '%'+target+'%');
			pstmt.setString(2, '%'+target+'%');
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int commentNum = rs.getInt("댓글번호");
				int writeNum = rs.getInt("원글번호");
				String nickname = rs.getString("작성자");
				String commentContent = rs.getString("댓글내용");
				Date writeDate = rs.getDate("작성일");
				
				AdminCommentVO vo = new AdminCommentVO();
				vo.setCommentNum(commentNum);
				vo.setWriteNum(writeNum);
				vo.setNickname(nickname);
				vo.setCommentContent(commentContent);
				vo.setWriteDate(writeDate);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);
			
		} catch (Exception e) { e.printStackTrace();}
		return list;
	}
	
	public void commentDelete(String target) {
		String[] targetArr = target.split(",");
		for(String targetStr : targetArr) {
			int tar;
			if(targetStr.matches("[+-]?\\d*(\\.\\d+)?")) 
				tar = Integer.parseInt(targetStr);
	         // 문자열이 정규식을 만족하면(숫자로만 이루어진 문자열이면)
	         else tar = 0;
			try {
				conn = Common.getConnection();
				String sql = "DELETE FROM COMMENTS WHERE COMMENT_NUM = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, tar);
				pstmt.executeUpdate();
				
				Common.close(pstmt);
				Common.close(conn);
				
			} catch (Exception e) { e.printStackTrace();}
		}
	}
}
