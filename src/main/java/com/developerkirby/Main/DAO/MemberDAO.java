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

	public List<MemberVO> memberSelect(String target) {
		List<MemberVO> list = new ArrayList<>();
		try {
			conn = Common.getConnection();
			String sql = "SELECT W.MEMBER_NUM \"회원번호\", NICKNAME \"닉네임\", GRADE \"등급\", COUNT(*) \"게시글수\",\r\n"
					+ "(SELECT COUNT(MEMBER_NUM) FROM COMMENTS C WHERE MEMBER_NUM = W.MEMBER_NUM GROUP BY MEMBER_NUM )\"댓글수\",\r\n"
					+ "PHONE \"핸드폰\", EMAIL \"이메일\", PF_IMG \"프로필\", IS_ADOK \"광고수신\", REG_DATE \"가입일\"\r\n"
					+ "FROM WRITE W, MEMBER M, MEMGRADE G\r\n"
					+ "WHERE M.MEMBER_NUM = W.MEMBER_NUM\r\n"
					+ "AND (SELECT COUNT(*) FROM WRITE W GROUP BY W.MEMBER_NUM HAVING W.MEMBER_NUM = M.MEMBER_NUM) \r\n"
					+ "BETWEEN LOWRITE AND HIWRITE AND M.MEMBER_NUM = ?\r\n"
					+ "GROUP BY W.MEMBER_NUM, NICKNAME, GRADE, PHONE, EMAIL, PF_IMG, IS_ADOK, REG_DATE\r\n"
					+ "UNION\r\n"
					+ "SELECT MEMBER_NUM \"회원번호\", NICKNAME \"닉네임\", '새싹' \"등급\", 0 \"게시글수\",\r\n"
					+ "(SELECT COUNT(MEMBER_NUM) FROM COMMENTS C WHERE MEMBER_NUM = M.MEMBER_NUM GROUP BY MEMBER_NUM )\"댓글수\",\r\n"
					+ "PHONE \"핸드폰\", EMAIL \"이메일\", PF_IMG \"프로필\", IS_ADOK \"광고수신\", REG_DATE \"가입일\"\r\n"
					+ "FROM MEMBER M\r\n"
					+ "WHERE MEMBER_NUM NOT IN(SELECT MEMBER_NUM FROM WRITE) AND MEMBER_NUM >= 7000 AND MEMBER_NUM = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, target);
			pstmt.setString(2, target);
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

				MemberVO vo = new MemberVO();
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
	
	public boolean memberDelete(String memberNum) {
		int result = 0;

		try {
			conn = Common.getConnection();
			String sql = "DELETE FROM MEMBER WHERE MEMBER_NUM = ?";
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
	
	public boolean memberNameCheck(String getNickname) {
		try {
			conn = Common.getConnection();
			String sql = "SELECT * FROM MEMBER WHERE NICKNAME = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getNickname);
			rs = pstmt.executeQuery();

        	if (rs.next()) return false; // 이미 존재하므로 사용 불가능함!
        	Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			return true;
		} catch (Exception e) { e.printStackTrace();}
		return false;
	}
	
	public boolean memberPhoneCheck(String getPhone) {
		try {
			conn = Common.getConnection();
			String sql = "SELECT * FROM MEMBER WHERE PHONE = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getPhone);
			rs = pstmt.executeQuery();

        	if (rs.next()) return false; // 이미 존재하므로 사용 불가능함!
        	Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
			return true;
			
		} catch (Exception e) { e.printStackTrace();}
		return false;
	}
	
	public int memberInsert(String nickname, String pwd, String phone, String email, String adOk) {
		int cnt = 0, memberNum = 0;
		try {
			conn = Common.getConnection();
			String sql = "INSERT INTO MEMBER(MEMBER_NUM, NICKNAME, PWD, EMAIL, PHONE, IS_ADOK) \r\n"
					+ "VALUES(MEMBER_NUM.NEXTVAL, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nickname);
			pstmt.setString(2, pwd);
			pstmt.setString(3, email);
			pstmt.setString(4, phone);
			pstmt.setString(5, adOk);
			cnt = pstmt.executeUpdate();
			
			if(cnt == 1) { // insert된게 1건이면 = 회원가입이 성공적으로 되면
				PreparedStatement pstmt2 = null;
				String sql2 = "SELECT MEMBER_NUM \"회원번호\" FROM MEMBER WHERE NICKNAME = ?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, nickname);
				rs = pstmt2.executeQuery();
				if(rs.next()) {
					memberNum = rs.getInt("회원번호");
				}
				Common.close(rs);
				Common.close(pstmt2);
			}
			Common.close(pstmt);
			Common.close(conn);
			
			return memberNum;

		} catch (Exception e) { e.printStackTrace();}
		return cnt;
	}
	
}
