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

import com.aleksalitus.forumrdbms.dao.UserDao;
import com.aleksalitus.forumrdbms.entity.User;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoFactory;

import static com.aleksalitus.forumrdbms.util.ServletUtils.*;
import static com.aleksalitus.forumrdbms.util.InputValidator.*;
import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;

/**
 * Servlet implementation class UsersTableController
 */
@SuppressWarnings("serial")
public class UsersTableController extends HttpServlet implements SwitchActionController {
       
    public UsersTableController() {
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
		String id = request.getParameter(USER_ID);
		String name = request.getParameter(USER_NAME);
		String email = request.getParameter(USER_EMAIL);
		String password = request.getParameter(USER_PASSWORD);
		
		// switch action
		if (action == null) {
			throw new NullPointerException("Users: action = null");
		}
		else if (action.equals(ACTION_DELETE)) {
			delete(id,name, email, password,request,response);
		}
		else if (action.equals(ACTION_INSERT)) {
			insert(name, email, password,request,response);
		}
		else if (action.equals(ACTION_SEARCH)) {
			search(id,name, email,request,response);
		}
		else { // 'select all' or action is empty string etc
			selectAll(request,response);
		}
	}

	private void search(String id, String name, String email, 
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int countOfFilledFields = 0;
		if (!isEmptyOrSpacesOnly(id)) {
			countOfFilledFields++;
		}
		if (!isEmptyOrSpacesOnly(name)) {
			countOfFilledFields++;
		}
		if (!isEmptyOrSpacesOnly(email)) {
			countOfFilledFields++;
		}
		
		if (countOfFilledFields == 0 || countOfFilledFields == 2) {
			// invalid input: must be filled 1 or 3 fields
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT_FOR_SEARCH, PAGE_USERS,request, response);
			return;
		}
		
		UserDao dao = DaoFactory.getInstance().getUserDao();

		// if all fields filled correctly
		// select users with such id or name or password
		if (idIsValid(id) && loginIsValidForSearch(name) && emailIsValidForSearch(email)) {
			try {
				Set<User> resultList = new HashSet<>();
				User selectedById = dao.selectByUserId(Integer.valueOf(id));
				if(selectedById != null){
					resultList.add(selectedById);
				}
				resultList.addAll(dao.selectByUserName(name));
				resultList.addAll(dao.selectByUserEmail(email));
				
				request.getSession().setAttribute(ATTRIBUTE_MODEL_USERS_TO_VIEW, resultList);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_SELECT, PAGE_USERS, request, response);
				return;
			} catch (NumberFormatException e) {
				setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT_FOR_SEARCH, PAGE_USERS, request, response);
				return;
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_USERS,request, response);
				return;
			}
		}
		// select By Id only
		else if (idIsValid(id) && name.isEmpty() && email.isEmpty()) {
			try {
				User selectedById = dao.selectByUserId(Integer.valueOf(id));
				List<User> result = new ArrayList<>();
				result.add(selectedById);
				request.getSession().setAttribute(ATTRIBUTE_MODEL_USERS_TO_VIEW, result);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_SELECT, PAGE_USERS, request, response);
				return;
			} catch (NumberFormatException e) {
				setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT_FOR_SEARCH,PAGE_USERS, request, response);
				return;
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_USERS,request, response);
				return;
			}
		}
		// select By Name only
		else if (loginIsValidForSearch(name) && id.isEmpty() && email.isEmpty()) {
			try {
				
				List<User> result = dao.selectByUserName(name);
				request.getSession().setAttribute(ATTRIBUTE_MODEL_USERS_TO_VIEW, result);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_SELECT, PAGE_USERS, request, response);
				return;
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_USERS,request, response);
				return;
			}
		}
		// select By Email only
		else if (emailIsValidForSearch(email) && id.isEmpty() && name.isEmpty()) {
			try {

				List<User> result = dao.selectByUserEmail(email);
				request.getSession().setAttribute(ATTRIBUTE_MODEL_USERS_TO_VIEW,result);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_SELECT, PAGE_USERS, request, response);
				return;
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_USERS,request, response);
				return;
			}
		}
		// input isn't valid
		else{
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT_FOR_SEARCH, PAGE_USERS, request, response);
			return;
		}
	}


	private void insert(String name, String email, String password,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// check input
		if (!loginIsValid(name) || !emailIsValid(email)
				|| !passwordIsValid(password)) {

			// invalid input
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT, PAGE_USERS, request, response);
		} else {

			// input is valid
			// insert user to DB
			int fakeId = 10; // not used in 'insert' method, needed to create 'user' object
			User user = new User(fakeId, name, email,password);
			try {
				DaoFactory.getInstance().getUserDao().insert(user);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_UPDATE, PAGE_USERS, request, response);
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_USERS,request,response);
				return;
			} catch (NotUniquePropertyException e) {
				setResultMessageGoForward(RESULT_MESSAGE_CANNOT_INSERT, PAGE_USERS,request,response);
			}
		}
	}


	
	private void selectAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		List<User> users = new ArrayList<>(0);
		try {
			users = DaoFactory.getInstance().getUserDao().selectAll();
			request.getSession().setAttribute(ATTRIBUTE_MODEL_USERS_TO_VIEW, users);
			request.getRequestDispatcher(PAGE_USERS).forward(request, response);
		} catch (DBSystemException e) {
			request.getSession().setAttribute(ATTRIBUTE_MODEL_USERS_TO_VIEW, new ArrayList<>(0));
			setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_USERS,request,response);
		}
	}
	
	
	
	private void delete(String id, String name, String email, String password, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//TODO tests
		if(id == null || name == null || email == null || password == null){
			setResultMessageGoForward("Sorry, got an exeption: not tested yet", PAGE_USERS, request, response);
			return;
		}

		//check input
		if (!idIsValid(id) || !loginIsValid(name) || !emailIsValid(email)
				|| !passwordIsValid(password)) {
			// invalid input
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT, PAGE_USERS, request, response);
		} else {
			// input is valid
			// delete user from DB
			User userToDelete = new User(Integer.valueOf(id),name,email,password);
			try {
				List<User> allUsers = DaoFactory.getInstance().getUserDao().selectAll();
				if(allUsers.contains(userToDelete)){
					DaoFactory.getInstance().getUserDao().delete(userToDelete);
					setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_UPDATE, PAGE_USERS, request, response);
				} else {
					// no such user in table 'users'
					setResultMessageGoForward(RESULT_MESSAGE_CANNOT_DELETE_NOT_FOUND, PAGE_USERS, request, response);
				}
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR,PAGE_USERS,request,response);
			}
		}
	}
}
