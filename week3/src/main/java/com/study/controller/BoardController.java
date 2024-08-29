package com.study.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.exception.CommentException;
import com.study.exception.FileException;
import com.study.exception.PasswordException;
import com.study.exception.PostException;
import com.study.model.dto.request.CommentRequest;
import com.study.model.dto.request.ModifyRequest;
import com.study.model.dto.request.WriteRequest;
import com.study.model.dto.response.ApiResponse;
import com.study.model.dto.response.CommentResponse;
import com.study.model.dto.response.ListResponse;
import com.study.model.dto.response.ModifyResponse;
import com.study.model.dto.response.ViewResponse;
import com.study.model.vo.Category;
import com.study.model.vo.Comment;
import com.study.model.vo.FileMetaData;
import com.study.model.vo.Modify;
import com.study.model.vo.Page;
import com.study.model.vo.Search;
import com.study.model.vo.View;
import com.study.service.CategoryService;
import com.study.service.CommentService;
import com.study.service.FileService;
import com.study.service.ModifyService;
import com.study.service.PostService;
import com.study.util.Pagination;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081")
public class BoardController {

	private final PostService postService;
	private final CommentService commentService;
	private final FileService fileService;
	private final CategoryService categoryService;
	private final ModifyService modifyService;
	private final Pagination pagination;

	/**
	 * 1. 검색 조건으로 postList 가져오기
	 * 2. 모든 카테고리 가져오기
	 * 3. 검색 조건으로 조회 된 총 게시글 수, 현재 페이지를 넘겨서 페이징
	 * @param search: 검색 조건
	 * @return ApiResponse: ListResponse, 메세지 리턴
	 */
	@GetMapping("/api/list")
	public ApiResponse list(@ModelAttribute Search search) {
		ListResponse postListResponse = postService.findPostList(search);

		if(postListResponse.getPosts().isEmpty()) {
			return ApiResponse.builder().message("검색 된 게시글이 없습니다.").build();
		}

		List<Category> categories = categoryService.getCategories();
		Page page = pagination.pagination(postListResponse.getTotalPostCount(), search.getPage());

		ListResponse listResponse = ListResponse.builder()
			.posts(postListResponse.getPosts())
			.totalPostCount(postListResponse.getTotalPostCount())
			.categories(categories)
			.page(page)
			.search(search)
			.build();

		return ApiResponse
			.builder()
			.object(listResponse)
			.message("게시글 조회 성공")
			.build();
	}

	/**
	 * 1. 조회수 업데이트
	 * 2. ID에 맞는 게시글이 없으면 PostException
	 * 3. ID에 맞는 댓글과 파일 가져오기
	 * @param postId: 게시글의 ID
	 * @return ApiResponse: ViewResponse, 메세지 리턴
	 */
	@GetMapping("/api/view")
	public ApiResponse view(@RequestParam Long postId) {
		postService.updateViewsByPostId(postId);

		View post = postService.findPostById(postId);
		if(post==null) throw new PostException("존재하지 않는 게시글입니다.");

		List<Comment> comment = commentService.findCommentByPostId(postId);
		List<FileMetaData> files = fileService.findFilesByPostId(postId);

		ViewResponse viewResponse = ViewResponse.builder()
			.view(post)
			.comment(comment)
			.files(files)
			.build();

		return ApiResponse.builder()
			.object(viewResponse)
			.message("게시글 조회 성공")
			.build();
	}

	/**
	 * 1. ID로 파일 정보 가져오기
	 * 2. 파일이 NotNULL 이면 가져온 파일 정보로 다운로드 진행
	 * @param fileId: 파일 ID
	 * @param response: HttpServletResponse
	 */
	@GetMapping("/api/view/download")
	public void view(@RequestParam Long fileId, HttpServletResponse response) {
		try {
			FileMetaData file = fileService.findFileById(fileId);

			if (file != null) {
				String fileName = file.getFileName();
				String storeName = file.getStoreName();
				String path = file.getFilePath();
				String filePath = (path + storeName);

				// UTF-8로 인코딩된 바이트 배열을 8859_1로 디코딩
				fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

				fileService.transferFileData(new FileInputStream(filePath), response.getOutputStream());
			}
		} catch (IOException e) {
			throw new FileException("파일 다운로드 오류");
		}
	}

