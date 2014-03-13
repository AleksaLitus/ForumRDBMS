package com.aleksalitus.forumrdbms.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.aleksalitus.forumrdbms.dao.Dao;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.util.JdbcUtils;


public abstract class DaoImpl<BeanType> implements
		Dao<BeanType> {

	protected static final String DB_DRIVER_CLASS_NAME = "org.h2.Driver";
	protected static final String DB_URL_JDBC = "jdbc:h2:~/ForumDataBase";
	protected static final String DB_LOGIN = "Letos";
	protected static final String DB_PASSWORD = "7";
	
	protected static final String VALUE_FOR_PREP_ST = "_value_";

	public Connection getConnection() throws DBSystemException {
		JdbcUtils.initDriver(DB_DRIVER_CLASS_NAME);
		//TODO logging
		try {
			return DriverManager.getConnection(DB_URL_JDBC, DB_LOGIN, DB_PASSWORD);
		} catch (SQLException e) {
			throw new DBSystemException("Can't create connection", e);
		}
	}
}
