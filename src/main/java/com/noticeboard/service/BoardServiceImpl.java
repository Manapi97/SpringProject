package com.noticeboard.service;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.stereotype.Service;

import com.noticeboard.mapper.BoardMapper;
import com.noticeboard.model.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardMapper mapper;
	
	@Autowired
	private BoardServiceImpl boardServiceImpl;
	
	private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);
	
	@Override
	public void noticeRegist(BoardVO board) {
		
		mapper.noticeRegist(board);
	}
	
	@Override
	public List<BoardVO> noticeList(BoardVO board) {
		return mapper.noticeList(board);
	}
	
	@Override
	public int noticeListCnt(BoardVO board) {
		return mapper.noticeListCnt(board);
	}
	
	@Override
	public List<HashMap<String, Object>> noticeView(BoardVO board) {
		return mapper.noticeView(board);
	}
	
	@Override
	public void noticeUpdate(BoardVO board) {
		
		mapper.noticeUpdate(board);
	}
	
	@Override
	public void noticeDelete(BoardVO board) {
		
		mapper.noticeDelete(board);
	}
	
	@Override
	public void noticeImgRegist(BoardVO board) {
		mapper.noticeImgRegist(board);
	}
	
	@Override
	public List<HashMap<String, Object>> noticeImgList(BoardVO board) {
		return mapper.noticeImgList(board);
	}
	
	@Override
	public int noticeImgListCnt(BoardVO board) {
		return mapper.noticeImgListCnt(board);
	}
	
	@Override
	public List<HashMap<String, Object>> noticeImgView(BoardVO board) {
		return mapper.noticeImgView(board);
	}
	
	@Override
	public void noticeImgDelete(BoardVO board) {
		mapper.noticeImgDelete(board);
	}
	
	@Override
	public void noticeImgUpdate(BoardVO board) {
		
		mapper.noticeImgUpdate(board);
	}
	
	@Override
	public void noticeExcelDownload(String fileName, HttpServletResponse response) {
		log.info("************* ????????? ?????? ???????????? ?????? **************");
		
		try {
			
			XSSFWorkbook objWorkBook = new XSSFWorkbook();
            XSSFSheet objSheet = null;
            XSSFRow objRow = null;
            XSSFCell objCell = null;       //??? ??????

            //?????? ??????
            XSSFFont font = objWorkBook.createFont();
            font.setFontHeightInPoints((short)9);
           // font.setBoldweight((short)font.BOLDWEIGHT_BOLD);
            font.setFontName("????????????");

            //?????? ???????????? ?????? ??????, ??????
            XSSFCellStyle styleHd = objWorkBook.createCellStyle();    //?????? ?????????
            styleHd.setFont(font);
//            styleHd.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//            styleHd.setVerticalAlignment (HSSFCellStyle.VERTICAL_CENTER);

            objSheet = objWorkBook.createSheet("????????? ??????");     //???????????? ??????

            // ************ ?????? ?????? ***********************
            // 1???(?????? ?????? ??????)
            objRow = objSheet.createRow(0);
            objRow.setHeight ((short) 0x150);
            
            objCell = objRow.createCell(0);
            objCell.setCellValue("??????");
            objCell.setCellStyle(styleHd);

            objCell = objRow.createCell(1);
            objCell.setCellValue("??????");
            objCell.setCellStyle(styleHd);
            
            objCell = objRow.createCell(1);
            objCell.setCellValue("??????");
            objCell.setCellStyle(styleHd);
            
            objCell = objRow.createCell(1);
            objCell.setCellValue("?????????");
            objCell.setCellStyle(styleHd);
            
            BoardVO board = new BoardVO();
          
            // ?????? ????????? ??????
            board.setStartDataCount(-1);
            
            List<BoardVO> noticeList = boardServiceImpl.noticeList(board);
            System.out.println(noticeList.get(0));
            System.out.println(noticeList.get(0).getTitle());
//            System.out.println(noticeList.get(0).get("regDt").toString());
            System.out.println(noticeList);
            // 2???
            objRow = objSheet.createRow(1);
            objRow.setHeight ((short) 0x150);

            objCell = objRow.createCell(0);
            objCell.setCellValue("1");
            objCell.setCellStyle(styleHd);

            objCell = objRow.createCell(1);
            objCell.setCellValue("?????????");
            objCell.setCellStyle(styleHd);

            // ************ ?????? ?????? ***********************

            response.setContentType("Application/Msexcel");
            response.setHeader("Content-Disposition", "ATTachment; Filename="+URLEncoder.encode(fileName,"UTF-8")+".xlsx");

            OutputStream fileOut  = response.getOutputStream();
            objWorkBook.write(fileOut);
            fileOut.close();

            response.getOutputStream().flush();
            response.getOutputStream().close();
			
			log.info("************* ????????? ?????? ???????????? ?????? **************");
		} catch (Exception e) {
			log.error("noticeExcelDownload Exception!!");
			e.printStackTrace();
			log.info("************* ????????? ?????? ???????????? ?????? **************");
		}
	}

}
