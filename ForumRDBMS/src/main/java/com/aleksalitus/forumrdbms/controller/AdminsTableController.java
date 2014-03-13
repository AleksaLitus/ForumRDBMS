package com.aleksalitus.forumrdbms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aleksalitus.forumrdbms.dao.AdminDao;
import com.aleksalitus.forumrdbms.entity.Admin;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.exception.NotUniquePropertyException;
import com.aleksalitus.forumrdbms.impl.DaoFactory;

import static com.aleksalitus.forumrdbms.util.ServletUtils.*;
import static com.aleksalitus.forumrdbms.util.InputValidator.*;
import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;

/**
 * Servlet implementation class AdminsTableController
 */
@SuppressWarnings("serial")
public class AdminsTableController extends HttpServlet  implements SwitchActionController {  

    public AdminsTableController() {
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
    public void switchActionAndDoIt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	String action = request.getParameter(PARAMETER_ACTION);
		String login = request.getParameter(ADMIN_LOGIN);
		String password = request.getParameter(ADMIN_PASSWORD);
		
		// switch action
		if (action == null) {
			throw new NullPointerException("Admins: action = null");
		}
		else if (action.equals(ACTION_DELETE)) {
			delete(login,password,request,response);
		}
		else if (action.equals(ACTION_INSERT)) {
			insert(login, password,request,response);
		}
		else if (action.equals(ACTION_SEARCH)) {
			search(login, request,response);
		}
		else { // 'select all' or action is empty string etc
			selectAll(request,response);
		}
	}
    
    
	private void selectAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Admin> admins = new ArrayList<>(0);
		try {
			admins = DaoFactory.getInstance().getAdminDao().selectAll();
			request.getSession().setAttribute(ATTRIBUTE_MODEL_ADMINS_TO_VIEW, admins);
			request.getRequestDispatcher(PAGE_ADMINS).forward(request, response);
		} catch (DBSystemException e) {
			request.getSession().setAttribute(ATTRIBUTE_MODEL_ADMINS_TO_VIEW, new ArrayList<>(0));
			setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_ADMINS, request,response);
		}
	}

	
	private void delete(String login, String password, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//check input
		if (!loginIsValid(login) || !passwordIsValid(password)) {
			// invalid input
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT, PAGE_ADMINS, request, response);
		} else {
			// input is valid

			// delete admin from DB
			Admin adminToDelete = new Admin(login, password);
			try {
				List<Admin> allAdmins = DaoFactory.getInstance().getAdminDao().selectAll();
				// check count of admins
				if(allAdmins.size() <= 1){
					setResultMessageGoForward(RESULT_MESSAGE_CANNOT_DELETE_LAST_ADMIN, PAGE_ADMINS, request, response);
					return;
				}
				if(allAdmins.contains(adminToDelete)){
					DaoFactory.getInstance().getAdminDao().delete(adminToDelete);
					setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_UPDATE, PAGE_ADMINS, request, response);
				} else {
					// no such admin in table 'admins'
					setResultMessageGoForward(RESULT_MESSAGE_CANNOT_DELETE_NOT_FOUND, PAGE_ADMINS, request, response);
				}
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR,PAGE_ADMINS,request,response);
			}
		}
	}
	
	
	private void insert(String login, String password,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// check input
		if (!loginIsValid(login) || !passwordIsValid(password)) {
			// invalid input
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT, PAGE_ADMINS, request, response);
		} else {

			// input is valid
			// insert admin to DB
			Admin admin = new Admin(login, password);
			try {
				DaoFactory.getInstance().getAdminDao().insert(admin);
				setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_UPDATE, PAGE_ADMINS, request, response);
			} catch (DBSystemException e) {
				setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_ADMINS,request,response);
				return;
			} catch (NotUniquePropertyException e) {
				setResultMessageGoForward(RESULT_MESSAGE_CANNOT_INSERT, PAGE_ADMINS,request,response);
			}
		}
	}
	
	private void search(String login, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (!loginIsValidForSearch(login)) {
			request.getSession().setAttribute(ATTRIBUTE_MODEL_ADMINS_TO_VIEW, new ArrayList<>(0));
			setResultMessageGoForward(RESULT_MESSAGE_INVALID_INPUT_FOR_SEARCH, PAGE_ADMINS,request, response);
			return;
		}
		
		AdminDao dao = DaoFactory.getInstance().getAdminDao();

		try {
			List<Admin> result = dao.selectByLogin(login);
			request.getSession().setAttribute(ATTRIBUTE_MODEL_ADMINS_TO_VIEW, result);
			setResultMessageGoForward(RESULT_MESSAGE_SUCCESS_SELECT,PAGE_ADMINS, request, response);
		} catch (DBSystemException e) {
			request.getSession().setAttribute(ATTRIBUTE_MODEL_ADMINS_TO_VIEW, new ArrayList<>(0));
			setResultMessageGoForward(RESULT_MESSAGE_DB_ERROR, PAGE_ADMINS,request,response);
		}
	}
}
