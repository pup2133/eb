package com.study.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.study.dto.Files;

public class FileDAOImpl implements FileDAO{

	private final DataSource dataSource;

	public FileDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private void close(Connection con, PreparedStatement stmt, ResultSet rs) throws SQLException {
		if(rs!=null) rs.close();
		if(stmt!=null) stmt.close();
		if(con!=null) con.close();
	}

	// service 계층에서 connection 관리
	@Override
	public int saveFile(Files file){
		int result = 0;

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = dataSource.getConnection();

			String sql = "INSERT INTO file(file_name, store_name, path, post_id) value(?,?,?,?)";

			stmt = con.prepareStatement(sql);
			stmt.setString(1,file.getFileName());
			stmt.setString(2,file.getStoreName());
			stmt.setString(3,file.getPath());
			stmt.setLong(4,file.getPostId());
			result = stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally {
			try {
				close(con,stmt,null);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	// select 결과를 String[]에 담아 return
	// file[0] => file_name, file[1] => store.name, file[2] => path
	@Override
	public String[] getFile(int fileId){
		String[] file = new String[3];

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = dataSource.getConnection();

			String sql = "SELECT file_name, store_name, path FROM file WHERE file_id = ?";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1,fileId);
			rs = stmt.executeQuery();

			if(rs.next()){
				file[0] = rs.getString(1);
				file[1] = rs.getString(2);
				file[2] = rs.getString(3);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				close(con, stmt, rs);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return file;
	}
}
