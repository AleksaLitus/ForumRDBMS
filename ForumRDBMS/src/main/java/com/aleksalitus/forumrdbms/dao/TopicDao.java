package com.aleksalitus.forumrdbms.dao;

import java.util.List;
import com.aleksalitus.forumrdbms.dao.Dao;
import com.aleksalitus.forumrdbms.entity.Topic;
import com.aleksalitus.forumrdbms.exception.DBSystemException;

public interface TopicDao extends Dao<Topic> {

	public Topic selectByTopicId(int id) throws DBSystemException;

	public List<Topic> selectByTopicName(String containedInName) throws DBSystemException;

	public List<Topic> selectByAuthorId(int id) throws DBSystemException;
	
	public List<Topic> selectMostPopular(int count) throws DBSystemException;
}
