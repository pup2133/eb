package com.study.service;

import java.sql.SQLException;

import com.study.container.DI;
import com.study.dao.ModifyDAO;
import com.study.dto.Post;
import com.study.util.Validation;

import jakarta.servlet.http.HttpServletRequest;

public class ModifyServiceImpl implements ModifyService{

	private final ModifyDAO modifyDAO;

	public ModifyServiceImpl(ModifyDAO modifyDAO) {
		this.modifyDAO = modifyDAO;
	}

	// 게시글 수정
	@Override
	public void modifyPost(HttpServletRequest request) throws SQLException, IllegalStateException{

		String deleteList = request.getParameter("deleteList");

		Post post = new Post(
			Long.parseLong(request.getParameter("postId")),
			request.getParameter("category"),
			request.getParameter("writer"),
			request.getParameter("password"),
			request.getParameter("title"),
			request.getParameter("content")
		);

		// 비밀번호 확인
		String password = modifyDAO.getPassword(post.getPostId());
		if (!password.equals(post.getPassword())) {
			throw new IllegalStateException("비밀번호가 일치하지 않습니다");
		}

		// 유효성 체크
		Validation v = Validation.getINSTANCE();
		if (!v.validate(post)) {
			throw new IllegalArgumentException("유효성 실패");
		}

		// 파일 삭제
		if (deleteList.length() > 0) {
			String[] deletes = deleteList.split(",");
			for (String delete : deletes) {
				if(1!= modifyDAO.deleteFile(delete)) throw new SQLException("파일 삭제 실패");
			}
		}

		// 파일 추가
		FileServiceImpl fs = DI.getFileService();
		if(!fs.saveFile(post.getPostId(), request)) {
			throw new SQLException("파일 추가 실패");
		}

		// 포스트 업데이트
		if(1!= modifyDAO.updatePost(post)) {
			throw new SQLException("글 업데이트 실패");
		}

	}

	// 수정 페이지 보여줄 때 쓸거
	@Override
	public Post modifyPostView(Post post){
		try {
			return modifyDAO.modifyPostView(post);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
