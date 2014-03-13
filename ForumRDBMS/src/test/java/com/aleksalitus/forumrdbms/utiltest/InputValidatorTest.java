package com.aleksalitus.forumrdbms.utiltest;

import static org.junit.Assert.*;

import org.junit.Test;

import com.aleksalitus.forumrdbms.util.InputValidator;

public class InputValidatorTest {

	private String[] validEmails = { "abcdef@yahoo.com",
			"abcdef-100@yahoo.com", "abcdef.100@yahoo.com",
			"abcdef111@abcdef.com", "abcdef-100@abcdef.net",
			"abcdef.100@abcdef.com.au", "abcdef@1.com",
			"abcdef@gmail.com.com", "abcdef+100@gmail.com",
			"abcdef-100@yahoo-test.com" };

	private String[] invalidEmails = { " dsf f", "abcdef", "abcdef@.com.my",
			"abcdef123@gmail.a", "abcdef123@.com", "abcdef123@.com.com",
			".abcdef@abcdef.com", "abcdef()*@gmail.com", "abcdef@%*.com",
			"abcdef..2002@gmail.com", "abcdef.@gmail.com",
			"abcdef@abcdef@gmail.com", "abcdef@gmail.com.1a" };

	private String[] validIds = {"1","123", "1234567890", "9541"};
	private String[] invalidIds = {"dfhfdt","", "  4   d  ", "123454112678912345613","15@^&*"};
	
	private String[] validLogins = {"abc", "123456789112345", "sdf456","FDSFf4"};
	private String[] invalidLogins = {"", "sf fd ", "ffffffffffffffffffffffffffff", "d", "f2"};
	
	private String[] validPasswords = {"123456789112", "4654EDGGSdf4654d", "dfdhtjjuSESGSuk4ddfs"};
	private String[] invalidPasswords = {"", " d f ", "12345678911", "dd3"};
	
	@Test
	public void validEmailTest() {

		for (String email : validEmails) {
			boolean valid = InputValidator.emailIsValid(email);
			assertTrue(valid);
		}
	}

	@Test
	public void invalidEmailTest() {

		for (String email : invalidEmails) {
			boolean valid = InputValidator.emailIsValid(email);
			assertFalse(valid);
		}
	}

	@Test
	public void validLoginTest() {
		for (String elem : validLogins) {
			boolean valid = InputValidator.loginIsValid(elem);
			assertTrue(valid);
		}
	}
	
	@Test
	public void invalidLoginTest() {
		for (String elem : invalidLogins) {
			boolean valid = InputValidator.loginIsValid(elem);
			assertFalse(valid);
		}
	}

	@Test
	public void validIdTest() {
		for (String elem : validIds) {
			boolean valid = InputValidator.idIsValid(elem);
			assertTrue(valid);
		}
	}
	
	@Test
	public void invalidIdTest() {
		for (String elem : invalidIds) {
			boolean valid = InputValidator.idIsValid(elem);
			assertFalse(valid);
		}
	}

	@Test
	public void validPasswordTest() {
		for (String elem : validPasswords) {
			boolean valid = InputValidator.passwordIsValid(elem);
			assertTrue(valid);
		}
	}
	
	@Test
	public void invalidPasswordTest() {
		for (String elem : invalidPasswords) {
			boolean valid = InputValidator.passwordIsValid(elem);
			assertFalse(valid);
		}
	}
	

	@Test
	public void testLengthValid() {
		assertTrue(InputValidator.lengthIsValid("123456789", 5, 9));
		assertTrue(InputValidator.lengthIsValid("123", 3, 9));
		assertFalse(InputValidator.lengthIsValid("", 7, 9));
		assertFalse(InputValidator.lengthIsValid("1234", 7, 9));
		assertFalse(InputValidator.lengthIsValid("1234sdf", 1, 2));
	}

}
