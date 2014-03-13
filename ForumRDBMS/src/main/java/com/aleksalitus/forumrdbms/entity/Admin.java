package com.aleksalitus.forumrdbms.entity;

import java.io.Serializable;


/**
 * Web application (not forum) admin
 */
public class Admin implements Serializable {
	

	private static final long serialVersionUID = 7884511808663728L;
	
	private String login;
	private String password;

	public Admin(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String email) {
		this.password = email;
	}
	
	@Override
	public boolean equals(Object o){
		
		if(this == o){
			return true;
		}
		
		if(!(o instanceof Admin)){
			return false;
		}
		
		Admin admin = (Admin)o;
		
		return (admin.getLogin().equals(login) && 
				admin.getPassword().equals(password));
	}
	
	@Override
	public int hashCode(){
		return login.hashCode() + password.hashCode();
	}

}
