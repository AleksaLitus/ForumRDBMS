package com.aleksalitus.forumrdbms.dao;

import java.util.List;

import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;

public interface Dao<BeanType> {

	public List<BeanType> selectAll() throws DBSystemException;

	public void delete(BeanType object) throws DBSystemException;

	public void insert(BeanType bean) throws DBSystemException,
			NotUniquePropertyException;
}
