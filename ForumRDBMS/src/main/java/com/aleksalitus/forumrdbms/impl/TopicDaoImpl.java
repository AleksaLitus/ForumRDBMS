package com.aleksalitus.forumrdbms.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aleksalitus.forumrdbms.dao.TopicDao;
import com.aleksalitus.forumrdbms.entity.Topic;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoImpl;
import com.aleksalitus.forumrdbms.util.JdbcUtils;

public class TopicDaoImpl extends DaoImpl<Topic> implements TopicDao {

	private static final String SQL_SELECT_ALL = "SELECT * FROM Topics";
	private static final String SQL_DELETE = "DELETE FROM Topics WHERE topic_id = ? AND topic_name = ? AND author_id = ? AND messages_count = ?";
	private static final String SQL_INSERT = "INSERT INTO Topics (topic_name, author_id, messages_count) VALUES (?,?,?)";
	private static final String SQL_SELECT_BY_TOPIC_NAME = "SELECT * FROM Topics where topic_name like '%" + VALUE_FOR_PREP_ST + "%'";
	private static final String SQL_SELECT_BY_TOPIC_ID = "SELECT * FROM Topics WHERE topic_id = ?";
	private static final String SQL_SELECT_BY_AUTHOR_ID = "SELECT * FROM Topics WHERE author_id = ?";
	private static final String SQL_UPDATE_MESSAGES_COUNT_BY_TOPIC_ID = "UPDATE Topics SET MESSAGES_COUNT = ? WHERE topic_id = ?";
	private static final String SQL_UPDATE_MESSAGES_COUNT_BY_AUTHOR_ID = "UPDATE Topics SET MESSAGES_COUNT = ? WHERE author_id = ?";
	private static final String SQL_SELECT_ALL_ORDERED_BY_MES_COUNT = "SELECT * FROM Topics ORDER BY messages_count";
	
