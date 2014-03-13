package com.aleksalitus.forumrdbms.impltest;

import static org.junit.Assert.*;
import static com.aleksalitus.forumrdbms.impltest.ConstantsForTests.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.aleksalitus.forumrdbms.dao.MessageDao;
import com.aleksalitus.forumrdbms.entity.Message;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoFactory;

public class MessageDaoImplTest {

	@Test
	public void testSelectAll() throws DBSystemException {
		List<Message> messages = DaoFactory.getInstance().getMessageDao().selectAll();
		assertNotNull(messages);
		assertTrue(messages.contains(messageTest1));
		assertTrue(messages.contains(messageTest2));
	}

	
	
	@Test
	public void testInsertUniqueAndDelete() throws DBSystemException {

		MessageDao messageDao = DaoFactory.getInstance().getMessageDao();
		Message newMessage = createRandomMessage();

		// test insert
		try {
			messageDao.insert(newMessage);
		} catch (NotUniquePropertyException e) {
			fail("try again");
		}

		List<Message> messages = messageDao.selectByText(newMessage.getText());

		assertNotNull(messages);
		assertTrue(messages.size() == 1);

		//message with proper message_id
		Message insertedMessage = messages.get(0);

		assertTrue(insertedMessage.getText().equals(newMessage.getText()));
		assertTrue(insertedMessage.getAuthorId() == newMessage.getAuthorId());
		assertTrue(insertedMessage.getTopicId() == newMessage.getTopicId());

		// test delete
		messageDao.delete(insertedMessage);

		assertNull(messageDao.selectByMessageId(insertedMessage.getId()));
	}

	
	
	@Test
	public void testSelectByMessageId() throws DBSystemException {
		
		MessageDao userDao = DaoFactory.getInstance().getMessageDao();
		Message message1 = userDao.selectByMessageId(messageTest1.getId());
		Message message2 = userDao.selectByMessageId(messageTest2.getId());

		assertNotNull(message1);
		assertNotNull(message2);

		assertEquals(message1, messageTest1);
		assertEquals(message2, messageTest2);
	}

	
	
	@Test
	public void testSelectByAuthorId() throws DBSystemException {
		
		List<Message> messages = DaoFactory.getInstance().getMessageDao()
				.selectByAuthorId(messageTest1.getAuthorId());

		assertNotNull(messages);		

		assertTrue(messages.contains(messageTest1));
	}

	
	
	@Test
	public void testSelectByTopicId() throws DBSystemException {
		
		List<Message> messages = DaoFactory.getInstance().getMessageDao()
				.selectByTopicId(messageTest1.getTopicId());

		assertNotNull(messages);		

		assertTrue(messages.contains(messageTest1));
	}

	
	
	@Test
	public void testSelectByText() throws DBSystemException {
		
		List<Message> messages = DaoFactory.getInstance().getMessageDao()
				.selectByText(SUBSTR_MESSAGE_TEXT);

		assertNotNull(messages);
		assertNotNull(messages.get(0));
		assertNotNull(messages.get(1));

		assertNotNull(messages.get(0).getId());
		assertNotNull(messages.get(0).getText());
		assertNotNull(messages.get(0).getAuthorId());
		assertNotNull(messages.get(0).getTopicId());

		assertNotNull(messages.get(1).getId());
		assertNotNull(messages.get(1).getText());
		assertNotNull(messages.get(1).getAuthorId());
		assertNotNull(messages.get(1).getTopicId());

		assertEquals(messages.get(0), messageTest1);
		assertEquals(messages.get(1), messageTest2);

		assertEquals(2, messages.size());
	}

	
	
	/**
	 * @return  message with valid random properties and fake id (id is not used in insert method, 
	 * but checked in selectById method)
	 */
	private Message createRandomMessage() {
		Random r = new Random();
		int fakeId = 10000 + r.nextInt(999); 
		// foreign key
		int authorId = messageTest1.getAuthorId(); 
		int topicId = messageTest1.getTopicId();
		return new Message(fakeId, "messageText" + r.nextInt(99999), authorId, topicId);
	}
}
