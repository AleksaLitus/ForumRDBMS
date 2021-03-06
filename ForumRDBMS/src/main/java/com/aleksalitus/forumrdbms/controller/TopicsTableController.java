package com.aleksalitus.forumrdbms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aleksalitus.forumrdbms.dao.TopicDao;
import com.aleksalitus.forumrdbms.entity.Topic;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoFactory;

import static com.aleksalitus.forumrdbms.util.ServletUtils.*;
import static com.aleksalitus.forumrdbms.util.InputValidator.*;
import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;

/**
 * Servlet implementation class TopicsTableController
 */
@SuppressWarnings("serial")
public class TopicsTableController extends HttpServlet implements SwitchActionController {
       

    public TopicsTableController() {
        super();
    }


    @Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	switchActionAndDoIt(request,response);
	}
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switchActionAndDoIt(request, response);
	}
	
    
    @Override
    public void switchActionAndDoIt(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	
		String action = request.getParameter(PARAMETER_ACTION);
		String id = request.getParameter(TOPIC_ID);
		String name = request.getParameter(TOPIC_NAME);
		String authorId = request.getParameter(TOPIC_AUTHOR_ID);
		String messagesCount = request.getParameter(TOPIC_MESSAGES_COUNT);
		String mostPopularCount = request.getParameter(TOPIC_MOST_POPULAR_COUNT_TO_VIEW);
		
		// switch action
		if (action == null) {
			throw new NullPointerException("Topics: action = null");
		}
		else if (action.equals(ACTION_DELETE)) {
			delete(id,name, authorId, messagesCount,request,response);
		}
		else if (action.equals(ACTION_INSERT)) {
			insert(name, authorId, request,response);
		}
		else if (action.equals(ACTION_SEARCH)) {
			search(id,name, authorId, request,response);
		}
		else if (action.equals(ACTION_MOST_POPULAR_TOPICS)) {
			selectMostPopular(mostPopularCount,request,response);
		}
		else { // 'select all' or action is empty string etc
			selectAll(request,response);
		}
	}

    
	private void insert(String name, String authorId,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// check input
		if (!lengthIsValid(name, 3, 50) || !idIsValid(authorId)) {

			// invalid input
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT, PAGE_TOPICS,request, response);
		} else {

			// input is valid
			// insert topic to DB
			int fakeId = 10; // not used in 'insert' method, needed to create 'topic' object
			int initialMessagesCount = 0;
			Topic topic = new Topic(fakeId, name, Integer.valueOf(authorId), initialMessagesCount);
			try {
				DaoFactory.getInstance().getTopicDao().insert(topic);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_UPDATE,PAGE_TOPICS, request, response);
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_TOPICS,request, response);
				return;
			} catch (NotUniquePropertyException e) {
				setResultMessageGoForward(RESULT_MESSAGE_CANNOT_INSERT,PAGE_TOPICS, request, response);
			}
		}
	}


	private void selectMostPopular(String messagesCount,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	if(!idIsValid(messagesCount)){
    		setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT, PAGE_TOPICS, request, response);
    	}
    	else{
	    	List<Topic> topics = new ArrayList<>(0);
			try {
				int topicsCountToView = Integer.valueOf(messagesCount);
				topics = DaoFactory.getInstance().getTopicDao().selectMostPopular(topicsCountToView);
				request.getSession().setAttribute(ATTRIBUTE_MODEL_TOPICS_TO_VIEW, topics);
				request.getRequestDispatcher(PAGE_TOPICS).forward(request, response);
			} catch (IllegalArgumentException e) {
				request.getSession().setAttribute(ATTRIBUTE_MODEL_TOPICS_TO_VIEW, new ArrayList<>(0));
				setResultMessageGoForward(RESULT_MESSAGE_COUNT_IS_TOO_BIG, PAGE_TOPICS,request,response);
			} catch (DBSystemException e) {
				request.getSession().setAttribute(ATTRIBUTE_MODEL_TOPICS_TO_VIEW, new ArrayList<>(0));
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_TOPICS,request,response);
			}
    	}
	}


	private void selectAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		List<Topic> topics = new ArrayList<>(0);
		try {
			topics = DaoFactory.getInstance().getTopicDao().selectAll();
			request.getSession().setAttribute(ATTRIBUTE_MODEL_TOPICS_TO_VIEW, topics);
			request.getRequestDispatcher(PAGE_TOPICS).forward(request, response);
		} catch (DBSystemException e) {
			request.getSession().setAttribute(ATTRIBUTE_MODEL_TOPICS_TO_VIEW, new ArrayList<>(0));
			setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_TOPICS,request,response);
		}
	}
	
	

	private void delete(String id, String name, String authorId, String messagesCountStr, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//TODO tests
		if(id == null || name == null || authorId == null || messagesCountStr == null){
			setResultMessageGoForward("Sorry, got an exeption: not tested yet", PAGE_TOPICS, request, response);
			return;
		}
		//check input
		int messagesCount = 0;
		try{
			messagesCount = Integer.valueOf(messagesCountStr);
		}
		catch(NumberFormatException e){
			// invalid input
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT, PAGE_TOPICS, request, response);
			return;
		}

		if (!idIsValid(id) || !lengthIsValid(name, 3, 50) || !idIsValid(authorId) || messagesCount < 0) {
			// invalid input
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT, PAGE_TOPICS, request, response);
		} else {
			// input is valid
			// delete topic from DB
			Topic topicToDelete = new Topic(Integer.valueOf(id),name,Integer.valueOf(authorId), Integer.valueOf(messagesCount));
			try {
				TopicDao dao = DaoFactory.getInstance().getTopicDao();
				if(dao.selectByTopicId(Integer.valueOf(id)) != null){
					dao.delete(topicToDelete);
					setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_UPDATE, PAGE_TOPICS, request, response);
				} else {
					// no such topic in table 
					setResultMessageGoForward(RESULT_MESSAGE_CANNOT_DELETE_NOT_FOUND, PAGE_TOPICS, request, response);
				}
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR,PAGE_TOPICS,request,response);
			}
		}
	}
	
	
	// TODO testing!!!!!!!!!!!!!!!!!!!!!!
	private void search(String id, String name, String authorId,
			HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		//setResultMessageGoForward("Apologies, not yet implemented.",PAGE_TOPICS,request,response);
		int countOfFilledFields = 0;
		if (!isEmptyOrSpacesOnly(id)) {
			countOfFilledFields++;
		}
		if (!isEmptyOrSpacesOnly(name)) {
			countOfFilledFields++;
		}
		if (!isEmptyOrSpacesOnly(authorId)) {
			countOfFilledFields++;
		}
		
		if (countOfFilledFields == 0 || countOfFilledFields == 2) {
			// invalid input: must be filled 1 or 3 fields
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT_FOR_SEARCH, PAGE_USERS,request, response);
			return;
		}
		
		TopicDao dao = DaoFactory.getInstance().getTopicDao();

		// all fields filled correctly
		// select topics with such id or name or author id or messages count
		if (idIsValid(id) && loginIsValidForSearch(name) && idIsValid(authorId)) {
			try {
				Set<Topic> resultList = new HashSet<>();
				Topic selectedById = dao.selectByTopicId(Integer.valueOf(id));
				if(selectedById != null){
					resultList.add(selectedById);
				}
				resultList.addAll(dao.selectByTopicName(name));
				resultList.addAll(dao.selectByAuthorId(Integer.valueOf(authorId)));
				
				request.getSession().setAttribute(ATTRIBUTE_MODEL_TOPICS_TO_VIEW, resultList);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_SELECT, PAGE_TOPICS, request, response);
				return;
			} catch (NumberFormatException e) {
				setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT_FOR_SEARCH, PAGE_TOPICS, request, response);
				return;
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_TOPICS,request, response);
				return;
			}
		}
		// select By Id only
		else if (idIsValid(id) && name.isEmpty() && authorId.isEmpty()) {
			try {
				Topic selectedById = dao.selectByTopicId(Integer.valueOf(id));
				List<Topic> result = new ArrayList<>(0);
				result.add(selectedById);
				request.getSession().setAttribute(ATTRIBUTE_MODEL_TOPICS_TO_VIEW, result);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_SELECT, PAGE_TOPICS, request, response);
				return;
			} catch (NumberFormatException e) {
				setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT_FOR_SEARCH,PAGE_TOPICS, request, response);
				return;
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_TOPICS,request, response);
				return;
			}
		}
		// select By Name only
		else if (lengthIsValid(name, 1, 50) && id.isEmpty() && authorId.isEmpty()) {
			try {
				
				List<Topic> result = dao.selectByTopicName(name);
				request.getSession().setAttribute(ATTRIBUTE_MODEL_TOPICS_TO_VIEW, result);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_SELECT, PAGE_TOPICS, request, response);
				return;
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_TOPICS,request, response);
				return;
			}
		}
		// select By authorId only
		else if (idIsValid(authorId) && id.isEmpty() && name.isEmpty()) {
			try {

				List<Topic> result = dao.selectByAuthorId(Integer.valueOf(authorId));
				request.getSession().setAttribute(ATTRIBUTE_MODEL_TOPICS_TO_VIEW,result);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_SELECT, PAGE_TOPICS, request, response);
				return;
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_TOPICS,request, response);
				return;
			}
		}
		// invalid input 
		else{
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT_FOR_SEARCH, PAGE_TOPICS, request, response);
			return;
		}
	}
}
