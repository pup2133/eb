<%@ page import="com.study.dto.Post" %>
<%@ page import="com.study.dto.Comment" %>
<%@ page import="com.study.dto.Files" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.ZoneId" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시판 - 보기</title>
    <style>
        .comments{
            display: flex;
            flex-direction: column;
        }

        .comments td {
            padding: 4px;
        }

        .files {
            display: flex;
            flex-direction: column;
        }

        .files td {
            border-bottom: 1px solid #ddd;
            padding: 8px;
        }

        /* 모달 배경 스타일 */
        .modal {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            display: none;
            align-items: center;
            justify-content: center;
            z-index: 1000;
        }

        /* 모달 내용 스타일 */
        .modal > div {
            background-color: #fff;
            padding: 20px;
            max-width: 400px;
            width: 100%;
            box-sizing: border-box;
        }

        /* 버튼 스타일 */
        .modal button {
            background-color: black;
            color: #fff;
            border: none;
            padding: 10px 20px;
            margin: 5px;
            cursor: pointer;
        }

        .password{
            display: flex;
        }

        button[onclick="cancel()"]{
            background-color: lightgray;
        }

        .buttons{
            margin-left: 100px;
        }

        button[onclick="check()"]:hover {
            background-color: lightgreen;
        }

        button[onclick="cancel()"]:hover {
            background-color: lightgreen;
        }

        /* 비밀번호 입력 박스 스타일 */
        .modal input[type="password"] {
            width: 70%;
            height: 50%;
            padding: 10px;
            margin-top: 9px;
            margin-left: 20px;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }

        .content{
            height: 150px;
        }
    </style>
</head>
<body>
<h2>자유게시판 - 보기</h2>
<%
    String page1 = (String) request.getAttribute("page");

    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    String categoryId = (String) request.getAttribute("categoryId");
    String searchWord = (request.getAttribute("searchWord") != null) ? (String) request.getAttribute("searchWord") : "";

    String parameter = "page="+page1;
    if(startDate!=null&&endDate!=null&&categoryId!=null&&!searchWord.equals("")){
        parameter += "&start_date="+startDate+"&end_date="+endDate+"&category="+categoryId+"&search_word="+searchWord;
    }

    LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	String currentDate = now.format(formatter);
%>
<table border="1px">
    <% Post post = (Post)request.getAttribute("post");%>
    <tr>
        <td><%=post.getWriter()%></td>
        <td>등록일시 <%=post.getCreatedDate()%></td>
        <td>수정일시 <%=post.getRevisionDate()%></td>
    </tr>
    <tr>
        <td colspan="2">
            [<%=post.getCategory()%>]
            <%=post.getTitle()%>
        </td>
        <td>조회수: <%=post.getViews()%></td>
    </tr>
    <tr>
        <td class="content" colspan="3">
            <%=post.getContent()%>
        </td>
    </tr>
    <tr class="files">
        <% for (Files file : post.getFiles()) { %>
        <td>
            <svg xmlns="http://www.w3.org/2000/svg" id="Outline" viewBox="0 0 24 24" width="12" height="12"><path d="M9.878,18.122a3,3,0,0,0,4.244,0l3.211-3.211A1,1,0,0,0,15.919,13.5l-2.926,2.927L13,1a1,1,0,0,0-1-1h0a1,1,0,0,0-1,1l-.009,15.408L8.081,13.5a1,1,0,0,0-1.414,1.415Z"/><path d="M23,16h0a1,1,0,0,0-1,1v4a1,1,0,0,1-1,1H3a1,1,0,0,1-1-1V17a1,1,0,0,0-1-1H1a1,1,0,0,0-1,1v4a3,3,0,0,0,3,3H21a3,3,0,0,0,3-3V17A1,1,0,0,0,23,16Z"/></svg>
            <a href="/board/free/view?download=<%=file.getFileId()%>"><%=file.getFileName()%></a>
        </td>
        <% } %>
    </tr>
    <tr class="comments">
        <% for (Comment comment : post.getComments()) { %>
        <td class="comment">
            <%=comment.getWriter()%>  <%=comment.getContent()%> <%=comment.getCreatedDate()%>
        </td>
        <% } %>
    </tr>
    <tr>
        <input name="postId" id="postId" type="hidden" value="<%=post.getPostId()%>">
        <td>
            <input name="writer" type="text">
        </td>
        <td>
            <input name="comment" type="text">
        </td>
        <td>
            <button type="button" onclick="addComment()">등록</button>
        </td>
    </tr>
    <tr>
        <td colspan="3">
            <button type="button" onclick="goToList()">목록</button>
            <button type="button" onclick="modifyPost()">수정</button>
            <button type="button" onclick="deletePost()">삭제</button>
        </td>
    </tr>
</table>
<div class="modal">
    <div>
        <div class="password">
            <h3>비밀번호</h3>
            <input id="password" type="password" placeholder="비밀번호를 입력해 주세요.">
        </div>
        <div class="buttons">
            <button onclick="cancel()">취소</button>
            <button onclick="check()">확인</button>
        </div>
    </div>
</div>
<script>
    const modifyPost = () =>{
        const postId = document.getElementById('postId').value;
        window.location.href = "/board/free/modify?<%=parameter%>&post="+postId;
    }

    const goToList = () => {
        window.location.href = "/board/free/list?<%=parameter%>";
    }

    const deletePost = () =>{
        let modal = document.querySelector('.modal');
        if (modal) {
            modal.style.display = 'flex';
        }
    }

    const cancel = () => {
        let password = document.getElementById("password");
        let modal = document.querySelector('.modal');

        if (modal) {
            modal.style.display = 'none';
            password.value = "";
            password.innerText ="";
        }
    }

    const check = () => {
        const password = document.getElementById("password").value;
        const postId = document.getElementById("postId").value;

        const urlEncodedData = new URLSearchParams({
            "postId": postId,
            "password": password
        }).toString();

        fetch("/board/free/view",{
            method : "POST",
            headers : {
                "Content-Type" : "application/x-www-form-urlencoded"
            },
            body : urlEncodedData
        }).then((response) => {
            if(response.status===200){
                alert("삭제완료");
                window.location.href = "/board/free/list?<%=parameter%>"
            }else {
                alert("비밀번호 불일치");
            }
        });
    }

    const addComment = () =>{
        const postId = document.getElementById("postId").value;
        const comment = document.getElementsByName('comment')[0];
        const writer = document.getElementsByName('writer')[0];

        const urlEncodedData = new URLSearchParams({
            "postId" : postId,
            "comment" : comment.value,
            "writer" : writer.value
        }).toString();

        fetch("/board/free/comment",{
            method : "POST",
            headers : {
                "Content-Type" : "application/x-www-form-urlencoded"
            },
            body : urlEncodedData
        }).then((response) => {
            if(response.status===200){
                const commentsClass = document.querySelector('.comments');
                const commentClass = document.createElement('td');
                commentClass.textContent = writer.value + "  " + comment.value + " <%=currentDate%>";

                commentsClass.appendChild(commentClass);
                comment.value = "";
                writer.value = "";
            }
        });

    }
</script>
</body>
</html>