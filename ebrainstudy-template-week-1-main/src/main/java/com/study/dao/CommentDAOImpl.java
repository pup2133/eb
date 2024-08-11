package com.study.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.study.dto.Comment;

public class CommentDAOImpl implements CommentDAO {

	private final DataSource dataSource;

	public CommentDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private void close(Connection con, PreparedStatement stmt, ResultSet rs) throws SQLException {
		if(rs!=null) rs.close();
		if(stmt!=null) stmt.close();
		if(con!=null) con.close();
	}

	@Override
	public void addComment(String postId, Comment comment) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;

		try{
			con = dataSource.getConnection();
			String sql = "INSERT INTO comment(writer, content, created_date, post_id) value(?,?,CURRENT_TIMESTAMP,?)";
			stmt = con.prepareStatement(sql);

			stmt.setString(1, comment.getWriter());
			stmt.setString(2, comment.getContent());
			stmt.setString(3, postId);
			int updateResult = stmt.executeUpdate();

			if(updateResult==0) throw new SQLException("댓글 추가 실패");

		} finally {
			close(con,stmt,null);
		}

	}
}
