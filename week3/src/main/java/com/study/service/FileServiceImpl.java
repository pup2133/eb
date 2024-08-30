package com.study.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.exception.FileException;
import com.study.mapper.FileMapper;
import com.study.model.vo.FileMetaData;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{

	private static final String PATH = "/Users/yun/Desktop/store/";

	private final FileMapper fileMapper;

	/**
	 * 1. 파일의 이름을 받아 UUID로 Store에 저장할 이름 생성
	 * @param fileName: 파일의 이름
	 * @return String: UUID 이용해 생성한 StoreName
	 */
	private String getEncrypt(String fileName){
		Integer index = fileName.lastIndexOf('.');
		String extension = fileName.substring(index);

		UUID storeName = UUID.randomUUID();

		return storeName + extension;
	}

	/**
	 * 1. files 정보로 store, DB에 파일 저장
	 * 2. 저장 실패 시 FileException
	 * @param postId: 게시글 ID
	 * @param files: 사용자가 업로드한 파일들
	 */
	@Override
	public void saveFilesByPostId(Long postId, MultipartFile[] files){
		try {
			for (MultipartFile multipartFile : files) {
				if (!multipartFile.isEmpty()) {
					String filName = multipartFile.getOriginalFilename();
					String storeName = getEncrypt(Objects.requireNonNull(filName));
					String uploadPath = (PATH + storeName);

					FileMetaData fileMetaData = FileMetaData.builder()
						.fileName(filName)
						.storeName(storeName)
						.filePath(PATH)
						.build();

					transferFileData(multipartFile.getInputStream(), new FileOutputStream(uploadPath));
					fileMapper.saveFile(postId, fileMetaData);
				}
			}
		}catch (IOException e) {
			throw new FileException("파일이 저장되지 않았습니다.");
		}
	}

	/**
	 * 1. 파일 ID로 파일 정보 가져오기
	 * @param fileId: 파일의 ID
	 * @return FileMetaData: 파일 ID의 파일 정보
	 */
	@Override
	public FileMetaData findFileById(Long fileId) {
		return fileMapper.findFileById(fileId);
	}

	/**
	 * 1. 게시글 ID로 게시글에 등록된 파일 정보 가져오기
	 * @param postId: 게시글 ID
	 * @return List<FileMetaData>: 게시글에 등록된 파일들
	 */
	@Override
	public List<FileMetaData> findFilesByPostId(Long postId) {
		return fileMapper.findFilesByPostId(postId);
	}

	/**
	 * 1. 로컬 스토어에 저장되어 있는 파일 삭제
	 * 2. 삭제 실패 시 FileException
	 * @param storeName: 파일의 StoreName
	 */
	@Override
	public void deleteFileFromStore(String storeName){
		try {
			Path path = Paths.get(PATH+storeName);
			Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new FileException("파일이 삭제되지 않았습니다.");
		}
	}

	/**
	 * 1. InputStream에서 데이터를 읽어 OutputStream에 저장
	 * 2. 저장 실패 시 FileException
	 * @param inputStream: 데이터의 원본 스트림
	 * @param outputStream: 읽어들인 데이터를 저장할 스트림
	 */
	@Override
	public void transferFileData(InputStream inputStream, OutputStream outputStream){
		try (inputStream; outputStream) {
			byte[] buffer = new byte[4096];

			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.flush();
		} catch (IOException e) {
			throw new FileException("파일이 저장되지 않았습니다.");
		}
	}
}
