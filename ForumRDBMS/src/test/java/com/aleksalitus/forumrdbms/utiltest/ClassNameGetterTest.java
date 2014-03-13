package com.aleksalitus.forumrdbms.utiltest;

import static org.junit.Assert.*;

import org.junit.Test;

import com.aleksalitus.forumrdbms.util.ClassNameGetter;

public class ClassNameGetterTest {

	@Test
	public void testGetCurrentClassName() {
		assertNotNull(ClassNameGetter.getCurrentClassName());
		assertEquals(getClass().getName(), ClassNameGetter.getCurrentClassName());
	}

}
