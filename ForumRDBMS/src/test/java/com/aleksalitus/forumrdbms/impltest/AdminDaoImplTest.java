package com.aleksalitus.forumrdbms.impltest;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.aleksalitus.forumrdbms.dao.AdminDao;
import com.aleksalitus.forumrdbms.entity.Admin;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoFactory;

import static com.aleksalitus.forumrdbms.impltest.ConstantsForTests.*;

public class AdminDaoImplTest {

	@Test
	public void testSelectAll() throws DBSystemException {
		List<Admin> admins = DaoFactory.getInstance().getAdminDao().selectAll();
		assertNotNull(admins);
		assertTrue(admins.contains(adminTest1));
		assertTrue(admins.contains(adminTest2));
	}

	@Test
	public void testInsertUniqueAndDelete() throws DBSystemException {

		AdminDao adminDao = DaoFactory.getInstance().getAdminDao();
		Admin newAdmin = createRandomAdmin();
		
		//test insert
		try {
			adminDao.insert(newAdmin);
		} catch (NotUniquePropertyException e1) {
			fail("try again");
		}

		List<Admin> admins = adminDao.selectByLogin(newAdmin.getLogin());

		assertNotNull(admins);
		assertNotNull(admins.get(0));
		
		assertTrue(!admins.isEmpty());
		assertTrue(admins.contains(newAdmin));
		
		//test delete
		adminDao.delete(newAdmin);

		admins = adminDao.selectByLogin(newAdmin.getLogin());

		assertFalse(admins.contains(newAdmin));

	}

	@Test(expected = NotUniquePropertyException.class)
	public void testInsertNotUnique() throws DBSystemException,
			NotUniquePropertyException {

		// try to insert not unique admin
		DaoFactory.getInstance().getAdminDao().insert(adminTest1);

	}

	@Test
	public void testSelectByLogin() throws DBSystemException {

		List<Admin> admins = DaoFactory.getInstance().getAdminDao()
				.selectByLogin(SUBSTR_ADMIN_LOGIN);

		assertNotNull(admins);
		assertNotNull(admins.get(0));
		assertNotNull(admins.get(1));

		assertNotNull(admins.get(0).getLogin());
		assertNotNull(admins.get(0).getPassword());

		assertNotNull(admins.get(1).getLogin());
		assertNotNull(admins.get(1).getPassword());

		assertEquals(admins.get(0), adminTest1);
		assertEquals(admins.get(1), adminTest2);

		assertEquals(2, admins.size());
	}

	
	/**
	 * @return Admin with valid random properties and fake id (id is not used in insert method, 
	 * but checked in selectById)
	 */
	private Admin createRandomAdmin() {
		return new Admin("login" + new Random().nextInt(999999), 
				"pswd" + new Random().nextInt(999999));
	}

}
