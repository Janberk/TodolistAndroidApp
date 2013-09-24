package org.dieschnittstelle.mobile.android.todolist.handler;

import java.util.List;

import org.dieschnittstelle.mobile.android.todolist.client.TodolistRESTAccessor;
import org.dieschnittstelle.mobile.android.todolist.client.TodolistRESTAccessorTest;
import org.dieschnittstelle.mobile.android.todolist.model.Todo;
import org.dieschnittstelle.mobile.android.todolist.model.User;

import android.util.Log;

public class TodoRESTHandler {

	public static TodolistRESTAccessor rest = new TodolistRESTAccessor();

	protected static final String logger = TodoRESTHandler.class
			.getSimpleName();

	public static List<Todo> getAllTodosFromRest() {
		if (TodolistRESTAccessorTest.callWebapp()) {
			return rest.readAllTodos();
		}
		return null;
	}

	public static List<User> getAllUsersFromRest() {
		if (TodolistRESTAccessorTest.callWebapp()) {
			return rest.readAllUsers();
		}
		return null;
	}

	public static void writeCurrentTodoToRest(Todo todo) {
		if (TodolistRESTAccessorTest.callWebapp()) {
			rest.writeTodo(todo);
			Log.i(logger, "REST: sending todo (id: " + todo.getId()
					+ ") to server!");
		}
	}

	public static void writeCurrentUserToRest(User user) {
		if (TodolistRESTAccessorTest.callWebapp()) {
			rest.writeUser(user);
			Log.i(logger, "REST: sending user (name: " + user.getName()
					+ ") to server!");
		}
	}

}
