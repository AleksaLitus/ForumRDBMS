package com.aleksalitus.forumrdbms.util;

import static com.aleksalitus.forumrdbms.constants.ConstantsForServlets.ATTRIBUTE_ACTION_RESULT;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class ServletUtils {

	private ServletUtils(){
		
	}
	
	/**
	 * Utility method, sets an asttribute 'result message' and does 'forward' to specified page
	 * @param resultMessage message to view 
	 * @param page page to go forward
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void setResultMessageGoForward(String resultMessage,String page, HttpServletRequest request,HttpServletResponse response) 
			throws ServletException, IOException {
		request.setAttribute(ATTRIBUTE_ACTION_RESULT, resultMessage);
		request.getRequestDispatcher(page).forward(request, response);
	}
}
