package com.aleksalitus.forumrdbms.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aleksalitus.forumrdbms.dao.UserDao;
import com.aleksalitus.forumrdbms.entity.User;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoImpl;
import com.aleksalitus.forumrdbms.util.JdbcUtils;

public class UserDaoImpl extends DaoImpl<User> implements UserDao {

	private static final String SQL_SELECT_ALL = "SELECT * FROM Users";
	private static final String SQL_DELETE = "DELETE FROM Users WHERE user_id = ? AND user_name = ? AND email = ? AND password = ?";
	private static final String SQL_INSERT = "INSERT INTO Users (user_name, email, password) VALUES (?,?,?)";
	private static final String SQL_SELECT_BY_USER_ID = "SELECT * FROM Users WHERE user_id = ?";
	private static final String SQL_SELECT_BY_USER_NAME = "SELECT * FROM Users where user_name like '%" + VALUE_FOR_PREP_ST + "%'";
	private static final String SQL_SELECT_BY_USER_EMAIL = "SELECT * FROM Users where email like '%" + VALUE_FOR_PREP_ST + "%'";

	
	UserDaoImpl(){
		//NOP
	}
	
	
	@Override
	public List<User> selectAll() throws DBSystemException {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_SELECT_ALL);
			resSet = statement.executeQuery();

			List<User> result = new ArrayList<>();
			while (resSet.next()) {
				User user = new User(
						resSet.getInt(resSet.getMetaData().getColumnName(1)),
						resSet.getString(resSet.getMetaData().getColumnName(2)),
						resSet.getString(resSet.getMetaData().getColumnName(3)),
						resSet.getString(resSet.getMetaData().getColumnName(4)));
				result.add(user);
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

	@Override
	public void insert(User user) throws DBSystemException,
			NotUniquePropertyException {
		
		// check if name and email are unique
		Iterator<User> iterator = selectAll().iterator();
		while (iterator.hasNext()) {
			User currentUser = (User) iterator.next();
			if (user.getName().equals(currentUser.getName()))
				throw new NotUniquePropertyException(
						"User with such name already exists: " + user.getName());
			if (user.getEmail().equals(currentUser.getEmail()))
				throw new NotUniquePropertyException(
						"User with such email already exists: "
								+ user.getEmail());
		}
		
		//unique user, insert
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_INSERT);
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_INSERT
					+ ", userId: " + user.getId() + ", name: " + user.getName());
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}

	}

	
	//TODO delete also user's messages and topics
	@Override
	public void delete(User user) throws DBSystemException {
		
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_DELETE);
			statement.setInt(1, user.getId());
			statement.setString(2, user.getName());
			statement.setString(3, user.getEmail());
			statement.setString(4, user.getPassword());
			statement.executeUpdate();
			connection.commit();
			
			DaoFactory.getInstance().getMessageDao().deleteMessagesByAuthorId(user.getId());
			//DaoFactory.getInstance().getTopicsDao().deleteTopicsByAuthorId(user.getId());
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_DELETE
					+ ", id: " + user.getId() + ", name: "
					+ user.getName());
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}

	
	@Override
	public User selectByUserId(int id) throws DBSystemException {
		
		if (id < 1){
			throw new IllegalArgumentException("id<1");
		}

		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_SELECT_BY_USER_ID);
			statement.setInt(1, id);
			resSet = statement.executeQuery();

			if (resSet.next()) {
				User user = new User(
						resSet.getInt(resSet.getMetaData().getColumnName(1)), 
						resSet.getString(resSet.getMetaData().getColumnName(2)),
						resSet.getString(resSet.getMetaData().getColumnName(3)),
						resSet.getString(resSet.getMetaData().getColumnName(4))
						);
				return user;
			}
			connection.commit();
			
			//not found
			return null;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_SELECT_BY_USER_ID);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}

	
	@Override
	public List<User> selectByUserName(String name) throws DBSystemException {
		
		if (name.isEmpty() || name.replace(" ", "").isEmpty()){
			throw new IllegalArgumentException("argument 'name' is empty");
		}
		
		return selectBy(name, SQL_SELECT_BY_USER_NAME);
	}

	@Override
	public List<User> selectByUserEmail(String email) throws DBSystemException {
		
		if (email.isEmpty() || email.replace(" ", "").isEmpty()){
			throw new IllegalArgumentException("argument 'email' is empty");
		}
		
		return selectBy(email, SQL_SELECT_BY_USER_EMAIL);
	}
	
	
	
	private List<User> selectBy(String criterion, String sqlQuery) throws DBSystemException{
		
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(sqlQuery.replace(VALUE_FOR_PREP_ST, criterion));
			resSet = statement.executeQuery();

			List<User> result = new ArrayList<>(0);
			while (resSet.next()) {
				User user = new User(
						resSet.getInt(resSet.getMetaData().getColumnName(1)), 
						resSet.getString(resSet.getMetaData().getColumnName(2)),
						resSet.getString(resSet.getMetaData().getColumnName(3)),
						resSet.getString(resSet.getMetaData().getColumnName(4))
						);
				result.add(user);
			}
			connection.commit();
			return result;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + sqlQuery);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}
}
