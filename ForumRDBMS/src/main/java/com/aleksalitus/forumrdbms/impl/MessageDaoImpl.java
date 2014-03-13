package com.aleksalitus.forumrdbms.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aleksalitus.forumrdbms.dao.MessageDao;
import com.aleksalitus.forumrdbms.entity.Message;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoImpl;
import com.aleksalitus.forumrdbms.util.JdbcUtils;

public class MessageDaoImpl extends DaoImpl<Message> implements MessageDao {

	private static final String SQL_SELECT_ALL = "SELECT * FROM Messages";
	private static final String SQL_DELETE = "DELETE FROM Messages WHERE message_id = ? AND author_id = ? AND topic_id = ?";
	private static final String SQL_DELETE_ALL_BY_TOPIC_ID = "DELETE FROM Messages WHERE message_id = ?";
	private static final String SQL_DELETE_ALL_BY_AUTHOR_ID = "DELETE FROM Messages WHERE author_id = ?";
	private static final String SQL_INSERT = "INSERT INTO Messages (message_text, author_id, topic_id) VALUES (?,?,?)";
	private static final String SQL_SELECT_BY_MESSAGE_ID =  "SELECT * FROM Messages WHERE message_id = ?";
	private static final String SQL_SELECT_BY_AUTHOR_ID = "SELECT * FROM Messages WHERE author_id = ?";
	private static final String SQL_SELECT_BY_TOPIC_ID = "SELECT * FROM Messages WHERE topic_id = ?";
	private static final String SQL_SELECT_BY_PIECE_OF_TEXT = "SELECT * FROM Messages where message_text like '%" + VALUE_FOR_PREP_ST + "%'";
	
	
	MessageDaoImpl(){
		//NOP
	}
	
