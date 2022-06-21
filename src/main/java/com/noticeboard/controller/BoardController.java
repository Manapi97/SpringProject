package com.noticeboard.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.noticeboard.model.BoardVO;
import com.noticeboard.service.BoardService;
import com.noticeboard.service.FileUploadService;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@GetMapping("/noticeList")
	public void boardList() {
		log.info("게시판 목록 페이지 진입");
	}
	
	@GetMapping("/noticeRegist")
	public void boardRegist() {
		log.info("게시판 등록 페이지 진입");
	}
	
	@GetMapping("/noticeImgList")
	public void boardImgList() {
		log.info("이미지 게시판 목록 페이지 진입");
	}
	
	@GetMapping("/noticeImgView")
	public void boardImgView() {
		log.info("이미지 상세 페이지 진입");
	}
	
	@GetMapping("/noticeImgWrite")
	public void boardImgWrite() {
		log.info("이미지 게시판 글쓰기 페이지 진입");
	}
	
	@GetMapping("/noticeImgUpdate")
	public void boardImgUpdate() {
		log.info("이미지 게시판 수정 페이지 진입");
	}
	
    /* 게시글 등록 */
    @PostMapping("/noticeRegist")
    public String noticeRegist(BoardVO board) {
    	log.info("************* 게시판 등록 시작 **************");
        log.info("BoardVO : " + board);
        try {
        	boardService.noticeRegist(board); 
		} catch (Exception e) {
			// TODO: handle exception
			log.error("noticeRegist Exception!!");
			e.printStackTrace();
			log.info("************* 게시판 등록 종료 **************");
	        return "redirect:/board/noticeRegist";
		}
        
        log.info("************* 게시판 등록 종료 **************");
        return "redirect:/board/noticeRegist";
    }
    
    /* 게시판 목록 조회*/
    @PostMapping("/noticeList")
    public @ResponseBody Object noticeList(BoardVO board) {
    	log.info("************* 게시판 목록 조회 시작 **************");
    	log.info("BoardVO : " + board);
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
        
        try {
        	int noticeListCnt = 0;
        	
        	// 한페이지에 10개씩 보여주기
        	board.setDisplayRow(10);
        	
        	noticeListCnt = boardService.noticeListCnt(board);
        	board.setTotalCount(noticeListCnt);
        	
        	List<BoardVO> noticeList = boardService.noticeList(board);
        	result.put("noticeList", noticeList);
        	result.put("beginPage", board.getBeginPage());
        	result.put("endPage", board.getEndPage());
        	result.put("page", board.getPage());
        	result.put("totalPagingCount", board.getTotalPagingCount());
        	result.put("code", 200);
		} catch (Exception e) {
			log.error("noticeList Exception!!");
			result.put("code", 401);
			e.printStackTrace();
			log.info("************* 게시판 목록 조회 종료 **************");
			return result;
			
		}
        log.info("************* 게시판 목록 조회 종료 **************");
        return result;
        
    }
    
    /* 게시글 상세 조회*/
    @PostMapping("/noticeView")
    public @ResponseBody Object noticeView(BoardVO board) {
    	log.info("************* 게시글 상세 조회 시작 **************");
    	Map<String, Object> result = new HashMap<String, Object>();
    	
        log.info("BoardVO : " + board);
        try {
        	
        	List<HashMap<String, Object>> noticeView = boardService.noticeView(board);
        	result.put("noticeView", noticeView);
        	result.put("code", 200);
		} catch (Exception e) {
			log.error("noticeList Exception!!");
			e.printStackTrace();
			result.put("code", 401);
			
			log.info("************* 게시글 상세 조회 종료 **************");
			return result;
			
		}
        log.info("************* 게시글 상세 조회 종료 **************");
        return result;
        
    }
    
    /* 게시글 수정 */
    @PostMapping("/noticeUpdate")
    public String noticeUpdate(BoardVO board) {
    	log.info("************* 게시글 수정 시작 **************");
        log.info("BoardVO : " + board);
        try {
        	boardService.noticeUpdate(board);
        	
		} catch (Exception e) {
			log.error("noticeUpdate Exception!!");
			e.printStackTrace();
			log.info("************* 게시글 수정 종료 **************");
			return "redirect:/board/noticeRegist";
		}
        
        log.info("************* 게시글 수정 종료 **************");
        return "redirect:/board/noticeRegist";
        
    }
    
    /* 게시글 삭제 */
    @PostMapping("/noticeDelete")
    public String noticeDelete(BoardVO board) {
    	log.info("************* 게시글 삭제 시작 **************");
        log.info("BoardVO : " + board);
        
        try {
        	boardService.noticeDelete(board);
		} catch (Exception e) {
			log.error("noticeDelete Exception!!");
			e.printStackTrace();
			log.info("************* 게시글 삭제 종료 **************");
			return "redirect:/board/noticeRegist";
		}
        log.info("************* 게시글 삭제 종료 **************");
        return "redirect:/board/noticeRegist";
        
    }
    
    /* 게시글 엑셀 다운로드 */
    @ResponseBody
	@RequestMapping(value = "/noticeExcelDownload", method = { RequestMethod.POST })
    public void noticeExcelDownload(@RequestParam String fileName, HttpServletResponse response) {
    	log.info("************* 게시글 엑셀 다운로드 시작 **************");
        
        try {
        	 boardService.noticeExcelDownload(fileName, response);
		} catch (Exception e) {
			log.error("noticeExcelDownload Exception!!");
			e.printStackTrace();
			log.info("************* 게시글 엑셀 다운로드 종료 **************");
			
		}
        log.info("************* 게시글 엑셀 다운로드 종료 **************");
        
    }
    
    /* 이미지 게시판 목록 조회*/
    @PostMapping("/noticeImgList")
    public @ResponseBody Object noticeImgList(BoardVO board) {
    	log.info("************* 이미지 게시판 목록 조회 시작 **************");
    	log.info("BoardVO : " + board);
    	
    	Map<String, Object> result = new HashMap<String, Object>();
    	
        
        try {
        	int noticeImgListCnt = 0;
        	
        	// 한페이지에 8개씩 보여주기
        	board.setDisplayRow(8);
        	
        	noticeImgListCnt = boardService.noticeImgListCnt(board);
        	board.setTotalCount(noticeImgListCnt);
        	
        	List<HashMap<String, Object>> noticeImgList = boardService.noticeImgList(board);
        	result.put("noticeImgList", noticeImgList);
        	result.put("beginPage", board.getBeginPage());
        	result.put("endPage", board.getEndPage());
        	result.put("page", board.getPage());
        	result.put("totalPagingCount", board.getTotalPagingCount());
        	result.put("code", 200);
		} catch (Exception e) {
			log.error("noticeList Exception!!");
			result.put("code", 401);
			e.printStackTrace();
			log.info("************* 이미지 게시판 목록 조회 종료 **************");
			return result;
			
		}
        log.info("************* 이미지 게시판 목록 조회 종료 **************");
        return result;
        
    }
    
    /* 이미지 게시글 상세 조회*/
    @PostMapping("/noticeImgView")
    public @ResponseBody Object noticeImgView(BoardVO board) {
    	log.info("************* 이미지 게시글 상세 조회 시작 **************");
    	Map<String, Object> result = new HashMap<String, Object>();
    	
        log.info("BoardVO : " + board);
        try {
        	
        	List<HashMap<String, Object>> noticeImgView = boardService.noticeImgView(board);
        	result.put("noticeImgView", noticeImgView);
        	result.put("code", 200);
		} catch (Exception e) {
			log.error("noticeImgView Exception!!");
			e.printStackTrace();
			result.put("code", 401);
			
			log.info("************* 이미지 게시글 상세 조회 종료 **************");
			return result;
			
		}
        log.info("************* 이미지 게시글 상세 조회 종료 **************");
        return result;
    }
    
    /* 이미지 게시글 삭제 */
    @PostMapping("/noticeImgDelete")
    public String noticeImgDelete(BoardVO board) {
    	log.info("************* 이미지 게시글 삭제 시작 **************");
        log.info("BoardVO : " + board);
        
        try {
        	// DB 삭제
        	boardService.noticeImgDelete(board);
        	
        	// 이미지 파일 삭제
        	fileUploadService.FileDelete(board.getImgFilePath());
        	
		} catch (Exception e) {
			log.error("noticeImgDelete Exception!!");
			e.printStackTrace();
			log.info("************* 이미지 게시글 삭제 종료 **************");
			return "redirect:/board/noticeImgList";
		}
        log.info("************* 이미지 게시글 삭제 종료 **************");
        return "redirect:/board/noticeImgList";
        
    }
    
}
