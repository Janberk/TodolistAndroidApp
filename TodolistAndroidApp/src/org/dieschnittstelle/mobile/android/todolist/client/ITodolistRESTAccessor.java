package org.dieschnittstelle.mobile.android.todolist.client;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.dieschnittstelle.mobile.android.todolist.model.Todo;
import org.dieschnittstelle.mobile.android.todolist.model.User;

@Path("/todolist")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ITodolistRESTAccessor {

	@GET
	public List<Todo> readAllTodos();

	@GET
	public List<User> readAllUsers();

	@GET
	@Path("/{todoId}")
	public Todo readTodo(@PathParam("todoId") long id);

	@GET
	@Path("/{userId}")
	public User readUser(@PathParam("userId") long id);

	@PUT
	public boolean writeTodo(Todo todo);

	@PUT
	public boolean writeUser(User user);

	@POST
	public boolean deleteTodo(long id);

	@POST
	public boolean deleteUser(String name);

}
