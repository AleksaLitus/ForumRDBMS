package com.aleksalitus.forumrdbms.exception;

import com.aleksalitus.forumrdbms.exception.DbException;

public class DBSystemException extends DbException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6432623135369704956L;

	public DBSystemException(String message) {
		super(message);
	}

	public DBSystemException(Throwable cause) {
		super(cause);
	}

	public DBSystemException(String message, Throwable cause) {
		super(message, cause);
	}
}
