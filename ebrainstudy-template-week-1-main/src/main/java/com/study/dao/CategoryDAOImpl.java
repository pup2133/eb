package com.study.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.study.dto.Category;

public class CategoryDAOImpl implements CategoryDAO {

	private final DataSource dataSource;

	public CategoryDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private void close(Connection con, PreparedStatement stmt, ResultSet rs) throws SQLException {
		if(rs!=null) rs.close();
		if(stmt!=null) stmt.close();
		if(con!=null) con.close();
	}

	// 카테고리 목록 가져오기
	@Override
	public List<Category> getCategory() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Category> list = new ArrayList<>();

		try {
			con = dataSource.getConnection();

			String sql = "SELECT * FROM category";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				long categoryId = rs.getInt(1);
				String category = rs.getString(2);

				list.add(new Category(categoryId, category));
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
		return list;
	}
}
