package com.study.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.dto.FileMetaData;
import com.study.mapper.FileMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{

	private static final String PATH = "/Users/yun/Desktop/store/";

	private final FileMapper fileMapper;

	/* 로컬 스토어에 저장할 파일 이름을 UUID로 바꿔서 저장 */
	private String getEncrypt(String fileName){
		Integer index = fileName.lastIndexOf('.');
		String extension = fileName.substring(index);

		UUID uuid = UUID.randomUUID();
		String storeName = uuid + extension;

		return storeName;
	}

	/*
	*
	*
	*/
	@Override
	public void saveFile(Long postId, MultipartFile[] file) {
		try {
			for (MultipartFile multipartFile : file) {
				if (!multipartFile.isEmpty()) {
					String filName = multipartFile.getOriginalFilename();
					String storeName = getEncrypt(filName);
					String uploadPath = (PATH + storeName);

					FileMetaData fileMetaData = FileMetaData.builder()
						.fileName(filName)
						.storeName(storeName)
						.filePath(PATH)
						.build();

					transferData(multipartFile.getInputStream(), new FileOutputStream(uploadPath));

					fileMapper.saveFile(postId,fileMetaData);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/* postId값을 가진 포스트에 저장된 파일들 가져오기 */
	@Override
	public List<FileMetaData> getFiles(Long postId) {
		return fileMapper.getFiles(postId);
	}

	/* fileId 값을 가진 파일 정보 가져오기 */
	@Override
	public FileMetaData getFile(Long fileId) {
		return fileMapper.getFile(fileId);
	}

	/* 로컬 스토어에 저장되어 있는 storeName 파일 삭제*/
	@Override
	public void removeFileFromStore(String storeName){
		try {
			Path path = Paths.get(PATH+storeName);
			Files.deleteIfExists(path);
		} catch (IOException e) {
			throw new IllegalStateException("파일 삭제 실패");
		}
	}

	/* InputStream과 OutPutStream을 받아 파일 읽기 */
	// resource
	@Override
	public void transferData(InputStream inputStream, OutputStream outputStream){
		try (inputStream; outputStream) {
			byte[] buffer = new byte[4096];

			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.flush();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
