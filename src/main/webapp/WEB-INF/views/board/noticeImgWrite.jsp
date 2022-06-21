<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width", initial-scale="1">
<script type="text/javascript" src="/resources/static/lib/jquery-3.0.0.min.js"></script>
<script type="text/javascript">
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
    
    if(document.getElementById("file").value === "" || 
    document.getElementById("file").value == null ){
      alert("이미지 파일을 등록해주세요");
      return false;
    }
    return true;
  }

  $(function(){
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
    <form action="/board/uploadImgNotice" method="post" enctype="multipart/form-data" onSubmit="return submitData();">
      <input hidden="hidden" />
      <table style="text-align: center; border: 1px solid #dddddd">
          <thead>
            <tr>
              <th colspan="5">게시판 글쓰기 양식</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td colspan="5" ><input type="text" id="title" class="form-control" placeholder="글 제목" name="title" maxlength="50"></td>
            </tr>
            <tr>
              <td colspan="5" ><textarea class="form-control" id="content" placeholder="글 내용" name="content" maxlength="2048" style="height:350px;"></textarea></td>
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