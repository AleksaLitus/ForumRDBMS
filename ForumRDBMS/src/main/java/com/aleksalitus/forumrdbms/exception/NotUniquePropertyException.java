package com.aleksalitus.forumrdbms.exception;

import com.aleksalitus.forumrdbms.exception.DbException;

@SuppressWarnings("serial")
public class NotUniquePropertyException extends DbException {

	public NotUniquePropertyException(String message) {
		super(message);
	}

	public NotUniquePropertyException(String message, Throwable cause) {
		super(message, cause);
	}
}
