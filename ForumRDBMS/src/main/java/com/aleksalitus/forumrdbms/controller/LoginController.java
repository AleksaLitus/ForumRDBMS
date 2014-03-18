package com.aleksalitus.forumrdbms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;

import com.aleksalitus.forumrdbms.entity.Admin;
import com.aleksalitus.forumrdbms.exception.DBSystemException;
import com.aleksalitus.forumrdbms.impl.DaoFactory;
//import com.aleksalitus.forumrdbms.util.ClassNameGetter;
import static com.aleksalitus.forumrdbms.util.InputValidator.*;

import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;


/**
 * Servlet implementation class LoginController
 */

@SuppressWarnings("serial")
public class LoginController extends HttpServlet {
	
	private static final String PARAM_LOGIN = "login";
	private static final String PARAM_PASSWORD = "password";
	
	private static final String ATTRIBUTE_LOGIN_RESULT = "loginResult";
	
	private static final String RESULT_MESSAGE_DONT_RECOGNIZED = "Hmm, we don't recognize that login. Please try again.";
	
	//TODO logging
	//private final Logger logger = Logger.getLogger(ClassNameGetter.getCurrentClassName());

	public LoginController() {
		super();
	}


	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doActions(request,response);
	}
	
	//TODO
//	@Override
//	protected void doGet(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException{
//		doActions(request,response);
//	}


	private void doActions(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		String login = request.getParameter(PARAM_LOGIN);
		String password = request.getParameter(PARAM_PASSWORD);
		
		if(login == null && password == null){
			
			response.sendRedirect(PAGE_MAIN);
			return;
		}

		//validate input
		if(!loginIsValid(login)){
			request.setAttribute(ATTRIBUTE_LOGIN_RESULT, RESULT_MESSAGE_INVALID_LOGIN);
			request.getRequestDispatcher(PAGE_LOGIN).forward(request, response);
			//logger.trace("Invalid admin's login entered");
			return;
		}
		if (!passwordIsValid(password)) {
			request.setAttribute(ATTRIBUTE_LOGIN_RESULT, RESULT_MESSAGE_INVALID_PASSWORD);
			request.getRequestDispatcher(PAGE_LOGIN).forward(request, response);
			//logger.trace("Invalid admin's password entered");
			return;
		}
		
		// select all admins with the same login from DB
		List<Admin> admins = new ArrayList<>(0);
		try {
			admins = DaoFactory.getInstance().getAdminDao().selectByLogin(login);
		} catch (DBSystemException e) {
			request.setAttribute(ATTRIBUTE_LOGIN_RESULT, RESULT_MESSAGE_DB_ERROR);
			request.getRequestDispatcher(PAGE_LOGIN).forward(request, response);
			//logger.trace("DB error");
			return;
		} 
		
		//no admin with such login and password in DB
		if(admins.isEmpty()){
			request.setAttribute(ATTRIBUTE_LOGIN_RESULT, RESULT_MESSAGE_DONT_RECOGNIZED);
			request.getRequestDispatcher(PAGE_LOGIN).forward(request,response);
			//logger.trace("Unrecognized admin");
			return;
		}
		
		//check if admin with such login and password exists (in list from DB)
		Admin admin = new Admin(login,password);
		if(admins.contains(admin)){
				// admin's login and password recognized
				// do login (put admin's login into session) 
			    // go to main page
				//request.getSession().setAttribute(ATTRIBUTE_ADMIN, admin.getLogin());
				request.getSession().setAttribute(ATTRIBUTE_ADMIN, request.getParameter("buttonName"));
				response.sendRedirect(PAGE_MAIN);
				//logger.trace("admin's login and password recognized. go to main page");
				return;
		}
	}
}