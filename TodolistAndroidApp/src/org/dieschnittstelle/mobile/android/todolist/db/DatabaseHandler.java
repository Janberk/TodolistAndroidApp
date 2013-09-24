package org.dieschnittstelle.mobile.android.todolist.db;

import java.util.ArrayList;
import java.util.List;

import org.dieschnittstelle.mobile.android.todolist.model.Todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	protected static final String logger = DatabaseHandler.class
			.getSimpleName();

	private static final String DATABASE_NAME = "todos.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TODOS_TABLE = "todos";

	public static final String TODO_ID = "_id";
	public static final String TODO_NAME = "name";
	public static final String TODO_CONTENT = "content";
	public static final String TODO_IMPORTANT = "important";
	public static final String TODO_DATE = "date";
	public static final String TODO_DONE = "done";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createDB = "CREATE TABLE " + TODOS_TABLE + " (" + TODO_ID
				+ " INTEGER PRIMARY KEY, " + TODO_NAME + " TEXT, "
				+ TODO_CONTENT + " TEXT, " + TODO_IMPORTANT + " BOOLEAN, "
				+ TODO_DATE + " TEXT, " + TODO_DONE + " BOOLEAN)";

		db.execSQL(createDB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST " + TODOS_TABLE);
		onCreate(db);
	}

	public void deleteTable(String tableName) {
		String name = tableName;
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(name, null, null);
		this.close();
	}

	public Todo addNewTodo(Todo todo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TODO_NAME, todo.getName());
		values.put(TODO_CONTENT, todo.getContent());
		values.put(TODO_IMPORTANT, todo.isImportant());
		values.put(TODO_DATE, todo.getDate());
		values.put(TODO_DONE, todo.isDone());

		long id = db.insert(TODOS_TABLE, null, values);
		todo.setId(id);

		Log.i(logger, " addNewTodo: " + todo.getData());

		this.close();
		return todo;
	}

	public Todo updateTodo(Todo todo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TODO_NAME, todo.getName());
		values.put(TODO_CONTENT, todo.getContent());
		values.put(TODO_IMPORTANT, todo.isImportant());
		values.put(TODO_DATE, todo.getDate());
		values.put(TODO_DONE, todo.isDone());

		db.update(TODOS_TABLE, values, TODO_ID + "=?",
				new String[] { String.valueOf(todo.getId()) });

		Log.i(logger, " updateTodo: " + todo.getData());

		this.close();
		return todo;
	}

	public Todo saveTodo(Todo todo) {

		if (todo.getId() > 0) {
			updateTodo(todo);
		} else {
			addNewTodo(todo);
		}
		return todo;
	}

	public List<Todo> getAllTodos() {
		SQLiteDatabase db = this.getReadableDatabase();
		List<Todo> todoList = new ArrayList<Todo>();
		Cursor cursor = null;

		cursor = db.query(TODOS_TABLE, null, null, null, null, null, null);

		if (cursor.moveToFirst()) {
			int idIndex = cursor.getColumnIndex(TODO_ID);
			int nameIndex = cursor.getColumnIndex(TODO_NAME);
			int contentIndex = cursor.getColumnIndex(TODO_CONTENT);
			int importantIndex = cursor.getColumnIndex(TODO_IMPORTANT);
			int dateIndex = cursor.getColumnIndex(TODO_DATE);
			int doneIndex = cursor.getColumnIndex(TODO_DONE);

			do {
				long id = cursor.getLong(idIndex);
				String name = cursor.getString(nameIndex);
				String content = cursor.getString(contentIndex);
				int important = cursor.getInt(importantIndex);
				String date = cursor.getString(dateIndex);
				int done = cursor.getInt(doneIndex);

				Todo todo = new Todo();
				todo.setId(id);
				todo.setName(name);
				todo.setContent(content);
				if (important == 1) {
					todo.setImportant(true);
				} else {
					todo.setImportant(false);
				}
				todo.setDate(date);
				if (done == 1) {
					todo.setDone(true);
				} else {
					todo.setDone(false);
				}

				todoList.add(todo);

			} while (cursor.moveToNext());
		}
		cursor.close();
		this.close();

		return todoList;

	}

	public Todo getTodoById(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Todo todo = new Todo();
		Cursor cursor = null;

		cursor = db.query(TODOS_TABLE, null, TODO_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);

		if (cursor.moveToFirst()) {
			int idIndex = cursor.getColumnIndex(TODO_ID);
			int nameIndex = cursor.getColumnIndex(TODO_NAME);
			int contentIndex = cursor.getColumnIndex(TODO_CONTENT);
			int importantIndex = cursor.getColumnIndex(TODO_IMPORTANT);
			int dateIndex = cursor.getColumnIndex(TODO_DATE);
			int doneIndex = cursor.getColumnIndex(TODO_DONE);

			long _id = cursor.getLong(idIndex);
			String name = cursor.getString(nameIndex);
			String content = cursor.getString(contentIndex);
			int important = cursor.getInt(importantIndex);
			String date = cursor.getString(dateIndex);
			int done = cursor.getInt(doneIndex);

			todo.setId(_id);
			todo.setName(name);
			todo.setContent(content);
			if (important == 1) {
				todo.setImportant(true);
			} else {
				todo.setImportant(false);
			}
			todo.setDate(date);
			if (done == 1) {
				todo.setDone(true);
			} else {
				todo.setDone(false);
			}
		}
		cursor.close();
		this.close();

		return todo;

	}

	public void deleteTodo(Todo todo) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TODOS_TABLE, TODO_ID + "=?",
				new String[] { String.valueOf(todo.getId()) });
		this.close();
	}

}
