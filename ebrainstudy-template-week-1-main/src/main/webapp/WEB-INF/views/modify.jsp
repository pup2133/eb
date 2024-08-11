<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.study.dto.Post" %>
<%@ page import="com.study.dto.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.dto.Files" %>
<html>
<head>
    <title>게시판 - 수정</title>
    <style>
        #files{
            display: flex;
            flex-direction: column;
        }
        #fileUpload{
            display: flex;
            flex-direction: column;
        }
    </style>
</head>
<body>
<h2>자유게시판 - 수정</h2>
<%
    Post post = (Post) request.getAttribute("post");
    List<Category> category = (List<Category>) request.getAttribute("category");
    int fileCount = (int) request.getAttribute("fileCount");

    String page1 = (String) request.getAttribute("page");

    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    String categoryId = (String) request.getAttribute("categoryId");
    String searchWord = (request.getAttribute("searchWord") != null) ? (String) request.getAttribute("searchWord") : "";

    String parameter = "page="+page1;

    if(startDate!=null&&endDate!=null&&categoryId!=null&&!searchWord.equals("")){
        parameter += "&start_date="+startDate+"&end_date="+endDate+"&category="+categoryId+"&search_word="+searchWord;
    }
%>
<form>
    <table>
        <input type="hidden" name="postId" value="<%=post.getPostId()%>">
        <tr>
            <td>카테고리*</td>
            <td>
                <select name="category">
                    <option value="<%=post.getCategoryId()%>">
                        <%=post.getCategory()%>
                    </option>
                    <% for (Category c : category) {
                        if(c.getCategory().equals(post.getCategory())) continue;
                    %>
                    <option value="<%=c.getCategoryId()%>">
                        <%=c.getCategory()%>
                    </option>
                    <% } %>
                </select>
            </td>
        </tr>
        <tr>
            <td>등록 일시</td>
            <td>
                <%=post.getCreatedDate()%>
            </td>
        </tr>
        <tr>
            <td>수정 일시</td>
            <td><%=post.getRevisionDate()%></td>
        </tr>
        <tr>
            <td>조회수</td>
            <td><%=post.getViews()%></td>
        </tr>
        <tr>
            <td>작성자*</td>
            <td><input name="writer" type="text" value="<%=post.getWriter()%>"></td>
        </tr>
        <tr>
            <td>비밀번호</td>
            <td><input name="password" type="password" value=""></td>
        </tr>
        <tr>
            <td>제목*</td>
            <td><input name="title" type="text" value="<%=post.getTitle()%>"></td>
        </tr>
        <tr>
            <td>내용</td>
            <td><textarea name="content"><%=post.getContent()%></textarea></td>
        </tr>
        <tr>
            <td>파일첨부</td>
            <td id="files">
                <% for (Files file : post.getFiles()) { %>
                <div>
                    <svg xmlns="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="12" height="12"><path d="M7.835,16.17c-.23-.23-.446-.482-.641-.748-.325-.446-.227-1.072,.22-1.397,.446-.325,1.071-.227,1.397,.219,.129,.178,.274,.349,.437,.511,.803,.803,1.87,1.245,3.005,1.245s2.203-.442,3.005-1.245l5.5-5.5c1.657-1.657,1.657-4.354,0-6.011s-4.354-1.657-6.011,0l-1.058,1.058c-.391,.391-1.023,.391-1.414,0s-.391-1.023,0-1.414l1.058-1.058c2.437-2.438,6.402-2.438,8.839,0,2.437,2.437,2.437,6.402,0,8.839l-5.5,5.5c-1.18,1.181-2.75,1.831-4.419,1.831s-3.239-.65-4.418-1.83Zm-1.582,7.83c1.67,0,3.239-.65,4.419-1.831l1.058-1.058c.391-.39,.391-1.023,0-1.414-.39-.391-1.023-.39-1.414,0l-1.059,1.058c-.803,.803-1.87,1.245-3.005,1.245s-2.202-.442-3.005-1.245-1.245-1.87-1.245-3.005,.442-2.203,1.245-3.005l5.5-5.5c.803-.803,1.87-1.245,3.005-1.245s2.203,.442,3.005,1.245c.16,.161,.306,.332,.436,.51,.324,.447,.949,.547,1.397,.221,.447-.325,.546-.95,.221-1.397-.19-.262-.405-.513-.639-.747-1.181-1.182-2.751-1.832-4.42-1.832s-3.239,.65-4.419,1.831L1.834,13.331C.653,14.511,.003,16.081,.003,17.75c0,1.669,.65,3.239,1.831,4.419,1.18,1.181,2.749,1.831,4.419,1.831Z"/></svg>
                    <input type="hidden" value="<%=file.getFileId()%>">
                    <%=file.getFileName()%>
                    <button type="button" onclick="fileDown(this)">다운로드</button>
                    <button type="button" onclick="fileDelete(this)">X</button>
                </div>
                <% } %>
                <div id="fileUpload">
                    <% for (int i = 1; i <= (3 - fileCount) ; i++) { %>
                    <input name="file" type="file">
                    <% } %>
                </div>
            </td>
        </tr>
        <tr>
            <td><button type="button" onclick="goToPost()">취소</button></td>
            <td><button type="button" onclick="modifyP()">저장</button></td>
        </tr>
    </table>
