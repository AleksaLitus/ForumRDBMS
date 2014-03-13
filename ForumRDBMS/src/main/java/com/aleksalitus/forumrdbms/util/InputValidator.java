package com.aleksalitus.forumrdbms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InputValidator  {

	public static final String RESULT_MESSAGE_INVALID_LOGIN = "Invalid login. Latin letters and digits are only permitted, minimum three characters.";
	public static final String RESULT_MESSAGE_INVALID_PASSWORD = "Invalid password. Minimum 12 characters.";
	public static final String RESULT_MESSAGE_INVALID_EMAIL = "Invalid email.";
	
	private static final String ID_PATTERN = "^[1-9]{1}[0-9]{0,9}$";
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String LOGIN_PATTERN = "^[a-zA-Z0-9_]{3,15}$";

	private InputValidator() {
		// NOP
	}

	/**
	 * Validate input with regular expression
	 * 
	 * @param input
	 *            input for validation
	 * @return true valid login, false invalid login
	 */
	public static boolean loginIsValid(final String input) {
		if(input == null){
			throw new NullPointerException("input login is null");
		}
		Pattern pattern = Pattern.compile(LOGIN_PATTERN);
	    Matcher matcher = pattern.matcher(input);
		return matcher.matches();	
	}

	/**
	 * Validate input with regular expression
	 * 
	 * @param input
	 *            input for validation
	 * @return true valid id, false invalid id
	 */
	public static boolean idIsValid(final String input) {
		if(input == null){
			throw new NullPointerException("input id is null");
		}
		Pattern pattern = Pattern.compile(ID_PATTERN);
	    Matcher matcher = pattern.matcher(input);
		return matcher.matches();	
	}

	/**
	 * Validate input with regular expression
	 * 
	 * @param input
	 *            input for validation
	 * @return true valid email, false invalid email
	 */
	public static boolean emailIsValid(final String input) {
		if(input == null){
			throw new NullPointerException("input email is null");
		}
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	    Matcher matcher = pattern.matcher(input);
		return matcher.matches();	
	}

	/**
	 * Validate input with regular expression
	 * 
	 * @param input
	 *            input for validation
	 * @return true valid email, false invalid email
	 */
	public static boolean passwordIsValid(final String input) {
		if(input == null){
			throw new NullPointerException("input password is null");
		}
		return lengthIsValid(input,12,40);
	}
	
	/**
	 * @return true if (min <= input <= max)
	 */
	public static boolean lengthIsValid(final String input, int minSize, int maxSize) {
		if(input == null){
			throw new NullPointerException("input string is null");
		}
		return (input.length() >= minSize && input.length() <= maxSize);
	}
	
	/**
	 * Validate input with regular expression
	 * 
	 * @param input
	 *            input for validation
	 * @return true valid input for search, false invalid input
	 */
	public static boolean emailIsValidForSearch(final String input) {
		if(input == null){
			throw new NullPointerException("input is null");
		}
		return lengthIsValid(input, 1, 40) && !input.contains(" ");
	}
	
	
	/**
	 * Validate input with regular expression
	 * 
	 * @param input
	 *            input for validation
	 * @return true valid input for login search, false invalid input
	 */
	public static boolean loginIsValidForSearch(final String input) {
		if(input == null){
			throw new NullPointerException("input is null");
		}
		return lengthIsValid(input, 1, 15) && !input.contains(" ");
	}
	
	/**
	 * Validate input with regular expression
	 * 
	 * @param input
	 *            input for validation
	 * @return true valid input, false invalid input
	 */
	public static boolean isEmptyOrSpacesOnly(final String input) {
		if(input == null){
			throw new NullPointerException("input is null");
		}
		return input.isEmpty() || input.replace(" ", "").isEmpty();
	}
	
}
