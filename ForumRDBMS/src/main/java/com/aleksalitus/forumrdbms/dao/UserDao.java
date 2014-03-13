package com.aleksalitus.forumrdbms.dao;

import java.util.List;

import com.aleksalitus.forumrdbms.dao.Dao;
import com.aleksalitus.forumrdbms.entity.User;
import com.aleksalitus.forumrdbms.exception.DBSystemException;

public interface UserDao extends Dao<User> {
	
	public User selectByUserId(int id) throws DBSystemException;

	public List<User> selectByUserName(String containedInName) throws DBSystemException;

	public List<User> selectByUserEmail(String containedInEmail) throws DBSystemException;
	
}
