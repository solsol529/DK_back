package com.developerkirby.Admin.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.developerkirby.Admin.Common;
import com.developerkirby.Admin.VO.AdminBoardVO;
import com.developerkirby.Admin.VO.AdminWriteVO;


public class AdminBoardDAO {
	
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private PreparedStatement pstmt = null;
	
	public List<AdminBoardVO> boardSelect() {
		List<AdminBoardVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "SELECT BOARD_NAME \"게시판\", COUNT(*) \"게시글수\" FROM WRITE GROUP BY BOARD_NAME \r\n"
					+ "UNION SELECT BOARD_NAME \"게시판\", 0 \"게시글수\" \r\n"
					+ "FROM BOARD WHERE BOARD_NAME NOT IN (SELECT BOARD_NAME FROM WRITE GROUP BY BOARD_NAME) \r\n"
					+ "ORDER BY \"게시글수\" DESC";
			pstmt = conn.prepareStatement(sql);
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
	public List<AdminBoardVO> boardSearchSelect(String target) {
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
	
	public void boardDelete(String target) {
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
	
	public List<AdminBoardVO> boardDetail(String target) {
		List<AdminBoardVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "SELECT COUNT(*) \"게시글수\" FROM WRITE WHERE BOARD_NAME = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, target);
			rs = pstmt.executeQuery();
			
			List<AdminWriteVO> writeList = new ArrayList<>();
			PreparedStatement pstmt1 = null;
			ResultSet rs1 = null;
			String sql1 = "SELECT * FROM (SELECT WRITE_NAME \"글제목\", NICKNAME \"작성자\", WRITE_DATE \"작성일\" FROM WRITE W, MEMBER M\r\n"
					+ "WHERE M.MEMBER_NUM = W.MEMBER_NUM AND BOARD_NAME = ? ORDER BY WRITE_DATE DESC) WHERE ROWNUM <=5";
			
			pstmt1 = conn.prepareStatement(sql1);
			pstmt1.setString(1, target);
			rs1 = pstmt1.executeQuery();
			while(rs1.next()) {
				AdminWriteVO vo = new AdminWriteVO();
				String writeName = rs1.getString("글제목");
				String nickname = rs1.getString("작성자");
				Date writeDate = rs1.getDate("작성일");
				SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
				String dateToStr = sdf.format(writeDate);
				vo.setNickname(nickname);
				vo.setWriteName(writeName);
				vo.setWriteDateStr(dateToStr);
				writeList.add(vo);
			}
			rs.next();
			AdminBoardVO vo = new AdminBoardVO();
			int countWrite = rs.getInt("게시글수");
			
			vo.setCountWrite(countWrite);
			vo.setWrites(writeList);
			list.add(vo);
			
			Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);
			
		} catch (Exception e) { e.printStackTrace();}
		return list;
	}
	
	public boolean boardNameCheck(String getBoardName) {
		try {
			conn = Common.getConnection();
			String sql = "SELECT * FROM BOARD WHERE BOARD_NAME = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getBoardName);
			rs = pstmt.executeQuery();
        	
        	if (rs.next()) return false; // 이미 존재하므로 사용 불가능함!
        	Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			return true;
		} catch (Exception e) { e.printStackTrace();}
		return false;
	}
	
	public boolean boardUpdate(String getBoardName, String getNewName) {
		try {
			conn = Common.getConnection();
			PreparedStatement pstmt1 = null;
			String sql1 = "INSERT INTO BOARD VALUES ('관리자')";
			pstmt1 = conn.prepareStatement(sql1);
			pstmt1.executeUpdate();
			
			PreparedStatement pstmt2 = null;
			String sql2 = "UPDATE WRITE SET BOARD_NAME = '관리자' WHERE BOARD_NAME = ?";
			pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setString(1, getBoardName);
			pstmt2.executeUpdate();
			
			String sql = "UPDATE BOARD SET BOARD_NAME = ? WHERE BOARD_NAME = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getNewName);
			pstmt.setString(2, getBoardName);
			int cnt =  pstmt.executeUpdate(); // INSERT / DELETE / UPDATE 레코드 반영된 갯수 확인
			// CREATE / DROP 관련 구문에서는 -1 을 반환
			
			PreparedStatement pstmt3 = null;
			String sql3 = "UPDATE WRITE SET BOARD_NAME = ? WHERE BOARD_NAME = '관리자'";
			pstmt3 = conn.prepareStatement(sql3);
			pstmt3.setString(1, getNewName);
			pstmt3.executeUpdate();
			
			PreparedStatement pstmt4 = null;
			String sql4 = "DELETE FROM BOARD WHERE BOARD_NAME = '관리자'";
			pstmt4 = conn.prepareStatement(sql4);
			pstmt4.executeUpdate();
			
			Common.close(rs);
			Common.close(pstmt1);
			Common.close(pstmt2);
			Common.close(pstmt3);
			Common.close(pstmt4);
			Common.close(pstmt);
			Common.close(conn);
			
			if(cnt == 1) return true;
			
		} catch (Exception e) { e.printStackTrace();}
		return false;
	}
	
	public boolean boardInsert(String getNewName) {
		try {
			
			conn = Common.getConnection();
			String sql = "INSERT INTO BOARD VALUES (?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getNewName);
			int cnt =  pstmt.executeUpdate(); // INSERT / DELETE / UPDATE 레코드 반영된 갯수 확인
			// CREATE / DROP 관련 구문에서는 -1 을 반환
			
			Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);
			
			if(cnt == 1) return true;
			
		} catch (Exception e) { e.printStackTrace();}
		return false;
	}
}
