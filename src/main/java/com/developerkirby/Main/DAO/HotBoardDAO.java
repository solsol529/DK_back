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


public class HotBoardDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public List<WriteVO> hotBoardSelect() {
		List<WriteVO> list = new ArrayList<>();
		String sql = "SELECT * FROM (SELECT COUNT(*) \"좋아요수\", W.WRITE_NAME \"게시글 제목\", W.WRITE_NUM \"게시글번호\", W.WRITE_DATE \"작성일\"\r\n"
				+ "FROM WRITE W, (SELECT DISTINCT * FROM GOOD) G \r\n"
				+ "WHERE G.WRITE_NUM = W.WRITE_NUM \r\n"
				+ "GROUP BY W.WRITE_NAME, W.WRITE_NUM, W.WRITE_DATE ORDER BY \"좋아요수\" DESC) where \"작성일\" BETWEEN sysdate-7 AND sysdate+7 and ROWNUM <= 5";
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String writeName = rs.getString("게시글 제목");
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
