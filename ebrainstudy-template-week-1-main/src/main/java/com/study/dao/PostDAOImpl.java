package com.study.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.study.dto.Comment;
import com.study.dto.Files;
import com.study.dto.Post;
import com.study.dto.Posts;
import com.study.dto.Search;

public class PostDAOImpl implements PostDAO{

	private final DataSource dataSource;

	public PostDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private void close(Connection con, PreparedStatement stmt, ResultSet rs) throws SQLException {
		if (rs != null) rs.close();
		if (stmt != null) stmt.close();
		if (con != null) con.close();
	}

	@Override
	public Post getPostById(String postId) {
		Post post = null;
		List<Comment> comments = new ArrayList<>();
		List<Files> files = new ArrayList<>();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = dataSource.getConnection();

			StringBuilder sql = new StringBuilder();

			sql.append(" SELECT comment_id, writer, content, DATE_FORMAT(created_date, '%Y-%m-%d %H:%i')")
				.append(" FROM comment")
				.append(" WHERE post_id = ?");

			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, postId);

			rs = stmt.executeQuery();
			while (rs.next()){
				comments.add(
					new Comment(
						rs.getLong(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4)
					)
				);
			}
			close(null, stmt, rs);

			sql.setLength(0);
			sql.append(" SELECT *")
				.append(" FROM file")
				.append(" WHERE post_id = ?");

			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, postId);

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
			close(null, stmt, rs);

			sql.setLength(0);
			sql.append("SELECT p.post_id, p.writer, p.title, p.content, p.views,")
				.append(" DATE_FORMAT(p.created_date, '%Y-%m-%d %H:%i'),")
				.append(" IFNULL(DATE_FORMAT(p.revision_date, '%Y-%m-%d %H:%i'), '-'),")
				.append(" c.category")
				.append(" FROM post p JOIN category c")
				.append(" ON p.category_id = c.category_id")
				.append(" WHERE p.post_id = ?");

			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, postId);

			rs = stmt.executeQuery();
			if (rs.next()) {
				post = new Post(
					rs.getLong(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4),
					rs.getInt(5),
					rs.getString(6),
					rs.getString(7),
					rs.getString(8),
					comments,
					files
				);
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
		return post;
	}

	// 10개씩 리스트, 리스트 총 개수 가져오기
	@Override
	public Posts getList(int offset) {
		List<Post> list = new ArrayList<>();
		int count = 0;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = dataSource.getConnection();
			StringBuilder sql = new StringBuilder();

			sql.append("SELECT p.post_id, p.writer, p.title, p.views,")
				.append(" DATE_FORMAT(p.created_date, '%Y-%m-%d %H:%i'), IFNULL(DATE_FORMAT(p.revision_date, '%Y-%m-%d %H:%i'), '-'),")
				.append(" c.category, COUNT(f.file_id)")
				.append(" FROM post p")
				.append(" JOIN category c ON p.category_id = c.category_id")
				.append(" LEFT JOIN file f ON p.post_id = f.post_id")
				.append(" GROUP BY p.post_id, p.writer, p.title, p.views, p.created_date, p.revision_date, c.category")
				.append(" ORDER BY p.post_id DESC")
				.append(" LIMIT 10 OFFSET ?");

			stmt = con.prepareStatement(sql.toString());

			stmt.setInt(1, offset);
			rs = stmt.executeQuery();

			while (rs.next()) {
				long postId = rs.getInt(1);
				String writer = rs.getString(2);
				String title = rs.getString(3);
				int views = rs.getInt(4);
				String createdDate = rs.getString(5);
				String revisionDate = rs.getString(6);
				String category = rs.getString(7);
				int file = rs.getInt(8);

				list.add(new Post(postId, category, title, writer, views, createdDate, revisionDate,file));
			}

			close(null,stmt,rs);

			String countSql = "SELECT COUNT(*) FROM post";
			stmt = con.prepareStatement(countSql);

			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				close(con, stmt, rs);
				return new Posts(list, count);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	// post_id랑 password가 일치하는 게시글이면 삭제
	// 일치하지 않으면 false return해서 controller에서 respone 처리함
	@Override
	public boolean deletePost(String postId, String password) {
		int updateResult = 0;

		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = dataSource.getConnection();

			String sql = "DELETE FROM post WHERE post_id = ? and password = ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, postId);
			stmt.setString(2, password);

			updateResult = stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				close(con, stmt, null);

				if (updateResult == 1) return true;
				else return false;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	// 조회수 업데이트
	@Override
	public void updateViews(String postId) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = dataSource.getConnection();

			String sql = "UPDATE post SET views = views + 1 WHERE post_id = ?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, postId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				close(con, stmt, null);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}
	}

	@Override
	public long savePost(Post post) {
		long postId = 0;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = dataSource.getConnection();

			String sql = "INSERT INTO post(writer, password, title, content, views, created_date, category_id) value(?,?,?,?,?,CURRENT_TIMESTAMP,?)";

			stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, post.getWriter());
			stmt.setString(2, post.getPassword());
			stmt.setString(3, post.getTitle());
			stmt.setString(4, post.getContent());
			stmt.setInt(5, 0);
			stmt.setString(6, post.getCategoryId());
			int result = stmt.executeUpdate();

			rs = stmt.getGeneratedKeys();

			if (rs.next() && result == 1) {
				postId = rs.getLong(1);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				close(con, stmt, rs);
				return postId;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	//검색기능
	@Override
	public Posts getSearchPost(Search search, int offset) {
		List<Post> list = new ArrayList<>();
		int count = 0;

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = dataSource.getConnection();

			StringBuilder sql = new StringBuilder();

			sql.append("SELECT p.post_id, p.writer, p.title, p.views,")
				.append(" DATE_FORMAT(p.created_date, '%Y-%m-%d %H:%i'), IFNULL(DATE_FORMAT(p.revision_date, '%Y-%m-%d %H:%i'), '-'),")
				.append(" c.category, COUNT(f.file_id)")
				.append(" FROM post p JOIN category c ON p.category_id = c.category_id")
				.append(" LEFT JOIN file f ON p.post_id = f.post_id")
				.append(" WHERE p.created_date BETWEEN ? AND ?")
				.append(" AND p.category_id = ?")
				.append(" AND (p.writer LIKE ? OR p.title LIKE ? OR p.content LIKE ?)")
				.append(" GROUP BY p.post_id, p.writer, p.title, p.views, p.created_date, p.revision_date, c.category")
				.append(" ORDER BY p.post_id DESC")
				.append(" LIMIT 10 OFFSET ?");

			stmt = con.prepareStatement(sql.toString());
			stmt.setString(1, search.getStartDate());
			stmt.setString(2, search.getEndDate());
			stmt.setInt(3, Integer.parseInt(search.getCategoryId()));
			stmt.setString(4, "%" + search.getSearchWord() + "%");
			stmt.setString(5, "%" + search.getSearchWord() + "%");
			stmt.setString(6, "%" + search.getSearchWord() + "%");
			stmt.setInt(7, offset);

			rs = stmt.executeQuery();

			while (rs.next()) {
				long postId = rs.getInt(1);
				String writer = rs.getString(2);
				String title = rs.getString(3);
				int views = rs.getInt(4);
				String createdDate = rs.getString(5);
				String revisionDate = rs.getString(6);
				String category = rs.getString(7);
				int file = rs.getInt(8);

				list.add(new Post(postId, category, title, writer, views, createdDate, revisionDate, file));
			}
			close(null, stmt, rs);

			sql.setLength(0);
			sql.append("SELECT COUNT(*)")
				.append(" FROM post p JOIN category c ON p.category_id = c.category_id")
				.append(" WHERE p.created_date BETWEEN ? AND ?")
				.append(" AND p.category_id = ?")
				.append(" AND (p.writer LIKE ? OR p.title LIKE ? OR p.content LIKE ?)")
				.append(" ORDER BY p.post_id DESC");

			stmt = con.prepareStatement(sql.toString());

			stmt.setString(1, search.getStartDate());
			stmt.setString(2, search.getEndDate());
			stmt.setInt(3, Integer.parseInt(search.getCategoryId()));
			stmt.setString(4, "%" + search.getSearchWord() + "%");
			stmt.setString(5, "%" + search.getSearchWord() + "%");
			stmt.setString(6, "%" + search.getSearchWord() + "%");
			rs = stmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				close(con, stmt, rs);
				return new Posts(list, count);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
