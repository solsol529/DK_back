package com.developerkirby.Main.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.developerkirby.Main.Common;


public class GoodDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public void addGood(int reqWriteNum, int reqMemberNum) {
		System.out.println("reqWriteNum : "+reqWriteNum+"reqMemberNum"+reqMemberNum);
		String sql = "INSERT INTO GOOD VALUES(?, ?)";
		
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reqMemberNum);
			pstmt.setInt(2, reqWriteNum);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(pstmt);
		Common.close(conn);
	}
	
	public int alreadyGood(int reqWriteNum, int reqMemberNum) {
		String sql = "SELECT * FROM GOOD WHERE WRITE_NUM = ? AND MEMBER_NUM = ?";
		
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reqMemberNum);
			pstmt.setInt(2, reqWriteNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				Common.close(rs);
				Common.close(pstmt);
				Common.close(conn);
				return 1; // 이미 좋아요 등록
			} else {
				Common.close(rs);
				Common.close(pstmt);
				Common.close(conn);
				return 0; // 아직 안 등록
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			return -1; // 오류 났을 시 반환값
		}
	}

}