</form>
<script>
    let deleteList = [];

    const validate = () =>{
        const category = document.getElementsByName("category")[0].value;
        const writer = document.getElementsByName("writer")[0].value;
        const password = document.getElementsByName("password")[0].value;
        const title = document.getElementsByName("title")[0].value;
        const content = document.getElementsByName("content")[0].value;

        if(category===undefined||category===""){
            alert("카테고리");
            return false;
        }else if(writer===undefined || writer==="" || !/^.{3,4}$/.test(writer)){
            alert("작성자");
            return false;
        }else if(password===undefined || password===""
            || !/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,15}$/.test(password)){
            alert("비밀번호");
            return false;
        }else if(title===undefined || title==="" || !/^.{4,99}$/.test(title)){
            alert("제목");
            return false;
        }else if(content===undefined || content==="" || !/^.{4,1999}$/.test(content)) {
            alert("내용");
            return false;
        }else {
            alert("유효성 성공");
            return true;
        }
    }

    const goToPost = () => {
        const postId = document.getElementsByName('postId')[0].value;
        window.location.href = "/board/free/view?<%=parameter%>"+"&post="+postId;
    }

    const fileDelete = (button) => {
        if(confirm("삭제?")){
            const div = button.parentElement;
            const fileId = div.querySelector('input[type="hidden"]').value;

            deleteList.push(fileId);

            div.remove();

            const fileUpload = document.getElementById('fileUpload');
            const newInput = document.createElement('input');

            newInput.type = "file";
            newInput.name = "file";

            fileUpload.appendChild(newInput);

            alert("삭제완료");
        }
    }

    const modifyP = () =>{
        if(validate()){
            const form = document.querySelector('form');
            const formData = new FormData(form);
            const postId = document.getElementsByName('postId')[0].value;

            formData.set("deleteList",deleteList.toString());

            fetch("/board/free/modify",{
                method : "POST",
                body : formData
            }).then((response) => {
                if(response.status===200){
                    alert("수정 완료");
                    window.location.href = "/board/free/view?<%=parameter%>&post="+postId;
                    return Promise.resolve();
                }else {
                    return response.text();
                }
            }).then(responseText =>{
                if(responseText){
                    alert(responseText);
                }
            });
        }
    }

    const fileDown = (button) =>{
        const fileId = button.parentElement.querySelector('input[type="hidden"]').value;
        window.location.href = "/board/free/view?download="+fileId;
    }
</script>
</body>
</html>
