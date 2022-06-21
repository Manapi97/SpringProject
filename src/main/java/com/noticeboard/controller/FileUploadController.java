package com.noticeboard.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.noticeboard.service.FileUploadService;

import com.noticeboard.model.BoardVO;
import com.noticeboard.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class FileUploadController {
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private BoardService boardService;
	
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
//	public static final Path path = Paths.get(System.getProperty("user.home"), ".upload");
	// 이미지 저장 경로
	public static final Path path = Paths.get("C:\\Users\\36912\\Documents\\workspace-spring-tool-suite-4-4.14.1.RELEASE\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\NoticeboardExmple\\resources\\static\\img");
	
	
	/* 이미지 게시글 업로드 */
	@RequestMapping(value = "/uploadImgNotice", method = { RequestMethod.POST })
    public String uploadImgNotice(
    		@RequestParam("file") MultipartFile multipartFile,
    		@RequestParam("title") Object title,
    		@RequestParam("content") Object content
    		) {
		log.info("************* 이미지 게시글 업로드 시작 **************");
		// multipartFile.getOriginalFilename() : 파일명
		// targetFile : 이미지 저장할 파일 경로
		String originalFileName = multipartFile.getOriginalFilename();
		File targetFile = null;

		BoardVO boardVO = new BoardVO();
		boardVO.setTitle(title.toString());
		boardVO.setContent(content.toString());
		
		// 현재 시간
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    	
    	// 파일명에 시간 추가
    	String saveFileName = "";
    	String saveFileNameArray[] = null;
    	if(originalFileName.split("[.]").length>1) {
    		saveFileNameArray= originalFileName.split("[.]");
    		saveFileName = saveFileNameArray[0] + "_" + sdf.format(timestamp) + "."+saveFileNameArray[1];
    	}
    	
		if(saveFileName != null ) {
    		targetFile = new File(path.resolve(saveFileName).toString());
    		boardVO.setImgFileName(saveFileName);
    		boardVO.setImgFilePath(targetFile.toString());
    	}
		
        try {
        	if(targetFile != null) {
        		fileUploadService.uploadImgFile(multipartFile, targetFile);
        	}
        	
        	boardService.noticeImgRegist(boardVO);
        	
            
        } catch (Exception e) {
        	log.error("uploadImgNotice Exception!!");
        	e.printStackTrace();
        	if(targetFile != null) {
        		// 파일 삭제
        		FileUtils.deleteQuietly(targetFile);
        	}
        	log.info("************* 이미지 게시글 업로드 종료 **************");
    		return "redirect:/board/noticeImgList";
        }

        log.info("************* 이미지 게시글 업로드 종료 **************");
        return "redirect:/board/noticeImgList";
    }
	
	/* 이미지 게시글 수정 업로드 */
	@RequestMapping(value = "/uploadUpdateImgNotice", method = { RequestMethod.POST })
    public String uploadUpdateImgNotice(
    		@RequestParam("file") MultipartFile multipartFile,
    		@RequestParam("no") int no,
    		@RequestParam("title") String title,
    		@RequestParam("content") String content,
    		@RequestParam("originalFilePath") String originalFilePath
    		) {
		
		log.info("************* 이미지 게시글 수정 업로드 시작 **************");
		// 파일이 없는 경우 제목과 내용만 수정
		BoardVO boardVO = new BoardVO();
		boardVO.setTitle(title);
		boardVO.setContent(content);
		boardVO.setNo(no);
		
		// 현재 시간
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

		if(multipartFile.isEmpty()) {


			try {
				log.info("************* 이미지 게시글 수정 시작 **************");
				boardService.noticeImgUpdate(boardVO);
				log.info("************* 이미지 게시글 수정 종료 **************");
			} catch (Exception e) {
				log.error("noticeImgUpdate Exception!!");
	        	e.printStackTrace();
				log.info("************* 이미지 게시글 수정 종료 **************");
				return "redirect:/board/noticeImgList";
			}
		}
		else { // 파일이 있는 경우 기존 이미지 파일 삭제 후 이미지 파일 추가 및 제목과 내옹 수정
			
			try {
				// 기존 이미지 파일 삭제
				fileUploadService.FileDelete(originalFilePath);
				
				String originalFileName = multipartFile.getOriginalFilename();
				File targetFile = null;
		    	// 파일명에 시간 추가
		    	String saveFileName = "";
		    	String saveFileNameArray[] = null;
		    	if(originalFileName.split("[.]").length>1) {
		    		saveFileNameArray= originalFileName.split("[.]");
		    		saveFileName = saveFileNameArray[0] + "_" + sdf.format(timestamp) + "."+saveFileNameArray[1];
		    	}
		    	
				if(saveFileName != null ) {
		    		targetFile = new File(path.resolve(saveFileName).toString());
		    		boardVO.setImgFileName(saveFileName);
		    		boardVO.setImgFilePath(targetFile.toString());
		    	}
				
	        	if(targetFile != null) {
	        		fileUploadService.uploadImgFile(multipartFile, targetFile);
	        	}
	        	
	        	boardService.noticeImgUpdate(boardVO);
		
		        log.info("************* 이미지 게시글 수정 업로드 종료 **************");
		        return "redirect:/board/noticeImgList";
				
			} catch (Exception e) {
				log.error("uploadUpdateImgNotice Exception!!");
	        	e.printStackTrace();
				log.info("************* 이미지 게시글 수정 업로드 종료 **************");
				return "redirect:/board/noticeImgList";
			}
			
		}
        log.info("************* 이미지 게시글 수정 업로드 종료 **************");
        return "redirect:/board/noticeImgList";

    }
	
}
