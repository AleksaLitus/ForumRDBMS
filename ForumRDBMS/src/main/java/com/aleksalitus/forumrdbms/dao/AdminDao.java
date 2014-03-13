package com.aleksalitus.forumrdbms.dao;

import java.util.List;

import com.aleksalitus.forumrdbms.dao.Dao;
import com.aleksalitus.forumrdbms.entity.Admin;
import com.aleksalitus.forumrdbms.exception.DBSystemException;

public interface AdminDao extends Dao<Admin> {
	
	public List<Admin> selectByLogin(String loginContains) throws DBSystemException;
	
}
