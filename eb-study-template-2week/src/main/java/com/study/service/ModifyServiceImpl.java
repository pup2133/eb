package com.study.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.dto.FileMetaData;
import com.study.dto.Post;
import com.study.mapper.ModifyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModifyServiceImpl implements ModifyService{

	private final ModifyMapper modifyMapper;
	private final FileService fileService;

	/*
	* getFiles()로 List<Document>를 가져와
	* fileId의 값을 가진 set과 fileId를 키 값으로 가지고 storeName을 value로 가지는 map을 생성
	* getPassword()로 가져온 비밀번호와 입력받은 비밀번호가 다를 시 IllegalStateException
	* (String)deleteList의 길이가 0일 시 파일 삭제 로직 실행 안함
	* 파일 삭제 로직 실행 deletes에 담긴 fileId가 post가 가진 파일이 맞는지 확인 후 일치하지 않을 시 IllegalStateException
	* 이후 deleteFileFromStore()로 로컬 스토어에 파일 삭제하고 deleteFile()로 DB에서 삭제
	* saveFile(), updateFile() 실패하면 SQLException
	*/
	@Override
	public void modifyPost(Post post, MultipartFile[] files, String[] removeFiles){
		String passwordByPostId = modifyMapper.getPassword(post.getPostId());

		if(!post.getPostPassword().equals(passwordByPostId)) {
			throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
		}

		/* 파일 삭제 */
		if(removeFiles!=null){
			List<FileMetaData> fileMetaDataList = fileService.getFiles(post.getPostId());

			Set<Long> postFileIds = null;
			Map<Long, String> postFileStoreNames = null;

			if(!fileMetaDataList.isEmpty()){
				/* fileId의 값을 가진 set */
				postFileIds = fileMetaDataList.stream().map(FileMetaData::getFileId).collect(Collectors.toSet());
				/* fileId를 키 값으로 가지고 storeName을 value로 가지는 map*/
				postFileStoreNames = fileMetaDataList.stream().collect(Collectors.toMap(FileMetaData::getFileId, FileMetaData::getStoreName));
			}

			for (String removeFile : removeFiles) {
				if(!Objects.requireNonNull(postFileIds).contains(Long.parseLong(removeFile))) {
					throw new IllegalStateException("파일이 일치하지 않습니다.");
				}

				String storeName = postFileStoreNames.get(Long.parseLong(removeFile));

				fileService.removeFileFromStore(storeName);
				modifyMapper.removeFile(removeFile);
			}
		}

		if(files!=null) fileService.saveFile(post.getPostId(), files);

		modifyMapper.modifyPost(post);
	}

	/* postId로 포스트와 파일 가져오고 build 해서 리턴 */
	@Override
	public Post modifyPostView(Long postId) {
		Post post = modifyMapper.modifyPostView(postId);
		List<FileMetaData> file = fileService.getFiles(post.getPostId());
		return Post.builder()
			.postId(post.getPostId())
			.categoryId(post.getCategoryId())
			.categoryName(post.getCategoryName())
			.postCreatedDate(post.getPostCreatedDate())
			.postRevisionDate(post.getPostRevisionDate())
			.postViews(post.getPostViews())
			.postWriter(post.getPostWriter())
			.postTitle(post.getPostTitle())
			.postContent(post.getPostContent())
			.files(file)
			.build();
	}
}
