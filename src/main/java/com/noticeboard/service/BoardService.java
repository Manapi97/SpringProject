package com.noticeboard.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.noticeboard.model.BoardVO;

public interface BoardService {
	
	/**
	 * 게시글 등록
	 * @param BoardVO
	 */
	public void noticeRegist(BoardVO board);
	
	/**
	 * 게시판 목록 조회
	 * @param BoardVO
	 * @return List<BoardVO>
	 */
	public List<BoardVO> noticeList(BoardVO board);
	
	
	/**
	 * 게시판 전체 데이터 갯수 조회
	 * @param BoardVO
	 * @return int
	 */
	public int noticeListCnt(BoardVO board);
	
	/**
	 * 게시글 상세 조회
	 * @param BoardVO
	 * @return List<HashMap<String, Object>>
	 */
	public List<HashMap<String, Object>> noticeView(BoardVO board);
	
	/**
	 * 게시글 수정
	 * @param BoardVO
	 */
	public void noticeUpdate(BoardVO board);
	
	/**
	 * 게시글 삭제
	 * @param BoardVO
	 */
	public void noticeDelete(BoardVO board);
	
	/**
	 * 게시글 엑셀 다운로드
	 * @param BoardVO
	 */
	public void noticeExcelDownload(String fileName, HttpServletResponse response);
	
	/**
	 * 이미지 게시글 등록
	 * @param BoardVO
	 */
	public void noticeImgRegist(BoardVO board);
	
	/**
	 * 이미지 게시판 목록 조회
	 * @param BoardVO
	 * @return List<HashMap<String, Object>>
	 */
	public List<HashMap<String, Object>> noticeImgList(BoardVO board);
	
	/**
	 * 이미지 게시판 전체 데이터 갯수 조회
	 * @param BoardVO
	 * @return int
	 */
	public int noticeImgListCnt(BoardVO board);
	
	/**
	 * 이미지 게시글 상세 조회
	 * @param BoardVO
	 * @return List<HashMap<String, Object>>
	 */
	public List<HashMap<String, Object>> noticeImgView(BoardVO board);
	
	/**
	 * 이미지 게시글 삭제
	 * @param BoardVO
	 */
	public void noticeImgDelete(BoardVO board);
	
	/**
	 * 이미지 게시글 수정
	 * @param BoardVO
	 */
	public void noticeImgUpdate(BoardVO board);
	

	

}
