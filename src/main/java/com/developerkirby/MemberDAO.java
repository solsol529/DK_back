package com.developerkirby;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	public List<MemberVO> memberSelect() {
		List<MemberVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM MEMBER";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int member_num = rs.getInt("member_num");
				String nickname = rs.getString("nickname");
				String pwd = rs.getString("pwd");
				Date reg_date = rs.getDate("reg_date");
				
				MemberVO vo = new MemberVO();
				vo.setMember_num(member_num);
				vo.setNickname(nickname);
				vo.setPwd(pwd);
				vo.setReg_date(reg_date);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// 로그인 관련된 메소드, 입력된 id,pwd와 DB의 id,pwd가 일치해야 로그인된것
	public boolean logingCheck(String nickname, String pwd) {
		try {
			conn = Common.getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM MEMBER WHERE NICKNAME = " + "'" + nickname + "'";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
        		String sqlNickname = rs.getString("NICKNAME");
        		String sqlPwd = rs.getString("PWD");
        		System.out.println("NICKNAME : " + sqlNickname);
        		System.out.println("PWD : " + sqlPwd);
        		if(nickname.equals(sqlNickname) && pwd.equals(sqlPwd)) return true;	
        	}
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			
		} catch (Exception e) { e.printStackTrace();}
		return false;
	}
	
}
