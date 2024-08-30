package com.study.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.mapper.PostMapper;
import com.study.model.dto.response.ListResponse;
import com.study.model.vo.FileMetaData;
import com.study.model.vo.Post;
import com.study.model.vo.Search;
import com.study.model.vo.View;
import com.study.model.vo.Write;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

	private final PostMapper postMapper;
	private final FileService fileService;

	/**
	 * 1. 검색 조건에 맞는 게시글 10개씩 조회, 게시글 총 개수 조회
	 * @param search: 검색 조건
	 * @return ListResponse: postList, TotalPostCount
	 */
	@Override
	public ListResponse findPostList(Search search) {
		Integer offset = ((search.getPage() - 1) * 10);

		List<Post> postList = postMapper.findPostList(search, offset);
		List<Post> posts = parserTitle(postList);

		Integer TotalPostCount = postMapper.findTotalPostCount(search);

		return ListResponse.builder().posts(posts).totalPostCount(TotalPostCount).build();
	}

	/**
	 * 1. 게시글 ID로 게시글 정보 가져오기
	 * @param postId: 게시글 ID
	 * @return View: 게시글 정보
	 */
	@Override
	public View findPostById(Long postId) {
		return postMapper.findPostById(postId);
	}

	/**
	 * 1. 게시글 ID와 비밀번호로 조건에 맞는 게시글 삭제
	 * 2. 로컬 스토어에 있는 파일 삭제
	 * @param postId: 게시글 ID
	 * @param postPassword: 사용자가 입력한 비밀번호
	 */
	@Override
	public void deletePostByIdAndPassword(Long postId, String postPassword){
		postMapper.deletePostByIdAndPassword(postId, postPassword);

		List<FileMetaData> files = fileService.findFilesByPostId(postId);

		/* 로컬 스토어에 있는 파일 삭제 */
		for (FileMetaData fileMetaData : files) {
			fileService.deleteFileFromStore(fileMetaData.getStoreName());
		}
	}

	/**
	 * 1. 게시글 저장
	 * 2. 저장된 게시글의 ID로 업로드한 파일들 저장
	 * @param write: 사용자가 입력한 게시글 정보
	 * @param files: 사용자가 업로드한 파일들
	 */
	@Override
	public void savePost(Write write, MultipartFile[] files){
		postMapper.savePost(write);
		fileService.saveFilesByPostId(write.getPostId(), files);
	}

	/**
	 * 1. 게시글 ID의 조회수 증가
	 * @param postId: 게시글 ID
	 */
	@Override
	public void updateViewsByPostId(Long postId) {
		postMapper.updateViewsByPostId(postId);
	}

	/**
	 * 1. 게시글의 비밀번호를 가져오기
	 * @param postId: 게시글 ID
	 * @return String: 게시글 ID의 비밀번호
	 */
	@Override
	public String findPasswordByPostId(Long postId){
		return postMapper.findPasswordByPostId(postId);
	}

	/**
	 * 1. 제목이 80자 넘어가면 40자로 줄임
	 * @param postList: Db에서 페이징 해서 가져온 postList
	 * @return List<Post>: 타이틀 수정 후 return
	 */
	private List<Post> parserTitle(List<Post> postList) {
		List<Post> posts = new ArrayList<>();

		for (Post post : postList) {
			String newTitle = post.getPostTitle();
			if (newTitle.length() >= 80) {
				newTitle = newTitle.substring(0, 30);
				newTitle += "...";
			}
			Post newPost = post.copyWith(newTitle);
			posts.add(newPost);
		}

		return posts;
	}


}
