package com.developerkirby.Main.DAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.developerkirby.Main.Common;
import com.developerkirby.Main.VO.MemberVO;

public class MemberDAO {
	private Connection conn = null;
	private Statement stmt = null; //표준 SQL문을 수행하기 위한 Statement 객체 얻기
	private ResultSet rs = null; // Statement의 수행 결과를 여러행으로 받음
	// SQL문을 미리 컴파일해서 재 사용하므로 Statement 인터페이스보다 훨씬 빨르게 데이터베이스 작업을 수행
	private PreparedStatement pstmt = null; 
	
	public MemberVO logingCheck(String nickname, String pwd) {
		MemberVO memberInfo = new MemberVO();
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM MEMBER WHERE NICKNAME = " + "'" + nickname + "'";
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
        		String sqlNickname = rs.getString("NICKNAME");
        		String sqlPwd = rs.getString("PWD");
        		int memberNum = rs.getInt("MEMBER_NUM");
        		memberInfo.setNickname(sqlNickname);
        		memberInfo.setPwd(sqlPwd);
        		memberInfo.setMemberNum(memberNum);
        	}
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
		} catch (Exception e) { e.printStackTrace();}
		return memberInfo;	
	}

	
	public boolean memberDelete(String memberNum) {
		int result = 0;
		String sql = "DELETE FROM MEMBER WHERE MEMBER_NUM = ?";
		
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberNum);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(pstmt);
		Common.close(conn);
		if(result == 1) return true;
		else return false;
	}
	
	public boolean memberUpdate(String value, String type, String nickname) {
		int result = 0;
		String updatesql = null;
		if(type.equals("mail")) {
			System.out.println("회원정보 변경 쿼리문 ~~~~");
			updatesql = "UPDATE MEMBER SET EMAIL = ? WHERE NICKNAME = ?"; 
		} else if(type.equals("pwd")) {
			updatesql = "UPDATE MEMBER SET PWD = ? WHERE NICKNAME = ?"; 
		} 
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(updatesql);
			pstmt.setString(1, value);
			pstmt.setString(2, nickname);
			
			result = pstmt.executeUpdate();
			System.out.println("회원정보수정 DB 결과 확인 : " + result);
		}catch(Exception e) {
			e.printStackTrace();
		}
		Common.close(rs);
		Common.close(pstmt);
		Common.close(conn);
		
		if(result == 1) return true;
		else return false;
	}
}
