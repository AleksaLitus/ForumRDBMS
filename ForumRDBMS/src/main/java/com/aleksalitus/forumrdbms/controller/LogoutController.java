package com.aleksalitus.forumrdbms.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;

/**
 * Servlet implementation class LogoutController
 */
@SuppressWarnings("serial")
public class LogoutController extends HttpServlet {

	public LogoutController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		removeAttributesAndRedirect(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		removeAttributesAndRedirect(request,response);
	}
	
	private void removeAttributesAndRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute(ATTRIBUTE_ADMIN);
		request.getSession().removeAttribute(ATTRIBUTE_MODEL_USERS_TO_VIEW);
		request.getSession().removeAttribute(ATTRIBUTE_MODEL_ADMINS_TO_VIEW);
		request.getSession().removeAttribute(ATTRIBUTE_MODEL_TOPICS_TO_VIEW);
		request.getSession().removeAttribute(ATTRIBUTE_MODEL_MESSAGES_TO_VIEW);
		response.sendRedirect(PAGE_LOGIN);
	}

}
