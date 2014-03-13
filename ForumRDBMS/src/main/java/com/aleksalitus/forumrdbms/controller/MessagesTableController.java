package com.aleksalitus.forumrdbms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aleksalitus.forumrdbms.dao.MessageDao;
import com.aleksalitus.forumrdbms.entity.Message;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoFactory;

import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;
import static com.aleksalitus.forumrdbms.util.InputValidator.*;
import static com.aleksalitus.forumrdbms.util.ServletUtils.setResultMessageGoForward;

/**
 * Servlet implementation class MessagesTableController
 */
@SuppressWarnings("serial")
public class MessagesTableController extends HttpServlet  implements SwitchActionController {

    public MessagesTableController() {
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
		String id = request.getParameter(MESSAGE_ID);
		String text = request.getParameter(MESSAGE_TEXT);
		String authorId = request.getParameter(MESSAGE_AUTHOR_ID);
		String topicId = request.getParameter(MESSAGE_TOPIC_ID);
		
		// switch action
		if (action == null) {
			throw new NullPointerException("Messages: action = null");
		}
		else if (action.equals(ACTION_DELETE)) {
			delete(id, authorId, topicId,  request,response);
		}
		else if (action.equals(ACTION_INSERT)) {
			insert(text, authorId, topicId, request,response);
		}
		else if (action.equals(ACTION_SEARCH)) {
			search(id, text, authorId, topicId, request,response);
		}
		else { // 'select all' or action is empty string etc
			selectAll(request,response);
		}
	}

    //not yet implemented
    private void search(String id, String text, String authorId,
			String topicId, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	//TODO impl
    	setResultMessageGoForward("Apologies, not yet implemented.",PAGE_TOPICS,request,response);
		
	}


	private void selectAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		List<Message> messages = new ArrayList<>(0);
		try {
			messages = DaoFactory.getInstance().getMessageDao().selectAll();
			request.getSession().setAttribute(ATTRIBUTE_MODEL_MESSAGES_TO_VIEW, messages);
			request.getRequestDispatcher(PAGE_MESSAGES).forward(request, response);
		} catch (DBSystemException e) {
			request.getSession().setAttribute(ATTRIBUTE_MODEL_MESSAGES_TO_VIEW, new ArrayList<>(0));
			setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_MESSAGES,request,response);
		}
	}
    
    
    private void delete(String id, String authorId, String topicId, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	//TODO tests
    	if(id == null || authorId == null || topicId == null){
			setResultMessageGoForward("Sorry, got an exeption: not tested yet", PAGE_MESSAGES, request, response);
			return;
		}
    	
		//check input
		if (!idIsValid(id) || !idIsValid(authorId) || !idIsValid(topicId)) {
			// invalid input
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT, PAGE_MESSAGES, request, response);
		} else {
			// input is valid
			// delete message from DB
			String fakeText = "fake"; // not used in delete method
			Message messageToDelete = new Message(Integer.valueOf(id), fakeText, Integer.valueOf(authorId),Integer.valueOf(topicId));
			try {
				MessageDao dao = DaoFactory.getInstance().getMessageDao();
				if(dao.selectByMessageId(Integer.valueOf(id)) != null){
					dao.delete(messageToDelete);
					setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_UPDATE, PAGE_MESSAGES, request, response);
				} else {
					// no such message in table 
					setResultMessageGoForward(RESULT_MESSAGE_CANNOT_DELETE_NOT_FOUND, PAGE_MESSAGES, request, response);
				}
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR,PAGE_MESSAGES,request,response);
			}
		}
	}
    

    private void insert(String text, String authorId, String topicId,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// check input
		if (!lengthIsValid(text, 1, 300) || !idIsValid(authorId) || !idIsValid(topicId)) {

			// invalid input
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT, PAGE_MESSAGES, request, response);
		} else {

			// input is valid
			// insert Message to DB
			int fakeId = 1000; // not used in 'insert' method, needed to create 'message' object
			Message message = new Message(fakeId, text, Integer.valueOf(authorId),Integer.valueOf(topicId));
			try {
				DaoFactory.getInstance().getMessageDao().insert(message);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_UPDATE, PAGE_MESSAGES, request, response);
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_MESSAGES,request,response);
				return;
			} catch (NotUniquePropertyException e) {
				setResultMessageGoForward(RESULT_MESSAGE_CANNOT_INSERT, PAGE_MESSAGES,request,response);
			}
		}
	}
	
}
