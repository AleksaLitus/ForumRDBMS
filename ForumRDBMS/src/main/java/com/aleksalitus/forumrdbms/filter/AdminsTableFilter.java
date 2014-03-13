package com.aleksalitus.forumrdbms.filter;

import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class AdminsTableFilter
 * Decides which text boxes to view according to selected action
 */
public class AdminsTableFilter extends BaseFilter implements Filter {

	public AdminsTableFilter() {
		super();
	}

	@Override
	protected void doHttpFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String actionWithData = request.getParameter(PARAMETER_ACTION);

		if (actionWithData.equals(ACTION_DELETE)) {

			// this actions requires all text boxes
			request.setAttribute(ADMIN_LOGIN, true);
			request.setAttribute(ADMIN_PASSWORD, true);
		}
		else if (actionWithData.equals(ACTION_INSERT)) {
			
			// this actions requires all text boxes
			request.setAttribute(ADMIN_LOGIN, true);
			request.setAttribute(ADMIN_PASSWORD, true);
		}
		else if (actionWithData.equals(ACTION_SEARCH)) {

			request.setAttribute(ADMIN_LOGIN, true);
			request.setAttribute(ADMIN_PASSWORD, false);
		}
		// select all action || actionWithData == null || actionWithData is empty string etc
		else {
			// 'select all' action requires no text boxes -> controller
			request.getRequestDispatcher(CONTROLLER_ADMINS + "?" + 
					PARAMETER_ACTION + "=" + ACTION_SELECT_ALL).forward(request, response);
			return;
		}
		
		chain.doFilter(request, response);
	}
}
