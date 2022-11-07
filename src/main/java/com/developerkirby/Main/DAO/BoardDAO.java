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
import com.developerkirby.Main.VO.BoardVO;
import com.developerkirby.Main.VO.WriteVO;


public class BoardDAO {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public List<BoardVO> boardSelect() {
	    List<BoardVO> list = new ArrayList<>();
	    
	    try {
	        conn = Common.getConnection();
	        String sql = "SELECT BOARD_NAME FROM BOARD";
	        pstmt = conn.prepareStatement(sql);
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()) {
            String boardName = rs.getString("BOARD_NAME");
	        List<WriteVO> writeList = new ArrayList<>();
            PreparedStatement pstmt1 = null;
            ResultSet rs1 = null;
            String sql1 = "SELECT * FROM (SELECT (CASE WHEN LENGTH(WRITE_NAME) <= 18 THEN WRITE_NAME \r\n"
            		+ "ELSE CONCAT(SUBSTR(WRITE_NAME,1,16),'...') END) \"글제목\", NICKNAME \"작성자\", \r\n"
            		+ "(CASE WHEN (SYSDATE - WRITE_DATE) >= 1 THEN CONCAT(TRUNC(SYSDATE - WRITE_DATE), '일 전')\r\n"
            		+ "WHEN (SYSDATE - WRITE_DATE) >= 1/24 THEN CONCAT(TRUNC((SYSDATE - WRITE_DATE)*24), '시간 전')\r\n"
            		+ "WHEN (SYSDATE - WRITE_DATE) >= 1/24/60 THEN CONCAT(TRUNC((SYSDATE - WRITE_DATE)*60*24), '분 전') \r\n"
            		+ "ELSE '방금 전' END )\"작성일\", \r\n"
            		+ "WRITE_NUM \"게시글번호\" FROM WRITE W, MEMBER M\r\n"
            		+ "WHERE M.MEMBER_NUM = W.MEMBER_NUM AND BOARD_NAME = ? ORDER BY WRITE_DATE DESC, \"글제목\") WHERE ROWNUM <=5";
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setString(1, boardName);
            rs1 = pstmt1.executeQuery();
            System.out.print(boardName);
            
	        while(rs1.next()) {
                WriteVO vo = new WriteVO();
                String writeName = rs1.getString("글제목");
                String writeDateStr = rs1.getString("작성일");
//                SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
//                String dateToStr = sdf.format(writeDate);
                int writeNum = rs1.getInt("게시글번호");
                vo.setWriteName(writeName);
                vo.setWriteDateStr(writeDateStr);
                vo.setWriteNum(writeNum);
                writeList.add(vo);
                System.out.print(writeName);
	             }

            BoardVO vo = new BoardVO();
            vo.setBoardName(boardName);
            vo.setWrites(writeList);
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
	
	public List<BoardVO> boardNameSelect(String reqBoardName){
		List<BoardVO> list = new ArrayList<>();
		String sql = "SELECT BOARD_NAME FROM BOARD WHERE BOARD_NAME = ?";
		
		try {
			conn = Common.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, reqBoardName);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String boardName = rs.getString("BOARD_NAME");
				
				BoardVO vo = new BoardVO();
				vo.setBoardName(boardName);
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
	
