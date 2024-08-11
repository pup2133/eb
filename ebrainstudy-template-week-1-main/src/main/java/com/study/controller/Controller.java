package com.study.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {

	// service
	View doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
