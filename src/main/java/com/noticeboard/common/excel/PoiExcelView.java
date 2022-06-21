package com.noticeboard.common.excel;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import lombok.extern.slf4j.Slf4j;

/***************************************************
 * <ul>
 * <li>클래스 설 명 : 공통 엑셀 다운로드</li>
 * <li>작   성  일 : 2022. 06. 20.</li>
 * </ul>
 * 
 * <pre>
 * ======================================
 * 변경자/변경일 :
 * 변경사유/내역 :
 * ======================================
 * </pre>
 ***************************************************/
@Slf4j
public class PoiExcelView extends AbstractXlsxView {

	public static final String MSIEBROWSER = "MSIE";
	public static final String TRIDENTBROWSER = "Trident";
	public static final String FIREFOXBROWSER = "Firefox";
	public static final String OPERABROWSER = "Opera";
	public static final String CHROMEBROWSER = "Chrome";
	public static final String ENCODING = "UTF-8";

	// 엑셀 다운로드 파일명
	String fileName = "";

	public PoiExcelView(String fileName) {
		this.fileName = fileName;
	}

	/*****************************************************
	 * 브라우저 구분 얻기
	 * 
	 * @param request Request 객체
	 * @return String 브라우저명
	 *****************************************************/
	private String getBrowser(HttpServletRequest request) {
		// 브라우저 헤더 정보
		String header = request.getHeader("User-Agent");

		if (header.indexOf(MSIEBROWSER) > -1) {
			return MSIEBROWSER;
		} else if (header.indexOf(TRIDENTBROWSER) > -1) { // IE11 문자열 깨짐 방지
			return TRIDENTBROWSER;
		} else if (header.indexOf(OPERABROWSER) > -1) {
			return OPERABROWSER;
		} else if (header.indexOf(CHROMEBROWSER) > -1) {
			return CHROMEBROWSER;
		}
		return FIREFOXBROWSER;
	}