	TopicDaoImpl(){
		//NOP
	}
	

	
	@Override
	public List<Topic> selectAll() throws DBSystemException {
		
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_SELECT_ALL);
			resSet = statement.executeQuery();
			List<Topic> result = new ArrayList<>(0);
			while (resSet.next()) {
				int id = resSet.getInt(resSet.getMetaData().getColumnName(1));
				String name = resSet.getString(resSet.getMetaData().getColumnName(2));
				int authorId = resSet.getInt(resSet.getMetaData().getColumnName(3));
				int messagesCount = resSet.getInt(resSet.getMetaData().getColumnName(4));
				Topic topic = new Topic(id,name,authorId,messagesCount);
				result.add(topic);
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
	public void insert(Topic topic) throws DBSystemException, NotUniquePropertyException {
		
		// check if name is unique
		for(Topic t : selectAll()){
			if (topic.getName().equals(t.getName()))
				throw new NotUniquePropertyException(
						"Topic with such name already exists: " + topic.getName());
		}
		
		// unique, insert
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			int messagesCountAtFirst = 0;
			statement = connection.prepareStatement(SQL_INSERT);
			statement.setString(1, topic.getName());
			statement.setInt(2, topic.getAuthorId());
			statement.setInt(3, messagesCountAtFirst);
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_INSERT
					+ ", name: " + topic.getName());
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}

	}

	
	
	@Override
	public void delete(Topic topic) throws DBSystemException {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			// delete topic
			statement = connection.prepareStatement(SQL_DELETE);
			statement.setInt(1, topic.getId());
			statement.setString(2, topic.getName());
			statement.setInt(3, topic.getAuthorId());
			statement.setInt(4, topic.getMessagesCount());
			statement.executeUpdate();
			connection.commit();
			// topic was deleted, now delete all messages with deleted_topic's id 
			// (if topic has messages)
			MessageDaoImpl messageDao = DaoFactory.getInstance().getMessageDao();
			if(messageDao.selectByTopicId(topic.getId()) != null && !messageDao.selectByTopicId(topic.getId()).isEmpty()){
				messageDao.deleteMessagesByTopicId(topic.getId());
			}
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_DELETE
					+ ", id: " + topic.getId());
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}

	
	
	@Override
	public Topic selectByTopicId(int id) throws DBSystemException {
		
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
			statement = connection.prepareStatement(SQL_SELECT_BY_TOPIC_ID);
			statement.setInt(1, id);
			resSet = statement.executeQuery();

			if (resSet.next()) {
				Topic topic = new Topic(
						resSet.getInt(resSet.getMetaData().getColumnName(1)), 
						resSet.getString(resSet.getMetaData().getColumnName(2)), 
						resSet.getInt(resSet.getMetaData().getColumnName(3)),
						resSet.getInt(resSet.getMetaData().getColumnName(4)));
				return topic;
			}
			connection.commit();

			// not found
			return null;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = "
					+ SQL_SELECT_BY_TOPIC_ID);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}

	
	
	@Override
	public List<Topic> selectByTopicName(String name) throws DBSystemException {
		
		//check argument
		if (name.isEmpty() || name.replace(" ", "").isEmpty()) {
			throw new IllegalArgumentException("name is empty");
		}
		
		//select
		return selectBy(name, SQL_SELECT_BY_TOPIC_NAME);
	}

	
	
	@Override
	public List<Topic> selectByAuthorId(int id) throws DBSystemException {
		
		//check argument
		if (id < 1){
			throw new IllegalArgumentException("id<1");
		}

		//select
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

			List<Topic> topics = new ArrayList<>(0);
			while (resSet.next()) {
				Topic topic = new Topic(
						resSet.getInt(resSet.getMetaData().getColumnName(1)), 
						resSet.getString(resSet.getMetaData().getColumnName(2)), 
						resSet.getInt(resSet.getMetaData().getColumnName(3)),
						resSet.getInt(resSet.getMetaData().getColumnName(4)));
				topics.add(topic);
			}
			
			connection.commit();
			return topics;
			
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_SELECT_BY_AUTHOR_ID + "; id = " + id);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}
	
	
	
	@Override
	public List<Topic> selectMostPopular(int count) throws DBSystemException {

		// first argument check
		if (count < 1) {
			throw new IllegalArgumentException("argument 'count' < 1");
		}

		List<Topic> topics = selectAllOrderedByMessageCount();

		if (topics.isEmpty()) {
			return new ArrayList<Topic>(0);
		}
		// second argument check
		if (count > topics.size()) {
			throw new IllegalArgumentException(
					"argument 'count' > countOfTopics");
		}
		
		// select last topics, with biggest messages_count
		List<Topic> result = topics.subList(topics.size() - count, topics.size());
		Collections.reverse(result);
		return result;
	}
	
	
	//TODO tests
	void changeMessagesCountByTopicId(int topicId, int change) throws DBSystemException {
		
		Topic topic;
		try {
			topic = selectByTopicId(topicId);
		} catch (DBSystemException e) {
			throw new DBSystemException("Can't execute SQL = " + SQL_SELECT_BY_TOPIC_ID);
		}
		
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_UPDATE_MESSAGES_COUNT_BY_TOPIC_ID);
			//set messages count incremented
			statement.setInt(1, (topic.getMessagesCount() + change));
			statement.setInt(2,topicId);
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = "
					+ SQL_UPDATE_MESSAGES_COUNT_BY_TOPIC_ID + "; topicId = " + topicId
					+ "; messagesCount=" + (topic.getMessagesCount() + 1));
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}
	
	
	//TODO tests
	void decrementMessagesCountByDeletedAuthorId(int deletedAuthorId) throws DBSystemException {

		List<Topic> topics;
		try {
			topics = selectByAuthorId(deletedAuthorId);
		} catch (DBSystemException e) {
			throw new DBSystemException("Can't execute SQL = "
					+ SQL_SELECT_BY_TOPIC_ID);
		}

		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			for (Topic topic : topics) {
				// find out how many messages (with deletedAuthorId) this topic has
				int deletedMessagesCount = DaoFactory.getInstance()
						.getMessageDao().selectByAuthorId(deletedAuthorId).size();
				// safe and slow transaction
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(SQL_UPDATE_MESSAGES_COUNT_BY_AUTHOR_ID);
				// decrement messages count
				statement.setInt(1,(topic.getMessagesCount() - deletedMessagesCount));
				statement.setInt(2, deletedAuthorId);
				statement.executeUpdate();
				connection.commit();
			}
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = "
					+ SQL_UPDATE_MESSAGES_COUNT_BY_TOPIC_ID + "; topicId = " + deletedAuthorId);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}
	
	//TODO tests
	void deleteTopicsByAuthorId(int authorId) throws DBSystemException{
		
		List<Topic> topicsToDelete = selectByAuthorId(authorId);
		for(Topic topic : topicsToDelete){
			DaoFactory.getInstance().getMessageDao().deleteMessagesByTopicId(topic.getId());
		}
	}
	
	
	
	private List<Topic> selectBy(String criterion, String sqlQuery)
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

			List<Topic> result = new ArrayList<>(0);
			while (resSet.next()) {
				Topic topic = new Topic(
						resSet.getInt(resSet.getMetaData().getColumnName(1)), 
						resSet.getString(resSet.getMetaData().getColumnName(2)), 
						resSet.getInt(resSet.getMetaData().getColumnName(3)),
						resSet.getInt(resSet.getMetaData().getColumnName(4)));
				result.add(topic);
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
	
	
	
	
	private List<Topic> selectAllOrderedByMessageCount()
			throws DBSystemException {

		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resSet = null;
		try {
			// safe and slow transaction
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(SQL_SELECT_ALL_ORDERED_BY_MES_COUNT);
			resSet = statement.executeQuery();
			List<Topic> result = new ArrayList<>(0);
			while (resSet.next()) {
				int id = resSet.getInt(resSet.getMetaData().getColumnName(1));
				String name = resSet.getString(resSet.getMetaData().getColumnName(2));
				int authorId = resSet.getInt(resSet.getMetaData().getColumnName(3));
				int messagesCount = resSet.getInt(resSet.getMetaData().getColumnName(4));
				
				Topic topic = new Topic(id,name,authorId,messagesCount);
				result.add(topic);
			}
			connection.commit();
			return result;
		} catch (SQLException e) {
			JdbcUtils.rollbackQuietly(connection);
			throw new DBSystemException("Can't execute SQL = " + SQL_SELECT_ALL_ORDERED_BY_MES_COUNT);
		} finally {
			JdbcUtils.closeResourses(connection, statement, resSet);
		}
	}
}
