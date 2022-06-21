<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>게시판 사이트</title>
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script> -->
<link rel="stylesheet" type="text/css" href="/resources/static/css/noticeList.css">
<script type="text/javascript" src="/resources/static/lib/function.js"></script>
<script type="text/javascript" src="/resources/static/lib/jquery-3.0.0.min.js"></script>
<script>
  var page = 1;
  var beginPage = 0;
  var endPage = 0;
  var totalPagingCount = 0;
  var excelBoardList = new Array();
  // 게시판 목록 조회
  function noticeList(pageParam){
    $.ajax({
      url: "/board/noticeList",
      type: "post",
      data: {
          page : pageParam,
      },
      error : function(request, status, error){
          alert('목록 조회 실패');
          console.dir(request);
          console.dir(status);
          console.dir(error);
      },
      success : function(result){
        console.dir("result");
        console.dir(result);

        var noticeList = result.noticeList;
        excelBoardList = noticeList;
        var boardList = document.getElementById("boardList");
        page = result.page;
        beginPage = result.beginPage;
        endPage = result.endPage;
        totalPagingCount = result.totalPagingCount;

        if(!isEmpty(noticeList)){
          boardList.innerText = '';
          for (let i = 0; i < noticeList.length; i++) {
            var contentString = '';
            contentString += "<tr>";
            contentString += "<td>"+noticeList[i].no+"</td>";
            contentString += "<th class='titleButton' id='titleNumber"+noticeList[i].no+"'>";
            contentString += "<a href='/board/noticeRegist?id="+noticeList[i].no+"'>"+noticeList[i].title+"</a></th>";  
            contentString += "<td>"+noticeList[i].regDt.split(' ')[0]+"</td>";
            contentString += "</tr>";
            boardList.innerHTML += contentString;
          }
          var pageHTML = "";
          pageHTML += "<li><a href='#' id='prev'> 이전 </a></li>";

          for (var i = result.beginPage; i <= result.endPage; i++) {
            if (result.page == i) {
              pageHTML +="<li class='on'><a href='#' id='pagingNumber" + i + "'>" + i + "</a></li>";
            } else {
              pageHTML += "<li class='otherButton'><a href='#' id='pagingNumber" + i + "'>" + i + "</a></li>";
            }
          }

          pageHTML += "<li><a href='#' id='next'> 다음 </a></li>";

          if(!isEmpty(pageHTML)){
            $("#paging").html(pageHTML);
          }
          
        }
        else {
          alert("목록 조회 실패");
        }

      }
    });
  }


  $(function(){

    noticeList(page);

    $(document).on("click", "#prev", function() {
      console.dir("이전버튼 클릭");
      if(page == 1){
        return null;
      }
      noticeList(page-1);
    });

    $(document).on("click", "#next", function() {
      console.dir("다음 버튼 클릭");
      if(totalPagingCount == 0 || page == totalPagingCount){
        return null;
      }
      noticeList(page+1);
    });

    $(document).on("click", ".otherButton", function(e) {
      console.dir("페이징 번호 클릭");
      if(isEmpty(e.target.innerText)){
        alert("페이지 이동 오류");
        return null;
      }

      var pageNumber = parseInt(e.target.innerText);
      if(typeof pageNumber != "number"){
        alert("페이지 이동 오류");
      }
      else {
        noticeList(pageNumber);
      }
      
    });

    $(document).on("click", ".titleButton", function(e) {
      console.dir("게시판 제목 클릭");
      console.dir(e);
      
      if(isEmpty(e.currentTarget.id.split("titleNumber"))){
        alert("게시판 본문 이동 오류");
        return null;
      }

      var idNumber = "";
      idNumber = e.currentTarget.id.split("titleNumber")[1];


      
    });

  });

  function excelDownloadAll(){
    if(!excelBoardList.length > 0){
      alert("다운로드할 데이터가 없습니다.");
      return;
    }
    // $.ajax({
    //   url: "/board/noticeExcelDownload",
    //   type: "post",
    //   data : {
    //     fileName : "allData",
    //   },
    //   error : function(request, status, error){
    //       alert('엑셀 다운로드 실패');
    //       console.dir(request);
    //       console.dir(status);
    //       console.dir(error);
    //   },
    //   success : function(result){
    //     console.dir("엑셀 결과");
    //     console.dir(result);
    //   }
    // });

  }
</script>
</head>
<body>
	<section class="notice">
		<div class="page-title">
			  <div class="container">
				  <h3>게시판</h3>
			  </div>
		  </div>
	  
		<!-- board list area -->
		  <div id="board-list">
			  <div class="container">
				  <table class="board-table">
					  <thead>
					  <tr>
						  <th scope="col" class="th-num">번호</th>
						  <th scope="col" class="th-title">제목</th>
						  <th scope="col" class="th-date">등록일</th>
					  </tr>
					  </thead>
					  <tbody id="boardList">
					  <!-- <tr>
						  <td>3</td>
						  <th><a href="#!">[공지사항] 개인정보 처리방침 변경안내처리방침</a></th>
						  <td>2017.07.13</td>
					  </tr>
	  
					  <tr>
						  <td>2</td>
						  <th><a href="#!">공지사항 안내입니다. 이용해주셔서 감사합니다</a></th>
						  <td>2017.06.15</td>
					  </tr>
	  
					  <tr>
						  <td>1</td>
						  <th><a href="#!">공지사항 안내입니다. 이용해주셔서 감사합니다</a></th>
						  <td>2017.06.15</td>
					  </tr> -->
					  </tbody>
				  </table>
			  </div>
		  </div>
      <div id="pagingBox" style="text-align: center;">
        <ul id="paging">
        </ul>
      </div>
      
		<div id="board-search">
			<div class="container">
				<div class="search-window">
					  <div class="search-wrap" style="height: 50px;">
              <form id="excelForm" name="excelForm" method="post" action="/board/noticeExcelDownload">
                <input type="text" name="fileName" />
                <input type="submit" value="xlsx파일로 받기" />
              </form>
              <!-- <button type="submit" class="btn btn-dark" onclick="excelDownloadAll()" >엑셀 전체 다운로드</button> -->
						  <button type="submit" class="btn btn-dark" onclick="location.href = '/board/noticeRegist';" >글쓰기</button>
					  </div>
				</div>
			</div>
		</div>
	  
	</section>
</body>
</html>