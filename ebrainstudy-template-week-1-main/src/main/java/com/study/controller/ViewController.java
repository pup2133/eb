package com.study.controller;

import java.io.IOException;

import com.study.container.DI;
import com.study.dto.Post;
import com.study.service.FileServiceImpl;
import com.study.service.ViewServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ViewController implements Controller {

    @Override
    public View doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ViewServiceImpl vs = DI.getViewService();
        FileServiceImpl fs = DI.getFileService();

        String download = request.getParameter("download");

        if(download!=null){
            fs.fileDownload(response,download);
            return null;
        }else {
            String postId = request.getParameter("post");
            vs.addViews(postId);
            Post post = vs.getPostById(postId);
            request.setAttribute("post",post);
            return new View("/WEB-INF/views/view.jsp");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ViewServiceImpl vs = DI.getViewService();
        boolean result = vs.deletePost(request.getParameter("postId"), request.getParameter("password"));
        if (result) response.setStatus(response.SC_OK);
        else response.setStatus(response.SC_BAD_REQUEST);
    }
}
