package com.aleksalitus.forumrdbms.impltest;

import com.aleksalitus.forumrdbms.entity.Admin;
import com.aleksalitus.forumrdbms.entity.Message;
import com.aleksalitus.forumrdbms.entity.Topic;
import com.aleksalitus.forumrdbms.entity.User;

/**
 *  Contains objects already existing in DB, and string constants
 */
public final class ConstantsForTests {

	private ConstantsForTests(){
		
	}

	// for UserDaoImpl
	public static final User userTest1 = new User(4, "test_user_name_1",
			"test_user_email_1", "test_user_password_1");
	public static final User userTest2 = new User(5, "test_user_name_2",
			"test_user_email_2", "test_user_password_2");
	public static final String SUBSTR_USER_NAME = "test_user_name";
	public static final String SUBSTR_USER_EMAIL = "test_user_email";
	
	
	// for AdminDaoImpl
	public static final Admin adminTest1 = new Admin("test_admin_login_1",
			"test_admin_password_1");
	public static final Admin adminTest2 = new Admin("test_admin_login_2",
			"test_admin_password_2");
	public static final String SUBSTR_ADMIN_LOGIN = "test_admin_login";

	
	// for TopicDaoImpl
	public static final Topic topicTest1 = new Topic(3, "test_topic_name_1", 1, 0);
	public static final Topic topicTest2 = new Topic(4, "test_topic_name_2", 2, 0);
	public static final String SUBSTR_TOPIC_NAME = "test_topic_name";

	
	// for MessageDaoImpl
	public static final Message messageTest1 = new Message(3, "test_message_text_1", 1, 1);
	public static final Message messageTest2 = new Message(4, "test_message_text_2", 2, 2);
	public static final String SUBSTR_MESSAGE_TEXT = "test_message_text";
}
