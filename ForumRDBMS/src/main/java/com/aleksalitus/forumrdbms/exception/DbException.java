package com.aleksalitus.forumrdbms.exception;

public class DbException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1780875165562465669L;

	public DbException(String message) {
		super(message);
	}

	public DbException(Throwable cause) {
		super(cause);
	}

	public DbException(String message, Throwable cause) {
		super(message, cause);
	}

}
