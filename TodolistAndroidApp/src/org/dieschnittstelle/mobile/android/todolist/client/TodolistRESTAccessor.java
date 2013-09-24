package org.dieschnittstelle.mobile.android.todolist.client;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.dieschnittstelle.mobile.android.todolist.model.Todo;
import org.dieschnittstelle.mobile.android.todolist.model.User;
import org.jboss.resteasy.client.ProxyFactory;

public class TodolistRESTAccessor implements ITodolistRESTAccessor {

	private static ITodolistRESTAccessor restAccessor() {
		try {
			ITodolistRESTAccessor restaccessor = ProxyFactory.create(
					ITodolistRESTAccessor.class,
					"http://10.0.2.2:8080/TodolistWebapp");
			System.out
					.println("created accessor for calling the rest interface: "
							+ restaccessor);
			return restaccessor;
		} catch (Exception e) {
			System.out.println("got exception: " + e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@GET
	public List<Todo> readAllTodos() {

		try {

			ITodolistRESTAccessor rest = restAccessor();

			List<Todo> allTodos = rest.readAllTodos();
			System.out.println("read all todos: " + allTodos);

			return allTodos;

		} catch (Exception e) {
			System.out.println("got exception: " + e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@GET
	public List<User> readAllUsers() {

		try {

			ITodolistRESTAccessor rest = restAccessor();

			List<User> allUsers = rest.readAllUsers();
			System.out.println("read all users: " + allUsers);

			return allUsers;

		} catch (Exception e) {
			System.out.println("got exception: " + e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@GET
	@Path("/{todoId}")
	public Todo readTodo(@PathParam("todoId") long id) {
		return null;
	}

	@Override
	@GET
	@Path("/{userId}")
	public User readUser(@PathParam("userId") long id) {
		return null;
	}

	@Override
	@PUT
	public boolean writeTodo(Todo todo) {

		try {

			ITodolistRESTAccessor rest = restAccessor();

			rest.writeTodo(todo);
			System.out.println("** REST CLIENT write todo: " + todo.getId());

			return true;

		} catch (Exception e) {
			System.out.println("got exception: " + e);
			e.printStackTrace();
		}
		return false;
	}

	@Override
	@PUT
	public boolean writeUser(User user) {

		try {

			ITodolistRESTAccessor rest = restAccessor();

			rest.writeUser(user);
			System.out.println("** REST CLIENT write user: " + user.getName()); // getId()?

			return true;

		} catch (Exception e) {
			System.out.println("got exception: " + e);
			e.printStackTrace();
		}
		return false;
	}

	@Override
	@POST
	public boolean deleteTodo(long id) {
		try {

			ITodolistRESTAccessor rest = restAccessor();

			rest.deleteTodo(id);
			System.out.println("REST CLIENT delete todo: " + id);

			return true;

		} catch (Exception e) {
			System.out.println("got exception: " + e);
			e.printStackTrace();
		}
		return false;
	}

	@Override
	@POST
	public boolean deleteUser(String name) {
		try {

			ITodolistRESTAccessor rest = restAccessor();

			rest.deleteUser(name);
			System.out.println("REST CLIENT delete user: " + name);

			return true;

		} catch (Exception e) {
			System.out.println("got exception: " + e);
			e.printStackTrace();
		}
		return false;
	}

}
