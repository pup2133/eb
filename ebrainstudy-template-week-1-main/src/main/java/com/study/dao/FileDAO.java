package com.study.dao;

import com.study.dto.Files;

public interface FileDAO {
	int saveFile(Files file);
	String[] getFile(int fileId);
}
