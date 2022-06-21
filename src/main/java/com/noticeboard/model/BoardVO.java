package com.noticeboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoardVO extends Paging{
    
	// 일반 게시판
	/* 게시판 번호 */
	private int no;
    /* 게시판 제목 */
    private String title;
    /* 게시판 내용 */
    private String content;
    /* 등록 날짜 */
    private String regDt;
    /* 수정 날짜 */
    private String upStringDt;
    
    // 이미지 게시판
    /* 이미지파일명 */
    private String imgFileName;
    /* 이미지파일경로 */
    private String imgFilePath;


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public String getUpStringDt() {
		return upStringDt;
	}

	public void setUpStringDt(String upStringDt) {
		this.upStringDt = upStringDt;
	}
}
