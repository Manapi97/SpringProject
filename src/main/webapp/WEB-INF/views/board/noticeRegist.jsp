<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String no = request.getParameter("id"); %>
<!DOCTYPE html>
<html>
<head>
<title>게시판 사이트</title>
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script> -->
<link rel="stylesheet" type="text/css" href="/resources/static/css/noticeRegist.css">
<script type="text/javascript" src="/resources/static/lib/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="/resources/static/lib/function.js"></script>
</head>
<script>
    var no = "<%=no%>";
    $(function(){
        // 게시글 불러온 글 수정 삭제 버튼 추가
        if(isEmpty(no)){ // 글쓰기 버튼 클릭 후
            document.getElementById("saveButton").style.display = "block";
            document.getElementById("updateButton").style.display = "none";
            document.getElementById("deleteButton").style.display = "none";
        }
        else { // 게시글 불러온 후
            document.getElementById("saveButton").style.display = "none";
            document.getElementById("updateButton").style.display = "block";
            document.getElementById("deleteButton").style.display = "block";

            // 게시글 상세 조회
            $.ajax({
                url: "/board/noticeView",
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
                    var noticeView = result.noticeView;
                    if(!isEmpty(noticeView)){
                        document.getElementById("title").value = noticeView[0].title;
                        document.getElementById("content").value = noticeView[0].content;
                    }
                    else {
                        alert("조회 실패");
                    }
                }
            });
        }
    });

    // 글쓰기 버튼
	function saveButton(){
		
        $.ajax({
            url: "/board/noticeRegist",
            type: "post",
            data: {
                title : document.getElementById("title").value,
                content : document.getElementById("content").value,
            } ,
            // dataType: "json",
            error : function(request, status, error){
                alert('등록 실패');
                console.dir(request);
                console.dir(status);
                console.dir(error);
            },
            success : function(result){
              alert("등록이 완료되었습니다.");
              location.href="/board/noticeList";
            }
        });
	}

    // 수정 버튼
    function updateButton(){
		
        $.ajax({
            url: "/board/noticeUpdate",
            type: "post",
            data: {
                no : no,
                title : document.getElementById("title").value,
                content : document.getElementById("content").value,
            } ,
            // dataType: "json",
            error : function(request, status, error){
                alert('수정 실패');
                console.dir(request);
                console.dir(status);
                console.dir(error);
            },
            success : function(result){
              alert("수정이 완료되었습니다.");
              location.href="/board/noticeList";
            }
        });
	}
    
    // 삭제 버튼
    function deleteButton(){
		
        $.ajax({
            url: "/board/noticeDelete",
            type: "post",
            data: {
                no : no
            } ,
            error : function(request, status, error){
                alert('삭제 실패');
                console.dir(request);
                console.dir(status);
                console.dir(error);
            },
            success : function(result){
              alert("삭제가 완료되었습니다.");
              location.href="/board/noticeList";
            }
        });
	}
</script>
<body>
	<label for="title">제목 :</label>

	<input type="text" id="title" name="title" required
		   minlength="4" size="25">
	<br>
	<label for="content">내용 :</label>
	<br>
	<textarea id="content" name="content" required style="width:100px; height:100px;">
	</textarea>
	<div id="board-search">
		<div class="container">
			<div class="search-window">
				<div class="search-wrap" style="height: 50px; display: inline-block;">
					<button id="saveButton" type="submit" class="btn btn-dark" onclick="saveButton()">글쓰기</button>
                    <button id="updateButton" type="submit" class="btn btn-dark" onclick="updateButton()">수정</button>
                    <button id="deleteButton" type="submit" class="btn btn-dark" onclick="deleteButton()">삭제</button>
					<button id="mainView" type="submit" class="btn btn-dark" onclick="location.href = '/board/noticeList';">목록</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>