	@Override
	public List<Message> selectAll() throws DBSystemException {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			resSet = connection.prepareStatement(SQL_SELECT_ALL).executeQuery();

			List<Message> result = new ArrayList<>();
			while (resSet.next()) {
				Message message = new Message(
						resSet.getInt(resSet.getMetaData().getColumnName(1)), 
						resSet.getString(resSet.getMetaData().getColumnName(2)),
						resSet.getInt(resSet.getMetaData().getColumnName(3)),
						resSet.getInt(resSet.getMetaData().getColumnName(4))
						);
				result.add(message);
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
	public void insert(Message message) throws DBSystemException,
			NotUniquePropertyException {
		
		boolean success = false;
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_INSERT);
			statement.setString(1, message.getText());
			statement.setInt(2, message.getAuthorId());
			statement.setInt(3, message.getTopicId());
			statement.executeUpdate();
			connection.commit();
			success = true;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_INSERT
					+ ", messageId: " + message.getId() + ", text: "
					+ message.getText());
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
			if (success) {
				// increment message count of inserted message's topic by 1
				DaoFactory.getInstance().getTopicDao()
						.changeMessagesCountByTopicId(message.getTopicId(), 1);
			}
		}
	}

	
	@Override
	public void delete(Message message) throws DBSystemException {
		
		boolean success = false;
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_DELETE);
			statement.setInt(1, message.getId());
			statement.setInt(2, message.getAuthorId());
			statement.setInt(3, message.getTopicId());
			statement.executeUpdate();
			connection.commit();
			success = true;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_DELETE
					+ ", id: " + message.getId());
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
			if (success) {
				// decrement message count of inserted message's topic by 1
				DaoFactory.getInstance().getTopicDao()
						.changeMessagesCountByTopicId(message.getTopicId(), -1);
			}
		}
	}

	
	@Override
	public Message selectByMessageId(int id) throws DBSystemException {
		
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
			statement = connection.prepareStatement(SQL_SELECT_BY_MESSAGE_ID);
			statement.setInt(1, id);
			resSet = statement.executeQuery();

			if (resSet.next()) {
				Message message = new Message(
						resSet.getInt(resSet.getMetaData().getColumnName(1)), 
						resSet.getString(resSet.getMetaData().getColumnName(2)),
						resSet.getInt(resSet.getMetaData().getColumnName(3)),
						resSet.getInt(resSet.getMetaData().getColumnName(4))
						);
				return message;
			}
			connection.commit();
			
			//not found
			return null;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_SELECT_BY_MESSAGE_ID);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}

	
	@Override
	public List<Message> selectByAuthorId(int id) throws DBSystemException {

		// check argument
		if (id < 1) {
			throw new IllegalArgumentException("id<1");
		}

		// select
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_SELECT_BY_AUTHOR_ID);
			statement.setInt(1, id);
			resSet = statement.executeQuery();

			List<Message> messages = new ArrayList<>(0);
			while (resSet.next()) {
				Message message = new Message(
						resSet.getInt(resSet.getMetaData().getColumnName(1)), 
						resSet.getString(resSet.getMetaData().getColumnName(2)),
						resSet.getInt(resSet.getMetaData().getColumnName(3)),
						resSet.getInt(resSet.getMetaData().getColumnName(4))
						);
				messages.add(message);
			}

			connection.commit();
			return messages;

		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = "
					+ SQL_SELECT_BY_AUTHOR_ID + ": " + id);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}
	
	@Override
	public List<Message> selectByTopicId(int id) throws DBSystemException {

		//check argument
		if (id < 1){
			throw new IllegalArgumentException("id<1");
		}
		// select
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_SELECT_BY_TOPIC_ID);
			statement.setInt(1, id);
			resSet = statement.executeQuery();

			List<Message> messages = new ArrayList<>(0);
			while (resSet.next()) {
				Message message = new Message(
						resSet.getInt(resSet.getMetaData().getColumnName(1)),
						resSet.getString(resSet.getMetaData().getColumnName(2)),
						resSet.getInt(resSet.getMetaData().getColumnName(3)),
						resSet.getInt(resSet.getMetaData().getColumnName(4)));
				messages.add(message);
			}

			connection.commit();
			return messages;

		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = "
					+ SQL_SELECT_BY_TOPIC_ID + ": " + id);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}


	@Override
	public List<Message> selectByText(String pieceOfText) throws DBSystemException {
		
		//check argument
		if (pieceOfText.isEmpty() || pieceOfText.replace(" ", "").isEmpty()){
			throw new IllegalArgumentException("argument 'pieceOfText' is empty");
		}
		//select
		return selectBy(pieceOfText, SQL_SELECT_BY_PIECE_OF_TEXT);
	}
	
	
	private List<Message> selectBy(String criterion, String sqlQuery)
			throws DBSystemException {

		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(sqlQuery.replace(VALUE_FOR_PREP_ST,
					criterion));
			resSet = statement.executeQuery();

			List<Message> result = new ArrayList<>(0);
			while (resSet.next()) {
				Message message = new Message(
						resSet.getInt(resSet.getMetaData().getColumnName(1)),
						resSet.getString(resSet.getMetaData().getColumnName(2)),
						resSet.getInt(resSet.getMetaData().getColumnName(3)),
						resSet.getInt(resSet.getMetaData().getColumnName(4)));
				result.add(message);
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
	
	
	//TODO tests
	void deleteMessagesByTopicId(int topicId) throws DBSystemException{
		
		boolean success = false;
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		int deletedMessagesCount = 0;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			// count how many messages to delete
			deletedMessagesCount = selectByTopicId(topicId).size();
			statement = connection.prepareStatement(SQL_DELETE_ALL_BY_TOPIC_ID);
			statement.setInt(1, topicId);
			statement.executeUpdate();
			connection.commit();
			success = true;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_DELETE_ALL_BY_TOPIC_ID
					+ ", topic_id: " + topicId);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
			if (success) {
				// decrement messages count by deletedMessagesCount
				DaoFactory.getInstance().getTopicDao()
						.changeMessagesCountByTopicId(topicId, -deletedMessagesCount);
			}
		}
	}
	
	//TODO tests
	void deleteMessagesByAuthorId(int authorId) throws DBSystemException {

		boolean success = false;
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_DELETE_ALL_BY_AUTHOR_ID);
			statement.setInt(1, authorId);
			statement.executeUpdate();
			connection.commit();
			success = true;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = "
					+ SQL_DELETE_ALL_BY_AUTHOR_ID + ", author_id: " + authorId);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
			if (success) {
				// decrement messages count
				DaoFactory.getInstance().getTopicDao().decrementMessagesCountByDeletedAuthorId(authorId);
			}
		}
	}
}