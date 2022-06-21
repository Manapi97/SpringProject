<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<% String no = request.getParameter("id"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width", initial-scale="1">
<script type="text/javascript" src="/resources/static/lib/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="/resources/static/lib/function.js"></script>
<script type="text/javascript">
  var no = "<%=no%>";
  var imgFilePath = "";
  $(function(){
    $.ajax({
      url: "/board/noticeImgView",
      type: "post",
      data: {
          no : no,
      },
      error : function(request, status, error){
          alert('조회 실패');
          console.dir(request);
          console.dir(status);
          console.dir(error);
      },
      success : function(result){
        console.dir("result");
        console.dir(result);
        var noticeImgView = result.noticeImgView;
        if(!isEmpty(noticeImgView)){
            document.getElementById("no").innerHTML = noticeImgView[0].no;
            document.getElementById("title").value = noticeImgView[0].title;
            document.getElementById("content").value = noticeImgView[0].content;
            imgFilePath = noticeImgView[0].imgFilePath;

            var filePathArray = noticeImgView[0].imgFilePath.split("\\resources\\");
            if(filePathArray.length != 2){
              alert("조회 실패");
              return;
            }
            filePathArray[1] = filePathArray[1].replaceAll("\\", "/");
            var filePath = "/resources/" + filePathArray[1];
            document.getElementById("noticeImg").src = filePath;
        }
        else {
            alert("조회 실패");
        }
      }
    });
  });
  function noticeImgDelete() {
    if(confirm("정말 삭제하시겠습니까??") == true){    //확인
      if(imgFilePath === ""){
        alert("이미지 파일 경로가 잘못되었습니다.");
        return false;
      }

      $.ajax({
        url: "/board/noticeImgDelete",
        type: "post",
        data: {
            no : no,
            imgFilePath : imgFilePath,
        },
        error : function(request, status, error){
            alert('삭제 실패');
            console.dir(request);
            console.dir(status);
            console.dir(error);
        },
        success : function(result){
          alert("삭제되었습니다.");
          location.href = "/board/noticeImgList";
          console.dir("result");
          console.dir(result);
        }
      });
    }else{   //취소
      return false;
    }
  }
  
</script>
<title>이미지 상세 글</title>
</head>
<body>
	<div>
		<div>
      <table style="text-align: center; border: 1px solid #dddddd">
          <thead>
            <tr>
              <th colspan="5">이미지 상세 글</th>
            </tr>
          </thead>
          <tbody id="boardView">
            <tr>
              <td colspan="5" >번호 : <span id="no"></span></td>
            </tr>
            <tr>
              <td colspan="5" >제목 : <input type="text" id="title" value="" readonly></input></td>
            </tr>
            <tr>
              <td colspan="5" >사진</td>
            </tr>
            <tr>
              <td colspan="5" ><img id="noticeImg" class="image" src="" alt="이미지 없음" /></td>
            </tr>
            <tr>
              <td colspan="5" >내용</td>
            </tr>
            <tr>
              <td colspan="5" ><textarea class="form-control" id="content" name="content" maxlength="2048" style="height:350px;" readonly></textarea></td>
            </tr>
          </tbody>
      </table>
      <button type="button" onClick="location.href='/board/noticeImgList';" class="btn-primary pull-right" >목록</button>
      <button type="button" onClick="location.href='/board/noticeImgUpdate?id=<%=no%>';" class="btn-primary pull-right" >수정</button>
      <button type="button" onClick="noticeImgDelete();" class="btn-primary pull-right" >삭제</button>
      <br>
		</div>
	</div>
</body>
</html>