package com.study.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import com.study.dao.FileDAO;
import com.study.dto.Files;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class FileServiceImpl implements FileService{

	private static final String PATH = "/Users/yun/Desktop/store/";

	private final FileDAO fileDAO;

	public FileServiceImpl(FileDAO fileDAO) {
		this.fileDAO = fileDAO;
	}

	// 파일 이름 바꾸기
	private String getEncrypt(String fileName){
		String storeName = "";
		int index = fileName.lastIndexOf('.');
		String extension = fileName.substring(index);

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(fileName.getBytes());
			byte[] store = md.digest();

			StringBuffer sb = new StringBuffer();

			for (byte s : store) {
				sb.append(String.format("%02x", s));
			}

			sb.append(extension);

			storeName = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		return storeName;
	}

	// 파일 저장
	@Override
	public boolean saveFile(long postId, HttpServletRequest request) {
		try {
			Collection<Part> parts = request.getParts();
			for (Part part : parts) {
				if (part.getSubmittedFileName() != null && part.getSize() > 0) {

					String fileName = part.getSubmittedFileName();
					String storeName = getEncrypt(fileName);// file_name -> store_name
					String uploadPath = (PATH + storeName);

					fileDAO.saveFile(new Files(fileName, storeName, PATH, postId));

					try (InputStream inputStream = part.getInputStream();
						 OutputStream outputStream = new FileOutputStream(uploadPath)) {

						byte[] buffer = new byte[4096];
						int bytesRead;

						while ((bytesRead = inputStream.read(buffer)) != -1) {
							outputStream.write(buffer, 0, bytesRead);
						}

						outputStream.flush();
					} catch (IOException e) {
						// 예외 처리 코드
						e.printStackTrace();
					}
				}
			}
			return true;
		} catch (ServletException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 파일 다운로드
	@Override
	public void fileDownload(HttpServletResponse response, String fileId) {
		String[] file;

		try {
			file = fileDAO.getFile(Integer.parseInt(fileId)); // file_name, store_name, path

			String fileName = file[0];
			String storeName = file[1];
			String path = file[2];

			String filePath = (path + storeName); // ~~~/storeName

			// try-with-resources
			try (InputStream inputStream = new FileInputStream(filePath);
				 OutputStream outputStream = response.getOutputStream()) {

				fileName = new String(fileName.getBytes("UTF-8"), "8859_1");

				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

				byte[] buffer = new byte[4096];

				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}