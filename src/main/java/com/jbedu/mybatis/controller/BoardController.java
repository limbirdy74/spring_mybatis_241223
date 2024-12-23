package com.jbedu.mybatis.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jbedu.mybatis.dao.BoardDao;
import com.jbedu.mybatis.dto.BoardDto;

@Controller
public class BoardController {
	
	@Autowired
	private SqlSession sqlSession; // 의존성 자동 주입 -> DI.
	
	@RequestMapping(value = "/write_form")
	public String write_form(HttpServletRequest request, Model model) {
		return "write_form";
	}

	@RequestMapping(value = "/writeOk")
	public String boardWrite(HttpServletRequest request, Model model) {
		
		String bname = request.getParameter("bname");
		String btitle = request.getParameter("btitle");
		String bcontent = request.getParameter("bcontent");
		
		BoardDao boardDao = sqlSession.getMapper(BoardDao.class);
		int insertFlag = boardDao.boardWriteDao(bname, btitle, bcontent);
		
//		System.out.println(insertFlag);
		
		if (insertFlag == 1) {  // 글 작성 성공시. 팝업창
			
			model.addAttribute("msg", "글 입력이 되었습니다.");
			model.addAttribute("url", "boardList");
			return "alert";  
		} 
		
		return "redirect:boardList";
	}
	
	@RequestMapping(value = "/boardList")
	public String boardList(HttpServletRequest request, Model model) {
		
		BoardDao boardDao = sqlSession.getMapper(BoardDao.class);
		ArrayList<BoardDto> boardDtos = boardDao.boardListDao();
		
		model.addAttribute("bDtos", boardDtos);
		
		return "boardList";
	}
	
	@RequestMapping(value = "/delete_form")
	public String delete_form(HttpServletRequest request, Model model) {
		return "delete_form";
	}
	
	@RequestMapping(value = "/deleteOk")
	public String boardDelete(HttpServletRequest request, Model model) {
		
		String bnum = request.getParameter("bnum");
		
		BoardDao boardDao = sqlSession.getMapper(BoardDao.class);
		int deleteFlag = boardDao.boardDeleteDao(bnum); 
		
//		System.out.println(deleteFlag);
		
		if (deleteFlag == 0) {  // 존재하지 않는 글번호 삭제 실패
			
			model.addAttribute("msg", "이미 삭제된 글번호 입니다");
			model.addAttribute("url", "boardList");
			return "alert";  
		} 
		
		return "redirect:boardList";
	}
	
	@RequestMapping(value = "/alert")
	public String alert() {
		return "alert";
	}
	
	@RequestMapping(value = "/contentView")  // 글내용보기
	public String contentView(HttpServletRequest request, Model model) {
		
		String bnum = request.getParameter("bnum");  //내용을 출력할 글 번호(유저가 클릭한 글번호)
		
		BoardDao boardDao = sqlSession.getMapper(BoardDao.class);
		BoardDto boardDto = boardDao.contentViewDao(bnum); 
		
		model.addAttribute("bDto", boardDto);
		
		return "contentView";
	}	
}
