package com.aleksalitus.forumrdbms.dao;

import java.util.List;

import com.aleksalitus.forumrdbms.dao.Dao;
import com.aleksalitus.forumrdbms.entity.Message;
import com.aleksalitus.forumrdbms.exception.DBSystemException;

public interface MessageDao extends Dao<Message> {

	public Message selectByMessageId(int id) throws DBSystemException;
	
	public List<Message> selectByText(String pieceOfText) throws DBSystemException;

	public List<Message> selectByAuthorId(int id) throws DBSystemException;

	public List<Message> selectByTopicId(int id) throws DBSystemException;
}
