package com.aleksalitus.forumrdbms.impl;

import com.aleksalitus.forumrdbms.impl.AdminDaoImpl;
import com.aleksalitus.forumrdbms.impl.DaoFactory;
import com.aleksalitus.forumrdbms.impl.MessageDaoImpl;
import com.aleksalitus.forumrdbms.impl.TopicDaoImpl;
import com.aleksalitus.forumrdbms.impl.UserDaoImpl;


public class DaoFactory {

	private static AdminDaoImpl adminDao = null;
	private static UserDaoImpl userDao = null;
	private static TopicDaoImpl topicDao = null;
	private static MessageDaoImpl messageDao = null;

	private static DaoFactory instance = null;
	
	private DaoFactory(){
		//NOP
	}

	public static synchronized DaoFactory getInstance() {
		if (instance == null) {
			instance = new DaoFactory();
		}
		return instance;
	}

	public AdminDaoImpl getAdminDao() {
		if (adminDao == null) {
			adminDao = new AdminDaoImpl();
		}
		return adminDao;
	}

	public UserDaoImpl getUserDao() {
		if (userDao == null) {
			userDao = new UserDaoImpl();
		}
		return userDao;
	}

	public TopicDaoImpl getTopicDao() {
		if (topicDao == null) {
			topicDao = new TopicDaoImpl();
		}
		return topicDao;
	}

	public MessageDaoImpl getMessageDao() {
		if (messageDao == null) {
			messageDao = new MessageDaoImpl();
		}
		return messageDao;
	}
}