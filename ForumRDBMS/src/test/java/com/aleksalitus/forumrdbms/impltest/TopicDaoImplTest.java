package com.aleksalitus.forumrdbms.impltest;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.aleksalitus.forumrdbms.dao.TopicDao;
import com.aleksalitus.forumrdbms.entity.Topic;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoFactory;

import static com.aleksalitus.forumrdbms.impltest.ConstantsForTests.*;

public class TopicDaoImplTest {

	@Test
	public void testSelectAll() throws DBSystemException {
		
		List<Topic> topics = DaoFactory.getInstance().getTopicDao().selectAll();
		assertNotNull(topics);
		assertTrue(topics.contains(topicTest1));
		assertTrue(topics.contains(topicTest2));
	}

	@Test
	public void testInsertUniqueAndDelete() throws DBSystemException {

		TopicDao userDao = DaoFactory.getInstance().getTopicDao();
		Topic newTopic = createRandomTopic();

		// test insert
		try {
			userDao.insert(newTopic);
		} catch (NotUniquePropertyException e) {
			fail("try again");
		}

		List<Topic> topics = userDao.selectByTopicName(newTopic.getName());

		assertNotNull(topics);
		assertTrue(topics.size() == 1);

		Topic insertedTopic = topics.get(0);

		assertTrue(insertedTopic.getName().equals(newTopic.getName()));
		assertTrue(insertedTopic.getAuthorId() == newTopic.getAuthorId());

		userDao.delete(insertedTopic);

		assertNull(userDao.selectByTopicId(insertedTopic.getId()));
	}

	
	
	@Test(expected = NotUniquePropertyException.class)
	public void testInsertNotUnique() throws DBSystemException,
			NotUniquePropertyException {

		// try to insert not unique topic
		DaoFactory.getInstance().getTopicDao().insert(topicTest1);
	}

	
	
	@Test
	public void testSelectByTopicId() throws DBSystemException {

		TopicDao userDao = DaoFactory.getInstance().getTopicDao();
		Topic topic1 = userDao.selectByTopicId(topicTest1.getId());
		Topic topic2 = userDao.selectByTopicId(topicTest2.getId());

		assertNotNull(topic1);
		assertNotNull(topic2);

		assertEquals(topic1, topicTest1);
		assertEquals(topic2, topicTest2);
	}

	@Test
	public void testSelectByTopicName() throws DBSystemException {

		List<Topic> topics = DaoFactory.getInstance().getTopicDao()
				.selectByTopicName(SUBSTR_TOPIC_NAME);

		assertNotNull(topics);
		assertNotNull(topics.get(0));
		assertNotNull(topics.get(1));

		assertNotNull(topics.get(0).getId());
		assertNotNull(topics.get(0).getName());
		assertNotNull(topics.get(0).getAuthorId());

		assertNotNull(topics.get(1).getId());
		assertNotNull(topics.get(1).getName());
		assertNotNull(topics.get(1).getAuthorId());

		assertEquals(topics.get(0), topicTest1);
		assertEquals(topics.get(1), topicTest2);

		assertEquals(2, topics.size());
	}

	@Test
	public void testSelectByAuthorId() throws DBSystemException {
		
		List<Topic> topics = DaoFactory.getInstance().getTopicDao()
				.selectByAuthorId(topicTest1.getAuthorId());

		assertNotNull(topics);		

		assertTrue(topics.contains(topicTest1));
	}

	
	@Test
	public void testSelectMostPopular() throws DBSystemException{
		List<Topic> topics = DaoFactory.getInstance().getTopicDao()
				.selectMostPopular(2);
		
		assertNotNull(topics);		
		
		assertEquals(2, topics.size());
		assertTrue(topics.get(0).getMessagesCount() >= topics.get(1).getMessagesCount());
	}
	
	
	/**
	 * @return  Topic with valid random properties and fake id (id is not used in insert method, 
	 * but checked in selectById)
	 */
	private Topic createRandomTopic() {
		Random r = new Random();
		int fakeId = 10000 + r.nextInt(999); 
		int authorId = topicTest1.getAuthorId(); // foreign key
		int messagesCount = topicTest1.getMessagesCount();
		return new Topic(fakeId, "topicName" + r.nextInt(99999), authorId, messagesCount);
	}
}
