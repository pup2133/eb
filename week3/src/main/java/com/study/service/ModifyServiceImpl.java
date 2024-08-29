package com.study.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.exception.FileException;
import com.study.mapper.ModifyMapper;
import com.study.model.vo.FileMetaData;
import com.study.model.vo.Modify;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModifyServiceImpl implements ModifyService{

	private final ModifyMapper modifyMapper;
	private final FileService fileService;

	/**
	 * 1. store, Db 파일 삭제
	 * 2. 업로드한 파일 저장
	 * 3. 게시글 최종 업데이트
	 * @param modify: 게시글 수정 정보
	 * @param requestFiles: 수정 시 삭제할 파일들
	 */
	@Override
	public void modifyPost(Modify modify, MultipartFile[] requestFiles) {
		deleteFilesData(modify);
		saveFilesData(modify.getPostId(), requestFiles);
		modifyMapper.modifyPost(modify);
	}

	/**
	 * 1. store, Db 파일 삭제
	 * @param modify: 게시글 수정 정보
	 */
	private void deleteFilesData(Modify modify){
		String[] removeFiles = modify.getRemoveFiles();
		List<FileMetaData> files = fileService.findFilesByPostId(modify.getPostId());

		if (removeFiles != null && !files.isEmpty()) {
			/* key: fileId */
			Set<Long> postFileIds = files.stream().map(FileMetaData::getFileId).collect(Collectors.toSet());

			/* key: fileId value: storeName */
			Map<Long, String> postFileStoreNames = files.stream()
				.collect(Collectors.toMap(FileMetaData::getFileId, FileMetaData::getStoreName));

			for (String fileId : removeFiles) {

				/* 파일 검증 */
				boolean validFile = Objects.requireNonNull(postFileIds).contains(Long.parseLong(fileId));
				if ( !validFile ) {
					throw new FileException("파일이 일치하지 않습니다.");
				}

				String storeName = postFileStoreNames.get(Long.parseLong(fileId));

				fileService.deleteFileFromStore(storeName);
				modifyMapper.deleteFileById(fileId);
			}
		}
	}

	/**
	 * 1. 업로드한 파일 저장
	 * @param postId: 게시글 ID
	 * @param requestFiles: 사용자가 업로드한 파일들
	 */
	private void saveFilesData(Long postId, MultipartFile[] requestFiles){
		if (requestFiles != null) {
			fileService.saveFilesByPostId(postId, requestFiles);
		}
	}

	/**
	 * 1. 게시글 ID로 게시글 정보 가져오기
	 * @param postId: 게시글 ID
	 * @return Modify: 게시글 정보
	 */
	@Override
	public Modify findPostById(Long postId) {
		return modifyMapper.findPostById(postId);
	}
}
