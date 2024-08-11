<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.study.dto.Post" %>
<%@ page import="com.study.dto.Category" %>
<html>
<head>
    <title>게시판 - 목록</title>
</head>

<style>
    table {
        width: 100%;
        border-collapse: collapse;
    }

    th, td {
        border: 1px solid #ddd;
        padding: 8px;
        text-align: center;
    }

    th {
        background-color: #f2f2f2;
        font-weight: bold;
    }


    td div {
        display: flex;
        justify-content: center;
    }

    /* 페이지 네비게이션 스타일 */
    .pagination {
        display: flex;
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .pagination li {
        margin: 0 5px;
        list-style: none;
    }

    .pagination a {
        display: block;
        text-decoration : none;
    }
</style>
<body>
<%
    List<Post> list = (List<Post>) request.getAttribute("list");
    List<Category> category = (List<Category>) request.getAttribute("category");
    int totalPost = (int) request.getAttribute("totalPost");
    String page1 = (String) request.getAttribute("page");

	String startDate = (String) request.getAttribute("startDate");
	String endDate = (String) request.getAttribute("endDate");
	String categoryId = (String) request.getAttribute("categoryId");
	String searchWord = (request.getAttribute("searchWord") != null) ? (String) request.getAttribute("searchWord") : "";

    String parameter = "";
    if(startDate!=null&&endDate!=null&&categoryId!=null&&!searchWord.equals("")){
        parameter += "&start_date="+startDate+"&end_date="+endDate+"&category="+categoryId+"&search_word="+searchWord;
    }
%>
<form action="/board/free/list">
    <h2>자유게시판 - 목록</h2>
    <input type="hidden" name="page" value="<%=page1%>">
    <% if(startDate!=null){%>
    <input name="start_date" type="date" value="<%=startDate%>">
    <%} else{ %>
    <input name="start_date" type="date" value="2023-08-15">
    <%}%>
    ~
    <% if(endDate!=null){%>
    <input name="end_date" type="date" value="<%=endDate%>">
    <%} else { %>
    <input name="end_date" type="date" value="2024-08-15">
    <%}%>
    <select name="category">
        <%if(categoryId==null){%>
        <option value="" selected>전체 카테고리</option>
        <% for (Category c : category) { %>
        <option value="<%=c.getCategoryId()%>"><%=c.getCategory()%></option>
        <%}%>
        <%} else{ %>
        <option value="">전체 카테고리</option>
        <% for (Category c : category) {
			if(c.getCategoryId()==Long.parseLong(categoryId)){
        %>
        <option value="<%=c.getCategoryId()%>" selected><%=c.getCategory()%></option>
        <%} else { %>
        <option value="<%=c.getCategoryId()%>"><%=c.getCategory()%></option>
        <%} } }%>
    </select>
    <input name="search_word" type="text" value="<%=searchWord%>">
    <button>검색</button>
</form>
<p>총 <%=totalPost%>건</p>
<table>
    <tr>
        <th style="width: 7%">카테고리</th>
        <th style="width: 60%">제목</th>
        <th style="width: 7%">작성자</th>
        <th style="width: 6%">조회수</th>
        <th style="width: 10%">등록 일시</th>
        <th style="width: 10%">수정 일시</th>
    </tr>
    <% for (Post post : list) { %>
    <tr>
        <td style="width: 7%">
            <%=post.getCategoryId()%>
            <% if(post.getFile()>0){%>
            <svg xmlns="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="12" height="12"><path d="M7.835,16.17c-.23-.23-.446-.482-.641-.748-.325-.446-.227-1.072,.22-1.397,.446-.325,1.071-.227,1.397,.219,.129,.178,.274,.349,.437,.511,.803,.803,1.87,1.245,3.005,1.245s2.203-.442,3.005-1.245l5.5-5.5c1.657-1.657,1.657-4.354,0-6.011s-4.354-1.657-6.011,0l-1.058,1.058c-.391,.391-1.023,.391-1.414,0s-.391-1.023,0-1.414l1.058-1.058c2.437-2.438,6.402-2.438,8.839,0,2.437,2.437,2.437,6.402,0,8.839l-5.5,5.5c-1.18,1.181-2.75,1.831-4.419,1.831s-3.239-.65-4.418-1.83Zm-1.582,7.83c1.67,0,3.239-.65,4.419-1.831l1.058-1.058c.391-.39,.391-1.023,0-1.414-.39-.391-1.023-.39-1.414,0l-1.059,1.058c-.803,.803-1.87,1.245-3.005,1.245s-2.202-.442-3.005-1.245-1.245-1.87-1.245-3.005,.442-2.203,1.245-3.005l5.5-5.5c.803-.803,1.87-1.245,3.005-1.245s2.203,.442,3.005,1.245c.16,.161,.306,.332,.436,.51,.324,.447,.949,.547,1.397,.221,.447-.325,.546-.95,.221-1.397-.19-.262-.405-.513-.639-.747-1.181-1.182-2.751-1.832-4.42-1.832s-3.239,.65-4.419,1.831L1.834,13.331C.653,14.511,.003,16.081,.003,17.75c0,1.669,.65,3.239,1.831,4.419,1.18,1.181,2.749,1.831,4.419,1.831Z"/></svg>
            <%}%>
        </td>
        <td style="width: 60%"><a href="/board/free/view?page=<%=page1%><%=parameter%>&post=<%=post.getPostId()%>"><%=post.getTitle()%></a>
        </td>
        <td style="width: 7%"><%=post.getWriter()%>
        </td>
        <td style="width: 6%"><%=post.getViews()%>
        </td>
        <td style="width: 10%"><%=post.getCreatedDate()%>
        </td>
        <td style="width: 10%"><%=post.getRevisionDate()%></td>
    </tr>
    <% } %>
    <tr>
        <%
            // 보여줄 페이지 갯수 개시글 105개면 11페이지
            int totalPages = (totalPost + 10 - 1) / 10;

			// 현재 페이지
            int currentPage = Integer.parseInt(page1 != null ? page1 : "1");

			// 시작페이지, 종료페이지
            int startPage = ((currentPage - 1) / 10) * 10 + 1;
            int endPage = Math.min(startPage + 10 - 1, totalPages);

            // 이전 범위 및 다음 범위 페이지 계산
            int prevRange = startPage - 10;
            int nextRange = endPage + 1;

            String url = "/board/free/list?page=";
            String prev = url + prevRange + parameter;
            String next = url + nextRange + parameter;
        %>
        <td colspan="6">
            <div>
                <ul class="pagination">
                    <li class="<%= (startPage <= 1) ? "disabled" : "" %>">
                        <a href="<%= startPage > 1 ? prev : "#" %>">&laquo;</a>
                    </li>

                    <% for (int p = startPage; p <= endPage; p++) { %>
                    <li class="<%= (p == currentPage) ? "active" : "" %>">
                        <a href="<%=url%><%=p%><%=parameter%>"><%=p%></a>
                    </li>
                    <% } %>

                    <li class="<%= (endPage >= totalPages) ? "disabled" : "" %>">
                        <a href="<%= endPage < totalPages ? next : "#" %>">&raquo;</a>
                    </li>
                </ul>
            </div>
        </td>
    </tr>
    <tr>
        <td>
            <button type="button" onclick="goToWrite()">등록</button>
        </td>
    </tr>
</table>
<script>
    const goToWrite = () =>{
        window.location.href = "/board/free/write?page=<%=page1%><%=parameter%>";
    }
</script>
</body>
</html>