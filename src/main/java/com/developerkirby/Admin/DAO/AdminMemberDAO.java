package com.developerkirby.Admin.DAO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.developerkirby.Admin.Common;
import com.developerkirby.Admin.VO.AdminMemberVO;


public class AdminMemberDAO {

	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private PreparedStatement pstmt = null;

	public List<AdminMemberVO> memberSelect() {
		List<AdminMemberVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
//			stmt = conn.createStatement();
			String sql = "SELECT W.MEMBER_NUM \"회원번호\", NICKNAME \"닉네임\", GRADE \"등급\", COUNT(*) \"게시글수\",\r\n"
					+ "(SELECT COUNT(MEMBER_NUM) FROM COMMENTS C WHERE MEMBER_NUM = W.MEMBER_NUM GROUP BY MEMBER_NUM )\"댓글수\",\r\n"
					+ "PHONE \"핸드폰\", EMAIL \"이메일\", PF_IMG \"프로필\", IS_ADOK \"광고수신\", REG_DATE \"가입일\"\r\n"
					+ "FROM WRITE W, MEMBER M, MEMGRADE G\r\n"
					+ "WHERE M.MEMBER_NUM = W.MEMBER_NUM\r\n"
					+ "AND (SELECT COUNT(*) FROM WRITE W GROUP BY W.MEMBER_NUM HAVING W.MEMBER_NUM = M.MEMBER_NUM) \r\n"
					+ "BETWEEN LOWRITE AND HIWRITE\r\n"
					+ "GROUP BY W.MEMBER_NUM, NICKNAME, GRADE, PHONE, EMAIL, PF_IMG, IS_ADOK, REG_DATE\r\n"
					+ "UNION\r\n"
					+ "SELECT MEMBER_NUM \"회원번호\", NICKNAME \"닉네임\", '새싹' \"등급\", 0 \"게시글수\",\r\n"
					+ "(SELECT COUNT(MEMBER_NUM) FROM COMMENTS C WHERE MEMBER_NUM = M.MEMBER_NUM GROUP BY MEMBER_NUM )\"댓글수\",\r\n"
					+ "PHONE \"핸드폰\", EMAIL \"이메일\", PF_IMG \"프로필\", IS_ADOK \"광고수신\", REG_DATE \"가입일\"\r\n"
					+ "FROM MEMBER M\r\n"
					+ "WHERE MEMBER_NUM NOT IN(SELECT MEMBER_NUM FROM WRITE) AND MEMBER_NUM >= 7000";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				int memberNum = rs.getInt("회원번호");
				String nickname= rs.getString("닉네임");
				String grade = rs.getString("등급");
				int countWrite = rs.getInt("게시글수");
				int countComment = rs.getInt("댓글수");
				String phone = rs.getString("핸드폰");
				String email = rs.getString("이메일");
				String pfImg = rs.getString("프로필");
				String isAdOk = rs.getString("광고수신");
				Date regDate = rs.getDate("가입일");

				AdminMemberVO vo = new AdminMemberVO();
				vo.setMemberNum(memberNum);
				vo.setNickname(nickname);
				vo.setGrade(grade);
				vo.setCountWrite(countWrite);
				vo.setCountComment(countComment);
				vo.setPhone(phone);
				vo.setEmail(email);
				vo.setPfImg(pfImg);
				vo.setIsAdOk(isAdOk);
				vo.setRegDate(regDate);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);

		} catch (Exception e) { e.printStackTrace();}
		return list;
	}
	public List<AdminMemberVO> memberSearchSelect(String target) {
		List<AdminMemberVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "SELECT W.MEMBER_NUM \"회원번호\", NICKNAME \"닉네임\", GRADE \"등급\", COUNT(*) \"게시글수\",\r\n"
					+ "(SELECT COUNT(MEMBER_NUM) FROM COMMENTS C WHERE MEMBER_NUM = W.MEMBER_NUM GROUP BY MEMBER_NUM )\"댓글수\",\r\n"
					+ "PHONE \"핸드폰\", EMAIL \"이메일\", PF_IMG \"프로필\", IS_ADOK \"광고수신\", REG_DATE \"가입일\"\r\n"
					+ "FROM WRITE W, MEMBER M, MEMGRADE G\r\n"
					+ "WHERE M.MEMBER_NUM = W.MEMBER_NUM\r\n"
					+ "AND (SELECT COUNT(*) FROM WRITE W GROUP BY W.MEMBER_NUM HAVING W.MEMBER_NUM = M.MEMBER_NUM) \r\n"
					+ "BETWEEN LOWRITE AND HIWRITE AND (W.MEMBER_NUM = ? OR NICKNAME LIKE ? OR GRADE = ?)\r\n"
					+ "GROUP BY W.MEMBER_NUM, NICKNAME, GRADE, PHONE, EMAIL, PF_IMG, IS_ADOK, REG_DATE\r\n"
					+ "UNION\r\n"
					+ "SELECT MEMBER_NUM \"회원번호\", NICKNAME \"닉네임\", '새싹' \"등급\", 0 \"게시글수\",\r\n"
					+ "(SELECT COUNT(MEMBER_NUM) FROM COMMENTS C WHERE MEMBER_NUM = M.MEMBER_NUM GROUP BY MEMBER_NUM )\"댓글수\",\r\n"
					+ "PHONE \"핸드폰\", EMAIL \"이메일\", PF_IMG \"프로필\", IS_ADOK \"광고수신\", REG_DATE \"가입일\"\r\n"
					+ "FROM MEMBER M\r\n"
					+ "WHERE MEMBER_NUM NOT IN(SELECT MEMBER_NUM FROM WRITE) AND MEMBER_NUM >= 7000\r\n"
					+ "AND (MEMBER_NUM = ? OR NICKNAME LIKE ?)";
			pstmt = conn.prepareStatement(sql);
			if(target.matches("[+-]?\\d*(\\.\\d+)?")) pstmt.setInt(1, Integer.parseInt(target));
			// 문자열이 정규식을 만족하면(숫자로만 이루어진 문자열이면)
			else pstmt.setInt(1, 0);
			pstmt.setString(2, '%'+target+'%');
			pstmt.setString(3, target);
			if(target.matches("[+-]?\\d*(\\.\\d+)?")) pstmt.setInt(4, Integer.parseInt(target));
			// 문자열이 정규식을 만족하면(숫자로만 이루어진 문자열이면)
			else pstmt.setInt(4, 0);
			pstmt.setString(5, '%'+target+'%');
			rs = pstmt.executeQuery();

			while(rs.next()) {
				int memberNum = rs.getInt("회원번호");
				String nickname= rs.getString("닉네임");
				String grade = rs.getString("등급");
				int countWrite = rs.getInt("게시글수");
				int countComment = rs.getInt("댓글수");
				String phone = rs.getString("핸드폰");
				String email = rs.getString("이메일");
				String pfImg = rs.getString("프로필");
				String isAdOk = rs.getString("광고수신");
				Date regDate = rs.getDate("가입일");

				AdminMemberVO vo = new AdminMemberVO();
				vo.setMemberNum(memberNum);
				vo.setNickname(nickname);
				vo.setGrade(grade);
				vo.setCountWrite(countWrite);
				vo.setCountComment(countComment);
				vo.setPhone(phone);
				vo.setEmail(email);
				vo.setPfImg(pfImg);
				vo.setIsAdOk(isAdOk);
				vo.setRegDate(regDate);
				list.add(vo);
			}
			Common.close(rs);
			Common.close(pstmt);
			Common.close(conn);

		} catch (Exception e) { e.printStackTrace();}
		return list;
	}

	public boolean logingCheck(String nickname, String pwd) {
		try {
			conn = Common.getConnection();
			String sql = "SELECT * FROM MEMBER WHERE MEMBER_NUM = 1 AND NICKNAME = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nickname);
			rs = pstmt.executeQuery();

        	while(rs.next()) {
        		String sqlId = rs.getString("nickname");
        		String sqlPwd = rs.getString("PWD");
        		System.out.println("ID : " + sqlId);
        		System.out.println("PWD : " + sqlPwd);
        		if(nickname.equals(sqlId) && pwd.equals(sqlPwd)) return true;
        	}
        	Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		} catch (Exception e) { e.printStackTrace();}
		return false;
	}
}
