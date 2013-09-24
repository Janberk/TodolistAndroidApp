package org.dieschnittstelle.mobile.android.todolist.model;

import java.io.Serializable;

public class Todo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7777777L;

	private static int ID = 0;

	private String name;
	private String content;
	private boolean done;
	private boolean important;
	private String date;
	private long id;

	public Todo() {
	}

	public Todo(String name, String content, boolean done, boolean important,
			String date, long id) {
		super();
		this.setName(name);
		this.setContent(content);
		this.setDone(done);
		this.setImportant(important);
		this.setDate(date);
		this.setId(id == -1 ? ID++ : id);
	}

	public Todo(String name, String content, long id) {
		super();
		this.setName(name);
		this.setContent(content);
		this.setId(id == -1 ? ID++ : id);
	}
	
	public Todo(String name) {
		super();
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isDone() {
		return this.done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public boolean isImportant() {
		return this.important;
	}

	public void setImportant(boolean important) {
		this.important = important;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Todo updateFrom(Todo todo) {
		this.setName(todo.getName());
		this.setContent(todo.getContent());
		this.setImportant(todo.isImportant());
		this.setDate(todo.getDate());
		this.setDone(todo.isDone());
		return this;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Todo)) {
			return false;
		} else {
			return ((Todo) other).getId() == this.getId();
		}
	}

	public String getData() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + this.getName() + "\n" + "Content: "
				+ this.getContent() + "\n" + "Important: " + this.isImportant()
				+ "\n" + "Date: " + this.getDate() + "\n" + "Done: "
				+ this.isDone() + "\n" + "ID: " + this.getId() + "\n");
		return sb.toString();
	}

}
