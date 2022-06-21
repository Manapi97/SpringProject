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
  function submitData(){
    if(document.getElementById("title").value === "" || 
    document.getElementById("title").value == null ){
      alert("제목을 입력해주세요");
      return false;
    }

    if(document.getElementById("content").value === "" || 
    document.getElementById("content").value == null ){
      alert("내용을 입력해주세요");
      return false;
    }

    return true;
  }

  $(function(){
    document.getElementById("no").innerHTML = no;

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
            document.getElementById("no").value = noticeImgView[0].no;
            document.getElementById("title").value = noticeImgView[0].title;
            document.getElementById("content").value = noticeImgView[0].content;
            document.getElementById("originalFilePath").value = noticeImgView[0].imgFilePath;

            imgFilePath = noticeImgView[0].imgFilePath;

        }
        else {
            alert("조회 실패");
        }
      }
    });

    $('input[type="text"]').keydown(function() {
      if (event.keyCode === 13) {
        event.preventDefault();
      };
    });
  });
</script>
<title>이미지 글쓰기</title>
</head>
<body>
	<div>
		<div>
    <form action="/board/uploadUpdateImgNotice" method="post" enctype="multipart/form-data" onSubmit="return submitData();">
      <input hidden="hidden" id="originalFilePath" name="originalFilePath" />
      <table style="text-align: center; border: 1px solid #dddddd">
          <thead>
            <tr>
              <th colspan="5">이미지 게시판 수정</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td><b>번호는 수정할 수 없습니다.</b></td> 
            </tr>
            <tr>
              <td colspan="5" >번호 : <input type="text" name="no" id="no" readonly></input></td>
            </tr>
            <tr>
              <td colspan="5" >제목 : <input type="text" id="title" class="form-control" placeholder="글 제목" name="title" maxlength="50"></td>
            </tr>
            <tr>
              <td>내용</td> 
            </tr>
            <tr>
              <td colspan="5" ><textarea class="form-control" id="content" placeholder="글 내용" name="content" maxlength="2048" style="height:350px;"></textarea></td>
            </tr>
            <tr>
              <td colspan="5" ><b>이미지 파일을 추가하지 않으면 이미지 파일은 수정되지 않습니다.</b></td>
            </tr>
            <tr>
              <td colspan="5" ><input type="file" id="file" name="file"></td>
            </tr>
          </tbody>
      </table>
      <input type="submit" class="btn-primary pull-right" value="글쓰기">
      <br>
    </form>
		</div>
	</div>
</body>
</html>