package org.dieschnittstelle.mobile.android.todolist.model;

public class User {

	private static int ID = 0;

	private String name;
	private String surname;
	private String email;
	private String password;
	private long id;

	public User() {

	}

	public User(String name, String surname, String email, String password,
			long id) {
		super();
		this.setName(name);
		this.setSurname(surname);
		this.setEmail(email);
		this.setPassword(password);
		this.setId(id == -1 ? ID++ : id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User updateFrom(User user) {
		this.setName(user.getName());
		this.setSurname(user.getSurname());
		this.setEmail(user.getEmail());
		this.setPassword(user.getPassword());
		return this;
	}

	public String getPersonalData() {
		StringBuilder sb = new StringBuilder();

		name = this.getName();
		surname = this.getSurname();
		email = this.getEmail();
		password = this.getPassword();

		sb.append("Userdata: \n" + "Name: " + name + "\n" + "Surname: "
				+ surname + "\n" + "Email: " + email + "\n" + "Password: "
				+ password + "\n");

		return sb.toString();
	}

}
