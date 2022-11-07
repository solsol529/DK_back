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
import com.developerkirby.Main.VO.WriteVO;


public class NewComBoardDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public List<WriteVO> newComBoardSelelct() {
		List<WriteVO> list = new ArrayList<>();
		String sql = "SELECT * FROM (SELECT WRITE_NAME \"게시글명\", NICKNAME \"댓글작성자\", COMMENT_CONTENT \"댓글내용\", W.WRITE_NUM \"게시글번호\", W.WRITE_DATE \"작성일\",\r\n"
				+ "C.WRITE_DATE \"댓글 작성일\" \r\n"
				+ "    FROM COMMENTS C, MEMBER M, WRITE W\r\n"
				+ "    WHERE C.WRITE_NUM = W.WRITE_NUM AND C.MEMBER_NUM = M.MEMBER_NUM ORDER BY C.WRITE_DATE DESC)\r\n"
				+ "    WHERE ROWNUM <= 5";
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String writeName = rs.getString("게시글명");
                Date writeDate = rs.getDate("작성일");
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
                String dateToStr = sdf.format(writeDate);
                int writeNum = rs.getInt("게시글번호");
                
                WriteVO vo = new WriteVO();
                vo.setWriteName(writeName);
                vo.setWriteDateStr(dateToStr);
                vo.setWriteNum(writeNum);
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
}