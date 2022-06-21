package com.noticeboard.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
	
	/**
	 * 이미지 파일 업로드
	 * @param MultipartFile, File
	 */
	public void uploadImgFile(MultipartFile multipartFile, File targetFile);
	
	/**
	 * 파일 삭제
	 * @param File
	 */
	public void FileDelete(File targetFile);
	
	/**
	 * 파일 삭제
	 * @param String
	 */
	public void FileDelete(String filePath);
	
}
