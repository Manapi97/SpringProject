package com.noticeboard.service;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.noticeboard.mapper.BoardMapper;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	
	@Autowired
	private BoardMapper mapper;
	
	private static final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);
	
	@Override
	/**
	 * 이미지 파일 업로드
	 * @param MultipartFile, File
	 */
	public void uploadImgFile(MultipartFile multipartFile, File targetFile) {
		
		try {
	        InputStream fileStream = multipartFile.getInputStream();
	        
	        // 파일 복사
	        if(targetFile != null) {
	        	FileUtils.copyInputStreamToFile(fileStream, targetFile);
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 파일 삭제
	 * @param File
	 */
	public void FileDelete(File targetFile) {
		log.info("************* 파일 삭제 시작 **************");
		
		try {
			FileUtils.deleteQuietly(targetFile);
			log.info("************* 파일 삭제 종료**************");
		} catch (Exception e) {
			log.error("FileDelete Exception!!");
			e.printStackTrace();
			log.info("************* 파일 삭제 종료 **************");
		}
	}
	
	/**
	 * 파일 삭제
	 * @param String
	 */
	public void FileDelete(String filePath) {
		log.info("************* 파일 삭제 시작 **************");
		
		try {
			File targetFile = new File(filePath);
			FileUtils.deleteQuietly(targetFile);
			log.info("************* 파일 삭제 종료**************");
		} catch (Exception e) {
			log.error("FileDelete Exception!!");
			e.printStackTrace();
			log.info("************* 파일 삭제 종료 **************");
		}
	}
	


}
