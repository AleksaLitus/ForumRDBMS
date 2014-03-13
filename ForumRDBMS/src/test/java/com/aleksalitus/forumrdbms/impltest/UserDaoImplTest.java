package com.aleksalitus.forumrdbms.impltest;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.aleksalitus.forumrdbms.dao.UserDao;
import com.aleksalitus.forumrdbms.entity.User;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoFactory;

import static com.aleksalitus.forumrdbms.impltest.ConstantsForTests.*;

public class UserDaoImplTest {

	@Test
	public void testSelectAll() throws DBSystemException {
		List<User> users = DaoFactory.getInstance().getUserDao().selectAll();
		assertNotNull(users);
		assertTrue(users.contains(userTest1));
		assertTrue(users.contains(userTest2));
	}

	@Test
	public void testInsertUniqueAndDelete() throws DBSystemException {

		UserDao userDao = DaoFactory.getInstance().getUserDao();
		User newUser = createRandomUser();

		// test insert
		try {
			userDao.insert(newUser);
		} catch (NotUniquePropertyException e) {
			fail("try again");
		}

		List<User> users = userDao.selectByUserEmail(newUser.getEmail());

		assertNotNull(users);
		assertTrue(users.size() == 1);
		
		User insertedUser = users.get(0);
		
		assertTrue(insertedUser.getEmail().equals(newUser.getEmail()));
		assertTrue(insertedUser.getPassword().equals(newUser.getPassword()));
		
		// test delete
		userDao.delete(insertedUser);

		assertNull(userDao.selectByUserId(insertedUser.getId()));
	}

	@Test(expected = NotUniquePropertyException.class)
	public void testInsertNotUnique() throws DBSystemException,
			NotUniquePropertyException {

		// try to insert not unique user
		DaoFactory.getInstance().getUserDao().insert(userTest1);
	}

	@Test
	public void testSelectByUserId() throws DBSystemException {

		User user1 = DaoFactory.getInstance().getUserDao()
				.selectByUserId(userTest1.getId());
		User user2 = DaoFactory.getInstance().getUserDao()
				.selectByUserId(userTest2.getId());

		assertNotNull(user1);
		assertNotNull(user2);

		assertEquals(user1, userTest1);
		assertEquals(user2, userTest2);
	}

	@Test
	public void testSelectByUserName() throws DBSystemException {
		
		List<User> users = DaoFactory.getInstance().getUserDao()
				.selectByUserName(SUBSTR_USER_NAME);

		assertNotNull(users);
		assertNotNull(users.get(0));
		assertNotNull(users.get(1));

		assertEquals(users.get(0), userTest1);
		assertEquals(users.get(1), userTest2);

		assertEquals(2, users.size());
	}

	@Test
	public void testSelectByEmail() throws DBSystemException {

		List<User> users = DaoFactory.getInstance().getUserDao()
				.selectByUserEmail(SUBSTR_USER_EMAIL);

		assertNotNull(users);
		assertNotNull(users.get(0));
		assertNotNull(users.get(1));

		assertEquals(users.get(0), userTest1);
		assertEquals(users.get(1), userTest2);

		assertEquals(2, users.size());
	}

	
	/**
	 * @return User with valid random properties and fake id (id is not used in insert method, 
	 * but checked in selectById)
	 */
	private User createRandomUser() {
		Random r = new Random();
		int fakeId = 10000 + r.nextInt(999); //not used in insert method, but checked in selectById
		return new User(fakeId,
				"username" + r.nextInt(99999), 
				r.nextInt(99999) + "email@.gmailcom", 
				"passw" + r.nextInt(99999));
	}
}
