package com.aleksalitus.forumrdbms.entity;

import java.io.Serializable;

public class Message implements Serializable {
	

	private static final long serialVersionUID = 594085785694632L;
	private int id;
	private String text;
	private int authorId;
	private int topicId;

	public Message(int id, String text, int authorId, int topicId) {
		this.id = id;
		this.text = text;
		this.authorId = authorId;
		this.topicId = topicId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
	@Override
	public boolean equals(Object o){
		
		if(this == o){
			return true;
		}
		
		if(!(o instanceof Message)){
			return false;
		}
		
		Message message = (Message)o;
		
		return (message.getId() == id &&
		   message.getText().equals(text) &&
		   message.getAuthorId() == authorId &&
		   message.getTopicId() == topicId);
	}
	
	@Override
	public int hashCode(){
		return id + authorId + topicId + text.hashCode();
	}
}
