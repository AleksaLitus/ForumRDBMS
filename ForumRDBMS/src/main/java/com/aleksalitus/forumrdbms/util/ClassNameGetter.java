package com.aleksalitus.forumrdbms.util;

/*
 * Helps to get current class name from static context
 */
public final class ClassNameGetter {
	
	private ClassNameGetter(){
		
	}
	
	/**
	 * @return name of class where called
	 */
	public static String getCurrentClassName(){
		//StackTrace[0] = Thread
		//StackTrace[1] = ClassNameGetter
		//StackTrace[2] = Class where called, that's we need
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}
	
}