	/**
	 * 1. 댓글 등록 후 등록한 댓글의 정보 가져오기
	 * 2. 댓글이 NULL 이면 CommentException
	 * @param commentRequest: 작성자, 작성내용이 포함됨
	 * @return ApiResponse: commentResponse, 댓글 조회 성공 메세지 리턴
	 */
	@PostMapping("/api/view/save-comment")
	public ApiResponse view(@RequestBody CommentRequest commentRequest) {
		CommentResponse commentResponse = commentService.saveComment(commentRequest.toVo());

		if(commentResponse == null) {
			throw new CommentException("댓글을 불러오는 데 실패했습니다.\n다시 시도해 주세요.");
		}

		return ApiResponse.builder().object(commentResponse).message("댓글 조회 성공").build();
	}

	/**
	 * 1. ID로 게시글의 비밀번호 가져오기
	 * 2. 사용자가 입력한 Password 와 일치하지 않으면 PasswordException
	 * 3. 일치하면 게시글 삭제 (CASCADE)
	 * @param postId: 게시글 ID
	 * @param postPassword: 사용자가 입력한 Password
	 * @return ApiResponse: 게시글 삭제 메세지 리턴
	 */
	@DeleteMapping("/api/view/remove-post")
	public ApiResponse view(@RequestParam Long postId, @RequestParam String postPassword){
		String password = postService.findPasswordByPostId(postId);

		if(!postPassword.equals(password)){
			throw new PasswordException("비밀번호가 일치하지 않습니다.");
		}

		postService.deletePostByIdAndPassword(postId, postPassword);

		return ApiResponse.builder().message("게시글이 삭제되었습니다.").build();
	}

	/**
	 * 1. ID로 게시글 정보 가져오기
	 * 2. 카테고리 전체 가져오기
	 * 3. ID로 게시글의 파일 정보 가져오기
	 * @param postId: 게시글 ID
	 * @return ApiResponse: ModifyResponse, 메세지 리턴
	 */
	@GetMapping("/api/modify")
	public ApiResponse modify(@RequestParam Long postId) {
		Modify post = modifyService.findPostById(postId);
		List<Category> categories = categoryService.getCategories();
		List<FileMetaData> files = fileService.findFilesByPostId(post.getPostId());

		ModifyResponse modifyResponse = ModifyResponse.builder()
			.post(post)
			.categories(categories)
			.files(files)
			.build();

		return ApiResponse.builder()
			.object(modifyResponse)
			.message("조회 성공")
			.build();
	}

	/**
	 * 1. ID로 게시글 password 가져오고 검증 실패 시 PasswordException
	 * 2. 성공 시 게시글 수정 로직 실행
	 * @param modifyRequest: ID, 카테고리, 작성자, 패스워드, 제목, 내용, 삭제할 파일들
	 * @param files: 업로드한 파일들
	 * @return ApiResponse: 게시글 수정 메세지 리턴
	 */
	@PutMapping("/api/modify")
	public ApiResponse modify(@ModelAttribute @Valid ModifyRequest modifyRequest,
							  @RequestParam(value = "file", required = false) MultipartFile[] files) {

		String password = postService.findPasswordByPostId(modifyRequest.getPostId());

		boolean validPassword = modifyRequest.getPostPassword().equals(password);
		if (!validPassword) {
			throw new PasswordException("비밀번호가 일치하지 않습니다.");
		}

		modifyService.modifyPost(modifyRequest.toVo(), files);

		return ApiResponse.builder().message("게시글이 수정되었습니다.").build();
	}

	/**
	 * 1. 전체 카테고리 가져옴
	 * @return List<Category>: 전체 카테고리 목록 리턴
	 */
	@GetMapping("/api/write")
	public List<Category> write(){
		return categoryService.getCategories();
	}

	/**
	 * 1. 사용자가 입력한 패스워드와 확인용 패스워드가 일치하는지 검증 후 실패하면 PasswordException
	 * 2. 성공하면 게시글 저장 로직 실행
	 * @param writeRequest: ID, 카테고리, 작성자, 패스워드, 확인용 패스워드, 제목, 내용
	 * @param file: 업로드한 파일
	 * @return ApiResponse: 게시글 등록 메세지 리턴
	 */
	@PostMapping("/api/write")
	public ApiResponse write(@ModelAttribute @Valid WriteRequest writeRequest,
						  	 @RequestParam("file")MultipartFile[] file) {
		boolean validPassword = writeRequest.getPostPassword().equals(writeRequest.getConfirmPassword());

		if( !validPassword ) throw new PasswordException("비밀번호가 일치하지 않습니다.");

		postService.savePost(writeRequest.toVo(),file);

		return ApiResponse.builder().message("게시글이 등록되었습니다.").build();
	}
}
