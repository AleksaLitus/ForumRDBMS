package com.aleksalitus.forumrdbms.constants;

/**
 * Constants: session attributes, pages etc
 */
public final class ConstantsForServlets {

	/**
	 *  attributes, properties (session, request)
	 */
	public static final String ATTRIBUTE_ADMIN = "admin";
	public static final String ATTRIBUTE_ACTION_RESULT = "actionResult";
	
	public static final String ATTRIBUTE_MODEL_USERS_TO_VIEW = "modelUsersToView";
	public static final String ATTRIBUTE_MODEL_ADMINS_TO_VIEW = "modelAdminsToView";
	public static final String ATTRIBUTE_MODEL_TOPICS_TO_VIEW = "modelTopicsToView";
	public static final String ATTRIBUTE_MODEL_MESSAGES_TO_VIEW = "modelMessagesToView";
	
	
	public static final String PARAMETER_ACTION = "action";
	
	
	/**
	 *  entity's properties 
	 */
	public static final String ADMIN_LOGIN = "adminLogin";
	public static final String ADMIN_PASSWORD = "adminPassword";
	
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String USER_EMAIL = "userEmail";
	public static final String USER_PASSWORD = "userPassword";
	
	public static final String MESSAGE_ID = "messageId";
	public static final String MESSAGE_TEXT = "messageText";
	public static final String MESSAGE_AUTHOR_ID = "messageAuthorId";
	public static final String MESSAGE_TOPIC_ID = "messageTopicId";
	
	public static final String TOPIC_ID = "topicId";
	public static final String TOPIC_NAME = "topicName";
	public static final String TOPIC_AUTHOR_ID = "topicAuthorId";
	public static final String TOPIC_MESSAGES_COUNT = "topicMessagesCount";
	
	// not an entity's property 
	public static final String TOPIC_MOST_POPULAR_COUNT_TO_VIEW = "topicMostPopularCountToView";
	

	
	/**
	 *  actions with data
	 */
	public static final String ACTION_SELECT_ALL = "selectAll";
	public static final String ACTION_DELETE = "delete";
	public static final String ACTION_INSERT = "insert";
	public static final String ACTION_SEARCH = "search";
	public static final String ACTION_MOST_POPULAR_TOPICS = "mostPopular";
	
	
	/**
	 *  result messages 
	 */
	public static final String RESULT_MESSAGE_SUCCESS_UPDATE = "Done successfully. Please refresh selection.";
	public static final String RESULT_MESSAGE_SUCCESS_SELECT = "Search result: ";
	public static final String RESULT_MESSAGE_DB_ERROR = "Database error. Please try later.";
	public static final String RESULT_MESSAGE_INVALID_INPUT = "Invalid input. Please fill all text boxes correctly. Perhaps, input is too short or formatted incorrectly.";
	public static final String RESULT_MESSAGE_COUNT_IS_TOO_BIG = "Invalid input. Requested count of topics is too big.";
	public static final String RESULT_MESSAGE_INVALID_INPUT_FOR_SEARCH = "Invalid input: fill all text boxes or only one of them; minimum length of input is 1 character; id must contain numbers only.";
	public static final String RESULT_MESSAGE_CANNOT_INSERT = "Cannot insert: not unique properties.";
	public static final String RESULT_MESSAGE_CANNOT_DELETE_NOT_FOUND = "Cannot delete: not found in the table.";
	public static final String RESULT_MESSAGE_CANNOT_DELETE_LAST_ADMIN = "Cannot delete last admin of DB.";

	
	/**
	 *  pages *.jsp
	 */
	public static final String PAGE_MAIN = "main.jsp";
	public static final String PAGE_LOGIN = "login.jsp";
	public static final String PAGE_ADMINS = "admins.jsp";
	public static final String PAGE_USERS = "users.jsp";
	public static final String PAGE_TOPICS = "topics.jsp";
	public static final String PAGE_MESSAGES = "messages.jsp";
	
	
	/**
	 *  conttroller's requests
	 */
	public static final String CONTROLLER_MAIN = "main.do";
	public static final String CONTROLLER_LOGIN = "login.do";
	public static final String CONTROLLER_ADMINS = "admins.do";
	public static final String CONTROLLER_USERS = "users.do";
	public static final String CONTROLLER_TOPICS = "topics.do";
	public static final String CONTROLLER_MESSAGES = "messages.do";
	
	
	
	/**
	 *  cookies constants
	 */
	public static final String COOKIE_WITH_LOGIN = "cookieContainsAdminsLogin&Password";
//	public static final int COOKIE_MAX_AGE = 3600 * 24 * 30; //30 days
//	public static final String COOKIE_SEPARATOR = "_|sep@r@tor|_";
//	public static final String COOKIE_WITH_LOGIN = "adminLogin&Password";
	

	private ConstantsForServlets() {
		// NOP
	}
}
