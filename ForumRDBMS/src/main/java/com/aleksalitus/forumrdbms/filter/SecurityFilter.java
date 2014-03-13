package com.aleksalitus.forumrdbms.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;

//import com.aleksalitus.forumrdbms.util.ClassNameGetter;

import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.*;

/**
 * Checking if user is logged in, if not - redirect to login form
 */
public class SecurityFilter extends BaseFilter implements Filter {

	// TODO logging
	//private static final Logger logger = Logger.getLogger(ClassNameGetter.getCurrentClassName());

	public SecurityFilter() {
		super();
	}

	@Override
	protected void doHttpFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		//logger.trace("do SecurityFilter call");

		if (request.getSession(false) != null && 
				request.getSession().getAttribute(ATTRIBUTE_ADMIN) != null) {

			// logged in (session exists and contains admin object)
			// go forward
			chain.doFilter(request, response);
			//logger.trace("logged in. go forward");

		} else {
			
			// not logged in
			// external redirect to login.jsp
			response.sendRedirect(PAGE_LOGIN);
			//logger.trace("not logged in. go to login form");
		}
	}
}
