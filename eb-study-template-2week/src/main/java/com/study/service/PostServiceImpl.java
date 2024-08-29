package com.study.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.dto.FileMetaData;
import com.study.dto.Post;
import com.study.dto.Posts;
import com.study.dto.Search;
import com.study.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

	private final PostMapper postMapper;
	private final FileService fileService;

	/* postId로 포스트 가져오기 */
	@Override
	public Post getPostById(Long postId) {
		return postMapper.getPostById(postId);
	}

	/*
	* getPostList()에 search와 offset을 넘겨주고 조건에 맞는 post 가져오기
	* getPostAllCount()로 조건에 맞는 post의 개수 가져오고
	* return posts.build
	*/
	@Override
	public Posts getPostList(Search search) {
		Integer offset = ((search.getPage() - 1) * 10);

		List<Post> post = postMapper.getPostList(search, offset);
		Integer count = postMapper.getPostAllCount(search);

		return Posts.builder()
			.list(post)
			.count(count)
			.build();
	}

	/*
	* 일치하면 post 삭제, CASCADE로 댓글, 파일 전체 삭제됨
	* postId로 포스트에 있는 파일들 정보 가져오고 로컬 스토어에 storeName 파일 삭제
	*/
	@Override
	public void removePost(Long postId, String password) {
		postMapper.removePost(postId, password);

		List<FileMetaData> list = fileService.getFiles(postId);

		for (FileMetaData fileMetaData : list) {
			fileService.removeFileFromStore(fileMetaData.getStoreName());
		}
	}

	/*
	* 포스트 저장 시 입력한 비밀번호와 확인용 비밀번호가 일치하지 않으면 IllegalStateException
	* savePost(), saveFile() 실패하면 SQLException
	*/
	@Override
	public void savePost(Post post, MultipartFile[] files){
		postMapper.savePost(post);
		fileService.saveFile(post.getPostId(), files);
	}

	/* 조회수 업데이트 */
	@Override
	public void modifyViews(Long postId) {
		postMapper.modifyViews(postId);
	}
}
