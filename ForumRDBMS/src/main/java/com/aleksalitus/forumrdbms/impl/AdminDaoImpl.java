package com.aleksalitus.forumrdbms.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aleksalitus.forumrdbms.dao.AdminDao;
import com.aleksalitus.forumrdbms.dao.Dao;
import com.aleksalitus.forumrdbms.entity.Admin;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoImpl;
import com.aleksalitus.forumrdbms.util.JdbcUtils;

public class AdminDaoImpl extends DaoImpl<Admin> implements
		Dao<Admin>, AdminDao {

	private static final String SQL_SELECT_ALL = "SELECT * FROM Admins";
	private static final String SQL_DELETE = "DELETE FROM Admins WHERE login = ? AND password = ?";
	private static final String SQL_INSERT = "INSERT INTO Admins (login, password) VALUES (?,?)";
	private static final String SQL_SELECT_BY_LOGIN = "SELECT * FROM Admins where login like '%" + VALUE_FOR_PREP_ST + "%'";
	
	
	AdminDaoImpl(){
		//NOP
	}

	/**
	 * @return list contains all admins from table 'admins'; empty list if nothig found
	 * @see com.aleksalitus.forumrdbms.dao.Dao#selectAll()
	 */
	@Override
	public List<Admin> selectAll() throws DBSystemException {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			resSet = connection.prepareStatement(SQL_SELECT_ALL).executeQuery();
			
			List<Admin> result = new ArrayList<>(0);
			while (resSet.next()) {
				Admin admin = new Admin(
						resSet.getString(resSet.getMetaData().getColumnName(1)), 
						resSet.getString(resSet.getMetaData().getColumnName(2))
						);
				result.add(admin);
			}
			connection.commit();
			return result;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_SELECT_ALL);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}
	
	/**
	 * Inserts object into table 'admins'
	 * @see com.aleksalitus.forumrdbms.dao.Dao#insert(java.lang.Object)
	 */
	@Override
	public void insert(Admin admin) throws DBSystemException,
			NotUniquePropertyException {
		
		// check if login is unique
		for (Admin currentAdmin : selectByLogin(admin.getLogin())) {
			if (admin.getLogin().equals(currentAdmin.getLogin()))
				throw new NotUniquePropertyException(
						"Admin with such login already exists: " + admin.getLogin());
		}
		
		//login is unique, do logics
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe, slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_INSERT);
			statement.setString(1, admin.getLogin());
			statement.setString(2, admin.getPassword());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_INSERT
					+ ", login: " + admin.getLogin() + ", password: "
					+ admin.getPassword());
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}

	
	/**
	 * Deletes an object from table 'admins'
	 * @see com.aleksalitus.forumrdbms.dao.Dao#delete(java.lang.Object)
	 */
	@Override
	public void delete(Admin admin) throws DBSystemException {
		
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_DELETE);
			statement.setString(1, admin.getLogin());
			statement.setString(2, admin.getPassword());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_DELETE
					+ ", login: " + admin.getLogin() + ", password: "
					+ admin.getPassword());
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}


	/**
	 * @param containedInLogin string contained in admin's login
	 * @return list contains admins with the login similar containedInLogin parameter (empty list if nothing found)
	 * @see com.aleksalitus.forumrdbms.dao.AdminDao#selectByLogin(java.lang.String)
	 */
	@Override
	public List<Admin> selectByLogin(String containedInLogin) throws DBSystemException {
		
		// check argument
		if (containedInLogin.isEmpty() || containedInLogin.replaceAll(" ", "").isEmpty()){
			throw new IllegalArgumentException("argument is empty");
		}

		//do logics
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			
			statement = connection.prepareStatement(
					SQL_SELECT_BY_LOGIN.replace(VALUE_FOR_PREP_ST, containedInLogin));
			resSet = statement.executeQuery();
			
			List<Admin> result = new ArrayList<>(0);
			while (resSet.next()) {
				Admin admin = new Admin(
						resSet.getString(resSet.getMetaData().getColumnName(1)), 
						resSet.getString(resSet.getMetaData().getColumnName(2))
						);
				result.add(admin);
			}
			connection.commit();
			return result;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_SELECT_BY_LOGIN);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}
}
