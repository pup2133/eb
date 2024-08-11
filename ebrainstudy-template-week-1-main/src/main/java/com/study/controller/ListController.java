package com.study.controller;

import java.io.IOException;

import com.study.container.DI;
import com.study.dto.Posts;
import com.study.dto.Search;
import com.study.service.CategoryServiceImpl;
import com.study.service.ListServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListController implements Controller {

    @Override
    public View doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ListServiceImpl ls = DI.getListService();
        CategoryServiceImpl cs = DI.getCategoryService();

        Posts posts = null;

        if(request.getParameter("start_date")!=null){
            Search search = new Search(
                request.getParameter("start_date"),
                request.getParameter("end_date"),
                request.getParameter("category"),
                request.getParameter("search_word"),
                request.getParameter("page")
            );
            posts = ls.getSearchPost(search);
            request.setAttribute("page",request.getParameter("page"));
        }else {
            posts = ls.getList(request.getParameter("page"));
            request.setAttribute("page",request.getParameter("page"));
        }

        request.setAttribute("list", posts.getList());
        request.setAttribute("category",cs.getCategory());
        request.setAttribute("totalPost",posts.getCount());

        return new View("/WEB-INF/views/list.jsp");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
