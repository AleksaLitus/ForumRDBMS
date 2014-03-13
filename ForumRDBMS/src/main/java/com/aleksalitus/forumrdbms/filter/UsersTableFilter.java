package com.aleksalitus.forumrdbms.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;

/**
 * Servlet Filter implementation class UsersTableFilter
 * Decides which text boxes to view according to selected action
 */
public class UsersTableFilter extends BaseFilter implements Filter {

	public UsersTableFilter() {
		super();
	}

	@Override
	protected void doHttpFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String actionWithData = request.getParameter(PARAMETER_ACTION);
		
		if (actionWithData.equals(ACTION_DELETE)) {

			// this actions requires all text boxes
			request.setAttribute(USER_ID, true);
			request.setAttribute(USER_NAME, true);
			request.setAttribute(USER_EMAIL, true);
			request.setAttribute(USER_PASSWORD, true);
		}
		else if (actionWithData.equals(ACTION_INSERT)) {
			// not required text boxes
			request.setAttribute(USER_ID, false);
			// required 
			request.setAttribute(USER_NAME, true);
			request.setAttribute(USER_EMAIL, true);
			request.setAttribute(USER_PASSWORD, true);
		}
		else if (actionWithData.equals(ACTION_SEARCH)) {

			// required text boxes
			request.setAttribute(USER_ID, true);
			request.setAttribute(USER_NAME, true);
			request.setAttribute(USER_EMAIL, true);
			//not required
			request.setAttribute(USER_PASSWORD, false);
		}
		// select all action || actionWithData == null || actionWithData is empty string etc
		else {
			// 'select all' action requires no text boxes -> controller
			request.getRequestDispatcher(CONTROLLER_USERS + "?" + 
					PARAMETER_ACTION + "=" + ACTION_SELECT_ALL).forward(request, response);
			return;
		}

		chain.doFilter(request, response);
	}
}
