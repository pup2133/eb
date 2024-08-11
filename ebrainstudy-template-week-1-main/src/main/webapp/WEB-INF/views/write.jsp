<%@ page import="com.study.dto.Category" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시판 - 등록</title>
    <style>
        .f {
            display: flex;
            flex-direction: column;
        }

        input[type="file"] {
            margin-top: 2px;
        }

        input[name="title"], textarea {
            width: 100%;
        }

        button[onclick="savePost()"] {
            margin-left: 283px;
        }
    </style>
</head>
<body>
<h2>자유게시판 - 등록</h2>
<%
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    String categoryId = (String) request.getAttribute("categoryId");
    String searchWord = (request.getAttribute("searchWord") != null) ? (String) request.getAttribute("searchWord") : "";
    String page1 = (String) request.getAttribute("page");

    String parameter = "page="+page1;

	if(startDate!=null&&endDate!=null&&categoryId!=null&&!searchWord.equals("")){
		parameter += "&start_date="+startDate+"&end_date="+endDate+"&category="+categoryId+"&search_word="+searchWord;
    }
%>
<form>
    <table border="1px">
        <tr>
            <th>카테고리</th>
            <td>
                <select name="category">
                    <option value="">카테고리 선택</option>
                    <% ArrayList<Category> category = (ArrayList<Category>)request.getAttribute("category");
                        for (Category c : category) {
                    %>
                    <option value="<%=c.getCategoryId()%>"><%=c.getCategory()%>
                    </option>
                    <% } %>
                </select>

            </td>
        </tr>
        <tr>
            <th>작성자</th>
            <td><input name="writer" type="text"></td>
        </tr>
        <tr>
            <th>비밀번호*</th>
            <td>
                <input name="password" type="password" placeholder="비밀번호">
                <input name="confirmPassword" type="password" placeholder="비밀번호 확인">
            </td>
        </tr>
        <tr>
            <th>제목*</th>
            <td><input name="title" type="text"></td>
        </tr>
        <tr>
            <th>내용*</th>
            <td><textarea name="content"></textarea></td>
        </tr>
        <tr>
            <th>파일첨부*</th>
            <td class="f">
                <input name="file" type="file">
                <input name="file" type="file">
                <input name="file" type="file">
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <button type="button" onclick="moveToList()">취소</button>
                <button type="button" onclick="savePost()">저장</button>
            </td>
        </tr>
    </table>
</form>
<script>
    const validate = () => {
        const category = document.getElementsByName("category")[0].value;
        const writer = document.getElementsByName("writer")[0].value;
        const password = document.getElementsByName("password")[0].value;
        const confirmPassword = document.getElementsByName("confirmPassword")[0].value;
        const title = document.getElementsByName("title")[0].value;
        const content = document.getElementsByName("content")[0].value;

        if (category === undefined || category === "") {
            alert("카테고리");
            return false;
        } else if (writer === undefined || writer === "" || !/^.{3,4}$/.test(writer)) {
            alert("작성자");
            return false;
        } else if (password === undefined || password === "" || password !== confirmPassword
            || !/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,15}$/.test(password)) {
            alert("비밀번호");
            return false;
        } else if (title === undefined || title === "" || !/^.{4,99}$/.test(title)) {
            alert("제목");
            return false;
        } else if (content === undefined || content === "" || !/^.{4,1999}$/.test(content)) {
            alert("내용");
            return false;
        } else {
            alert("유효성 성공");
            return true;
        }
    }

    const savePost = () => {
        if (validate()) {
            const form = document.querySelector('form');
            const formData = new FormData(form);

            fetch("/board/free/write", {
                method: "POST",
                body: formData
            }).then((response) => {
                if (response.status === 200) {
                    alert("등록완료");
                    window.location.href = "/board/free/list?<%=parameter%>";
                } else {
                    alert("등록실패");
                }
            });
        }
    }

    const moveToList = () => {
        window.location.href = "/board/free/list?<%=parameter%>";
    }
</script>
</body>
</html>
