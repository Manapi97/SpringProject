<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>이미지 게시판 사이트</title>
  <!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script> -->
  <link rel="stylesheet" type="text/css" href="/resources/static/css/noticeImgList.css">
  <script type="text/javascript" src="/resources/static/lib/function.js"></script>
  <script type="text/javascript" src="/resources/static/lib/jquery-3.0.0.min.js"></script>
  <script>
    var page = 1;
    var beginPage = 0;
    var endPage = 0;
    var totalPagingCount = 0;
    // 이미지 게시판 목록 조회
    function noticeImgList(pageParam){
      $.ajax({
        url : "/board/noticeImgList",
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

          var noticeImgList = result.noticeImgList;
          var imgBoardList = document.getElementById("imgBoardList");

          page = result.page;
          beginPage = result.beginPage;
          endPage = result.endPage;
          totalPagingCount = result.totalPagingCount;
          
          if(!isEmpty(noticeImgList)){
            imgBoardList.innerText = '';
            var contentString = '';
            for (let i = 0; i < noticeImgList.length; i++) {
              if(isEmpty(noticeImgList[i].imgFileName) 
              || isEmpty(noticeImgList[i].imgFilePath)){
                continue;
              }

              console.dir("배열");
              console.dir(noticeImgList[i]);
              var filePathArray = noticeImgList[i].imgFilePath.split("\\resources\\");
              console.dir(filePathArray);

              if(filePathArray.length != 2){
                continue;
              }

              filePathArray[1] = filePathArray[1].replaceAll("\\", "/");

              var filePath = "/resources/" + filePathArray[1];

              
              if(i == 0 || i == 4){
                contentString += "<tr>";
              }
              contentString += "<td scope='col'>";
              contentString += "<img class='image' src='";
              contentString += filePath; 
              contentString += "' alt='이미지없음' />";
              contentString += "<br><span>";
              contentString +=  noticeImgList[i].no + " 번</span>";
              contentString += "<br><a href='/board/noticeImgView?id=";
              contentString += noticeImgList[i].no + "'>";
              contentString +=  noticeImgList[i].title + "</a>";
              contentString += "</td>";
              
              if(i == imgBoardList.length-1 || i == 3 || i == 7){
                contentString += "</tr>";
                
              }
            }
            imgBoardList.innerHTML += contentString;
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
            alert("게시글이 없습니다. 게시글을 등록해주세요.");
          }

        }
      });
    }

    $(function(){
      noticeImgList(page);

      $(document).on("click", "#prev", function() {
        console.dir("이전버튼 클릭");
        if(page == 1){
          return null;
        }
        noticeImgList(page-1);
      });

      $(document).on("click", "#next", function() {
        console.dir("다음 버튼 클릭");
        if(totalPagingCount == 0 || page == totalPagingCount){
          return null;
        }
        noticeImgList(page+1);
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
          noticeImgList(pageNumber);
        }
        
      });
    });
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
    <div class="table-box">
      <table class="board-table" border="1">
        <colgroup>
          <col width="25%">
          <col width="25%">
          <col width="25%">
          <col width="25%">
        </colgroup>
        <thead>
        </thead>
        <tbody id="imgBoardList">
          <!-- <tr>
            <td scope="col"><img class="image" src="/resources/static/img/heart.jpg" alt="이미지 d" />
              <br><span>1번</span>
              <br><span>제목</span>
            </td>
            <td scope="col"><img class="image"src="/resources/static/img/import package.jpg" alt="이미지 d" /></td>
            <td scope="col"><img class="image" src="/resources/static/img/ah.jpg" alt="이미지 d" /></td>
            <td scope="col"><img class="image" src="/resources/static/img/import package.jpg" alt="이미지 d" /></td>
          </tr>
          <tr>
            <td scope="col"><img class="image" src="/resources/static/img/heart.jpg" alt="이미지 d" /></td>
            <td scope="col"><img class="image" src="/resources/static/img/import package.jpg" alt="이미지 d" /></td>
            <td scope="col"><img class="image" src="/resources/static/img/heart.jpg" alt="이미지 d" /></td>
            <td scope="col"><img class="image" src="/resources/static/img/import package.jpg" alt="이미지 d" /></td>
          </tr> -->
        </tbody>
      </table>
    </div>
    <!-- 페이징 박스 -->
    <div id="pagingBox" style="text-align: center;">
      <ul id="paging">
      </ul>
    </div>
    <div id="board-search">
      <div class="container">
        <div class="search-window">
          <div class="search-wrap" style="height: 50px;">
            <button type="submit" class="btn btn-dark"
              onclick="location.href = '/board/noticeImgWrite';">글쓰기</button>
          </div>
        </div>
      </div>
    </div>

  </section>
</body>

</html>