	/*****************************************************
	 * Disposition 지정
	 * 
	 * @param filename 엑셀 다운로드 파일명
	 * @param request  Request 객체
	 * @param response Response 객체
	 *****************************************************/
	private void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) {
		// 브라우저명
		String browser = getBrowser(request);

		// Disposition 지정
		String dispositionPrefix = "attachment; filename=";
		// 인코딩 파일명
		String encodedFilename = null;

		try {

			if (browser.equals(MSIEBROWSER)) {
				encodedFilename = URLEncoder.encode(filename, ENCODING).replaceAll("\\+", "%20");
			} else if (browser.equals(TRIDENTBROWSER)) { // IE11 문자열 깨짐 방지
				encodedFilename = URLEncoder.encode(filename, ENCODING).replaceAll("\\+", "%20");
			} else if (browser.equals(FIREFOXBROWSER)) {
				encodedFilename = "\"" + filename.getBytes(StandardCharsets.UTF_8) + "\"";
			} else if (browser.equals(OPERABROWSER)) {
				encodedFilename = "\"" + filename.getBytes(StandardCharsets.UTF_8) + "\"";
			} else if (browser.equals(CHROMEBROWSER)) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < filename.length(); i++) {
					char c = filename.charAt(i);
					if (c > '~') {
						sb.append(URLEncoder.encode("" + c, ENCODING));
					} else {
						sb.append(c);
					}
				}
				encodedFilename = sb.toString();
			} else {
				throw new IOException("Not supported browser");
			}

		} 
		catch (NullPointerException e) {
			System.out.println("NullPointer Occured");
			
		}
		catch (BadSqlGrammarException e) {
			System.out.println("BadSqlGrammar Occured");
			
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);
		if (OPERABROWSER.equals(browser)) {
			response.setContentType("application/octet-stream;charset=UTF-8");
		}
	}

	/*****************************************************
	 * 다운로드 엑셀 문서 생성
	 * 
	 * @param model    엑셀 생성 데이타
	 * @param workbook Workbook 객체
	 * @param request  Request 객체
	 * @param response Response 객체
	 *****************************************************/
	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 엑셀 문서 타입
		String mimetype = "application/x-msdownload";

		response.setContentType(mimetype);
		setDisposition(fileName, request, response);

		// Sheet 객체 생성
		Sheet sheet = workbook.createSheet();

		// 엑셀 헤더 목록
		List<String> titleList = (List<String>) model.get("titleList");
		// 엑셀 필드 목록
		List<String> compareList = (List<String>) model.get("fieldList");
		// 엑셀 데이타 목록
		List<Object> contentOrgList = (List<Object>) model.get("contentList");
		List<Map<String, Object>> contentList = new ArrayList<>();
		for (Object obj : contentOrgList) {
			try {
				
				Field[] fields = obj.getClass().getDeclaredFields();
				
				Field[] fields2 = obj.getClass().getSuperclass().getDeclaredFields();
				Map <String, Object>resultMap = new HashMap<>();
				for(int i=0; i<=fields.length-1;i++){
					fields[i].setAccessible(true);
					resultMap.put(fields[i].getName(), fields[i].get(obj));
				}
				for(int i=0; i<=fields2.length-1;i++){
					fields2[i].setAccessible(true);
					resultMap.put(fields2[i].getName(), fields2[i].get(obj));
				}
				contentList.add(resultMap);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				log.error(e.getMessage());
			}
			
		}

		// 헤더 인덱스
		int titleIndex = 0;

		// 엑셀 헤더 생성 및 값 설정
		for (String title : titleList) {
			setText(getCell(sheet, 0, titleIndex++), String.valueOf(title));
		}

		// 컨텐츠 인덱스
		int contentIndex = 0;
		// 데이타 키 인덱스
		int keyIndex = 0;
		// Cell 객체
		Cell cell = null;
		// 필드 키
		String key = "";
		// 필드 값
		Object value = null;

		Font defaultFont = workbook.createFont();
		defaultFont.setFontName("굴림");
		defaultFont.setFontHeightInPoints((short) 8);

		DataFormat numberFormat = workbook.createDataFormat();
		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setAlignment(HorizontalAlignment.LEFT);
		numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		numberStyle.setDataFormat(numberFormat.getFormat("#,##0"));
		numberStyle.setFont(defaultFont);

		CellStyle leftStyle = workbook.createCellStyle();
		leftStyle.setAlignment(HorizontalAlignment.LEFT);
		leftStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		leftStyle.setFont(defaultFont);

		CellStyle defaultStyle = workbook.createCellStyle();
		defaultStyle.setAlignment(HorizontalAlignment.CENTER);
		defaultStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		defaultStyle.setFont(defaultFont);

		// 숫자형 제외 컬럼명 목록
		List<String> notNumberList = Arrays.asList("MEMBER_ID", "mbshNo", "wkrId", "hscd");
		// 레프트 정렬 컬럼명 목록
		List<String> leftStringList = Arrays.asList("MEMBER_ID");

		// 콤마 제거 대상 key의 단어 목록
		List<String> commaRemoveWordList = Arrays.asList("AMT", "MONY");

		for (Map<String, Object> map : contentList) {
			// 필드 인덱스
			int fieldIndex = 0;

			// 한 시트에 데이타 6만건 초과시 시트 추가
			if (contentIndex != 0 && contentIndex % 60000 == 0) {

				// 헤더 row 스타일 지정
				this.setCellStyle(sheet, workbook);

				for (int i = 0; i < titleList.size(); i++) {
					sheet.autoSizeColumn(i);
					sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 512);
				}

				sheet = workbook.createSheet();

				titleIndex = 0;
				for (String title : titleList) {
					setText(getCell(sheet, 0, titleIndex++), String.valueOf(title));
				}

				contentIndex = 0;
			}

			// 엑셀 데이타 그리기 (스타일 포함)
			for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
				key = iterator.next();
				value = map.get(key) == null ? "" : String.valueOf(map.get(key));
				for (String word : commaRemoveWordList) {
					if (StringUtils.upperCase(key).indexOf(word) > 0) {
						value = ((String) value).replaceAll(",", "");
					}
				}

				if (compareList != null) {
					if (compareList.contains(key)) {
						keyIndex = compareList.indexOf(key);
					} else {
						continue;
					}
				} else {
					keyIndex = fieldIndex++;
				}

				// 데이타 row 생성 및 값 셋팅
				cell = getCell(sheet, 1 + contentIndex, keyIndex);
				
				cell.setCellValue((String) value);
				// 레프트 정렬 스타일 별도
				if (leftStringList.contains(key) && StringUtils.isNotEmpty((String) value)) {
					cell.setCellStyle(leftStyle);
				} else {
					cell.setCellStyle(defaultStyle);
				}

				// 숫자형 제외 컬럼이 아니고 순수 숫자인 경우
				/*
				//if (!notNumberList.contains(key) && StringUtils.isNotEmpty((String) value)
				//		&& NumberUtils.isNumber((String) value)) {
				//	cell.setCellValue(Double.parseDouble((String) value));
				//	cell.setCellStyle(numberStyle);
				//} else {
				//	cell.setCellValue((String) value);
				//	// 레프트 정렬 스타일 별도
				//	if (leftStringList.contains(key) && StringUtils.isNotEmpty((String) value)) {
				//		cell.setCellStyle(leftStyle);
				//	} else {
				//		cell.setCellStyle(defaultStyle);
				//	}
				//}
				*/
			}
			contentIndex++;
		}

		// 헤더 row 스타일 지정
		this.setCellStyle(sheet, workbook);
		// 셀크기 자동 조정 테스트
		for (int i = 0; i < titleList.size(); i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, Math.min(255 * 256, sheet.getColumnWidth(i) + 512));
		}
		
	}

	/*****************************************************
	 * 엑셀 행 생성 및 cell 정보 호출
	 * 
	 * @param sheet    sheet 객체
	 * @param row      행 번호
	 * @param keyIndex 키 인덱스
	 * @return Cell Cell 객체
	 *****************************************************/
	private Cell getCell(Sheet sheet, int row, int keyIndex) {
		Row sheetRow = sheet.getRow(row);
		if (sheetRow == null) {
			sheetRow = sheet.createRow(row);
		}
		Cell cell = sheetRow.getCell(keyIndex);
		if (cell == null) {
			cell = sheetRow.createCell(keyIndex);
		}
		return cell;
	}

	/*****************************************************
	 * 엑셀 cell 값 셋팅
	 * 
	 * @param cell Cell 객체
	 * @param text 설정 값
	 *****************************************************/
	private void setText(Cell cell, String text) {
		cell.setCellValue(text);
	}

	/*****************************************************
	 * 엑셀 cell style 값 셋팅
	 * 
	 * @param cell Cell 객체
	 * @param text 설정 값
	 *****************************************************/
	private void setCellStyle(Sheet sheet, Workbook workbook) {

		// 헤더 row 스타일 지정
		if (sheet.getRow(0) != null) {
			CellStyle style = workbook.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Font font = workbook.createFont();
			font.setFontName("굴림");
			font.setFontHeightInPoints((short) 9);
			font.setBold(true);
			style.setFont(font);

			Iterator<org.apache.poi.ss.usermodel.Cell> cellIter = sheet.getRow(0).cellIterator();
			while (cellIter.hasNext()) {
				cellIter.next().setCellStyle(style);
			}	
		}

	}
}
