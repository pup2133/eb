package com.study.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.study.dto.Files;
import com.study.dto.Post;

public class ModifyDAOImpl implements ModifyDAO{

	private final DataSource dataSource;

	public ModifyDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private void close(Connection con, PreparedStatement stmt, ResultSet rs) throws SQLException {
		if(rs!=null) rs.close();
		if(stmt!=null) stmt.close();
		if(con!=null) con.close();
	}

	// post_id 와 일치하는 row에 password를 가져옴
	@Override
	public String getPassword(long id){
		String password = "";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = dataSource.getConnection();

			String sql = "SELECT password from post where post_id = ?";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);

			rs = stmt.executeQuery();
			if(rs.next()) {
				password += rs.getString(1);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				close(con,stmt,rs);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		return password;
	}

	// file_id가 일치하는 파일 삭제
	@Override
	public int deleteFile(String fileId){
		int updateResult = 0;

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = dataSource.getConnection();

			String sql = "DELETE FROM file WHERE file_id = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1,fileId);
			updateResult = stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				close(con, stmt,null);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}

		return updateResult;
	}

	@Override
	public int updatePost(Post post){
		int updateResult = 0;

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dataSource.getConnection();

			StringBuilder sql = new StringBuilder();

			sql.append("UPDATE post")
				.append(" SET category_id = ?, revision_date = CURRENT_TIMESTAMP, writer = ?, title = ?, content = ?")
				.append(" WHERE post_id = ?");

			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, post.getCategoryId());
			stmt.setString(2, post.getWriter());
			stmt.setString(3, post.getTitle());
			stmt.setString(4, post.getContent());
			stmt.setString(5, String.valueOf(post.getPostId()));
			updateResult = stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				close(con,stmt,null);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}
		return updateResult;
	}

	// 단건일 경우는 따로 가져와도 좋음
	@Override
	public Post modifyPostView(Post post){
		Post result = null;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			List<Files> files = new ArrayList<>();

			con = dataSource.getConnection();

			String fileSql = "SELECT * FROM file WHERE post_id = ?";

			stmt = con.prepareStatement(fileSql);
			stmt.setLong(1, post.getPostId());

			rs = stmt.executeQuery();
			while (rs.next()){
				files.add(
					new Files(
						rs.getLong(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4)
					)
				);
			}
			close(null,stmt,rs);

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT p.post_id, p.writer, p.title, p.content, p.views,")
				.append(" DATE_FORMAT(p.created_date, '%Y-%m-%d %H:%i'),")
				.append(" IFNULL(DATE_FORMAT(p.revision_date, '%Y-%m-%d %H:%i'), '-'), c.category, c.category_id")
				.append(" FROM post p JOIN category c ON p.category_id = c.category_id")
				.append(" WHERE p.post_id = ?");

			stmt = con.prepareStatement(sql.toString());
			stmt.setLong(1,post.getPostId());
			rs = stmt.executeQuery();

			if(rs.next()){
				result = new Post(
					rs.getLong(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4),
					rs.getInt(5),
					rs.getString(6),
					rs.getString(7),
					rs.getString(8),
					rs.getString(9),
					files
				);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				close(con, stmt, rs);
				return result;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
