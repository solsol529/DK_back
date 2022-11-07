package com.developerkirby.Main.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.developerkirby.Main.Common;
import com.developerkirby.Main.VO.CommentsVO;



public class CommentsDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public List<CommentsVO> commentsWriteDetail(int reqWriteNum) {
		List<CommentsVO> list = new ArrayList<>();
		String sql = "SELECT NICKNAME \"댓글작성자\", COMMENT_CONTENT \"댓글내용\", COMMENT_NUM \"댓글번호\",\r\n"
				+ "C.WRITE_DATE \"댓글 작성일\", M.PF_IMG \"프로필\" \r\n"
				+ "FROM COMMENTS C, MEMBER M, WRITE W\r\n"
				+ "WHERE C.WRITE_NUM = W.WRITE_NUM AND C.MEMBER_NUM = M.MEMBER_NUM\r\n"
				+ "AND W.WRITE_NUM = ?";
		
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reqWriteNum);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String pfImg = rs.getString("프로필");
				String nickname = rs.getString("댓글작성자");
				String commentContent = rs.getString("댓글내용");
				Date writeDate = rs.getDate("댓글 작성일");
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
                String dateToStr = sdf.format(writeDate);
                int commentNum = rs.getInt("댓글번호");
                
                CommentsVO vo = new CommentsVO();
                vo.setPfImg(pfImg);
                vo.setNickname(nickname);
                vo.setCommentContent(commentContent);
                vo.setWriteDateStr(dateToStr);
                vo.setCommentNum(commentNum);
                list.add(vo);
			}
			Common.close(rs);
			Common.close(stmt);
			Common.close(conn);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void addComments(int reqMemberNum, String reqCommentContent, int reqWriteNum) {
		String sql = "INSERT INTO COMMENTS(COMMENT_NUM, MEMBER_NUM, COMMENT_CONTENT, WRITE_NUM) VALUES(COMMENT_NUM.NEXTVAL, ?, ?, ?)";
		
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reqMemberNum);
			pstmt.setString(2, reqCommentContent);
			pstmt.setInt(3, reqWriteNum);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
        Common.close(pstmt);
        Common.close(conn);
	}
	
	public void deleteComments(int reqCommentNum) {
		String sql = "DELETE FROM COMMENTS WHERE COMMENT_NUM = ?";
		int cnt = 0;
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reqCommentNum);
			cnt = pstmt.executeUpdate();
			System.out.println(cnt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Common.close(pstmt);
		Common.close(conn);
	}

}
