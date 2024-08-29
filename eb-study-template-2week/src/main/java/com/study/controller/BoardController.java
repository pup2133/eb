package com.study.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.study.dto.Category;
import com.study.dto.Comment;
import com.study.dto.FileMetaData;
import com.study.dto.Post;
import com.study.dto.Posts;
import com.study.dto.Search;
import com.study.dto.request.CommentRequestDto;
import com.study.dto.request.PostRequestDto;
import com.study.dto.response.CommentResponseDto;
import com.study.service.CategoryService;
import com.study.service.CommentService;
import com.study.service.FileService;
import com.study.service.ModifyService;
import com.study.service.PostService;
import com.study.util.Page;
import com.study.util.Pagination;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/free/*")
public class BoardController {

	private final CategoryService categoryService;
	private final CommentService commentService;
	private final FileService fileService;
	private final ModifyService modifyService;
	private final PostService postService;
	private final Pagination pagination;

	/* 목록 */

	/*
	* @ModelAttribute로 받아온 params로 search객체 생성
	* search 객체를 getPostList()에 넘기고
	* 페이징을 위해 pagination()에 post.getCount()와, search.getPage() 넘김
	* posts.getList()가 비어있을 경우 검색 조건에 맞는 포스트들이 없으니
	* model에 message : 검색되 게시물이 없습니다. 추가
	* 있을경우 pages와, posts.getList() 모델에 추가
	* 공통적으로는 검색에 쓸 전체 카테고리 목록과, posts.getCount()를 모델에 추가
	*/
	@GetMapping("/list")
	public String list(@ModelAttribute Search params, Model model){
		Search search = Search.builder()
			.startDate(params.getStartDate())
			.endDate(params.getEndDate())
			.categoryId(params.getCategoryId())
			.searchWord(params.getSearchWord())
			.page(params.getPage())
			.build();

		Posts posts = postService.getPostList(search);
		Page pages = pagination.pagination(posts.getCount(), search.getPage());
		List<Category> category = categoryService.getCategory();

		if(posts.getList().isEmpty()){
			model.addAttribute("message","검색된 게시물이 없습니다.");
		}else {
			model.addAttribute("pages", pages);
			model.addAttribute("list", posts.getList());
		}

		model.addAttribute("category",category);
		model.addAttribute("totalPost",posts.getCount());

		return "list";
	}

	/* 수정 */


	/*
	* 전체 카테고리, postId로 포스트 조회
	* fileCount는 view에서 반복문 돌릴 때 사용
	*/
	@GetMapping("/modify")
	public String modify(@RequestParam("post_id") Long postId, Model model){
		List<Category> category = categoryService.getCategory();
		Post post = modifyService.modifyPostView(postId);

		model.addAttribute("post",post);
		model.addAttribute("category",category);
		model.addAttribute("fileCount",post.getFiles().size());

		return "modify";
	}

	/*
	* modifyPost()에서
	* 비밀번호 불일치, postId의 fileId 불일치 시 IllegalStateException
	* 파일 삭제, 파일 업로드, 포스트 업데이트 실패 시  SQLException
	*/
	@ResponseBody
	@PostMapping("/modify")
	public ResponseEntity<String> modify(@ModelAttribute @Valid PostRequestDto postRequestDto,
										 @RequestParam(value = "file", required = false) MultipartFile[] files,
										 @RequestParam(value = "removeFiles", required = false) String[] removeFiles){
		try {
			Post post = postRequestDto.toEntity();
			modifyService.modifyPost(post, files, removeFiles);
			return new ResponseEntity<>("수정 완료", null, HttpStatus.OK);
		} catch (IllegalStateException e){
			return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
		}
	}

	/* 보기 */

	/*
	* fileId로 fileName, storeName, path 가져 온 후
	* response에 ContentType, Header set
	* writeFile()에 InputStream, OutPutStream 넘겨서 파일 읽기
	*/
	//리소스 정리
	@GetMapping("/view/download")
	public void download(@RequestParam("file") Long fileId, HttpServletResponse response){
		try {
			FileMetaData file = fileService.getFile(fileId);
			if(file!=null){
				String fileName = file.getFileName();
				String storeName = file.getStoreName();
				String path = file.getFilePath();
				String filePath = (path + storeName);

				// UTF-8로 인코딩된 바이트 배열을 8859_1로 디코딩
				fileName = new String(fileName.getBytes("UTF-8"), "8859_1");

				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

				fileService.transferData(new FileInputStream(filePath),response.getOutputStream());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	* updateViews()로 포스트 조회수 +1
	* postId로 찾은 post, comment, files model.add
	*/
	@GetMapping("/view")
	public String view(@RequestParam("post_id") Long postId, Model model){
		postService.modifyViews(postId);

		Post post = postService.getPostById(postId);
		List<Comment> comments = commentService.getCommentByPostId(postId);
		List<FileMetaData> files = fileService.getFiles(postId);

		model.addAttribute("post", post);
		model.addAttribute("comment", comments);
		model.addAttribute("file", files);
		return "view";
	}

	/*
	* 댓글 등록 후 추가된 댓글 가져와서 body에 담아 보냄
	* 댓글 등록 실패 시 SQLException, body에 null, status 500
	*/
	@ResponseBody
	@PostMapping("/view/save-comment")
	public ResponseEntity<CommentResponseDto> View(@RequestBody CommentRequestDto comment){
		Comment entityComment = comment.toEntity();
		Comment resultComment = commentService.saveComment(entityComment);

		if(resultComment==null){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		CommentResponseDto responseDto = resultComment.toResponseDto();
		return ResponseEntity.ok(responseDto);
	}

	/*
	* removePost()에서 파일 삭제 실행
	* 로컬 스토리지에서 파일 삭제 후 포스트 삭제
	* CASCADE로 댓글, 파일 DB에서 한 번에 삭제
	*/
	@ResponseBody
	@DeleteMapping("/view/remove-post")
	private ResponseEntity<String> View(@RequestParam("postId") Long postId, @RequestParam("password") String password){
		try {
			postService.removePost(postId, password);
			return ResponseEntity.ok("삭제 완료");
		}catch (IllegalStateException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	/* 등록 */

	/* 전체 카테고리만 model.add */
	@GetMapping("/write")
	public String write(Model model){
		List<Category> category = categoryService.getCategory();
		model.addAttribute("category", category);
		return "write";
	}

	/*
	* savePost()에서 비밀번호가 일치하지 않으면 IllegalStateException
	* 파일, 포스트 저장 실패 시 SQLException
	*/
	@ResponseBody
	@PostMapping("/write")
	public ResponseEntity<String> write(@ModelAttribute @Valid PostRequestDto postRequestDto, @RequestParam("file")MultipartFile[] file) {
		try {
			if(postRequestDto.getPostPassword().equals(postRequestDto.getConfirmPassword())){
				Post post = postRequestDto.toEntity();
				postService.savePost(post,file);
				return ResponseEntity.ok("등록 완료");
			}else {
				return ResponseEntity.ok("비밀번호가 일치하지 않습니다.");
			}
		} catch (IllegalStateException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

}
