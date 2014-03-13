package com.aleksalitus.forumrdbms.entity;

import java.io.Serializable;

public class Topic implements Serializable {

	private static final long serialVersionUID = 6138143660850547805L;
	
	private int id;
	private String name;
	private int authorId;
	private int messagesCount;
	
	public Topic(int id, String name, int authorId, int messagesCount) {
		this.id = id;
		this.name = name;
		this.authorId = authorId;
		this.messagesCount = messagesCount;
	}

	public int getMessagesCount() {
		return messagesCount;
	}

	public void setMessagesCount(int messagesCount) {
		this.messagesCount = messagesCount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	
	@Override
	public boolean equals(Object o){
		
		if(this == o){
			return true;
		}
		
		if(!(o instanceof Topic)){
			return false;
		}
		
		Topic topic = (Topic)o;
		
		return (topic.getId() == id &&
			topic.getName().equals(name) &&
			topic.getAuthorId() == authorId);
	}
	
	@Override
	public int hashCode(){
		return id + name.hashCode() + authorId;
	}
}
