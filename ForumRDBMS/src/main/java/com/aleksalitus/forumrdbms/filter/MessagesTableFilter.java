package com.aleksalitus.forumrdbms.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;

/**
 * Servlet Filter implementation class MessagesTableFilter 
 * Decides which text boxes to view according to selected action
 */
public class MessagesTableFilter extends BaseFilter implements Filter {

	public MessagesTableFilter() {
		super();
	}

	@Override
	protected void doHttpFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String actionWithData = request.getParameter(PARAMETER_ACTION);

		if (actionWithData.equals(ACTION_DELETE)) {

			// text boxes required
			request.setAttribute(MESSAGE_ID, true);
			request.setAttribute(MESSAGE_AUTHOR_ID, true);
			request.setAttribute(MESSAGE_TOPIC_ID, true);
			
			//not required
			request.setAttribute(MESSAGE_TEXT, false);
			
		} else if (actionWithData.equals(ACTION_INSERT)) {
			
			// text boxes required
			request.setAttribute(MESSAGE_TEXT, true);
			request.setAttribute(MESSAGE_AUTHOR_ID, true);
			request.setAttribute(MESSAGE_TOPIC_ID, true);

			// not required
			request.setAttribute(MESSAGE_ID, false);
			
		} else if (actionWithData.equals(ACTION_SEARCH)) {

			// all text boxes required
			request.setAttribute(MESSAGE_ID, true);
			request.setAttribute(MESSAGE_AUTHOR_ID, true);
			request.setAttribute(MESSAGE_TOPIC_ID, true);
			request.setAttribute(MESSAGE_TEXT, true);
		}
		// select all action || actionWithData == null || actionWithData is empty string etc
		else {
			// 'select all' action requires no text boxes -> controller
			request.getRequestDispatcher(CONTROLLER_MESSAGES + "?" + 
					PARAMETER_ACTION + "=" + ACTION_SELECT_ALL).forward(request, response);
			return;
		}

		chain.doFilter(request, response);
	}

}
