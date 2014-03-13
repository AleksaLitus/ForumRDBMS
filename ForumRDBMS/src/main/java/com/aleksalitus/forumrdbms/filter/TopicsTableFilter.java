package com.aleksalitus.forumrdbms.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;

/**
 * Servlet Filter implementation class TopicsTableFilter 
 * Decides which text boxes to view according to selected action
 */
public class TopicsTableFilter extends BaseFilter implements Filter {

	public TopicsTableFilter() {
		super();
	}

	@Override
	protected void doHttpFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String actionWithData = request.getParameter(PARAMETER_ACTION);

		if (actionWithData.equals(ACTION_DELETE)
				|| actionWithData.equals(ACTION_SEARCH)) {

			// text boxes required
			request.setAttribute(TOPIC_ID, true);
			request.setAttribute(TOPIC_NAME, true);
			request.setAttribute(TOPIC_AUTHOR_ID, true);
			request.setAttribute(TOPIC_MESSAGES_COUNT, true);
			
			// not required
			request.setAttribute(TOPIC_MOST_POPULAR_COUNT_TO_VIEW, false);
			
		} else if (actionWithData.equals(ACTION_INSERT)) {
			
			// text boxes required
			request.setAttribute(TOPIC_NAME, true);
			request.setAttribute(TOPIC_AUTHOR_ID, true);

			// not required
			request.setAttribute(TOPIC_ID, false);
			request.setAttribute(TOPIC_MESSAGES_COUNT, false);
			request.setAttribute(TOPIC_MOST_POPULAR_COUNT_TO_VIEW, false);
			
		} else if (actionWithData.equals(ACTION_MOST_POPULAR_TOPICS)) {

			// text box required
			request.setAttribute(TOPIC_MOST_POPULAR_COUNT_TO_VIEW, true);
			
			// not required
			request.setAttribute(TOPIC_ID, false);
			request.setAttribute(TOPIC_NAME, false);
			request.setAttribute(TOPIC_AUTHOR_ID, false);
			request.setAttribute(TOPIC_MESSAGES_COUNT, false);
		}
		// select all action || actionWithData == null || actionWithData is empty string etc
		else {
			// 'select all' action requires no text boxes -> controller
			request.getRequestDispatcher(CONTROLLER_TOPICS + "?" + 
					PARAMETER_ACTION + "=" + ACTION_SELECT_ALL).forward(request, response);
			return;
		}

		chain.doFilter(request, response);
	}
}
