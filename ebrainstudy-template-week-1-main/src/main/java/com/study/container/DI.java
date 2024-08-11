package com.study.container;

import javax.sql.DataSource;

import com.study.config.DataSourceConfig;
import com.study.dao.CategoryDAO;
import com.study.dao.CategoryDAOImpl;
import com.study.dao.CommentDAO;
import com.study.dao.CommentDAOImpl;
import com.study.dao.FileDAO;
import com.study.dao.FileDAOImpl;
import com.study.dao.ModifyDAO;
import com.study.dao.ModifyDAOImpl;
import com.study.dao.PostDAO;
import com.study.dao.PostDAOImpl;
import com.study.service.CategoryServiceImpl;
import com.study.service.CommentServiceImpl;
import com.study.service.FileServiceImpl;
import com.study.service.ListServiceImpl;
import com.study.service.ModifyServiceImpl;
import com.study.service.ViewServiceImpl;
import com.study.service.WriterServiceImpl;

public class DI {
	private static DataSource dataSource;

	static {
		try {
			dataSource = DataSourceConfig.getDataSource();
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize DataSource", e);
		}
	}

	private static final ModifyDAO modifyDAO = new ModifyDAOImpl(dataSource);
	private static final FileDAO fileDAO = new FileDAOImpl(dataSource);
	private static final CategoryDAO categoryDAO = new CategoryDAOImpl(dataSource);
	private static final CommentDAO commentDAO  = new CommentDAOImpl(dataSource);
	private static final PostDAO postDAO = new PostDAOImpl(dataSource);

	private static final FileServiceImpl fileService = new FileServiceImpl(fileDAO);
	private static final ModifyServiceImpl modifyService = new ModifyServiceImpl(modifyDAO);
	private static final CommentServiceImpl commentService = new CommentServiceImpl(commentDAO);
	private static final ListServiceImpl listService = new ListServiceImpl(postDAO);
	private static final ViewServiceImpl viewService = new ViewServiceImpl(postDAO);
	private static final WriterServiceImpl writerService = new WriterServiceImpl(postDAO);
	private static final CategoryServiceImpl categoryService = new CategoryServiceImpl(categoryDAO);

	public static ModifyServiceImpl getModifyService() {
		return modifyService;
	}
	public static FileServiceImpl getFileService(){
		return fileService;
	}
	public static CommentServiceImpl getCommentService(){
		return commentService;
	}
	public static ListServiceImpl getListService(){
		return listService;
	}
	public static ViewServiceImpl getViewService(){
		return viewService;
	}
	public static WriterServiceImpl getWriterService(){
		return writerService;
	}
	public static CategoryServiceImpl getCategoryService(){
		return categoryService;
	}

}
