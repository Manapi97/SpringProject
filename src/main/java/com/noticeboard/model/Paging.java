package com.noticeboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Paging {
	private int page = 1; // 현재 페이지(get)
	private int totalCount; // 전체 데이터 갯수(get)
	private int totalPagingCount; // 전체 페이징 갯수 
	private int beginPage; // 시작 페이징 번호
	private int endPage; // 끝 페이징 번호
	private int displayRow = 10; // 한 페이지에 몇개의 열(선택 set)
	private int displayPage = 10; // 한 페이지에 몇개의 페이지(선택 set)
	private int startDataCount; // 쿼리 시작 데이터 카운트
	private int endDataCount; // 쿼리 끝 데이터 카운트
	boolean prev; // prev 버튼 display 값
	boolean next; // next 버튼 display 값

	public void setTotalCount(int totalCount) {
		// setTotalCount()를 꼭 호출해야 paging이 되기 때문에        
		// paging()함수를 setTotalCount()를 호출했을 때 자동으로 호출되게 한다.
		this.totalCount = totalCount;
		paging();
	}
	
	public boolean isPrev() {
		return prev;
	}
	
	public boolean isNext() {
		return next;
	}
	
	private void paging() {
		// prev, next, beginPage, endPage를 계산해서 만든다.
		
		// 32/10 = 3.2 (올림) 4페이지
		totalPagingCount = (int)Math.ceil((double)totalCount/(double)displayRow);
		    
		// 마지막 페이지는 현재 페이지와 displayPage를 이용해서 구한다.
		endPage = (int)Math.ceil((double)page/(double)displayPage) * displayPage;
		
		beginPage = endPage - displayPage + 1;
				
		if(totalPagingCount < endPage) {
			endPage = totalPagingCount;
			next = false;
		}
		else {
			next = true;
		}
		
		// page가 1이 아닐때만 이전 버튼 나온다.
		prev = (beginPage == 1)?false:true; 
		
		endDataCount = page * displayPage;
		startDataCount = page * displayRow - displayRow ;
//		System.out.println("beginPage : "+ beginPage);
//		System.out.println("endPage : "+ endPage);
//		System.out.println("totalPagingCount : " + totalPagingCount);
		
		
	}
	
	
	
}
