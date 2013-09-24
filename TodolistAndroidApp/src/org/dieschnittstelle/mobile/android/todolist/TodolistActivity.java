package org.dieschnittstelle.mobile.android.todolist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.dieschnittstelle.mobile.android.todolist.db.DatabaseHandler;
import org.dieschnittstelle.mobile.android.todolist.handler.ColorHandler;
import org.dieschnittstelle.mobile.android.todolist.handler.TodoRESTHandler;
import org.dieschnittstelle.mobile.android.todolist.model.Todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TodolistActivity extends Activity {

	public static final String ARG_TODO_OBJECT = "org.dieschnittstelle.mobile.android.todolist.model.todo";
	public static final int REQUEST_TODO_CREATION = 1;
	public static final int REQUEST_TODO_DETAILS = 2;

	protected static final String logger = TodolistActivity.class
			.getSimpleName();

	protected DatabaseHandler dbHandler;

	private EditText editTodoname;
	private Button btnAdd;
	private CheckBox todoDoneBox;
	private CheckBox todoImportantBox;
	private List<Todo> todolist;
	private ListView listviewTodos;
	private ArrayAdapter<Todo> adapter;

	AlertDialog.Builder dialogLogout;
	AlertDialog.Builder dialogDeleteAll;

	private TextWatcher watcher;

	private ColorHandler colorHandler = new ColorHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_todolist);
		colorHandler
				.setActivityBackground(this, colorHandler.getMyColorBlack());

		dbHandler = new DatabaseHandler(this);

		watcher = new LocalTextWatcher();

		try {

			listviewTodos = (ListView) findViewById(R.id.todolist_listView);
			editTodoname = (EditText) findViewById(R.id.todolist_edit_name);
			btnAdd = (Button) findViewById(R.id.addBtn);

			colorHandler.setButtonColor(btnAdd,
					ColorHandler.MY_COLOR_ROYALBLUE,
					colorHandler.getMyColorWhite());

			this.todolist = new ArrayList<Todo>();
			this.adapter = new ArrayAdapter<Todo>(this,
					R.layout.item_in_listview, todolist) {

				@Override
				public View getView(int position, View itemView,
						ViewGroup parent) {
					ViewGroup listitemView = (ViewGroup) getLayoutInflater()
							.inflate(R.layout.item_in_listview, null);
					TextView todoNameView = (TextView) listitemView
							.findViewById(R.id.label_item_name);
					final Todo todo = todolist.get(position);
					todoNameView.setText(todo.getName());

					todoDoneBox = (CheckBox) listitemView
							.findViewById(R.id.item_todo_done);
					todoDoneBox.setChecked(todo.isDone());
					todoDoneBox
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {
									todo.setDone(isChecked);
									Log.i(logger,
											" onCheckedChanged: "
													+ todo.getData());
								}
							});
					todoImportantBox = (CheckBox) listitemView
							.findViewById(R.id.item_todo_important);
					todoImportantBox.setChecked(todo.isImportant());
					todoImportantBox
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {

								@Override
								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {
									todo.setImportant(isChecked);
									Log.i(logger,
											" onCheckedChanged: "
													+ todo.getData());
								}
							});

					return listitemView;
				}
			};
			this.adapter.setNotifyOnChange(true);
			listviewTodos.setAdapter(this.adapter);

			listviewTodos.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView,
						View itemView, int itemPosition, long itemId) {

					Todo todo = adapter.getItem(itemPosition);

					if (todoImportantBox.isChecked()) {
						todo.setImportant(true);
					} else {
						todo.setImportant(false);
					}

					if (todoDoneBox.isChecked()) {
						todo.setDone(true);
					} else {
						todo.setDone(false);
					}
					dbHandler.saveTodo(todo);
					showSelection(todo);
					Log.i(logger, " onItemClick: " + todo.getData());
				}

			});
			listviewTodos.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_INSET);

			btnAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					addNewTodo();
					editTodoname.setError(null);
				}
			});

			new AsyncTask<Void, Void, List<Todo>>() {
				@Override
				protected List<Todo> doInBackground(Void... todos) {

					// TEST
					List<Todo> list = dbHandler.getAllTodos();
					for (int i = 0; i < list.size(); i++) {
						Todo todo = list.get(i);
						Log.i(logger, " doInBackground: " + todo.getData());
					}

					return dbHandler.getAllTodos();
				}

				@Override
				protected void onPostExecute(List<Todo> todos) {

					for (int i = 0; i < todos.size(); i++) {
						Todo todo = todos.get(i);
						todolist.add(0, todo);
						// todolist.add(todo);
						Log.i(logger, " onPostExecute: " + todo.getData());
					}

					adapter.notifyDataSetChanged();
				}
			}.execute();

		} catch (Exception e) {
			String err = "Got Exception: " + e;
			Log.e(logger, err, e);
			Toast.makeText(this, err, Toast.LENGTH_LONG).show();
		}

		editTodoname.addTextChangedListener(watcher);

	}

	protected void addNewTodo() {

		String input = editTodoname.getText().toString();

		if (input.trim().length() != 0) {
			Todo createdTodo = new Todo();
			createdTodo.setName(editTodoname.getText().toString());
			createdTodo.setContent("");
			createdTodo.setDate(returnDate());
			this.todolist.add(0, createdTodo);
			// this.todolist.add(createdTodo);
			this.adapter.notifyDataSetChanged();
			dbHandler.saveTodo(createdTodo);
			TodoRESTHandler.writeCurrentTodoToRest(createdTodo);
			editTodoname.setText("");

		} else {
			Toast.makeText(this, "Please enter a name for your Todo!",
					Toast.LENGTH_SHORT).show();
		}
	}

	public String returnDate() {

		StringBuilder sb = new StringBuilder();

		final Calendar c = Calendar.getInstance();

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		String setMonth = String.valueOf(month);

		switch (month) {
		case 0: {
			setMonth = "January";
		}
			break;
		case 1: {
			setMonth = "February";
		}
			break;
		case 2: {
			setMonth = "March";
		}
			break;
		case 3: {
			setMonth = "April";
		}
			break;
		case 4: {
			setMonth = "May";
		}
			break;
		case 5: {
			setMonth = "June";
		}
			break;
		case 6: {
			setMonth = "July";
		}
			break;
		case 7: {
			setMonth = "August";
		}
			break;
		case 8: {
			setMonth = "September";
		}
			break;
		case 9: {
			setMonth = "October";
		}
			break;
		case 10: {
			setMonth = "November";
		}
			break;
		case 11: {
			setMonth = "December";
		}

		default:
			break;
		}

		String setYear = String.valueOf(year);
		String setDay = String.valueOf(day);

		sb.append(setMonth + " ");
		sb.append(setDay + ", ");
		sb.append(setYear);

		return sb.toString();
	}

	protected void showSelection(Todo todo) {
		Intent intent = new Intent(TodolistActivity.this,
				TodoDetailsActivity.class);
		intent.putExtra(ARG_TODO_OBJECT, todo);

		startActivityForResult(intent, REQUEST_TODO_DETAILS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		final Todo todo = data != null ? (Todo) data
				.getSerializableExtra(ARG_TODO_OBJECT) : null;

		Log.i(logger, " onActivityResult: " + todo.getData());

		final int todoPosition = this.todolist.indexOf(todo);

		if (resultCode == TodoDetailsActivity.RESPONSE_TODO_UPDATED) {

			new AsyncTask<Todo, Void, Todo>() {

				@Override
				protected Todo doInBackground(Todo... todos) {
					return todo;
				}

				@Override
				protected void onPostExecute(Todo updatedTodo) {
					todolist.get(todolist.indexOf(todo)).updateFrom(todo);
					adapter.notifyDataSetChanged();
				}
			}.execute();

		} else if (resultCode == TodoDetailsActivity.RESPONSE_TODO_DELETED) {

			new AsyncTask<Todo, Void, Todo>() {

				@Override
				protected Todo doInBackground(Todo... todos) {
					return todo;
				}

				@Override
				protected void onPostExecute(Todo todo) {

					try {
						dbHandler.deleteTodo(todo);
						todolist.remove(todoPosition);
						adapter.notifyDataSetChanged();

					} catch (ArrayIndexOutOfBoundsException e) {
						String err = "Got Exception: " + e;
						Log.e(logger, err, e);
					}
				}
			}.execute();
		} else if (resultCode == TodoDetailsActivity.RESPONSE_NOCHANGE) {

			new AsyncTask<Todo, Void, Todo>() {

				@Override
				protected Todo doInBackground(Todo... todos) {
					return todo;
				}

				@Override
				protected void onPostExecute(Todo todo) {
					adapter.notifyDataSetChanged();
				}
			}.execute();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_deleteAll:

			dialogDeleteAll = new AlertDialog.Builder(this);
			dialogDeleteAll.setIcon(R.drawable.delete_icon);
			dialogDeleteAll.setTitle("Delete All");
			dialogDeleteAll
					.setMessage("All Todos will be deleted. Select OK to continue.");
			dialogDeleteAll.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							todolist.clear();
							dbHandler.deleteTable("todos");
							adapter.clear();
						}
					});
			dialogDeleteAll.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {

						}
					});
			dialogDeleteAll.show();

			return true;

		case R.id.menu_logout:

			dialogLogout = new AlertDialog.Builder(this);
			dialogLogout.setIcon(R.drawable.logout_icon);
			dialogLogout.setTitle("Logout");
			dialogLogout.setMessage("Select OK to logout.");
			dialogLogout.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							Intent intent = new Intent(TodolistActivity.this,
									LoginActivity.class);
							startActivity(intent);
							finish();
						}
					});
			dialogLogout.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {

						}
					});
			dialogLogout.show();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class LocalTextWatcher implements TextWatcher {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			if (editTextIsEmpty(editTodoname) && editTodoname.isEnabled())
				editTodoname.setError("Todoname can't be empty!");
			else
				editTodoname.setError(null);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	}

	@Override
	public void onBackPressed() {

		Intent intent = new Intent(TodolistActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();

		return;
	}

	public boolean editTextIsEmpty(EditText edittext) {
		if (edittext.getText().toString().trim().length() < 1)
			return true;
		else
			return false;
	}

}
