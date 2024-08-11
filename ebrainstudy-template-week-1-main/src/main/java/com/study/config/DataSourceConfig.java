package com.study.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DataSourceConfig implements ServletContextListener {

	private static HikariDataSource dataSource;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3308/ebrainsoft_study"); // 데이터베이스 URL
		config.setUsername("ebsoft"); // 데이터베이스 사용자 이름
		config.setPassword("ebsoft"); // 데이터베이스 비밀번호
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.setMaximumPoolSize(10); // 최대 커넥션 풀 크기
		config.setConnectionTimeout(30000);
		config.setIdleTimeout(600000);
		config.setMaxLifetime(1800000);
		dataSource = new HikariDataSource(config);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (dataSource != null) {
			dataSource.close();
		}
	}

	public static DataSource getDataSource() {
		return dataSource;
	}

}
