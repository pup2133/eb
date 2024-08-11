package com.study.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@MultipartConfig
@WebServlet("/board/free/*")
public class BoardController extends HttpServlet {

	private Map<String, Controller> controllerMap = new HashMap<>();

	public BoardController(){
		controllerMap.put("/board/free/modify",new ModifyController());
		controllerMap.put("/board/free/write", new WriteController());
		controllerMap.put("/board/free/view", new ViewController());
		controllerMap.put("/board/free/list", new ListController());
		controllerMap.put("/board/free/comment", new CommentController());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();

		request.setAttribute("startDate",request.getParameter("start_date"));
		request.setAttribute("endDate",request.getParameter("end_date"));
		request.setAttribute("categoryId",request.getParameter("category"));
		request.setAttribute("searchWord",request.getParameter("search_word"));
		request.setAttribute("page",request.getParameter("page"));

		Controller controller = controllerMap.get(requestURI);
		if (controller == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if(request.getMethod().equalsIgnoreCase("GET")){
			View view = controller.doGet(request, response);
			view.render(request, response);
		}else if (request.getMethod().equalsIgnoreCase("POST")) {
			controller.doPost(request,response);
		}else {
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}
}
